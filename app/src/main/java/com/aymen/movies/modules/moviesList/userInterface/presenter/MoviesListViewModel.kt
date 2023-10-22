package com.aymen.movies.modules.moviesList.userInterface.presenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aymen.movies.repositories.communModels.MoviesItem
import com.aymen.movies.repositories.moviesListRepository.MoviesListRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesListViewModel(
    private val repository: MoviesListRepository
) : ViewModel() {

    val moviesList = MutableLiveData<Array<MoviesItem>>()
    val errorMessage = MutableLiveData<String>()

    fun getMoviesList(page: Int) {
        val response = repository.fetchMovies(page)
        response.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                response.body()?.let { jsonObject ->
                    val list = Gson().fromJson(
                        jsonObject.getAsJsonArray("results").toString(),
                        Array<MoviesItem>::class.java
                    )
                    moviesList.postValue(list)
                } ?: run {
                    errorMessage.postValue("empty_data")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}