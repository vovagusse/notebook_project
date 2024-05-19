package com.example.notebook_project.db.repository

enum class UserTheme {
    SYSTEM,
    LIGHT,
    DARK
}

//fun convertStringToUserTheme(userThemeStr: String) : UserTheme? {
//    return when(userThemeStr){
//        "SYSTEM" -> UserTheme.SYSTEM
//        "LIGHT" -> UserTheme.LIGHT
//        "DARK" -> UserTheme.DARK
//        else -> null
//    }
//}