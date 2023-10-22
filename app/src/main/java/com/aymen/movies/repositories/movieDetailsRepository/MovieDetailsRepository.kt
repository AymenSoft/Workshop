package com.aymen.movies.repositories.movieDetailsRepository

import com.aymen.movies.repositories.movieDetailsRepository.remoteDataSource.MovieDetailsApiInstance
import com.aymen.movies.utils.API_KEY

class MovieDetailsRepository {

    fun fetchMovieDetails(id: Int) = MovieDetailsApiInstance.MOVIE_DETAILS_API.fetchMovieDetails(id, API_KEY)
}