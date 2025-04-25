package com.go.playlistmaker.di

import TrackDatabase
import androidx.room.Room
import com.go.playlistmaker.favorites.data.db.MIGRATION_1_2
import com.go.playlistmaker.favorites.data.db.TrackFavoriteDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<TrackDatabase> {
        Room.databaseBuilder(
            get(),
            TrackDatabase::class.java,
            "track_database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    single<TrackFavoriteDao> { get<TrackDatabase>().trackFavoriteDao() }
}