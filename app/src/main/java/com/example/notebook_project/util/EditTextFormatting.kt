package com.example.notebook_project.util

import android.util.Log
import android.widget.EditText

//enum class FormatMD (id: Int) {
//    BOLD(0),
//    ITALIC(1),
//    UNDERLINED(2),
//    CODE_INLINE(3),
//    CODE(4)
//}

class EditTextFormatting{
    private val bold = "**"
    private val italic = "*"
    private val underlined = "_"
    private val code = "```"
    private val code_inline = "`"

    fun formatBold(editText: EditText){
        val start = editText.selectionStart
        val end = editText.selectionEnd
//        val txt = editText.text
//        Log.i("FORMAT", "Start: $start - \"${txt[start]}\", End: $end - \"${txt[end]}\"")
        val text = editText.text.also {
            it.insert(start, bold)
            it.insert(end+bold.length, bold)
        }
        editText.text = text
        editText.setSelection(start+bold.length)
    }
    fun formatItalic(editText: EditText){
        val start = editText.selectionStart
        val end = editText.selectionEnd
        val text = editText.text.also {
            it.insert(start, italic)
            it.insert(end+italic.length, italic)
        }
        editText.text = text
        editText.setSelection(start+italic.length)
    }
    fun formatUnderlined(editText: EditText){
        val start = editText.selectionStart
        val end = editText.selectionEnd
        val text = editText.text.also {
            it.insert(start, underlined)
            it.insert(end+underlined.length, underlined)
        }
        editText.text = text
        editText.setSelection(start+underlined.length)
    }
    fun formatCodeInline(editText: EditText){
        val start = editText.selectionStart
        val end = editText.selectionEnd
        val text = editText.text.also {
            it.insert(start, code_inline)
            it.insert(end+code_inline.length, code_inline)
        }
        editText.text = text
        editText.setSelection(start+code_inline.length)
    }
    fun formatCodeBlock(editText: EditText){
        val start = editText.selectionStart
        val end = editText.selectionEnd
        val text = editText.text.also {
            it.insert(start, code)
            it.insert(end+code.length, code)
        }
        editText.text = text
        editText.setSelection(start+code.length)
    }
}