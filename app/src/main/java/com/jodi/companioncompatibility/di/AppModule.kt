package com.jodi.companioncompatibility.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.jodi.companioncompatibility.BuildConfig
import com.jodi.companioncompatibility.JodiApp
import com.jodi.companioncompatibility.data.local.ApiCacheManager
import com.jodi.companioncompatibility.data.local.JodiDao
import com.jodi.companioncompatibility.data.local.JodiDatabase
import com.jodi.companioncompatibility.data.pref.JodiPreferences
import com.jodi.companioncompatibility.data.remote.GptService
import com.jodi.companioncompatibility.feature.home.repository.HomeRepository
import com.jodi.companioncompatibility.feature.home.viewmodel.HomeViewModel
import com.jodi.companioncompatibility.feature.result.viewmodel.ResultsViewModel
import com.jodi.companioncompatibility.feature.splash.viewmodel.SplashViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGptService(retrofit: Retrofit): GptService {
        return retrofit.create(GptService::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeRepo(service: GptService): HomeRepository {
        return HomeRepository(service)
    }

    @Provides
    @Singleton
    fun provideSplashVM(): SplashViewModel {
        return SplashViewModel()
    }

    @Provides
    @Singleton
    fun provideHomeVM(repository: HomeRepository): HomeViewModel {
        return HomeViewModel(repository)
    }

    @Provides
    @Singleton
    fun provideResultsVM(): ResultsViewModel {
        return ResultsViewModel()
    }

    @Provides
    @Singleton
    fun provideJodiDao(database: JodiDatabase): JodiDao {
        return database.getDao()
    }

    @Provides
    @Singleton
    fun provideJodiRoomDatabase(application: Application): JodiDatabase {
        return Room.databaseBuilder(
            application,
            JodiDatabase::class.java,
            "tracker"
        ).fallbackToDestructiveMigration().build()
    }


    @Provides
    @Singleton
    fun providesJodiPreferences(): JodiPreferences {
        return JodiPreferences()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideAppContext(): Context {
        return JodiApp.getAppContext()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor,
        context: Context
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor).addInterceptor(authInterceptor)
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(pref: JodiPreferences): Interceptor {
        return Interceptor { chain ->
            val requestBuilder = chain.request()
            val newRequestBuilder =
                requestBuilder.newBuilder().header("Authorization", "Bearer ${BuildConfig.API_KEY}")
                    .build()
            chain.proceed(newRequestBuilder)
        }
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideApiCacheManager(context: Context): ApiCacheManager {
        return ApiCacheManager(context)
    }
}