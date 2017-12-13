package no.scienta.chattertap.gateways;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Map;

public class TwitterGateway extends HttpGateway {

  private static final String API_KEY = System.getProperty("twitter.key");
  private static final String API_SECRET = System.getProperty("twitter.secret");
  private static final MediaType X_WWW_FORM_URLENCODED = MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8");
  private static final String GEOCODE_RADIUS = "50km";

  private final String baseUrl;
  private String bearerToken;

  public TwitterGateway(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public Map<String, Object> search(String latitude, String longitude) throws IOException {

    String url = HttpUrl.parse(baseUrl).newBuilder()
        .addPathSegments("/1.1/search/tweets.json")
        .addQueryParameter("q", " ")
        .addQueryParameter("geocode", latitude + "," + longitude + "," + GEOCODE_RADIUS)
        .build().toString();

    Request request = new Request.Builder()
        .url(url)
        .addHeader("Authorization", "Bearer " + bearerToken())
        .build();

    Response response = httpClient.newCall(request).execute();
    String searchResult = response.body().string();

    return parseJson(searchResult);
  }


  private String bearerToken() throws IOException {
    if (this.bearerToken == null) this.bearerToken = authenticate();
    return this.bearerToken;
  }

  private String authenticate() throws IOException {

    Request request = new Request.Builder()
        .url(this.baseUrl + "/oauth2/token")
        .addHeader("Authorization", "Basic " + base64Encode(API_KEY, API_SECRET))
        .post(RequestBody.create(X_WWW_FORM_URLENCODED, "grant_type=client_credentials"))
        .build();

    Response response = httpClient.newCall(request).execute();

    if (response.isSuccessful()) {
      Map<String, String> token = parseJson(response.body().string());
      if (token.containsKey("token_type") && token.get("token_type").equalsIgnoreCase("bearer")) {
        return token.get("access_token");
      }
    }
    throw new RuntimeException("Could not authenticate with Twitter API: " + response);
  }

  private String base64Encode(String apiKey, String apiSecret) throws UnsupportedEncodingException {
    return Base64.getEncoder().encodeToString((apiKey + ":" + apiSecret).getBytes("utf-8"));
  }

  private Map parseJson(String searchResult) {
    return new Gson().fromJson(searchResult, Map.class);
  }
}
