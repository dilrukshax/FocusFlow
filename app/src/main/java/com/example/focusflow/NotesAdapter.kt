package com.example.focusflow

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class NotesAdapter(private var notes: List<Note>, private val context: Context) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    private val db: NoteDatabaseHelp = NoteDatabaseHelp(context)

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.txtTitleView)
        val contentTextView: TextView = itemView.findViewById(R.id.txtContentView)
        val dateTextView: TextView = itemView.findViewById(R.id.txtDateView) // Added date text view
        val priorityTextView: TextView = itemView.findViewById(R.id.txtPriorityView) // Added priority text view
        val updateButton: ImageView = itemView.findViewById(R.id.noteUpdateBtn)
        val deleteButton: ImageView = itemView.findViewById(R.id.noteDeleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content
        holder.dateTextView.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(note.date) // Display formatted date
        holder.priorityTextView.text = getPriorityText(note.priority) // Set priority text

        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateNoteActivity::class.java).apply {
                putExtra("note_id", note.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            db.deleteNote(note.id)
            refreshData(db.getAllNotes())
            Toast.makeText(holder.itemView.context, "Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    fun refreshData(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }

    private fun getPriorityText(priority: Int): String {
        return when (priority) {
            Note.PRIORITY_LOW -> "Low"
            Note.PRIORITY_MEDIUM -> "Medium"
            Note.PRIORITY_HIGH -> "High"
            else -> "Unknown"
        }
    }
}
