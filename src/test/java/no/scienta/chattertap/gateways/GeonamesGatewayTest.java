package no.scienta.chattertap.gateways;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class GeonamesGatewayTest {

  @Rule
  public WireMockRule wireMockRule=new WireMockRule(8089);

  @Test
  public void exampleTest() throws IOException {
    stubFor(get(urlMatching("/searchJSON.*"))
        .willReturn(aResponse()
            .withStatus(200)
            .withBody("{\"totalResultsCount\":226,\"geonames\":[{\"adminCode1\":\"12\",\"lng\":\"10.74609\",\"geonameId\":3143244,\"toponymName\":\"Oslo\",\"countryId\":\"3144096\",\"fcl\":\"P\",\"population\":580000,\"countryCode\":\"NO\",\"name\":\"Oslo\",\"fclName\":\"city, village,...\",\"countryName\":\"Norway\",\"fcodeName\":\"capital of a political entity\",\"adminName1\":\"Oslo\",\"lat\":\"59.91273\",\"fcode\":\"PPLC\"},{\"adminCode1\":\"12\",\"lng\":\"10.66824\",\"geonameId\":3152136,\"toponymName\":\"Holmenkollen\",\"countryId\":\"3144096\",\"fcl\":\"P\",\"population\":0,\"countryCode\":\"NO\",\"name\":\"Holmenkollen\",\"fclName\":\"city, village,...\",\"countryName\":\"Norway\",\"fcodeName\":\"populated place\",\"adminName1\":\"Oslo\",\"lat\":\"59.96107\",\"fcode\":\"PPL\"}]}")));


    Map<String, Object> result = new GeonamesGateway("http://localhost:8089").search("oslo");

    assertTrue(result.containsKey("geonames"));
    assertEquals(2, ((ArrayList)result.get("geonames")).size());
  }
}
