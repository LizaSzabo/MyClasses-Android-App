package hu.bme.aut.android.classmanageractivity.Fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.classmanageractivity.R
import hu.bme.aut.android.classmanageractivity.data.EventItem


class NewClassEventItem : DialogFragment()  {
    private lateinit var EventNameEditText: EditText
    private lateinit var categorySpinner: Spinner

    interface NewClassEventItemDialogListener {
        fun onEventItemCreated(newItem: EventItem)
    }

    private lateinit var listener: NewClassEventItemDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewClassEventItemDialogListener
            ?: throw RuntimeException("Activity must implement the NewEventItemDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("New Event")
            .setView(getContentView())
            .setPositiveButton("OK") { _, _ ->
                if (true) {
                    listener.onEventItemCreated(getEventItem())
                }
            }
            .setNegativeButton("cancel", null)
            .create()
    }

    companion object {
        const val TAG = "NewEventItemDialogFragment"
    }

    private fun getContentView(): View {
        val contentView =
            LayoutInflater.from(context).inflate(R.layout.dialog_new_classevent, null)
             EventNameEditText = contentView.findViewById(R.id.ClassEventItemNameEditText)
            categorySpinner = contentView.findViewById(R.id.EventItemCategorySpinner)
            categorySpinner.adapter = ArrayAdapter(
                 requireContext(),
                 android.R.layout.simple_spinner_dropdown_item,
                 resources.getStringArray(R.array.category_items)
             )
        return contentView
    }

    private fun getEventItem() = EventItem(
        id = null,
        className= "",
        eventName = EventNameEditText.text.toString(),
        startDate = "",
        endDate = "",
        category= EventItem.EventCategory.getByOrdinal(categorySpinner.selectedItemPosition)
            ?: EventItem.EventCategory.HF
    )
}