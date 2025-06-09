package com.example.project10_

data class Note(
    val id: Long, // Added an ID field, common for database entities
    val title: String,
    val description: String,
    val timestamp: Long, // Added timestamp for sorting or display
    val imagePath: String? = null // Optional path to an image file
)
