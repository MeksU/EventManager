package pl.meksu.eventmanager

import pl.meksu.eventmanager.data.Event

interface SortingStrategy {
    fun sortEvents(items: List<Event>): List<Event>
}

class SortByNameStrategy: SortingStrategy {
    override fun sortEvents(items: List<Event>): List<Event> {
        return items.sortedBy { it.title }
    }
}

class SortByDateStrategy: SortingStrategy {
    override fun sortEvents(items: List<Event>): List<Event> {
        return items.sortedBy { it.date }
    }
}

class SortByAddressStrategy: SortingStrategy {
    override fun sortEvents(items: List<Event>): List<Event> {
        return items.sortedBy { it.address }
    }
}