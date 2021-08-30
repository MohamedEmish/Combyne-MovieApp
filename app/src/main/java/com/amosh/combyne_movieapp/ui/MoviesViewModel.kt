package com.amosh.combyne_movieapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amosh.combyne_movieapp.fragment.MovieObject
import com.amosh.combyne_movieapp.fragment.PageInfo
import com.amosh.combyne_movieapp.repository.MoviesRepository
import com.amosh.combyne_movieapp.type.*
import com.amosh.combyne_movieapp.util.AppResponseState
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
        allMoviesResponseList.clear()
        allMoviesResponseList.addAll(data)
        return AppResponseState.Success(allMoviesResponseList, pageInfo)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private val _addMovies: MutableLiveData<AppResponseState<MovieObject>> = MutableLiveData()
    val addMovies: LiveData<AppResponseState<MovieObject>>
        get() = _addMovies

    private fun handleAddMovie(movieObject: MovieObject) {
        viewModelScope.launch {
            if (validInputs(movieObject)) {
                val createMovieInput = CreateMovieInput(
                    fields = Input.fromNullable(
                        CreateMovieFieldsInput(
                            aCL = Input.absent(),
                            title = movieObject.title,
                            releaseDate = Input.fromNullable(movieObject.objectId),
                            seasons = Input.fromNullable(movieObject.seasons)
                        )
                    )
                )
                repository.createMovie(
                    createMovieInput
                ).onEach {
                    _addMovies.value = it
                }.launchIn(viewModelScope)

            } else {
                _addMovies.value = AppResponseState.Error("Please fill all data")
            }
        }
    }

    private fun validInputs(movieObject: MovieObject): Boolean {
        return when {
            movieObject.title.isEmpty() -> false
            movieObject.releaseDate == null -> false
            movieObject.seasons == null -> false
            else -> true
        }
    }

}


sealed class MoviesEvent {
    object GetMovies : MoviesEvent()
    data class AddMovie(
        val movieObject: MovieObject
    ) : MoviesEvent()
}