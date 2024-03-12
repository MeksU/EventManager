package pl.meksu.eventmanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liked-table")
data class LikedEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "event-id")
    val eventId: Long,
    @ColumnInfo(name = "user-login")
    val userLogin: String
)