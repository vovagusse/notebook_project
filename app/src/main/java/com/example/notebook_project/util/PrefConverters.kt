package com.example.notebook_project.util

import com.example.notebook_project.db.repository.SortOrder
import com.example.notebook_project.db.repository.SortingParameter
import com.example.notebook_project.db.repository.StorageType
import com.example.notebook_project.db.repository.UserTheme

fun convertStringToSortingParameter(sortParamStr: String) : SortingParameter? {
    return when(sortParamStr){
        "BY_NAME" -> SortingParameter.BY_NAME
        "BY_DATE_OF_CREATION" -> SortingParameter.BY_DATE_OF_CREATION
        "BY_DATE_LAST_EDITED" -> SortingParameter.BY_DATE_LAST_EDITED
        else -> null
    }
}
fun convertStringToSortOrder(sortTypeStr: String) : SortOrder? {
    return when(sortTypeStr){
        "ASCENDING" -> SortOrder.ASCENDING
        "DESCENDING" -> SortOrder.DESCENDING
        else -> null
    }
}
fun convertStringToUserTheme(userThemeStr: String) : UserTheme? {
    return when(userThemeStr){
        "SYSTEM" -> UserTheme.SYSTEM
        "LIGHT" -> UserTheme.LIGHT
        "DARK" -> UserTheme.DARK
        else -> null
    }
}
fun convertStringToStorageType(storageTypeStr: String) : StorageType? {
    return when(storageTypeStr){
        "INTERNAL" -> StorageType.INTERNAL
        "EXTERNAL" -> StorageType.EXTERNAL
        else -> null
    }
}