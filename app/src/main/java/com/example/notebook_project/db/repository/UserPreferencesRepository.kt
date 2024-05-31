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

interface UserPreferencesRepository {
    abstract val userPreferencesFlow: Flow<UserPreferences>

    //WRITING IS PERFORMED <HERE>
    suspend fun updateSortOrder(sortOrder: SortOrder)
    suspend fun updateSortParam(sortParam: SortingParameter)
    suspend fun updateStorageType(storageType: StorageType)
    suspend fun updateUserTheme(userTheme: UserTheme)
    suspend fun updateFolder(folder: String)
}