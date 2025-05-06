package com.go.playlistmaker.favorites.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE favoriteTracks_new (
                trackId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                trackName TEXT NOT NULL,
                artistName TEXT NOT NULL,
                trackTimeMillis TEXT NOT NULL,
                artworkUrl100 TEXT NOT NULL,
                collectionName TEXT NOT NULL,
                releaseDate TEXT NOT NULL,
                primaryGenreName TEXT NOT NULL,
                country TEXT NOT NULL,
                previewUrl TEXT NOT NULL,
                createdAt INTEGER NOT NULL,
                isFavorite INTEGER NOT NULL
            )
        """.trimIndent()
        )

        database.execSQL(
            """
            INSERT INTO favoriteTracks_new 
            SELECT 
                trackId, 
                trackName, 
                artistName, 
                CAST(trackTimeMillis AS TEXT), 
                artworkUrl100, 
                collectionName, 
                releaseDate, 
                primaryGenreName, 
                country, 
                previewUrl, 
                createdAt, 
                isFavorite 
            FROM favoriteTracks
        """.trimIndent()
        )

        database.execSQL("DROP TABLE favoriteTracks")

        database.execSQL("ALTER TABLE favoriteTracks_new RENAME TO favoriteTracks")
    }
}