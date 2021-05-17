package hu.bme.aut.android.classmanageractivity.data

import androidx.room.*
import hu.bme.aut.android.myclasses.data.ClassItem

@Dao
interface EventItemDao {
    @Query("SELECT * FROM eventitem ORDER BY startDate")
    fun getAll(): List<EventItem>

    @Query("SELECT * FROM eventitem ORDER BY endDate")
    fun getAllEnd(): List<EventItem>

    @Query("SELECT * FROM eventitem WHERE className = :name ORDER BY startDate")
    fun getClass(name : String): List<EventItem>

    @Query("SELECT * FROM eventitem WHERE className = :name ORDER BY endDate")
    fun getClassEnd(name : String): List<EventItem>

    @Query("SELECT * FROM eventitem WHERE category = :num ORDER BY startDate")
    fun getEvent(num : Int): List<EventItem>

    @Query("SELECT * FROM eventitem WHERE category = :num ORDER BY endDate")
    fun getEventEnd(num : Int): List<EventItem>

    @Insert
    fun insert(eventItems: EventItem): Long

    @Update
    fun update(eventItem: EventItem)

    @Delete
    fun deleteItem(eventItem: EventItem)
}