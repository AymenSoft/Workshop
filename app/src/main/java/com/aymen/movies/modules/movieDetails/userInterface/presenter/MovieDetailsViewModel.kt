package com.aymen.movies.modules.movieDetails.userInterface.presenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aymen.movies.repositories.communModels.MovieDetails
import com.aymen.movies.repositories.movieDetailsRepository.MovieDetailsRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsViewModel(
    private val repository: MovieDetailsRepository
): ViewModel() {

    val movieDetails = MutableLiveData<MovieDetails>()
    val errorMessage = MutableLiveData<String>()

    fun getMovieDetails(id: Int){
        val response = repository.fetchMovieDetails(id)
        response.enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val movie = Gson().fromJson(response.body()!!.toString(), MovieDetails::class.java)
                movieDetails.postValue(movie)
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                errorMessage.postValue(t.message)
            }

        })
    }

}