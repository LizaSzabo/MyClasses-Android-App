package hu.bme.aut.android.classmanageractivity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.android.myclasses.adapter.ClassAdapter
import hu.bme.aut.android.myclasses.data.ClassItem
import hu.bme.aut.android.shoppinglist.data.ClassManagerDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), ClassAdapter.ClassItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ClassAdapter
    private lateinit var database: ClassManagerDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAdd.setOnClickListener {
            if (className.text.isEmpty()) {
                Snackbar.make(findViewById(android.R.id.content), R.string.warning_message, Snackbar.LENGTH_SHORT).setTextColor(
                        Color.WHITE).setBackgroundTint(Color.RED).setDuration(2000).show()

                return@setOnClickListener
            }
             onClassItemCreated(getClassItem())
        }

        btnEvents.setOnClickListener{
            val EventsIntent = Intent(this, EventsActivity::class.java)
            startActivity(EventsIntent)
        }
        val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE EventItem ADD COLUMN category INTEGER")
            }
        }
        database = Room.databaseBuilder(
            applicationContext,
            ClassManagerDatabase::class.java,
            "class-list"
        ).addMigrations(MIGRATION_4_5)
            .build()

        initRecyclerView()
    }


    private fun initRecyclerView() {
        recyclerView = MainRecyclerView
        adapter = ClassAdapter(this, this)
        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.classItemDao().getAll()
            runOnUiThread {
               adapter.update(items)
            }
        }
    }

    private fun getClassItem() = ClassItem(
            id = null,
            name = className.text.toString()
    )

    override fun onItemChanged(item: ClassItem){
        thread{
            database.classItemDao().update(item)
            Log.d("MainActivity", "ClassItem update was successful")
        }
    }

    override fun onItemDeleted(item: ClassItem) {
        thread{
            database.classItemDao().deleteItem(item)
            Log.d("MainActivity", "ClassItem delete was successful")
            val items = database.classItemDao().getAll()
            runOnUiThread {
                adapter.deleteItem(item)
            }
        }
    }


    private fun onClassItemCreated(newItem: ClassItem) {
        thread {
            val newId = database.classItemDao().insert(newItem)
            val newClassItem = newItem.copy(
                id = newId
            )
            runOnUiThread {
                adapter.addItem(newClassItem)
            }
        }
    }
}