package com.example.notebook_project.db.repository

import android.content.Context
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.notebook_project.ui.editor.Keys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private val KEYS = Keys()
data class UserPreferences(val sortOrder: SortOrder)

const val USER_PREFERENCES_NAME = "user_preferences"

val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class UserPreferencesRepository (
    private val dataStore: DataStore<Preferences>,
    context: Context
) {
    private val sharedPreferences =
        context
            .applicationContext
            .getSharedPreferences(
                USER_PREFERENCES_NAME,
                Context.MODE_PRIVATE
            )
//SORTING ORDER
    private val _sortOrderFlow = MutableStateFlow(sortOrder)
    val sortOrderFlow : StateFlow<SortOrder> = _sortOrderFlow
    private val sortOrder: SortOrder
        get() {
            val order = sharedPreferences.getString(KEYS.sort_order, SortOrder.ASCENDING.name)
            return SortOrder.valueOf(order ?: SortOrder.ASCENDING.name)
        }
//SORTING PARAMETER
    private val _sortParamFlow = MutableStateFlow(sortParam)
    val sortParamFlow : StateFlow<SortingParameter> = _sortParamFlow
    private val sortParam: SortingParameter
        get() {
            val sortParam = sharedPreferences.getString(KEYS.sorting_parameter, SortingParameter.BY_NAME.name)
            return SortingParameter.valueOf(sortParam ?: SortingParameter.BY_NAME.name)
        }
//STORAGE TYPE
    private val _storageTypeFlow = MutableStateFlow(storageType)
    val storageTypeFlow : StateFlow<StorageType> = _storageTypeFlow
    private val storageType: StorageType
        get() {
            val storageType = sharedPreferences.getString(KEYS.storage_type, StorageType.INTERNAL.name)
            return StorageType.valueOf(storageType ?: StorageType.INTERNAL.name)
        }

    fun changeSortOrder(sortOrder: SortOrder){
        val currentOrder = sortOrderFlow.value
        if (currentOrder == sortOrder){
            return
        }
        updateSortOrder(sortOrder)
        _sortOrderFlow.value = sortOrder
    }

    private fun updateSortOrder(sortOrder: SortOrder){
        sharedPreferences.edit{
            putString(KEYS.sort_order, sortOrder.name)
        }
    }

    fun changeSortParam(sortParam: SortingParameter){
        val currentParam = sortParamFlow.value
        if (currentParam == sortParam){
            return
        }
        updateSortParam(sortParam)
        _sortParamFlow.value = sortParam
    }

    private fun updateSortParam(sortParam: SortingParameter){
        sharedPreferences.edit{
            putString(KEYS.sorting_parameter, sortParam.name)
        }
    }

    fun changeStorageType(storageType: StorageType){
        val currentType = storageTypeFlow.value
        if (currentType == storageType){
            return
        }
        updateStorageType(storageType)
        _sortParamFlow.value = sortParam
    }

    private fun updateStorageType(storageType: StorageType){
        sharedPreferences.edit{
            putString(KEYS.storage_type, storageType.name)
        }
    }
}