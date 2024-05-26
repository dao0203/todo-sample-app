package com.example.notifications.di

import com.example.notifications.Notifier
import com.example.notifications.SystemTrayNotifier
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NotificationsModule {
    @Binds
    fun bindNotifier(systemTrayNotifier: SystemTrayNotifier): Notifier
}
