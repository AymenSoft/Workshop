package com.aymen.movies.modules.moviesList.userInterface.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aymen.movies.R
import com.aymen.movies.databinding.ActivityHomeScreenBinding
import com.aymen.movies.modules.moviesList.userInterface.presenter.MoviesListViewModel
import com.aymen.movies.repositories.communModels.MoviesItem
import com.aymen.movies.modules.movieDetails.userInterface.view.MovieDetailsActivity
import com.aymen.movies.modules.moviesList.userInterface.presenter.MoviesListModelFactory
import com.aymen.movies.modules.moviesList.userInterface.presenter.adapters.MoviesAdapter
import com.aymen.movies.repositories.moviesListRepository.MoviesListRepository

/**
 * show list of movies
 * click on movie to show details
 * @author Aymen Masmoudi
 * */
class MoviesListActivity: AppCompatActivity(), MoviesAdapter.ClickListener {

    private lateinit var binding: ActivityHomeScreenBinding

    private var page = 1

    private lateinit var moviesViewModel: MoviesListViewModel

    private lateinit var adapter: MoviesAdapter
    private lateinit var arrayList: ArrayList<MoviesItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        moviesViewModel =
            ViewModelProvider(
                this,
                MoviesListModelFactory(MoviesListRepository(), application)
            )[MoviesListViewModel::class.java]

        arrayList = ArrayList()
        adapter = MoviesAdapter(this@MoviesListActivity)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
            this@MoviesListActivity,
            LinearLayoutManager.VERTICAL,
            false)
        binding.rvData.layoutManager = mLayoutManager
        binding.rvData.adapter = adapter

        //load more movies when scrolling to the bottom
        binding.rvData.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    page+=1
                    binding.srlListview.isRefreshing = true
                    moviesViewModel.getMoviesList(page)
                }
            }
        })

        //swipe to refresh movies list
        binding.srlListview.setOnRefreshListener {
            page = 1
            arrayList.clear()
            adapter.setMovies(arrayList)
            binding.tvLoading.visibility = View.VISIBLE
            binding.tvLoading.text = resources.getString(R.string.loading)
            binding.srlListview.isRefreshing = true
            moviesViewModel.getMoviesList(page)
        }

        moviesViewModel.moviesList.observe(this) {
            binding.srlListview.isRefreshing = false
            arrayList.addAll(it)
            if (arrayList.isEmpty()){
                binding.tvLoading.visibility= View.VISIBLE
                binding.tvLoading.text=resources.getString(R.string.empty_list)
            }else {
                binding.tvLoading.visibility= View.GONE
            }
            adapter.setMovies(arrayList)
        }
        moviesViewModel.errorMessage.observe(this){
            binding.srlListview.isRefreshing = false
            binding.tvLoading.visibility=View.VISIBLE
            binding.tvLoading.text=resources.getString(R.string.empty_list)
        }
        moviesViewModel.getMoviesList(page)

    }

    //use search from action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home_screen, menu)
        // Associate searchable configuration with the SearchView
        val searchViewItem = menu!!.findItem(R.id.menu_search_name)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = searchViewItem.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.search_name)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.isIconifiedByDefault = false // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filter.filter(query)
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    //start movie details activity when click from the list
    override fun onItemClickListener(position: Int) {
        val details = Intent(this@MoviesListActivity, MovieDetailsActivity::class.java)
        details.putExtra("movieId", arrayList[position].id)
        startActivity(details)
    }

}