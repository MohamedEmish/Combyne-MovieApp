package com.amosh.movieapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amosh.combyne_movieapp.fragment.MovieObject
import com.amosh.combyne_movieapp.fragment.PageInfo
import com.amosh.combyne_movieapp.type.CreateMovieFieldsInput
import com.amosh.combyne_movieapp.type.CreateMovieInput
import com.amosh.movieapp.models.AppResponseState
import com.amosh.movieapp.repository.MoviesRepository
import com.amosh.movieapp.utils.addIfNotExistAndNotNull
import com.apollographql.apollo.api.Input
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel
@Inject
constructor(
    private val repository: MoviesRepository
) : ViewModel() {
    fun setEvent(event: MoviesEvent) {
        when (event) {
            MoviesEvent.GetMovies -> handleGetMovies()
            is MoviesEvent.AddMovie -> handleAddMovie(event.movieObject)
        }
    }

    private val _allMovies: MutableLiveData<AppResponseState<List<MovieObject>>> = MutableLiveData()
    val allMoviesResponseList: MutableList<MovieObject> = mutableListOf()

    private val skip get() = allMoviesResponseList.size
    private var hasNextPage: Boolean = true

    val moviesList: LiveData<AppResponseState<List<MovieObject>>>
        get() = _allMovies


    private fun handleGetMovies() {
        if (!hasNextPage) return
        viewModelScope.launch {
            repository.getMovies(
                skip = skip
            ).onEach {
                when (it) {
                    is AppResponseState.Success -> _allMovies.value =
                        handleGetMoviesResponse(it.data ?: mutableListOf(), it.pageInfo)
                    else -> _allMovies.value = it
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun handleGetMoviesResponse(
        data: List<MovieObject>,
        pageInfo: PageInfo?
    ): AppResponseState<List<MovieObject>> {
        hasNextPage = pageInfo?.hasNextPage ?: false
        data.forEach {
            allMoviesResponseList.addIfNotExistAndNotNull(it)
        }
        return AppResponseState.Success(allMoviesResponseList, pageInfo)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private val _addMovies: MutableLiveData<AppResponseState<MovieObject>> = MutableLiveData()
    val addMovies: LiveData<AppResponseState<MovieObject>>
        get() = _addMovies

    private fun handleAddMovie(movieObject: CreateMovieFieldsInput) {
        viewModelScope.launch {
            val createMovieInput = CreateMovieInput(
                fields = Input.fromNullable(
                    movieObject
                )
            )
            repository.createMovie(
                createMovieInput
            ).onEach {
                _addMovies.value = it
            }.launchIn(viewModelScope)
        }
    }
}


sealed class MoviesEvent {
    object GetMovies : MoviesEvent()
    data class AddMovie(
        val movieObject: CreateMovieFieldsInput
    ) : MoviesEvent()
}