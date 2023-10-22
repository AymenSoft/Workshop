package com.aymen.movies.modules.movieDetails.userInterface.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.aymen.movies.R
import com.aymen.movies.databinding.ActivityMovieDetailsBinding
import com.aymen.movies.modules.movieDetails.userInterface.presenter.MovieDetailsModelFactory
import com.aymen.movies.modules.movieDetails.userInterface.presenter.MovieDetailsViewModel
import com.aymen.movies.repositories.communModels.Genre
import com.aymen.movies.repositories.movieDetailsRepository.MovieDetailsRepository
import com.aymen.movies.utils.BIG_POSTER_URL
import com.aymen.movies.utils.GestureListener
import com.bumptech.glide.Glide

/**
 * show movie details
 * @author Aymen Masmoudi
 * */
@Suppress("DEPRECATION")
@SuppressLint("ClickableViewAccessibility")
class MovieDetailsActivity : AppCompatActivity(), View.OnTouchListener,
    GestureListener.GestureInterface {

    private lateinit var binding: ActivityMovieDetailsBinding

    //detect user finger gestures
    private lateinit var gestureDetector: GestureDetector

    //detect body position to slide it up and down
    private var bodyPosition = 0 //0:default;1:bottom;2top

    private var movieId = 0

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieDetailsViewModel =
            ViewModelProvider(
                this,
                MovieDetailsModelFactory(MovieDetailsRepository(), application)
            )[MovieDetailsViewModel::class.java]

        //extend interface layout to full screen
        binding.layoutDetails.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        //get gesture detector instance
        gestureDetector = GestureDetector(this, GestureListener(this))
        //listen to view swipe events
        binding.rlBody.setOnTouchListener(this)

        binding.imgBack.setOnClickListener {
            finish()
        }

        movieDetailsViewModel.movieDetails.observe(this) {
            binding.tvName.text = it.title
            binding.tvRelease.text =
                String.format(resources.getString(R.string.release), it.releaseDate)
            binding.tvOverview.text =
                String.format(resources.getString(R.string.overview), it.overview)

            movieCategories(it.genres)

            val poster = "$BIG_POSTER_URL${it.posterPath}"
            Glide.with(this@MovieDetailsActivity)
                .load(poster)
                .into(binding.imgPoster)
        }
        movieDetailsViewModel.errorMessage.observe(this) {
            finish()
        }

        //get movie id
        val data = intent
        movieId = data.getIntExtra("movieId", 0)
        if (movieId > 0) movieDetailsViewModel.getMovieDetails(movieId) else finish()

    }

    //show movie categories as tags
    private fun movieCategories(genres: List<Genre>) {
        val categories = mutableListOf<String>()
        for (genre in genres) {
            categories.add(genre.name)
        }
        binding.tagCategories.tags = categories
    }

    //detect touch events
    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    //on swipe up events
    override fun swipeUp() {
        when (bodyPosition) {
            0 -> {
                binding.layoutDetails.transitionToState(R.id.top)
                bodyPosition = 2
            }

            1 -> {
                binding.layoutDetails.transitionToState(R.id.middle)
                bodyPosition = 0
            }
        }
    }

    //on swipe down events
    override fun swipeDown() {
        when (bodyPosition) {
            0 -> {
                binding.layoutDetails.transitionToState(R.id.bottom)
                bodyPosition = 1
            }

            2 -> {
                binding.layoutDetails.transitionToState(R.id.middle)
                bodyPosition = 0
            }
        }
    }

}