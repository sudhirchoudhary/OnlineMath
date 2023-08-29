package com.example.onlinemath.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ):AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app-database"
    ).build()

    @Provides
    @Singleton
    fun getExpressionDao(appDatabase: AppDatabase) = appDatabase.expressionDao()
}