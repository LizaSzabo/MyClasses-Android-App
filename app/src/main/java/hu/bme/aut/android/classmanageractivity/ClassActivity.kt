package hu.bme.aut.android.classmanageractivity

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.android.classmanageractivity.Fragments.DatePickerDialogFragment
import hu.bme.aut.android.classmanageractivity.Fragments.EndDatePicker
import hu.bme.aut.android.classmanageractivity.Fragments.NewClassEventItem
import hu.bme.aut.android.classmanageractivity.adapter.ClassEventsAdapter
import hu.bme.aut.android.classmanageractivity.data.EventItem
import hu.bme.aut.android.shoppinglist.data.ClassManagerDatabase
import kotlinx.android.synthetic.main.activity_class.*
import java.util.*
import kotlin.concurrent.thread


class ClassActivity : AppCompatActivity(), ClassEventsAdapter.ClassEventItemClickListener, NewClassEventItem.NewClassEventItemDialogListener, DatePickerDialogFragment.OnDateSelectedListener, EndDatePicker.OnDateSelectedListener{
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ClassEventsAdapter
    private lateinit var database: ClassManagerDatabase
    private lateinit var startdate: Calendar
    private lateinit var enddate: Calendar
    private lateinit var classname: String
    private  var sortend: Boolean = false
    private var setstart: Boolean = false
    private var setend: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class)

        val myIntent = getIntent()
        val title = myIntent.getStringExtra("Title")
        tvClassTitle.text = title
        if (title != null) {
            classname = title
        }

        btnAddClassEvent.setOnClickListener{
            NewClassEventItem().show(
                supportFragmentManager,
                NewClassEventItem.TAG
            )
        }

        SortByEndBtn.setOnClickListener{
            sortend = !sortend
            loadItemsInBackground()
        }

        database = Room.databaseBuilder(
            applicationContext,
            ClassManagerDatabase::class.java,
            "class-list"
        ).build()
        initRecyclerView()
    }


private fun initRecyclerView() {
    recyclerView = findViewById(R.id.ClassEventsRecyclerView)
    adapter = ClassEventsAdapter(this, supportFragmentManager)
    loadItemsInBackground()
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.adapter = adapter
}

    private fun loadItemsInBackground() {
        thread {
            var items = database.eventItemDao().getClass(classname)
            if(sortend) {items = database.eventItemDao().getClassEnd(classname)}
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onItemChanged(item: EventItem) {
        thread{
            database.eventItemDao().update(item)
            Log.d("ClassActivity", "EventItem update was successful")
        }
    }

    override fun onEventItemDeleted(item: EventItem) {
        thread{
            database.eventItemDao().deleteItem(item)
            Log.d("ClassActivity", "EventItem delete was successful")
            runOnUiThread {
                adapter.deleteItem(item)
            }
        }
    }

    override fun onEventItemCreated(newItem: EventItem) {
        thread {
            newItem.className = classname
            val newId = database.eventItemDao().insert(newItem)
            val newEventItem = newItem.copy(
                id = newId
            )
            runOnUiThread {
                adapter.addItem(newEventItem)
            }
        }
    }

    private fun requestNeededPermission() {
        if ( ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf( Manifest.permission.WRITE_CALENDAR),
                101)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            101 -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this@ClassActivity, "Permissions granted", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        this@ClassActivity,
                        "Permissions are NOT granted", Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }



    override fun onDateSelectedEnd(year: Int, month: Int, day: Int, item: EventItem, eventend: TextView) {
        enddate = Calendar.getInstance()
        enddate.set(year, month, day)
        val monthstring = month +1
        var nulla = ""
        var monthnulla = ""
        if(day < 10) {
            nulla = "0"
        }
        if(month < 9){
            monthnulla = "0"
        }
        val dataasstring = year.toString()+ "."+monthnulla+ monthstring.toString() + "."+nulla+ day.toString()

        eventend.text = dataasstring
        item.endDate = dataasstring
        onItemChanged(item)
        setend = true
        SaveInCalendar(item)

    }

    override fun onDateSelected(year: Int, month: Int, day: Int, item: EventItem, eventstart: TextView) {
        startdate = Calendar.getInstance()
        startdate.set(year, month, day)
        val monthstring = month +1
        var nulla = ""
        var monthnulla = ""
        if(day < 10) {
            nulla = "0"
        }
        if(month < 9){
            monthnulla = "0"
        }
        val dataasstring = year.toString()+ "."+ monthnulla+monthstring.toString() + "."+ nulla+ day.toString()

        eventstart.text = dataasstring
        item.startDate = dataasstring
        onItemChanged(item)
        setstart = true
        SaveInCalendar(item)

    }

    private fun SaveInCalendar(item: EventItem){
        requestNeededPermission()
        if(this::startdate.isInitialized &&this::enddate.isInitialized && setend && setstart) {
            if (startdate > enddate) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Az esemény naptárba írásához az end date legyen a start után!",
                    Snackbar.LENGTH_SHORT
                ).setTextColor(
                    Color.WHITE
                ).setBackgroundTint(Color.MAGENTA).setDuration(3000).show()
            }
            try {
                val values = ContentValues()
                values.put(CalendarContract.Events.DTSTART, startdate.getTimeInMillis())
                values.put(CalendarContract.Events.DTEND, enddate.getTimeInMillis())
                values.put(CalendarContract.Events.TITLE, item.eventName)
                values.put(CalendarContract.Events.DESCRIPTION, "Tárgy: ${item.className}")
                values.put(CalendarContract.Events.CALENDAR_ID, 1)
                values.put(
                    CalendarContract.Events.EVENT_TIMEZONE,
                    TimeZone.getDefault().getID()
                )
                val uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
                Log.d("LOG", uri.toString())
                setend = false
                setstart = false

            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
        else  Snackbar.make(findViewById(android.R.id.content), "Az esemény naptárba írásához válassza ki mindkét határidőt!", Snackbar.LENGTH_SHORT).setTextColor(
            Color.WHITE).setBackgroundTint(Color.RED).setDuration(2000).show()
    }

}