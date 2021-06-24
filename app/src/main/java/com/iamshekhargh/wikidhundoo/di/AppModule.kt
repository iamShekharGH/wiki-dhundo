package com.iamshekhargh.wikidhundoo.di

import android.app.Application
import androidx.room.Room
import com.iamshekhargh.wikidhundoo.database.PagesDao
import com.iamshekhargh.wikidhundoo.database.PagesDatabase
import com.iamshekhargh.wikidhundoo.network.WikiApi
import com.iamshekhargh.wikidhundoo.repository.WikiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
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
    fun provideRepoInstance2(
        api: WikiApi,
        dao: PagesDao,
        scope: CoroutineScope,
        app: Application
    ): WikiRepository =
        WikiRepository(api, scope, dao, app)


    @Provides
    @Singleton
    fun providesDatabaseObj(app: Application) =
        Room.databaseBuilder(app, PagesDatabase::class.java, "pages_table")
            .fallbackToDestructiveMigration().build()

    @Provides
    fun providePagesDao(database: PagesDatabase) = database.getPagesDao()

    @Provides
    @Singleton
    fun provideCoroutineScope() = CoroutineScope(SupervisorJob())

}