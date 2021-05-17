package hu.bme.aut.android.shoppinglist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.bme.aut.android.classmanageractivity.data.EventItem
import hu.bme.aut.android.classmanageractivity.data.EventItemDao
import hu.bme.aut.android.myclasses.data.ClassItem
import hu.bme.aut.android.myclasses.data.ClassItemDao


@Database(entities = [ClassItem::class, EventItem::class], version = 5)
@TypeConverters(value = [EventItem.EventCategory::class])
abstract class ClassManagerDatabase : RoomDatabase(){
    abstract fun classItemDao() : ClassItemDao
    abstract fun eventItemDao(): EventItemDao
}

