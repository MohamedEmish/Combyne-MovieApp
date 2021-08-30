package com.amosh.combyne_movieapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.amosh.combyne_movieapp.R
import com.amosh.combyne_movieapp.databinding.ActivityMainBinding
import com.amosh.combyne_movieapp.util.AppResponseState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!

    private val viewModel: MoviesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObservables()
        viewModel.setEvent(MoviesEvent.GetMovies)
    }

    private fun initObservables() {
        with(viewModel) {
            moviesList.observe(this@MainActivity, { response ->
                when(response) {
                    is AppResponseState.Error -> {
                        binding.tvCount.text = "error & message = ${response.message}"
                    }
                    is AppResponseState.Loading -> {
                        binding.tvCount.text = "loading"
                    }
                    is AppResponseState.Success -> {
                        binding.tvCount.text = "success & count = ${response.data.size ?: 0}"
                    }
                }

            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}