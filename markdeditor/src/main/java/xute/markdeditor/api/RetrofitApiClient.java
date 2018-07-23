package xute.markdeditor.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiClient {
  public static final String BASE_URL = "http://hapramp2.herokuapp.com/api/v2/";
  private static Retrofit retrofit = null;

  public static Retrofit getClient(final String token) {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient client = new OkHttpClient.Builder()
     .addInterceptor(new Interceptor() {
       @Override
       public Response intercept(Chain chain) throws IOException {
         Request request = chain.request()
          .newBuilder()
          .addHeader("Authorization", "Token " + token)
          .build();
         return chain.proceed(request);
       }
     })
     .addInterceptor(logging)
     .build();

    retrofit = new Retrofit.Builder()
     .baseUrl(BASE_URL)
     .addConverterFactory(GsonConverterFactory.create())
     .client(client)
     .build();
    return retrofit;
  }
}
