package com.ispc.testace.data;

import com.ispc.testace.data.service.ApiService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {
    private static Retrofit retrofit = null;

    public static ApiService getApiService(){
        if (retrofit == null){
            String baseUrl = EnvConfig.getApiBaseUrl();
            OkHttpClient client = new OkHttpClient.Builder()
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return  retrofit.create(ApiService.class);
    }
}
