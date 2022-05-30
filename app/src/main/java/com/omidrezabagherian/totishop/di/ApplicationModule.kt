package com.omidrezabagherian.totishop.di

import com.omidrezabagherian.totishop.data.ShopRepository
import com.omidrezabagherian.totishop.data.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Singleton
    @Provides
    fun provideShopRepository(
        remoteDataSource: RemoteDataSource
    ): ShopRepository = ShopRepository(remoteDataSource)
}