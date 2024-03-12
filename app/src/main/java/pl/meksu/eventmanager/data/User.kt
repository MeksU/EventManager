package pl.meksu.eventmanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user-table")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "user-login")
    val login: String = "",
    @ColumnInfo(name = "user-password")
    val password: String = "",
    @ColumnInfo(name = "user-type")
    val type: Long = 0L
)