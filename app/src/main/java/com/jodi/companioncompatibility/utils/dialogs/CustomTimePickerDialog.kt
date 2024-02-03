package com.jodi.companioncompatibility.utils.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.jodi.companioncompatibility.R
import com.jodi.companioncompatibility.databinding.CustomTimePickerDialogBinding
import com.jodi.companioncompatibility.utils.extensions.setDialogMetrics
import java.util.Calendar

class CustomTimePickerDialog(
    private val context: Context,
    private val activity: Activity,
    private val onTimeSelected: (String) -> Unit
) : Dialog(context, R.style.CustomAlertDialog) {

    private lateinit var timeBinding: CustomTimePickerDialogBinding
    private var timeValue: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        timeBinding = CustomTimePickerDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(timeBinding.root)

        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        updateTime(hour, minute)

        timeBinding.timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            updateTime(hourOfDay, minute)
        }

        timeBinding.btnOk.setOnClickListener {
            onTimeSelected.invoke(timeValue)
            dismiss()
        }

        timeBinding.btnCancel.setOnClickListener { dismiss() }
        setCancelable(false)
        activity.setDialogMetrics(this, 0.7f)
    }

    private fun updateTime(hour: Int, minute: Int) {
        val amPm: String
        val _formattedHour: Int

        if (hour == 0) {
            _formattedHour = 12
            amPm = "AM"
        } else if (hour < 12) {
            _formattedHour = hour
            amPm = "AM"
        } else if (hour == 12) {
            _formattedHour = 12
            amPm = "PM"
        } else {
            _formattedHour = hour - 12
            amPm = "PM"
        }
        val formattedHour = if (_formattedHour < 10) "0$_formattedHour" else "$_formattedHour"
        val formattedMinute = if (minute < 10) "0$minute" else minute.toString()
        timeValue = "${formattedHour}:${formattedMinute} $amPm"
    }

}
