package com.anime.application.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

/*@Module
@InstallIn(SingletonComponent::class) // This should be the appropriate component
object NetworkModule {
    @Provides
    @Singleton // Example scope, change as per your requirement
    fun provideApiService(): ApiService {
        return RetrofitProvider.apiService
    }
}*/



@Module
@InstallIn(SingletonComponent::class) // SingletonComponent for app-wide services
object NetworkModule {

    // Provide ApiService
    @Provides
    @Singleton // Singleton scope for network-related services
    fun provideApiService(): ApiService {
        return RetrofitProvider.apiService // Make sure RetrofitProvider is set up correctly
    }

   /* // Provide Room Database instance
    @Provides
    @Singleton // Singleton scope for database
    fun provideAppDatabase(context: Context): TransactionDatabase {
        return Room.databaseBuilder(
            context,
            TransactionDatabase::class.java,
            "app_database" // Use an appropriate database name
        ).build()
    }

    // Provide TransactionDao from the database instance
    @Provides
    @Singleton
    fun provideTransactionDao(appDatabase: TransactionDatabase): TransactionDao {
        return appDatabase.transactionDao()
    }*/
}



