import androidx.room.Database
import androidx.room.RoomDatabase
import com.go.playlistmaker.favorites.data.db.TrackFavorite
import com.go.playlistmaker.favorites.data.db.TrackFavoriteDao

@Database(
    entities = [TrackFavorite::class],
    version = 2
)
abstract class TrackDatabase : RoomDatabase() {
    abstract fun trackFavoriteDao(): TrackFavoriteDao
}