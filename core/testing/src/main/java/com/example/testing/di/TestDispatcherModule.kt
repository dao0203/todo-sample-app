package com.example.testing.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher


@Module
@InstallIn(SingletonComponent::class)
class TestDispatcherModule {

    @Provides
    fun provideTestDispatcher(): TestDispatcher = StandardTestDispatcher()
}
