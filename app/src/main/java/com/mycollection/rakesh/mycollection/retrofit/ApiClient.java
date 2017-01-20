package com.mycollection.rakesh.mycollection.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gleecus on 12/5/16.
 */

public class ApiClient {

    public static final String BASE_URL = "https://android.rnyoo.ws/v1/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
