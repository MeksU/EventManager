package pl.meksu.eventmanager.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Event::class, User::class, LikedEvent::class],
    version = 9,
    exportSchema = false
)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao
    abstract fun likedEventDao(): LikedEventDao
}