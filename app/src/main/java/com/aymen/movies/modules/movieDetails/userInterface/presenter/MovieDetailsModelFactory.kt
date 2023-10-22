package com.aymen.movies.modules.movieDetails.userInterface.presenter

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aymen.movies.repositories.movieDetailsRepository.MovieDetailsRepository

class MovieDetailsModelFactory constructor(
    private val repository: MovieDetailsRepository, application: Application
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            MovieDetailsViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}