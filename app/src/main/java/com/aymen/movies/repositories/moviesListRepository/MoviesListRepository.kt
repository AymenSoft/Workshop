package com.aymen.movies.repositories.moviesListRepository

import com.aymen.movies.repositories.moviesListRepository.remoteDataSource.MoviesListApiInstance
import com.aymen.movies.utils.API_KEY

class MoviesListRepository{

    fun fetchMovies(page: Int) = MoviesListApiInstance.MOVIES_API.fetchMoviesList(API_KEY, "popularity.desc", page)

}