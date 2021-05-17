package hu.bme.aut.android.classmanageractivity.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.classmanageractivity.Fragments.DatePickerDialogFragment
import hu.bme.aut.android.classmanageractivity.Fragments.EndDatePicker
import hu.bme.aut.android.classmanageractivity.R
import hu.bme.aut.android.classmanageractivity.data.EventItem
import kotlinx.android.synthetic.main.classevent_item.view.*
import kotlinx.android.synthetic.main.events_main.*
import kotlinx.android.synthetic.main.item_event_list.*
import kotlinx.android.synthetic.main.item_event_list.view.*
import java.util.*
import kotlinx.android.synthetic.main.item_event_list.view.EventItem as EventItem1


class EventAdapter (private val listener: EventItemClickListener, private val date: FragmentManager) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    private val items = mutableListOf<EventItem>()

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val classNameTextView: TextView
        val eventNameTextView: TextView
        val startDateTextView: TextView
        val endDateTextView: TextView
        val removeButton: Button
        val startButton: Button
        val endButton: Button
        val categoryTextView: TextView
        val itemLayout: View

        var item: EventItem? = null

        init {
            eventNameTextView = itemView.tvEventName
            classNameTextView = itemView.tvEventClass
            startDateTextView = itemView.tvEventStart
            endDateTextView = itemView.tvEventEnd
            startButton = itemView.AddStart
            endButton = itemView.AddEnd
            categoryTextView = itemView.tvCategory
            itemLayout = itemView

            startButton.setOnClickListener(){
                item?.let {
                    DatePickerDialogFragment(it, startDateTextView).show(date,"DATE_TAG")

                }
            }

            endButton.setOnClickListener(){
                item?.let {
                    EndDatePicker(it, endDateTextView).show(date,"DATE_TAG")
                }
            }

            removeButton = itemView.findViewById(R.id.EventItemRemoveButton)
            removeButton.setOnClickListener(){
                item?.let {
                    listener.onEventItemDeleted(it)
                }
            }
        }
    }


    interface EventItemClickListener {
        fun onItemChanged(item: EventItem)
        fun onEventItemDeleted(item: EventItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_event_list, parent, false)
        return EventViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }


    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = items[position]
        holder.eventNameTextView.text = item.eventName
        holder.classNameTextView.text = item.className
        holder.startDateTextView.text = item.startDate
        holder.endDateTextView.text = item.endDate
        holder.categoryTextView.text = item.category?.name
        holder.item = item

        val current = Calendar.getInstance()
        val currentyear = current.get(Calendar.YEAR)
        val currentmonth = (current.get(Calendar.MONTH) + 1)
        val currentday = current.get(Calendar.DAY_OF_MONTH)
        var nulla = ""
        var monthnulla = ""
        if (currentday < 10) {
            nulla = "0"
        }
        if (currentmonth < 10) {
            monthnulla = "0"
        }
        val dataasstring =
            currentyear.toString() + "." + monthnulla + currentmonth.toString() + "." + nulla + currentday.toString()
        when {
            item.startDate!! > dataasstring -> {
                holder.itemLayout.setBackgroundResource(R.drawable.bordergreen);
            }
            item.endDate!! > dataasstring -> {
                holder.itemLayout.setBackgroundResource(R.drawable.borderorange);
            }
            else -> holder.itemLayout.setBackgroundResource(R.drawable.borderred)
        }
    }

    fun update(eventItems: List<EventItem>){
        items.clear()
        items.addAll(eventItems)
        notifyDataSetChanged()
    }

    fun deleteItem(item : EventItem){
        items.remove(item)
        notifyDataSetChanged()
    }
}