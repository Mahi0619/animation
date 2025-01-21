package com.anime.application.base

import android.app.DatePickerDialog
import android.content.Context
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    fun showPastDatePickerDialog(context: Context, editText: EditText) {
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            editText.setText(formatDate(cal.time))
        }

        // Create the DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            context,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        // Restrict the selection to past dates only
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        // Show the DatePickerDialog
        datePickerDialog.show()
    }

    private fun formatDate(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd").format(date)
    }




}
