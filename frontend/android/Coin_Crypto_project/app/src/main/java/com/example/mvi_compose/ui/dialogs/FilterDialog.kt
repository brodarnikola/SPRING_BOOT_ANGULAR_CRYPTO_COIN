package com.example.mvi_compose.ui.dialogs

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FilterDialog(
    onDismiss: () -> Unit,
    onApply: (String, String, String, String, String) -> Unit
) {
    var eurFilter by remember { mutableStateOf("") }
    var usdFilter by remember { mutableStateOf("") }
    var gbpFilter by remember { mutableStateOf("") }
    var dateFromState by remember { mutableStateOf("") }
    var dateToState by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Filter Exchange Rates") },
        text = {
            Column {
                OutlinedTextField(
                    value = eurFilter,
                    onValueChange = { eurFilter = it },
                    label = { Text("EUR Rate Filter") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = usdFilter,
                    onValueChange = { usdFilter = it },
                    label = { Text("USD Rate Filter") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = gbpFilter,
                    onValueChange = { gbpFilter = it },
                    label = { Text("GBP Rate Filter") },
                    modifier = Modifier.fillMaxWidth()
                )
                DatePickerField(
                    label = "From Date",
                    date = dateFromState,
                    onDateChange = { dateFromState = it }
                )
                DatePickerField(
                    label = "To Date",
                    date = dateToState,
                    onDateChange = { dateToState = it }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onApply(
                    eurFilter,
                    usdFilter,
                    gbpFilter,
                    dateFromState,
                    dateToState
                )
            }) {
                Text("Apply")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun DatePickerField(
    label: String,
    date: String,
    onDateChange: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // Parse the initial date
    val initialDate = try {
        dateFormatter.parse(date)?.let {
            calendar.time = it
            calendar
        } ?: Calendar.getInstance()
    } catch (e: Exception) {
        Calendar.getInstance()
    }

    val year = initialDate.get(Calendar.YEAR)
    val month = initialDate.get(Calendar.MONTH)
    val day = initialDate.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                onDateChange(dateFormatter.format(calendar.time))
            },
            year,
            month,
            day
        )
    }

    OutlinedTextField(
        value = date,
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() },
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Select date",
                modifier = Modifier.clickable { datePickerDialog.show() }
            )
        }
    )
}