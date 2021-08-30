package com.amosh.combyne_movieapp.di

import com.amosh.combyne_movieapp.repository.MoviesRepository
import com.amosh.combyne_movieapp.room.MovieCacheMapper
import com.amosh.combyne_movieapp.room.MoviesDao
import com.apollographql.apollo.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMovieRepository(
        moviesDao: MoviesDao,
        apolloClient: ApolloClient,
        movieCacheMapper: MovieCacheMapper
    ): MoviesRepository {
        return MoviesRepository(
            moviesDao, apolloClient, movieCacheMapper
        )
    }
}