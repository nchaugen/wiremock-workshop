package no.scienta.chattertap.gateways;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GeonamesGateway extends HttpGateway {

  private static final String POPULATED_PLACE = "P";
  private static final String USERNAME = System.getProperty("geonames.username");

  private final String baseUrl;

  public GeonamesGateway(String baseUrl) {
    this.baseUrl = baseUrl;
  }


  public Map<String, Object> search(String placeTerm) throws IOException {

    Request request = new Request.Builder()
        .url(HttpUrl.parse(baseUrl).newBuilder()
            .addPathSegment("searchJSON")
            .addQueryParameter("q", placeTerm)
            .addQueryParameter("maxRows", "10")
            .addQueryParameter("featureClass", POPULATED_PLACE)
            .addQueryParameter("username", USERNAME)
            .build().toString())
        .build();

    Response response = httpClient.newCall(request).execute();
    String searchResult = response.body().string();

    return parseJson(searchResult);
  }

  
  private Map<String, Object> parseJson(String searchResult) {
    return new Gson().fromJson(
        searchResult, new TypeToken<HashMap<String, Object>>() {}.getType()
    );
  }
}
