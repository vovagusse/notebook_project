package com.example.notebook_project.db.repository


/**
 * enum class SortOrder{
 *      ASCENDING,
 *      DESCENDING
 * }
 * */
enum class SortOrder {
    ASCENDING,
    DESCENDING
}

//fun convertStringToSortType(sortTypeStr: String) : SortOrder? {
//    return when(sortTypeStr){
//        "ASCENDING" -> SortOrder.ASCENDING
//        "DESCENDING" -> SortOrder.DESCENDING
//        else -> null
//    }
//}