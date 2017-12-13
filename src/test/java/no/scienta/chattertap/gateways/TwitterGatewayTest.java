package no.scienta.chattertap.gateways;

import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;

public class TwitterGatewayTest {

  @Test
  public void exampleTest() throws IOException {
    Map<String, Object> result = new TwitterGateway("https://api.twitter.com").search("27.70169", "85.3206");

    assertTrue(result.containsKey("statuses"));
  }
}
