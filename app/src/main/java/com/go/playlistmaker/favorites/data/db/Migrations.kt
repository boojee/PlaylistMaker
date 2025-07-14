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

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE playlist (
                playlistId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                playlistName TEXT NOT NULL,
                playlistDescription TEXT NOT NULL,
                playlistUri TEXT NOT NULL,
                playlistTrackIds TEXT NOT NULL,
                playlistTracksCount INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE track (
                trackId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                trackName TEXT NOT NULL,
                artistName TEXT NOT NULL,
                trackTimeMillis TEXT NOT NULL,
                artworkUrl100 TEXT NOT NULL
            )
            """.trimIndent()
        )
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE track_new (
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
                isFavorite INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )

        database.execSQL(
            """
            INSERT INTO track_new (
                trackId, 
                trackName, 
                artistName, 
                trackTimeMillis, 
                artworkUrl100,
                collectionName,
                releaseDate,
                primaryGenreName,
                country,
                previewUrl,
                isFavorite
            )
            SELECT 
                trackId, 
                trackName, 
                artistName, 
                trackTimeMillis, 
                artworkUrl100,
                '',
                '',
                '',
                '',
                '',
                0
            FROM track
            """.trimIndent()
        )

        database.execSQL("DROP TABLE track")

        database.execSQL("ALTER TABLE track_new RENAME TO track")
    }
}

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE playlist_new (
                playlistId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                playlistName TEXT NOT NULL,
                playlistDescription TEXT NOT NULL,
                playlistUri TEXT NOT NULL,
                playlistTrackIds TEXT NOT NULL,
                playlistTracksCount INTEGER NOT NULL,
                playlistTrackTiming INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )

        database.execSQL(
            """
            INSERT INTO playlist_new (
                playlistId, 
                playlistName, 
                playlistDescription, 
                playlistUri, 
                playlistTrackIds, 
                playlistTracksCount
            )
            SELECT 
                playlistId, 
                playlistName, 
                playlistDescription, 
                playlistUri, 
                playlistTrackIds, 
                playlistTracksCount
            FROM playlist
            """.trimIndent()
        )

        database.execSQL("DROP TABLE playlist")

        database.execSQL("ALTER TABLE playlist_new RENAME TO playlist")
    }
}