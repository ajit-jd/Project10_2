package com.example.project10_

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    // private lateinit var toolbar: MaterialToolbar // Not using a dedicated toolbar for drawer toggle
    // private lateinit var toggle: ActionBarDrawerToggle // Not using toggle for now

    // private val notesList = mutableListOf<Note>() // Removed in-memory list
    private lateinit var noteAdapter: NoteAdapter // Declare as member variable
    private lateinit var noteDao: NoteDao

    private val addEditNoteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val title = data?.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
            val description = data?.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)

            if (title.isNullOrEmpty() || description.isNullOrEmpty()) {
                Toast.makeText(this, "Title or description cannot be empty", Toast.LENGTH_SHORT).show()
                return@registerForActivityResult // Exit if essential data is missing
            }

            val noteId = data?.getLongExtra(AddEditNoteActivity.EXTRA_NOTE_ID, -1L)

            if (noteId != null && noteId != -1L) { // Existing note was edited
                val updatedNote = Note(
                    id = noteId, // Use the existing ID
                    title = title,
                    description = description,
                    timestamp = System.currentTimeMillis(),
                    imagePath = null // Assuming imagePath is not editable for now or handled elsewhere
                )
                lifecycleScope.launch {
                    noteDao.update(updatedNote)
                }
                Toast.makeText(this, "Note updated successfully!", Toast.LENGTH_SHORT).show()
            } else { // New note
                val newNote = Note(
                    // id = 0L, // Handled by default in Note data class for autoGenerate
                    title = title,
                    description = description,
                    timestamp = System.currentTimeMillis(),
                    imagePath = null
                )
                lifecycleScope.launch {
                    noteDao.insert(newNote)
                }
                Toast.makeText(this, "Note created successfully!", Toast.LENGTH_SHORT).show()
            }
            // noteAdapter.updateNotes(notesList.toList()) // Removed: Flow observer will update UI
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        // Initialize Database and DAO
        val database = AppDatabase.getDatabase(applicationContext)
        noteDao = database.noteDao()

        // Initialize RecyclerView
        val recyclerViewNotes = findViewById<RecyclerView>(R.id.recyclerViewNotes)
        recyclerViewNotes.layoutManager = LinearLayoutManager(this) // Ensure layout manager is set

        // Initialize and set adapter
        noteAdapter = NoteAdapter(mutableListOf()) { clickedNote -> // Initialize with empty list
            // This is the onItemClick lambda
            val intent = Intent(this, AddEditNoteActivity::class.java)
            // Pass note data to AddEditNoteActivity using the consolidated keys
            intent.putExtra(AddEditNoteActivity.EXTRA_NOTE_ID, clickedNote.id)
            intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, clickedNote.title)
            intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, clickedNote.description)

            addEditNoteLauncher.launch(intent) // Use the existing launcher
        }
        recyclerViewNotes.adapter = noteAdapter // Ensure adapter is set after initialization

        // Observe Notes from Database
        lifecycleScope.launch {
            noteDao.getAllNotes().collectLatest { notesFromDb ->
                noteAdapter.updateNotes(notesFromDb)
            }
        }

        // Initialize UI elements for click listeners
        val menuIcon = findViewById<ImageView>(R.id.imageViewMenu)
        val addIcon = findViewById<ImageView>(R.id.imageViewAdd)
        val newNoteButton = findViewById<Button>(R.id.buttonNewNote)
        val clipWebpageButton = findViewById<Button>(R.id.buttonClipWebpage)

        // Set placeholder click listeners
        menuIcon.setOnClickListener {
            // Toast.makeText(this, "Hamburger menu clicked", Toast.LENGTH_SHORT).show() // Replaced by drawer open
            drawerLayout.openDrawer(GravityCompat.START)
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

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_notes -> {
                    Toast.makeText(this, "Notes selected", Toast.LENGTH_SHORT).show()
                    // TODO: Handle Notes navigation
                }
                R.id.nav_archive -> {
                    Toast.makeText(this, "Archive selected", Toast.LENGTH_SHORT).show()
                    // TODO: Handle Archive navigation
                }
                R.id.nav_settings -> {
                    Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show()
                    // TODO: Handle Settings navigation
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START) // Close the drawer
            true // Indicate event was handled
        }

        // Handle back press for drawer
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            }
        })
    }
}