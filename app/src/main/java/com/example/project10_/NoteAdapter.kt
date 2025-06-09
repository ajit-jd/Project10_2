package com.example.project10_

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(private val notes: List<Note>) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.descriptionTextView.text = note.description
        // Handle imagePath (String?) - For now, use a placeholder if null or empty
        // In a real app, you'd load the image from the path using a library like Glide or Picasso
        if (note.imagePath != null) {
            // TODO: Load image from note.imagePath
            // For now, setting a placeholder if imagePath is present but not loadable by default
            holder.thumbnailImageView.setImageResource(android.R.drawable.ic_menu_gallery)
        } else {
            holder.thumbnailImageView.setImageResource(android.R.drawable.ic_menu_gallery) // Default placeholder
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewNoteTitle)
        val descriptionTextView: TextView = itemView.findViewById(R.id.textViewNoteDescription)
        val thumbnailImageView: ImageView = itemView.findViewById(R.id.imageViewThumbnail)
    }
}
