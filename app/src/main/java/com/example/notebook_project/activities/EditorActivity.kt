package com.example.notebook_project.activities

import android.R.id.home
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.notebook_project.R
import com.example.notebook_project.databinding.ActivityEditorBinding
//import com.example.notebook_project.util.*


class EditorActivity : AppCompatActivity(){
    private lateinit var editorViewModel: EditorViewModel
    private  lateinit var vb : ActivityEditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(vb.root)
        setSupportActionBar(vb.appBarMain.myToolbar)
        editorViewModel = EditorViewModel()

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        supportActionBar?.subtitle = ""
    }

    override fun onSupportNavigateUp(): Boolean {
        exitDialogue()
        return super.onSupportNavigateUp()
    }
    private fun exitDialogue(){
        val title = resources.getText(R.string.confirm_exit_editing_title)
        val message = getString(R.string.confirm_exit_editing_message)
        val yes = getString(R.string.confirm_positive)
        val no = getString(R.string.confirm_negative)
        val cancel = getString(R.string.confirm_cancel)
        val exitConfirmDialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setIcon(R.drawable.ic_save_outlined)
            .setPositiveButton(yes){ dialogInterface, i ->
                editorViewModel.save()
                Toast.makeText(this, "Yes :)", Toast.LENGTH_SHORT).show()
                this.finish()
            }
            .setNegativeButton(no) { dialogInterface, i ->
                Toast.makeText(this, "No :(", Toast.LENGTH_SHORT).show()
                this.finish()
            }
            .setNeutralButton(cancel){ dialogInterface, i ->
                Toast.makeText(this, "Cancelled :|", Toast.LENGTH_SHORT).show()
                //do nothing
            }
        exitConfirmDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        Toast.makeText(this, "Home btn pressed", Toast.LENGTH_SHORT).show()
        when (item.itemId) {
            home -> {
                exitDialogue()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun gs(id: Int): String{
        return this.resources.getString(id)
    }
}