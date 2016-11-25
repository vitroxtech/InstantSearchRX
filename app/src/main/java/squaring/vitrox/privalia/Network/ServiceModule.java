package squaring.vitrox.privalia.Network;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import squaring.vitrox.privalia.Common.Config;
import squaring.vitrox.privalia.DependencyInjection.Module.ApplicationModule;

@Module(includes = ApplicationModule.class)

public class ServiceModule {

    @Provides
    @Singleton
    public ApiService traktapiService(@Named("trakt") OkHttpClient client) {
        String myUrl = Config.TRAKT_BASE_URL;
        //Jackson Mapper and deserializer
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new Retrofit.Builder()
                .baseUrl(myUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .client(client)
                .build()
                .create(ApiService.class);
    }

    @Provides
    @Singleton
    public TmbdService tmbdapiService(@Named("tmbd") OkHttpClient client) {
        String myUrl = Config.TMDB_BASE_URL;
        //Jackson Mapper and deserializer
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new Retrofit.Builder()
                .baseUrl(myUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .client(client)
                .build()
                .create(TmbdService.class);
    }


    @Provides @Named("trakt")
    @Singleton
    public OkHttpClient trakClient() {

        OkHttpClient httpClient =
                new OkHttpClient();
        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("trakt-api-key", Config.TRAKT_KEY)
                        .addHeader("trakt-api-version", "2");
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        return httpClient;
    }

    @Provides @Named("tmbd")
    @Singleton
    public OkHttpClient tmbdClient() {

        OkHttpClient httpClient =
                new OkHttpClient();
        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.httpUrl();
                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", Config.TMDB_KEY)
                        .build();
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }

        });
        return httpClient;
    }

}
