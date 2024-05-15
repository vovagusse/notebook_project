package com.example.notebook_project.util

import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.db.entities.NotebookMetadata


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



fun notebookTemplate(): MutableList<Notebook>{
    val a = mutableListOf(
        Notebook(1,
            "/files/note1.md",
            NotebookMetadata("Name",
            "11.05.2024",
            "11.05.2024")),
        Notebook(2,
            "/files/note2.md",
            NotebookMetadata("Name 2 very long name very long ass fucking name fuck yeah",
            "11.05.2024",
            "11.05.2024")),
        Notebook(3,
            "/files/note3.md",
            NotebookMetadata("Name 3",
            "11.05.2024",
            "11.05.2024")),
        Notebook(3,
            "/files/note3.md",
            NotebookMetadata("Name 4",
            "11.05.2024",
            "11.05.2024")),
        Notebook(3,
            "/files/note3.md",
            NotebookMetadata("Name 5",
            "11.05.2024",
            "11.05.2024")),
        Notebook(3,
            "/files/note3.md",
            NotebookMetadata("Name 6",
            "11.05.2024",
            "11.05.2024")),
        Notebook(3,
            "/files/note3.md",
            NotebookMetadata("Name 6",
            "11.05.2024",
            "11.05.2024")),
        Notebook(3,
            "/files/note3.md",
            NotebookMetadata("Name 7",
            "11.05.2024",
            "11.05.2024"))
    )
    return a
}