import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.go.playlistmaker.favorites.data.db.PlaylistConverter
import com.go.playlistmaker.favorites.data.db.TrackFavorite
import com.go.playlistmaker.favorites.data.db.TrackFavoriteDao
import com.go.playlistmaker.playlists.data.db.Playlist
import com.go.playlistmaker.playlists.data.db.PlaylistDao

@Database(
    entities = [TrackFavorite::class, Playlist::class],
    version = 3
)
@TypeConverters(
    value = [PlaylistConverter::class]
)
abstract class TrackDatabase : RoomDatabase() {
    abstract fun trackFavoriteDao(): TrackFavoriteDao
    abstract fun playlistDao(): PlaylistDao
}