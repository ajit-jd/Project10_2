package com.example.project10_

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L, // Default to 0 for Room to auto-generate IDs for new notes
    val title: String,
    val description: String,
    val timestamp: Long,
    val imagePath: String? = null
)
