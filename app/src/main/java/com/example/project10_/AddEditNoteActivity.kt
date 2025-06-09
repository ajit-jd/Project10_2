package com.example.project10_

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class AddEditNoteActivity : AppCompatActivity() {

    private lateinit var editTextNoteTitle: EditText
    private lateinit var editTextNoteContent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarAddEditNote)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Optional: Set title dynamically if needed, e.g. based on whether it's an add or edit operation
        // supportActionBar?.title = "Edit Note"

        editTextNoteTitle = findViewById(R.id.editTextNoteTitle)
        editTextNoteContent = findViewById(R.id.editTextNoteContent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save_note -> {
                val title = editTextNoteTitle.text.toString().trim()
                val content = editTextNoteContent.text.toString().trim()

                // Basic validation (optional, but good practice)
                if (title.isEmpty() && content.isEmpty()) {
                    Toast.makeText(this, "Note is empty, not saving.", Toast.LENGTH_SHORT).show()
                    // finish() // Optionally close if empty and not saving
                    return true // Or false if you want to keep the activity open
                }

                val toastMessage = "Save clicked! Title: $title, Content: $content"
                Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()

                // Here you would typically save the note to a database or ViewModel
                setResult(Activity.RESULT_OK)
                finish()
                true
            }
            android.R.id.home -> {
                // This ensures the same behavior as the system back button
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
