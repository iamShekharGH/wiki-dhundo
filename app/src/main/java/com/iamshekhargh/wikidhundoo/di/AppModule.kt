package com.iamshekhargh.wikidhundoo.di

import com.iamshekhargh.wikidhundoo.network.WikiApi
import com.iamshekhargh.wikidhundoo.repository.WikiDhundooRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesOkhttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

    @Provides
    @Singleton
    fun provideRetrofitObject(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().client(okHttpClient)
            .baseUrl(WikiApi.BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideWikiApi(retrofit: Retrofit): WikiApi =
        retrofit.create(WikiApi::class.java)

    @Provides
    @Singleton
    fun provideRepoInstance(api: WikiApi): WikiDhundooRepo = WikiDhundooRepo(api)

}