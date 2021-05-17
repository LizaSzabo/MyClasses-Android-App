package hu.bme.aut.android.classmanageractivity.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Color.green
import android.icu.text.DateFormat.DAY
import android.icu.text.DateTimePatternGenerator.DAY
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.android.classmanageractivity.EventsActivity
import hu.bme.aut.android.classmanageractivity.R
import hu.bme.aut.android.classmanageractivity.adapter.EventAdapter
import hu.bme.aut.android.classmanageractivity.data.EventItem
import hu.bme.aut.android.shoppinglist.data.ClassManagerDatabase
import kotlinx.android.synthetic.main.activity_class.*
import kotlinx.android.synthetic.main.classevent_item.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.events_main.*
import kotlinx.android.synthetic.main.item_class_list.*
import kotlinx.android.synthetic.main.item_event_list.*
import java.util.*
import kotlin.concurrent.thread


class MainEventsFragment(val data: FragmentManager) : Fragment(R.layout.events_main),  EventAdapter.EventItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EventAdapter
    private lateinit var database: ClassManagerDatabase
    private var sortend: Boolean = false


    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sortbtn.setOnClickListener{
            sortend = !sortend
            loadItemsInBackground()
        }

        database = activity?.applicationContext?.let {
            Room.databaseBuilder(
                it,
                ClassManagerDatabase::class.java,
                "class-list"
            ) .build()
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
            var items = database.eventItemDao().getAll()
            if(sortend) {items = database.eventItemDao().getAllEnd()}

            activity?.runOnUiThread {
                adapter.update(items)
            }
        }
    }

   override fun onItemChanged(item: EventItem) {
        thread{
            database.eventItemDao().update(item)
            Log.d("MainEventsFragment", "EventItem update was successful")
        }
    }

    override fun onEventItemDeleted(item: EventItem) {
        thread{
            database.eventItemDao().deleteItem(item)
            Log.d("MainEventsFragment", "EventItem delete was successful")
            activity?.runOnUiThread {
                adapter.deleteItem(item)
            }
        }
    }
}