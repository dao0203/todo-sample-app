package com.example.notifications

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
