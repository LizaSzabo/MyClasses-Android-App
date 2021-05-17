package hu.bme.aut.android.classmanageractivity.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.classmanageractivity.Fragments.DatePickerDialogFragment
import hu.bme.aut.android.classmanageractivity.Fragments.EndDatePicker
import hu.bme.aut.android.classmanageractivity.R
import hu.bme.aut.android.classmanageractivity.data.EventItem
import kotlinx.android.synthetic.main.classevent_item.view.*
import kotlinx.android.synthetic.main.item_event_list.view.*
import java.util.*

class ClassEventsAdapter (private val listener: ClassEventItemClickListener, private val date: FragmentManager) : RecyclerView.Adapter<ClassEventsAdapter.ClassEventViewHolder>() {
    private val items = mutableListOf<EventItem>()

    inner class ClassEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val classNameTextView: TextView = itemView.EventClass
        val eventNameTextView: TextView
        val startDateTextView: TextView
        val endDateTextView: TextView
        val eventcategory: TextView
        val removeButton: Button
        val startButton: Button
        val endButton: Button
        val itemLayout: View

        var item: EventItem? = null

        init {
            eventNameTextView = itemView.EventName
            startDateTextView = itemView.EventStart
            endDateTextView = itemView.EventEnd
            startButton = itemView.btnAddStart
            endButton = itemView.btnAddEnd
            eventcategory = itemView.findViewById(R.id.Category)
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

            removeButton = itemView.findViewById(R.id.ItemRemoveButton)
            removeButton.setOnClickListener(){
                item?.let {
                    listener.onEventItemDeleted(it)
                }
            }
        }
    }


    interface ClassEventItemClickListener {
        fun onItemChanged(item: EventItem)
        fun onEventItemDeleted(item: EventItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassEventViewHolder {

        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.classevent_item, parent, false)
        return ClassEventViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }


    override fun onBindViewHolder(holder: ClassEventViewHolder, position: Int) {
        val item = items[position]
        holder.classNameTextView.text = item.className
        holder.eventNameTextView.text = item.eventName
        holder.startDateTextView.text = item.startDate
        holder.endDateTextView.text = item.endDate
        holder.eventcategory.text = item.category?.name
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


    fun addItem(item : EventItem){
        items.add(item)
        notifyItemInserted(items.size - 1)
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