package com.example.notebook_project.util

fun makeFileName(notebookName: String) : String {
    val spl = notebookName
        .lowercase()
        .removeSurrounding(" ")
        .replace(" ", "_") + ".md"
    return spl
}