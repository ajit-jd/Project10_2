package com.example.project10_

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class AddEditNoteActivity : AppCompatActivity() {

    private lateinit var editTextNoteTitle: EditText
    private lateinit var editTextNoteContent: EditText
    private var currentNoteId: Long? = null

    companion object {
        const val EXTRA_NOTE_ID = "com.example.project10_.EXTRA_NOTE_ID"
        const val EXTRA_TITLE = "com.example.project10_.EXTRA_TITLE"
        const val EXTRA_DESCRIPTION = "com.example.project10_.EXTRA_DESCRIPTION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarAddEditNote)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        editTextNoteTitle = findViewById(R.id.editTextNoteTitle)
        editTextNoteContent = findViewById(R.id.editTextNoteContent)

        if (intent.hasExtra(EXTRA_NOTE_ID)) {
            currentNoteId = intent.getLongExtra(EXTRA_NOTE_ID, -1L)
            // Ensure -1L is not treated as a valid ID if that's your convention for new/error
            if (currentNoteId == -1L) currentNoteId = null

            val title = intent.getStringExtra(EXTRA_TITLE)
            val description = intent.getStringExtra(EXTRA_DESCRIPTION)

            editTextNoteTitle.setText(title)
            editTextNoteContent.setText(description)
            supportActionBar?.title = "Edit Note"
        } else {
            supportActionBar?.title = "New Note"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save_note -> {
                val titleText = editTextNoteTitle.text.toString().trim()
                val contentText = editTextNoteContent.text.toString().trim()

                // Basic validation (optional, but good practice)
                if (titleText.isEmpty() && contentText.isEmpty()) {
                    Toast.makeText(this, "Note is empty, not saving.", Toast.LENGTH_SHORT).show()
                    return true
                }

                // val toastMessage = "Save clicked! Title: $titleText, Content: $contentText"
                // Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show() // MainActivity shows "Note saved" toast

                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_TITLE, titleText)
                resultIntent.putExtra(EXTRA_DESCRIPTION, contentText)
                currentNoteId?.let { id ->
                    resultIntent.putExtra(EXTRA_NOTE_ID, id)
                }
                setResult(Activity.RESULT_OK, resultIntent)
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
