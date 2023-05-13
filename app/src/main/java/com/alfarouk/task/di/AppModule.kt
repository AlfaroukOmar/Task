package com.alfarouk.task.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.alfarouk.task.data.local.MovieDao
import com.alfarouk.task.data.local.db.AppDatabase
import com.alfarouk.task.data.remote.MovieApi
import com.alfarouk.task.presentation.app.TaskApp
import com.alfarouk.task.util.Constants
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): TaskApp {
        return app as TaskApp
    }


    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): MovieApi {
        return retrofit.create(MovieApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(appDatabase: AppDatabase): MovieDao {
        return appDatabase.MovieDao()
    }

    @Singleton
    @Provides
    fun getDatabase(@ApplicationContext app: Context): AppDatabase {
        return Room.databaseBuilder(
            app.applicationContext,
            AppDatabase::class.java,
            "movies.db"
        ).fallbackToDestructiveMigration()
            //   .addTypeConverter(GsonConverterFactory.create())
            .build()
    }

    private val READ_TIMEOUT = 30
    private val WRITE_TIMEOUT = 30
    private val CONNECTION_TIMEOUT = 10
    private val CACHE_SIZE_BYTES = 10 * 1024 * 1024L // 10 MB


    @Singleton
    @Provides
    fun provideOkHttpClient(
        headerInterceptor: HttpLoggingInterceptor,
        interceptor: Interceptor,
        cache: Cache
    ): OkHttpClient {
        headerInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val header = HttpLoggingInterceptor()
        header.apply { header.level = HttpLoggingInterceptor.Level.HEADERS }
        val okHttpClientBuilder = OkHttpClient().newBuilder()
        okHttpClientBuilder.connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.cache(cache)
        okHttpClientBuilder.addInterceptor(headerInterceptor)
        okHttpClientBuilder.addInterceptor(interceptor)
        return okHttpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return httpLoggingInterceptor
    }

    @Singleton
    @Provides
    fun provideDataInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val originalHttpUrl = originalRequest.url

            // Add a query parameter to the URL
            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", "0e45d61a32ac83a658635b676d92a08e")
                .addQueryParameter("language", "en-US")
                .build()

            // Add headers to the request
            val requestBuilder = originalRequest.newBuilder()
                .url(url)
                .header("Content-Type", "application/json")
            // Add more headers here as needed
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    internal fun provideCache(context: Context): Cache {
        val httpCacheDirectory = File(context.cacheDir.absolutePath, "HttpCache")
        return Cache(httpCacheDirectory, CACHE_SIZE_BYTES)
    }

    @Singleton
    @Provides
    fun provideContext(application: TaskApp): Context {
        return application.applicationContext
    }

    @Provides
    fun provideConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}