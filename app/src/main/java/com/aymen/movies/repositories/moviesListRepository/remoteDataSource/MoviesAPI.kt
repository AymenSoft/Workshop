package com.aymen.movies.repositories.moviesListRepository.remoteDataSource

import com.aymen.movies.repositories.RetrofitInstance
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

/**
 * movies api list
 * @author Aymen Masmoudi
 * */
interface MoviesAPI {

    /**
     * get movies list from server
     * @param api api key for security
     * @param sort sort movies by
     * @param page number of page for navigation
     * @return json object
     * */
    @GET("discover/movie")
    fun fetchMoviesList(
        @Query(API_KEY) api: String,
        @Query(SORT_BY) sort: String,
        @Query(PAGE) page: Int
    ): Call<JsonObject>

    companion object {
        const val API_KEY = "api_key"
        const val SORT_BY = "sort_by"
        const val PAGE = "page"
    }
}

object MoviesListApiInstance {
    val MOVIES_API : MoviesAPI by lazy {
        RetrofitInstance().retrofit.create(MoviesAPI::class.java)
    }
}
