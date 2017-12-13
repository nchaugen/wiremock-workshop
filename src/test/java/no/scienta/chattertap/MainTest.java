package no.scienta.chattertap;

import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import no.scienta.chattertap.pageobjects.ChatterTapPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static junit.framework.TestCase.*;

public class MainTest {

  private static int SERVER_PORT = 9080;
  private static String SERVER_URL = "http://localhost:" + SERVER_PORT;

  private static int WIREMOCK_PORT = 9089;
  private static String WIREMOCK_URL = "http://localhost:" + WIREMOCK_PORT;


  @Rule
  public WireMockRule wireMockRule = new WireMockRule(WIREMOCK_PORT);

  @BeforeClass
  public static void setupAll() {
    Main.startServer(SERVER_PORT, WIREMOCK_URL, WIREMOCK_URL);
  }

  @AfterClass
  public static void teardownAll() {
    Main.stopServer();
  }

  private WebDriver browser = new HtmlUnitDriver();

  @Test
  public void shouldDisplayPlacesForSuccessfulGeonamesQuery() {
    stubFor(get(urlMatching("/searchJSON.*"))
        .willReturn(ok().withBodyFile("geonames_kathmandu.json"))
    );

    ChatterTapPage app = new ChatterTapPage(browser, SERVER_URL);
    app.searchFor("kathmandu");

    assertEquals(10, app.getPlaces().size());
    assertFalse(app.hasNoHitsMessage());
  }

  @Test
  public void shouldDisplayNoHitsMessageForEmptyGeonamesQuery() {
    stubFor(get(urlMatching("/searchJSON?q=no%20hits.*"))
        .willReturn(ok().withBodyFile("geonames_no_hits.json"))
    );

    ChatterTapPage app = new ChatterTapPage(browser, SERVER_URL);
    app.searchFor("no hits");

    assertEquals(0, app.getPlaces().size());
    assertTrue(app.hasNoHitsMessage());
  }

  @Test
  public void shouldNotCacheGeonamesQueries() {
    stubFor(get(urlMatching("/searchJSON.*"))
        .willReturn(ok().withBodyFile("geonames_kathmandu.json"))
    );

    ChatterTapPage app = new ChatterTapPage(browser, SERVER_URL);
    app.searchFor("kathmandu");
    app.searchFor("kathmandu");

    verify(2, getRequestedFor(urlMatching("/searchJSON.*")));
  }

  @Test
  public void shouldHandleMalformedResponseGracefully() {
    stubFor(get(urlMatching("/searchJSON.*"))
        .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK))
    );

    ChatterTapPage app = new ChatterTapPage(browser, SERVER_URL);
    app.searchFor("kathmandu");

    assertTrue(app.hasErrorMessage());
  }

  @Test
  public void shouldHandleEmptyResponseGracefully() {
    stubFor(get(urlMatching("/searchJSON.*"))
        .willReturn(aResponse().withFault(Fault.EMPTY_RESPONSE))
    );

    ChatterTapPage app = new ChatterTapPage(browser, SERVER_URL);
    app.searchFor("kathmandu");

    assertTrue(app.hasErrorMessage());
  }

  @Test
  public void shouldHandleDelayedResponses() {
    stubFor(get(urlMatching("/searchJSON.*"))
        .willReturn(ok()
            .withFixedDelay(5000)
            .withBodyFile("geonames_kathmandu.json"))
    );

    ChatterTapPage app = new ChatterTapPage(browser, SERVER_URL);
    app.searchFor("kathmandu");
  }

}