package pl.meksu.eventmanager

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pl.meksu.eventmanager.data.EventRepository
import pl.meksu.eventmanager.data.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import pl.meksu.eventmanager.data.LikedEvent
import pl.meksu.eventmanager.data.User
import java.text.SimpleDateFormat
import java.util.Locale

class EventViewModel(
    private val eventRepository: EventRepository = Graph.eventRepository
): ViewModel() {

    var eventTitleState by mutableStateOf("")
    var eventDateState by mutableStateOf("")
    var eventTimeState by mutableStateOf("")
    var eventAddressState by mutableStateOf("")
    var eventLinkState by mutableStateOf("")

    fun onEventTitleChanged(newString:String) {
        eventTitleState = newString
    }

    fun onEventDateChanged(newString: String) {
        eventDateState = newString
    }

    fun onEventTimeChanged(newString: String) {
        eventTimeState = newString
    }

    fun onEventAddressChanged(newString: String) {
        eventAddressState = newString
    }
    fun onEventLinkChanged(newString: String) {
        eventLinkState = newString
    }

    lateinit var getAllEvents: Flow<List<Event>>
    lateinit var getAllUsers: Flow<List<User>>
    lateinit var loggedUser: String

    init {
        viewModelScope.launch {
            getAllEvents = eventRepository.getEvents()
            getAllUsers = eventRepository.getAllUsers()
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.addEvent(event= event)
        }
    }

    fun searchEvents(key: String): Flow<List<Event>> {
        return eventRepository.searchEvents(key)
    }

    fun searchByDate(key: String): Flow<List<Event>> {
        return eventRepository.searchByDate(key)
    }

    fun searchByAddress(key: String): Flow<List<Event>> {
        return eventRepository.searchByAddress(key)
    }

    fun getEventById(id:Long): Flow<Event> {
        return eventRepository.getAEventById(id)
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.updateEvent(event= event)
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.deleteEvent(event= event)
            getAllEvents = eventRepository.getEvents()
        }
    }

    fun validateDate(dateString: String): Boolean {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            dateFormat.isLenient = false
            val parsedDate = dateFormat.parse(dateString)

            parsedDate != null
        } catch (e: Exception) {
            false
        }
    }

    fun validateTime(timeString: String): Boolean {
        return try {
            val timeFormat = SimpleDateFormat("HH:mm", Locale.US)
            timeFormat.isLenient = false
            val parsedTime = timeFormat.parse(timeString)

            parsedTime != null
        } catch (e: Exception) {
            false
        }
    }

    fun isLink(input: String): Boolean {
        val regex = Regex("^(https?://).*")
        return regex.matches(input)
    }

    var loginValid by mutableStateOf(false)

    suspend fun checkLoginValid(login: String, password: String) {
        loginValid = eventRepository.isLoginValid(login, password)
    }

    suspend fun getUserType(login: String): Long? {
        return eventRepository.getUserType(login)
    }

    suspend fun addUser(login: String, password: String) {
        eventRepository.addUser(login, password)
    }

    suspend fun deleteUser(login: String) {
        eventRepository.deleteUser(login)
    }

    fun isEventLiked(eventId: Long, userLogin: String): Flow<Boolean> {
        return eventRepository.isEventLiked(eventId, userLogin)
    }

    suspend fun addLikedEvent(likedEvent: LikedEvent) {
        eventRepository.addLikedEvent(likedEvent)
    }

    suspend fun removeLikedEvent(eventId: Long, userLogin: String) {
        eventRepository.removeLikedEvent(eventId, userLogin)
    }

    suspend fun removeLikedAdmin(eventId: Long) {
        eventRepository.removeLikedAdmin(eventId)
    }

    suspend fun removeUserLiked(userLogin: String) {
        eventRepository.removeUserLiked(userLogin)
    }
}