package com.example.lab3api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET("search/movie")
    Call<MovieResponse> getMovies(@Query("api_key") String apiKey, @Query("query") String query);

    @GET("discover/movie")
    Call<MovieResponse> getMoviesByActors(@Query("api_key") String apiKey, @Query("with_cast") String ids);

    @GET("discover/movie")
    Call<MovieResponse> getMoviesByYear(@Query("api_key") String apiKey, @Query("year") int id);

    @GET("search/person")
    Call<ActorResponse> getActors(@Query("api_key") String apiKey, @Query("query") String query);
}
