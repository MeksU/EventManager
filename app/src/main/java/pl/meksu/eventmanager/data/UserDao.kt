package pl.meksu.eventmanager.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addUser(user: User)

    @Query("SELECT * FROM `user-table` WHERE `user-login`=:login")
    abstract fun getUserByLogin(login: String): Flow<User?>

    @Query("SELECT `user-type` FROM `user-table` WHERE `user-login`=:login")
    abstract fun getUserType(login: String): Flow<Long>

    @Query("DELETE FROM `user-table` WHERE `user-login`=:login")
    abstract suspend fun deleteUser(login: String)

    @Query("SELECT * FROM `user-table` WHERE `user-type`=0")
    abstract fun getAllUsers(): Flow<List<User>>
}
