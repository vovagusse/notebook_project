package com.example.notebook_project

import android.R
import android.os.Bundle
import com.example.notebook_project.db.NotebookTuple


fun parseMarkdown(source: String){

}

//
//private val markdownView: MarkdownView? = null
//
//fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//    this.markdownView = findViewById(R.id.markdownView)
//    this.updateMarkdownView()
//}
//
//private fun updateMarkdownView() {
//    markdownView.loadMarkdown(note_content.getText().toString())
//}



fun notebookTemplate(): MutableList<NotebookTuple>{
    val a = mutableListOf(
        NotebookTuple(1,
            "/files/note1.md",
            "Name",
            "A notebook i made aaaaaaa aaaaaaaa aaaaaa A notebook i made aaaaaaa aaaaaaaa aaaaaa",
            "11.05.2024",
            "11.05.2024"),
        NotebookTuple(2,
            "/files/note2.md",
            "Name 2",
            "A notebook i made aaaaaaa aaaaaaaa aaaaaa A notebook i made aaaaaaa aaaaaaaa aaaaaa A notebook i made aaaaaaa aaaaaaaa aaaaaa A notebook i made aaaaaaa aaaaaaaa aaaaaa",
            "11.05.2024",
            "11.05.2024"),
        NotebookTuple(3,
            "/files/note3.md",
            "Name 3",
            "A notebook i made aaaaaaa aaaaaaaa aaaaaa A notebook i made aaaaaaa aaaaaaaa aaaaaa A notebook i made aaaaaaa aaaaaaaa aaaaaa A notebook i made aaaaaaa aaaaaaaa aaaaaa A notebook i made aaaaaaa aaaaaaaa aaaaaa A notebook i made aaaaaaa aaaaaaaa aaaaaa A notebook i made aaaaaaa aaaaaaaa aaaaaa A notebook i made aaaaaaa aaaaaaaa aaaaaa A notebook i made aaaaaaa aaaaaaaa aaaaaa",
            "11.05.2024",
            "11.05.2024")
    )
    return a
}