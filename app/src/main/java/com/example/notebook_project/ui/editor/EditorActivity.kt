package com.example.notebook_project.ui.editor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notebook_project.databinding.ActivityEditorBinding

class EditorActivity : AppCompatActivity(){
    private  lateinit var vb : ActivityEditorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(vb.root)
    }
}