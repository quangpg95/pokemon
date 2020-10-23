package com.glori.pokemon.di

import android.app.Application
import androidx.room.Room
import com.glori.pokemon.database.AppDatabase
import com.glori.pokemon.database.PokemonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "pokemon")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(database: AppDatabase): PokemonDao {
        return database.pokemonDao()
    }
}