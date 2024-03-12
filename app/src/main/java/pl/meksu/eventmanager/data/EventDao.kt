package pl.meksu.eventmanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class EventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addEvent(eventEntity: Event)

    @Query("Select * from `event-table`")
    abstract fun getAllEvents(): Flow<List<Event>>

    @Query("SELECT * FROM `event-table` WHERE `event-title` LIKE '%' || :key || '%'")
    abstract fun searchEvents(key: String): Flow<List<Event>>

    @Query("SELECT * FROM `event-table` WHERE `event-date` LIKE '%' || :key || '%'")
    abstract fun searchByDate(key: String): Flow<List<Event>>

    @Query("SELECT * FROM `event-table` WHERE `event-address` LIKE '%' || :key || '%'")
    abstract fun searchByAddress(key: String): Flow<List<Event>>

    @Update
    abstract suspend fun updateEvent(eventEntity: Event)

    @Delete
    abstract suspend fun deleteEvent(eventEntity: Event)

    @Query("Select * from `event-table` where id=:id")
    abstract fun getEventById(id:Long): Flow<Event>

    @Query("SELECT EXISTS (SELECT 1 FROM `liked-table` WHERE `event-id`=:eventId AND `user-login`=:userLogin)")
    abstract fun isEventLiked(eventId: Long, userLogin: String): Flow<Boolean>
}