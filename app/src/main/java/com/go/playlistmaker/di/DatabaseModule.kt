package com.go.playlistmaker.di

import TrackDatabase
import androidx.room.Room
import com.go.playlistmaker.favorites.data.db.MIGRATION_1_2
import com.go.playlistmaker.favorites.data.db.MIGRATION_2_3
import com.go.playlistmaker.favorites.data.db.TrackFavoriteDao
import com.go.playlistmaker.playlists.data.db.PlaylistDao
import org.koin.dsl.module

val databaseModule = module {
    single<TrackDatabase> {
        Room.databaseBuilder(
            get(),
            TrackDatabase::class.java,
            "track_database"
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

    single<TrackFavoriteDao> { get<TrackDatabase>().trackFavoriteDao() }

    single<PlaylistDao> {
        get<TrackDatabase>().playlistDao()
    }
}