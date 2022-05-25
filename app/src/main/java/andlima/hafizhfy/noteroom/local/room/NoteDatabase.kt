package andlima.hafizhfy.noteroom.local.room

import andlima.hafizhfy.noteroom.local.room.notetable.Note
import andlima.hafizhfy.noteroom.local.room.notetable.NoteDao
import andlima.hafizhfy.noteroom.local.room.usertable.User
import andlima.hafizhfy.noteroom.local.room.usertable.UserDao
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Note::class, User::class],
    version = 2
)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao() : NoteDao
    abstract fun userDao() : UserDao

    companion object {
        private var INSTANCE : NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase? {
            synchronized(NoteDatabase::class) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                NoteDatabase::class.java, "Note.db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}