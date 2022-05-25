package andlima.hafizhfy.noteroom.local.room.usertable

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    // Login query
    // Return in List, so check the list size to notice if user registered or etc
    @Query("SELECT * FROM User WHERE email = :email")
    fun checkUser(email: String) : List<User>

    // Register
    @Insert
    fun registerUser(user: User) : Long
}