package pl.meksu.eventmanager.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class LikedEventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addLikedEvent(likedEvent: LikedEvent)

    @Query("DELETE FROM `liked-table` WHERE `event-id`=:eventId AND `user-login`=:userLogin")
    abstract suspend fun removeLikedEvent(eventId: Long, userLogin: String)

    @Query("DELETE FROM `liked-table` WHERE `event-id`=:eventId")
    abstract suspend fun removeLikedAdmin(eventId: Long)

    @Query("DELETE FROM `liked-table` WHERE `user-login`=:userLogin")
    abstract suspend fun removeUserLiked(userLogin: String)
}