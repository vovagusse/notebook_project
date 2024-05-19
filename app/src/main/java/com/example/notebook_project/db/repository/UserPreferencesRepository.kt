package com.example.notebook_project.db.repository

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.mutablePreferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.notebook_project.ui.editor.Keys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class UserPreferences(
    val sortOrder: SortOrder,
    val sortParam: SortingParameter,
    val storageType: StorageType,
    val userTheme: UserTheme,
    val folder: String
)

const val USER_PREFERENCES_NAME = "user_preferences"

val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME,
    produceMigrations = {context ->
        listOf(
            SharedPreferencesMigration(
                context,
                USER_PREFERENCES_NAME
            )
        )
    }
)

private val keys = Keys()


class UserPreferencesRepository (
    private val dataStore: DataStore<Preferences>,
) {
    private val TAG = "UserPreferencesRepo"

    object PreferenceKeys{
        val SORT_ORDER =
            stringPreferencesKey(keys.sort_order)
        val SORT_PARAM =
            stringPreferencesKey(keys.sorting_parameter)
        val STORAGE_TYPE =
            stringPreferencesKey(keys.storage_type)
        val USER_THEME =
            stringPreferencesKey(keys.app_theme)
        val FOLDER =
            stringPreferencesKey(keys.folder)
    }

//READING IS IMPLEMENTED <HERE>
    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch{ex ->
            if (ex is IOException){
                Log.e(TAG, "Error reading preferences", ex)
                emit(emptyPreferences())
            } else {
                throw ex
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    private fun mapUserPreferences(preferences: Preferences) : UserPreferences{
        val sortOrder =
            SortOrder.valueOf(
                preferences[PreferenceKeys.SORT_ORDER] ?: SortOrder.ASCENDING.name
            )
        val sortParam =
            SortingParameter.valueOf(
                preferences[PreferenceKeys.SORT_PARAM] ?: SortingParameter.BY_NAME.name)
        val storageType =
            StorageType.valueOf(
            preferences[PreferenceKeys.STORAGE_TYPE] ?: StorageType.INTERNAL.name)

        val theme =
            UserTheme.valueOf(
                preferences[PreferenceKeys.USER_THEME] ?: UserTheme.SYSTEM.name)
        val folder = preferences[PreferenceKeys.FOLDER] ?: ""

        return UserPreferences(sortOrder, sortParam, storageType, theme, folder)
    }

//WRITING IS PERFORMED <HERE>
    suspend fun updateSortOrder(sortOrder: SortOrder){
        dataStore.edit{
            it[PreferenceKeys.SORT_ORDER] = sortOrder.name
        }
    }
    suspend fun updateSortParam(sortParam: SortingParameter){
        dataStore.edit{
            it[PreferenceKeys.SORT_PARAM] = sortParam.name
        }
    }
    suspend fun updateStorageType(storageType: StorageType){
        dataStore.edit{
            it[PreferenceKeys.STORAGE_TYPE] = storageType.name
        }
    }
    suspend fun updateUserTheme(userTheme: UserTheme){
        dataStore.edit{
            it[PreferenceKeys.USER_THEME] = userTheme.name
        }
    }
    suspend fun updateFolder(folder: String){
        dataStore.edit{prefs ->
            prefs[PreferenceKeys.FOLDER] = folder
        }
    }
}