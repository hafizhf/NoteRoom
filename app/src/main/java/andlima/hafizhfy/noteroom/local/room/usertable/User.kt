package andlima.hafizhfy.noteroom.local.room.usertable

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,

    @ColumnInfo(name = "username")
    val username: String?,

    @ColumnInfo(name = "email")
    val email: String?,

    @ColumnInfo(name = "password")
    val password: String?,

    @ColumnInfo(name = "image")
    val image: String?
) : Parcelable