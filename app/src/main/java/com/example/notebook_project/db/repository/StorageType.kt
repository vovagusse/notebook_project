package com.example.notebook_project.db.repository

enum class StorageType {
    INTERNAL,
    EXTERNAL
}

//fun convertStringToStorageType(storageTypeStr: String) : StorageType? {
//    return when(storageTypeStr){
//        "INTERNAL" -> StorageType.INTERNAL
//        "EXTERNAL" -> StorageType.EXTERNAL
//        else -> null
//    }
//}