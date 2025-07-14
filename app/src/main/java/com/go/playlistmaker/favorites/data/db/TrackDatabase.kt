import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.go.playlistmaker.favorites.data.db.PlaylistConverter
import com.go.playlistmaker.favorites.data.db.TrackFavorite
import com.go.playlistmaker.favorites.data.db.TrackFavoriteDao
import com.go.playlistmaker.playlists.data.db.Playlist
import com.go.playlistmaker.playlists.data.db.PlaylistDao
import com.go.playlistmaker.playlistdetails.data.db.Track

@Database(
    entities = [TrackFavorite::class, Playlist::class, Track::class],
    version = 6
)
@TypeConverters(
    value = [PlaylistConverter::class]
)
abstract class TrackDatabase : RoomDatabase() {
    abstract fun trackFavoriteDao(): TrackFavoriteDao
    abstract fun playlistDao(): PlaylistDao
}