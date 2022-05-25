package andlima.hafizhfy.noteroom.local.room.notetable

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,

    @ColumnInfo(name = "createdAt")
    val createdAt: String?,

    @ColumnInfo(name = "ownerId")
    val ownerId: String?,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "description")
    val description: String?

) : Parcelable
