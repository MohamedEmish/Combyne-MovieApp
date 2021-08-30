package com.amosh.movieapp.ui.moviesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amosh.movieapp.databinding.FragmentMoviesListBinding
import com.amosh.movieapp.models.AppMessage
import com.amosh.movieapp.models.AppResponseState
import com.amosh.movieapp.ui.MoviesEvent
import com.amosh.movieapp.ui.MoviesViewModel
import com.amosh.movieapp.utils.BaseFragment
import com.amosh.movieapp.utils.Constants
import com.amosh.movieapp.utils.makeGone
import com.amosh.movieapp.utils.makeVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesListFragment : BaseFragment() {

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesViewModel by viewModels()

    private val adapter by lazy {
        MoviesAdapter()
    }

    private var isLoading: Boolean = false
    private var isScrolling: Boolean = false

    override fun getLayoutRoot(inflater: LayoutInflater): View {
        _binding = FragmentMoviesListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onBindingDestroy() {
        _binding = null
    }

    override fun afterCreation(bundle: Bundle?) {
        initRecyclerView()
        initObservables()
        viewModel.setEvent(MoviesEvent.GetMovies)
    }

    private fun initObservables() {
        with(viewModel) {
            moviesList.observe(viewLifecycleOwner, { response ->
                when (response) {
                    is AppResponseState.Error -> {
                        binding.progressOverlay.root.makeGone()
                        isLoading = false
                        showMessage(
                            AppMessage(
                                AppMessage.FAILURE,
                                response.message
                            )
                        )
                    }
                    is AppResponseState.Loading -> {
                        binding.progressOverlay.root.makeVisible()
                        isLoading = true
                    }
                    is AppResponseState.Success -> {
                        binding.progressOverlay.root.makeGone()
                        isLoading = false
                        adapter.submitList(response.data ?: mutableListOf())
                    }
                }
            })
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            rvMovies.adapter = adapter
            rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isScrolling = true
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManage = recyclerView.layoutManager as LinearLayoutManager
                    val firstItemPosition = layoutManage.findFirstVisibleItemPosition()
                    val visibleItemCount = layoutManage.childCount
                    val totalCount = layoutManage.itemCount

                    val isLastItem = firstItemPosition + visibleItemCount >= totalCount - 10
                    val isNotBeginning = firstItemPosition >= 0
                    val isTotalMoreThanVisible = totalCount >= Constants.PAGE_SIZE
                    val shouldPaginate =
                        !isLoading && isLastItem && isNotBeginning && isTotalMoreThanVisible && isScrolling
                    if (shouldPaginate) {
                        viewModel.setEvent(
                            MoviesEvent.GetMovies
                        )
                        isScrolling = false
                    }
                }
            })
        }
    }
}