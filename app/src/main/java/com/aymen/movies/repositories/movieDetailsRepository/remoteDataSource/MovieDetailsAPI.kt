package com.aymen.movies.repositories.movieDetailsRepository.remoteDataSource

import com.aymen.movies.repositories.RetrofitInstance
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * movies api list
 * @author Aymen Masmoudi
 * */
interface MovieDetailsAPI {
    /**
     * get movie details by id from server
     * @param id movie id
     * @param api api key for security
     * @return json object
     * */
    @GET("movie/{id}")
    fun fetchMovieDetails(
        @Path(MOVIE_ID) id: Int,
        @Query(API_KEY) api: String
    ): Call<JsonObject>

    companion object {
        const val API_KEY = "api_key"
        const val MOVIE_ID = "id"
    }
}

object MovieDetailsApiInstance {
    val MOVIE_DETAILS_API : MovieDetailsAPI by lazy {
        RetrofitInstance().retrofit.create(MovieDetailsAPI::class.java)
    }
}