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

    private val addEditNoteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // For now, just show a toast. Later, this is where you'd refresh the note list.
            Toast.makeText(this, "Note saved successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

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