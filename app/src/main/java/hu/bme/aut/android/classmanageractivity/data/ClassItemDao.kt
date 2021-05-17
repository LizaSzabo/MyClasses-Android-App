package hu.bme.aut.android.myclasses.data

import androidx.room.*

@Dao
interface ClassItemDao {
    @Query("SELECT * FROM classitem")
    fun getAll(): List<ClassItem>

    @Insert
    fun insert(classItems: ClassItem): Long

    @Update
    fun update(classItem: ClassItem)

    @Delete
    fun deleteItem(classItem: ClassItem)
}