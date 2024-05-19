package com.example.notebook_project.db.repository

enum class SortingParameter {
    BY_NAME,
    BY_DATE_OF_CREATION,
    BY_DATE_LAST_EDITED
}

//fun convertStringToSortingParameter(sortParamStr: String) : SortingParameter? {
//    return when(sortParamStr){
//        "BY_NAME" -> SortingParameter.BY_NAME
//        "BY_DATE_OF_CREATION" -> SortingParameter.BY_DATE_OF_CREATION
//        "BY_DATE_LAST_EDITED" -> SortingParameter.BY_DATE_LAST_EDITED
//        else -> null
//    }
//}