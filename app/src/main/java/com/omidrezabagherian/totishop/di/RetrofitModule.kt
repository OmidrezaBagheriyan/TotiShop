package com.omidrezabagherian.totishop.di

import com.omidrezabagherian.totishop.data.remote.ShopService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    private const val BASE_URL = "https://woocommerce.maktabsharif.ir/wp-json/wc/v3/"
    private const val CUSTOMER_KEY = "ck_63f4c52da932ddad1570283b31f3c96c4bd9fd6f"
    private const val CUSTOMER_SECRET = "cs_294e7de35430398f323b43c21dd1b29f67b5370b"

    @Singleton
    @Provides
    fun provideJsonConvertorFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideHttpLogging(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Singleton
    @Provides
    fun provideInterceptor(): Interceptor = Interceptor { chain ->
        val url = chain.request().url.newBuilder()
            .addQueryParameter("consumer_key", CUSTOMER_KEY)
            .addQueryParameter("consumer_secret", CUSTOMER_SECRET).build()
        val request = chain.request().newBuilder().url(url).build()
        val response = chain.proceed(request)
        response
    }

    @Singleton
    @Provides
    fun provideClient(
        interceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideService(retrofit: Retrofit): ShopService = retrofit.create(ShopService::class.java)

}