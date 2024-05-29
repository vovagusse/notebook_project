package com.example.notebook_project.util


/** Makes a beautiful formatted file name. */
fun makeFileName(notebookName: String, ext: String = "md") : String {
    val spl = notebookName
        .lowercase()
        .removeSurrounding(" ")
        .replace(" ", "_") + ".$ext"
    return spl
}