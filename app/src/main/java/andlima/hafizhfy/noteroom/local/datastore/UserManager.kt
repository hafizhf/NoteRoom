package andlima.hafizhfy.noteroom.local.datastore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserManager(context: Context) {
    private val dataStore : DataStore<Preferences> = context.createDataStore(name = "userpref")

    companion object {
        val ID = preferencesKey<String>("KEY_ID")
        val USERNAME = preferencesKey<String>("KEY_USERNAME")
        val EMAIL = preferencesKey<String>("KEY_EMAIL")
        val PASSWORD = preferencesKey<String>("KEY_PASSWORD")
        val IMAGE = preferencesKey<String>("KEY_IMAGE")
    }

    suspend fun loginUser(
        id: String,
        username: String,
        email: String,
        password: String,
        image: String
    ) {
        dataStore.edit {
            it[ID] = id
            it[USERNAME] = username
            it[EMAIL] = email
            it[PASSWORD] = password
            it[IMAGE] = image
        }
    }

    val id : Flow<String> = dataStore.data.map {
        it[ID] ?: ""
    }

    val username : Flow<String> = dataStore.data.map {
        it[USERNAME] ?: ""
    }

    val email : Flow<String> = dataStore.data.map {
        it[EMAIL] ?: ""
    }

    val password : Flow<String> = dataStore.data.map {
        it[PASSWORD] ?: ""
    }

    val image : Flow<String> = dataStore.data.map {
        it[IMAGE] ?: ""
    }

    suspend fun clearData() {
        dataStore.edit {
            it.clear()
        }
    }
}