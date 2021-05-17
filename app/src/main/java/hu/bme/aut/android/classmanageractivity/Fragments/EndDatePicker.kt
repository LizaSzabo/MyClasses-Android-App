package hu.bme.aut.android.classmanageractivity.Fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.classmanageractivity.data.EventItem
import java.util.*

class EndDatePicker(val item: EventItem, val eventend: TextView) : DialogFragment(), DatePickerDialog.OnDateSetListener{
    private lateinit var onDateSelectedListener: OnDateSelectedListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context !is OnDateSelectedListener){
            throw RuntimeException("The activity does not implement the OnDateSelectedListener interface")
        }
        onDateSelectedListener = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        onDateSelectedListener.onDateSelectedEnd(year, month, dayOfMonth, item, eventend)
    }

    interface OnDateSelectedListener {
        fun onDateSelectedEnd(year: Int, month: Int, day: Int, item: EventItem, eventend: TextView)
    }
}