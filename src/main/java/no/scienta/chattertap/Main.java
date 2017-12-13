package no.scienta.chattertap;

import no.scienta.chattertap.gateways.GeonamesGateway;
import no.scienta.chattertap.gateways.TwitterGateway;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Main {

  public static void startServer(int port, String geonamesUrl, String twitterUrl) {
    port(port);
    staticFiles.location("/public");

    GeonamesGateway geonames = new GeonamesGateway(geonamesUrl);
    TwitterGateway twitter = new TwitterGateway(twitterUrl);

    get("/", (req, res) -> {
      Map<String, Object> model = new HashMap<>();
      model.put("title", "ChatterTap");

      if (req.queryParams().contains("place")) {    
        String placeTerm = req.queryParams("place");
        model.put("place-term", placeTerm);
        model.put("places", geonames.search(placeTerm));
      }

      if (req.queryParams().contains("name")) {
        model.put("name", req.queryParams("name"));
        model.put("adminName", req.queryParams("adminName"));
        model.put("countryName", req.queryParams("countryName"));
        model.put("tweets", twitter.search(req.queryParams("lat"), req.queryParams("lng")));
      }

      return new HandlebarsTemplateEngine().render(
          new ModelAndView(model, "main.hbs")
      );
    });
  }

  public static void stopServer() {
    stop();
  }

  public static void main(String[] args) {
    startServer(4567, "http://api.geonames.org", "https://api.twitter.com");
//    startServer(4567, "http://localhost:8088", "http://localhost:8089");
  }
}
