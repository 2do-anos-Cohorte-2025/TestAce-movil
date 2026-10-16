package com.ispc.testace.data;

import com.ispc.testace.data.service.ApiService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.ispc.testace.data.local.TokenManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
public class ApiClient {
    private static Retrofit retrofit = null;

    public static ApiService getApiService() {
        if (retrofit == null) {

            // String baseUrl = "http://10.0.2.2:8000/"; // Endpoint para los emuladores
            String baseUrl = "http://192.168.1.20:8000/api/"; // Endpoint para los dispositivo físico


            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(authInterceptor())
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

    private static Interceptor authInterceptor() {
        return chain -> {
            Request original = chain.request();
            String path = original.url().encodedPath();
            boolean esEndpointAuth = path.endsWith("/auth/login/")
                    || path.endsWith("/auth/registro/");

            String token = TokenManager.getAccessToken();
            if (token == null || esEndpointAuth) {
                return chain.proceed(original);
            }

            Request conToken = original.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
            Response response = chain.proceed(conToken);

            if (response.code() == 401) {
                TokenManager.cerrarSesion();
                response.close();
                return chain.proceed(original);
            }
            return response;
        };
    }
}