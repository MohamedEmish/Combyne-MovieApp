package com.amosh.movieapp.repository

import com.amosh.combyne_movieapp.CreateMovieMutation
import com.amosh.combyne_movieapp.GetMoviesQuery
import com.amosh.combyne_movieapp.fragment.MovieObject
import com.amosh.combyne_movieapp.type.CreateMovieInput
import com.amosh.combyne_movieapp.type.MovieOrder
import com.amosh.movieapp.models.AppResponseState
import com.amosh.movieapp.room.MovieCacheMapper
import com.amosh.movieapp.room.MoviesDao
import com.amosh.movieapp.utils.Constants.PAGE_SIZE
import com.amosh.movieapp.utils.addIfNotExistAndNotNull
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class MoviesRepository
constructor(
    private val moviesDao: MoviesDao,
    private val apolloClient: ApolloClient,
    private val movieCacheMapper: MovieCacheMapper
) {
    suspend fun getMovies(
        first: Int = PAGE_SIZE,
        skip: Int
    ): Flow<AppResponseState<List<MovieObject>>> = flow {
        emit(AppResponseState.Loading)
        val networkMovies = apolloClient.query(
            GetMoviesQuery(
                first = first,
                skip = skip,
                order = Input.fromNullable(
                    mutableListOf(
                        MovieOrder.TITLE_ASC
                    )
                )
            )
        ).await()

        if (networkMovies.hasErrors()) {
            val error = networkMovies.errors?.get(0)?.message
                ?: "Something went wrong, please try again later"
            emit(AppResponseState.Error(error))
        } else {
            val movies: MutableList<MovieObject> = mutableListOf()
            networkMovies.data?.movies?.edges?.forEach {
                movies.addIfNotExistAndNotNull(
                    it?.node?.fragments?.movieObject
                )
            }
            movies.forEach {
                moviesDao.insert(movieCacheMapper.mapToEntity(it))
            }
        }
        val cachedMovies = moviesDao.get()
        emit(
            AppResponseState.Success(
                movieCacheMapper.mapFromEntityList(cachedMovies),
                networkMovies.data?.movies?.pageInfo?.fragments?.pageInfo,
            )
        )

    }.catch { e ->
        emit(
            AppResponseState.Error(
                e.message
                    ?: "Something went wrong, please try again later"
            )
        )
        val cachedMovies = moviesDao.get()
        emit(
            AppResponseState.Success(
                movieCacheMapper.mapFromEntityList(cachedMovies)
            )
        )
    }

    suspend fun createMovie(createMovieInput: CreateMovieInput): Flow<AppResponseState<MovieObject>> =
        flow {
            emit(AppResponseState.Loading)
            val networkMovies = apolloClient.mutate(CreateMovieMutation(createMovieInput)).await()

            if (networkMovies.hasErrors()) {
                val error = networkMovies.errors?.get(0)?.message
                    ?: "Something went wrong, please try again later"
                emit(AppResponseState.Error(error))
            } else {
                val movie = networkMovies.data?.createMovie?.movie?.fragments?.movieObject
                emit(
                    AppResponseState.Success(
                        movie
                    )
                )
            }

        }.catch { e ->
            emit(
                AppResponseState.Error(
                    e.message
                        ?: "Something went wrong, please try again later"
                )
            )
        }


}