package com.mycollection.rakesh.mycollection.retrofit;

import com.mycollection.rakesh.mycollection.model.Country;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by gleecus on 12/5/16.
 */

public interface ApiInterface {

    @Headers({
            "Content-Type: application/json",
            "x-rnyoo-client: RnyooAndroid",
            "Cache-Control: no-cache"
    })

    @GET("countries")
    Call<List<Country>> getCountries();

    /*@POST("movie/{id}")
    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);*/
}
