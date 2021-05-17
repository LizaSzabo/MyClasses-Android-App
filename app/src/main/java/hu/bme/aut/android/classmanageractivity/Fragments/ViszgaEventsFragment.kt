package hu.bme.aut.android.classmanageractivity.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import hu.bme.aut.android.classmanageractivity.R
import hu.bme.aut.android.classmanageractivity.adapter.EventAdapter
import hu.bme.aut.android.classmanageractivity.data.EventItem
import hu.bme.aut.android.shoppinglist.data.ClassManagerDatabase
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.hfevents_main.*
import kotlinx.android.synthetic.main.viszgaevents_main.*
import kotlin.concurrent.thread

class ViszgaEventsFragment(val data: FragmentManager) : Fragment(R.layout.viszgaevents_main),  EventAdapter.EventItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EventAdapter
    private lateinit var database: ClassManagerDatabase
    private var sortend: Boolean = false

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sortvizsgabtn.setOnClickListener{
            sortend = !sortend
            loadItemsInBackground()
        }

        database = activity?.applicationContext?.let {
            Room.databaseBuilder(
                it,
                ClassManagerDatabase::class.java,
                "class-list"
            ).build()
        }!!

        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = EventsRecyclerView
        adapter = EventAdapter(this, data)
        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(getContext())
        recyclerView.adapter = adapter
    }

    private fun loadItemsInBackground() {
        thread {
            var items = database.eventItemDao().getEvent(2)
            if(sortend) {items = database.eventItemDao().getEventEnd(2)}
            activity?.runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onItemChanged(item: EventItem) {
        thread {
            Log.d("VizsgaEventsFragment", "EventItem update was successful")
            database.eventItemDao().update(item)
        }
    }

    override fun onEventItemDeleted(item: EventItem) {
        thread {
            database.eventItemDao().deleteItem(item)
            Log.d("VizsgaEventsFragment", "EventItem delete was successful")
            activity?.runOnUiThread {
                adapter.deleteItem(item)
            }
        }
    }
}