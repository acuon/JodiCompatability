package com.jodi.companioncompatibility.utils.dialogs

import android.app.Activity
import android.content.Context
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import com.jodi.companioncompatibility.R
import com.jodi.companioncompatibility.databinding.CustomDatePickerDialogBinding
import com.jodi.companioncompatibility.utils.extensions.setDialogMetrics
import java.util.Calendar

class CustomDatePickerDialog(
    private val context: Context,
    private val activity: Activity,
    private val maxDate: Int? = null,
    private val onDateSelected: (String) -> Unit
) : Dialog(context, R.style.CustomAlertDialog) {

    private lateinit var dateBinding: CustomDatePickerDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dateBinding = CustomDatePickerDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(dateBinding.root)

        val today = Calendar.getInstance()

        maxDate?.let {
            val minDateCalendar = Calendar.getInstance()
            minDateCalendar.add(Calendar.YEAR, -it)
            dateBinding.datePicker.maxDate = minDateCalendar.timeInMillis
        }

        dateBinding.datePicker.init(
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { _, _, _, _ -> }

        dateBinding.btnOk.setOnClickListener {
            val dayOfMonth = dateBinding.datePicker.dayOfMonth
            val month = dateBinding.datePicker.month + 1
            val year = dateBinding.datePicker.year

            val formattedDay = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
            val formattedMonth = if (month < 10) "0$month" else "$month"

            val getDate = "$formattedDay-$formattedMonth-$year"

            onDateSelected.invoke(getDate)
            dismiss()
        }

        dateBinding.btnCancel.setOnClickListener { dismiss() }
        setCancelable(false)
        activity.setDialogMetrics(this, 0.7f)
    }
}
