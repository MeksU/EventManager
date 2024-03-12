package pl.meksu.eventmanager

import android.content.Context
import androidx.room.Room
import pl.meksu.eventmanager.data.EventDatabase
import pl.meksu.eventmanager.data.EventRepository

object Graph {
    private lateinit var database: EventDatabase

    val eventRepository by lazy {
        EventRepository(
            eventDao = database.eventDao(),
            userDao = database.userDao(),
            likedEventDao = database.likedEventDao()
        )
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, EventDatabase::class.java, "eventapp.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}