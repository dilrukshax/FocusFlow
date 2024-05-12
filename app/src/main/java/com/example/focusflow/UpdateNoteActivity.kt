package com.example.focusflow

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.focusflow.databinding.ActivityUpdateNoteBinding
import java.util.*

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var db: NoteDatabaseHelp
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelp(this)

        noteId = intent.getIntExtra("note_id", -1)
        if (noteId == -1) {
            finish()
            return
        }

        val note = db.getNoteByID(noteId)
        binding.UpdateEditTxtTitle.setText(note.title)
        binding.UpdateEditTxtContent.setText(note.content)

        binding.updateSaveNoteBtn.setOnClickListener {
            val newTitle = binding.UpdateEditTxtTitle.text.toString()
            val newContent = binding.UpdateEditTxtContent.text.toString()
            val updatedNote = Note(
                noteId,
                newTitle,
                newContent,
                note.date, // Keep the existing date
                note.priority // Keep the existing priority
            )
            db.updateNote(updatedNote)
            finish()
            Toast.makeText(this, "Saved Changes", Toast.LENGTH_SHORT).show()
        }
    }
}
