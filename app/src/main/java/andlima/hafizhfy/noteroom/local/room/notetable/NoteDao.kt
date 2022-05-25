package andlima.hafizhfy.noteroom.local.room.notetable

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDao {
    // Get user note by user id
    @Query("SELECT * FROM Note WHERE ownerId = :ownerId ORDER BY id DESC")
    fun getUserNotes(ownerId: Int) : List<Note>

    // Add new note by user
    @Insert
    fun addNewNote(note: Note) : Long

    // Update user note
    @Query("UPDATE Note SET title = :title, description = :description, createdAt = :createdAt WHERE id = :id")
    fun updateNote(title: String, description: String, createdAt: String, id: Int) : Int

    // Delete user note
    @Query("DELETE FROM Note WHERE id = :id")
    fun deleteNote(id: Int) : Int
}