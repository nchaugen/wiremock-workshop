package no.scienta.chattertap.gateways;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

class HttpGateway {
  private final Logger log = LoggerFactory.getLogger(this.getClass());

  final OkHttpClient httpClient = new OkHttpClient.Builder()
      .addInterceptor(new HttpLoggingInterceptor(log::info).setLevel(BODY))
      .build();
}
