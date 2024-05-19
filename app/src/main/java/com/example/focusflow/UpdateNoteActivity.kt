package com.example.focusflow

import DatePickerFragment
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.focusflow.databinding.ActivityUpdateNoteBinding
import java.text.SimpleDateFormat
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
        binding.UpdateSelectDateBtn.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(note.date)
        setPrioritySelection(note.priority)

        binding.UpdateSelectDateBtn.setOnClickListener {
            showDatePickerDialog()
        }

        binding.updateSaveNoteBtn.setOnClickListener {
            val newTitle = binding.UpdateEditTxtTitle.text.toString()
            val newContent = binding.UpdateEditTxtContent.text.toString()
            val newDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(binding.UpdateSelectDateBtn.text.toString()) ?: Date()
            val priority = getPrioritySelection()
            val updatedNote = Note(noteId, newTitle, newContent, newDate, priority)
            db.updateNote(updatedNote)
            finish()
            Toast.makeText(this, "Saved Changes", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { date ->
            binding.UpdateSelectDateBtn.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
        }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun setPrioritySelection(priority: Int) {
        when (priority) {
            Note.PRIORITY_LOW -> binding.radioLow.isChecked = true
            Note.PRIORITY_MEDIUM -> binding.radioMedium.isChecked = true
            Note.PRIORITY_HIGH -> binding.radioHigh.isChecked = true
        }
    }

    private fun getPrioritySelection(): Int {
        return when {
            binding.radioLow.isChecked -> Note.PRIORITY_LOW
            binding.radioMedium.isChecked -> Note.PRIORITY_MEDIUM
            binding.radioHigh.isChecked -> Note.PRIORITY_HIGH
            else -> Note.PRIORITY_LOW // Default to low priority if none selected
        }
    }
}
