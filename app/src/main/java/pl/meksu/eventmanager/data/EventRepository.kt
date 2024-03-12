package pl.meksu.eventmanager.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class EventRepository(
    private val eventDao: EventDao,
    private val userDao: UserDao,
    private val likedEventDao: LikedEventDao
) {
    suspend fun addEvent(event:Event) {
        eventDao.addEvent(event)
    }

    fun getEvents(): Flow<List<Event>> = eventDao.getAllEvents()

    fun searchEvents(key: String): Flow<List<Event>> = eventDao.searchEvents(key)

    fun searchByDate(key: String): Flow<List<Event>> = eventDao.searchByDate(key)

    fun searchByAddress(key: String): Flow<List<Event>> = eventDao.searchByAddress(key)

    fun getAEventById(id:Long): Flow<Event> {
        return eventDao.getEventById(id)
    }

    suspend fun updateEvent(event:Event) {
        eventDao.updateEvent(event)
    }

    suspend fun deleteEvent(event: Event) {
        eventDao.deleteEvent(event)
    }

    suspend fun isLoginValid(login: String, password: String): Boolean {
        val user = userDao.getUserByLogin(login).firstOrNull()
        return user != null && verifyPassword(password, user.password)
    }

    private fun verifyPassword(password: String, passwordUser: String): Boolean {
        return password == passwordUser
    }

    suspend fun getUserType(login: String): Long? {
        return userDao.getUserType(login).firstOrNull()
    }

    suspend fun addUsers() {
        userDao.addUser(User("admin", "admin", 1L))
        userDao.addUser(User("user", "user", 0L))
    }

    suspend fun addUser(login: String, password: String) {
        userDao.addUser(User(login, password, 0L))
    }

    suspend fun deleteUser(login: String) {
        userDao.deleteUser(login)
    }

    fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
    }

    fun isEventLiked(eventId: Long, userLogin: String): Flow<Boolean> {
        return eventDao.isEventLiked(eventId, userLogin)
    }

    suspend fun addLikedEvent(likedEvent: LikedEvent) {
        likedEventDao.addLikedEvent(likedEvent)
    }

    suspend fun removeLikedEvent(eventId: Long, userLogin: String) {
        likedEventDao.removeLikedEvent(eventId, userLogin)
    }

    suspend fun removeUserLiked(userLogin: String) {
        likedEventDao.removeUserLiked(userLogin)
    }

    suspend fun removeLikedAdmin(eventId: Long) {
        likedEventDao.removeLikedAdmin(eventId)
    }
}