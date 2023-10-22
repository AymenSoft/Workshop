package com.aymen.movies.modules.moviesList.userInterface.presenter

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aymen.movies.repositories.moviesListRepository.MoviesListRepository
import java.lang.IllegalArgumentException

class MoviesListModelFactory constructor(private val repository: MoviesListRepository, application: Application
): ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MoviesListViewModel::class.java)){
            MoviesListViewModel(this.repository) as T
        }else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}