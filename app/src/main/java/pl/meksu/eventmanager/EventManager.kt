package pl.meksu.eventmanager

import android.app.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EventManager:Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)

        GlobalScope.launch(Dispatchers.IO) {
            Graph.eventRepository.addUsers()
        }
    }
}