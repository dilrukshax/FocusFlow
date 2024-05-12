package com.example.focusflow

import DatePickerFragment
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.focusflow.databinding.ActivityNewNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class NewNote : AppCompatActivity() {

    private lateinit var binding: ActivityNewNoteBinding
    private lateinit var db: NoteDatabaseHelp
    private lateinit var selectedDate: Date
    private var selectedPriority: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelp(this)

        binding.saveNoteBtn.setOnClickListener {
            val title = binding.editTxtTitle.text.toString()
            val content = binding.editTxtContent.text.toString()

            // Check if date and priority are selected
            if (::selectedDate.isInitialized && selectedPriority != -1) {
                val note = Note(0, title, content, selectedDate, selectedPriority)
                db.insertNote(note)
                finish()
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please select date and priority", Toast.LENGTH_SHORT).show()
            }
        }

        // Date Selection
        binding.selectDateBtn.setOnClickListener {
            showDatePickerDialog()
        }

        // Priority Radio Buttons
        binding.priorityRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val checkedRadioButton = findViewById<RadioButton>(checkedId)
            selectedPriority = when (checkedRadioButton.id) {
                R.id.radioLow -> Note.PRIORITY_LOW
                R.id.radioMedium -> Note.PRIORITY_MEDIUM
                R.id.radioHigh -> Note.PRIORITY_HIGH
                else -> -1
            }
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { date ->
            selectedDate = date
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.selectDateBtn.text = sdf.format(date)
        }
        datePicker.show(supportFragmentManager, "datePicker")
    }
}
