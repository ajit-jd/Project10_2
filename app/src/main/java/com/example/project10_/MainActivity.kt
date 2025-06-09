package com.example.project10_

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val addEditNoteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // For now, just show a toast. Later, this is where you'd refresh the note list.
            Toast.makeText(this, "Note saved successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view to the new note list layout
        setContentView(R.layout.activity_note_list)

        // Initialize RecyclerView
        val recyclerViewNotes = findViewById<RecyclerView>(R.id.recyclerViewNotes)
        recyclerViewNotes.layoutManager = LinearLayoutManager(this)

        // Create sample data
        val sampleNotes = listOf(
            Note(
                id = 1L,
                title = "Meeting Notes",
                description = "Discussed project milestones and next steps.",
                timestamp = System.currentTimeMillis(),
                imagePath = null
            ),
            Note(
                id = 2L,
                title = "Grocery List",
                description = "Milk, eggs, bread, cheese, and vegetables.",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60, // An hour ago
                imagePath = null
            ),
            Note(
                id = 3L,
                title = "Travel Plans",
                description = "Flights, accommodation, and itinerary for the upcoming trip.",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 24, // A day ago
                imagePath = null
            )
        )

        // Create and set adapter
        val noteAdapter = NoteAdapter(sampleNotes)
        recyclerViewNotes.adapter = noteAdapter

        // Initialize UI elements for click listeners
        val menuIcon = findViewById<ImageView>(R.id.imageViewMenu)
        val addIcon = findViewById<ImageView>(R.id.imageViewAdd)
        val newNoteButton = findViewById<Button>(R.id.buttonNewNote)
        val clipWebpageButton = findViewById<Button>(R.id.buttonClipWebpage)

        // Set placeholder click listeners
        menuIcon.setOnClickListener {
            Toast.makeText(this, "Hamburger menu clicked", Toast.LENGTH_SHORT).show()
        }

        addIcon.setOnClickListener {
            Toast.makeText(this, "Add icon clicked", Toast.LENGTH_SHORT).show()
        }

        newNoteButton.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            addEditNoteLauncher.launch(intent)
        }

        clipWebpageButton.setOnClickListener {
            Toast.makeText(this, "Clip Webpage button clicked", Toast.LENGTH_SHORT).show()
        }
    }
}