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
import kotlinx.android.synthetic.main.zhevents_main.*
import java.util.*
import kotlin.concurrent.thread

class ZHEventsFragment(val data: FragmentManager) : Fragment(R.layout.zhevents_main) ,  EventAdapter.EventItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EventAdapter
    private lateinit var database: ClassManagerDatabase
    private var sortend: Boolean = false

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sortzhbtn.setOnClickListener{
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
            var items = database.eventItemDao().getEvent(1)
            if(sortend) {items = database.eventItemDao().getEventEnd(1)}
            activity?.runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onItemChanged(item: EventItem) {
        thread {
            database.eventItemDao().update(item)
            Log.d("ZHEventsFragment", "EventItem update was successful")
        }
    }

    override fun onEventItemDeleted(item: EventItem) {
        thread {
            database.eventItemDao().deleteItem(item)
            Log.d("ZHEventsFragment", "EventItem delete was successful")
            activity?.runOnUiThread {
                adapter.deleteItem(item)
            }
        }
    }
}
