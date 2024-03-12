package pl.meksu.eventmanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="event-table")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name="event-title")
    val title: String = "",
    @ColumnInfo(name="event-date")
    val date:String = "",
    @ColumnInfo(name="event-time")
    val time:String = "",
    @ColumnInfo(name="event-address")
    val address:String = "",
    @ColumnInfo(name="event-link")
    val link:String = ""
)