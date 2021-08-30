package com.amosh.combyne_movieapp.di

import com.amosh.combyne_movieapp.BuildConfig
import com.amosh.combyne_movieapp.util.Constants.TIMEOUT_DURATION
import com.apollographql.apollo.ApolloClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApolloModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): LoggingInterceptor {
        return LoggingInterceptor.Builder()
            .loggable(BuildConfig.DEBUG)
            .setLevel(Level.BASIC)
            .log(Platform.INFO)
            .request(" ")
            .response(" ")
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor, loggingInterceptor: LoggingInterceptor): OkHttpClient {
        val client = OkHttpClient.Builder()
            .readTimeout(TIMEOUT_DURATION.toLong(), TimeUnit.MILLISECONDS)
            .connectTimeout(TIMEOUT_DURATION.toLong(), TimeUnit.MILLISECONDS)
            .addInterceptor(AuthInterceptor())

        if (BuildConfig.DEBUG)
            client.addInterceptor(httpLoggingInterceptor)
                .addInterceptor(loggingInterceptor)

        return client.build()
    }

    @Singleton
    @Provides
    fun provideApolloClient(okHttpClient: OkHttpClient): ApolloClient {
        return ApolloClient.builder()
            .serverUrl(BuildConfig.HOST)
            .okHttpClient(okHttpClient)
            .build()
    }

    internal class AuthInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val authorisedRequest = chain.request().newBuilder()
                .addHeader(
                    "X-Parse-Client-Key",
                    "yiCk1DW6WHWG58wjj3C4pB/WyhpokCeDeSQEXA5HaicgGh4pTUd+3/rMOR5xu1Yi"
                )
                .addHeader(
                    "X-Parse-Application-Id",
                    "AaQjHwTIQtkCOhtjJaN/nDtMdiftbzMWW5N8uRZ+DNX9LI8AOziS10eHuryBEcCI"
                )
                .build()
            return chain.proceed(authorisedRequest)
        }
    }


}