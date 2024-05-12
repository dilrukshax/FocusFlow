package com.example.focusflow

import java.util.Date

data class Note(val id: Int, val title: String, val content: String, val date: Date, val priority: Int) {
    companion object {
        const val PRIORITY_LOW = 1
        const val PRIORITY_MEDIUM = 2
        const val PRIORITY_HIGH = 3
    }
}
