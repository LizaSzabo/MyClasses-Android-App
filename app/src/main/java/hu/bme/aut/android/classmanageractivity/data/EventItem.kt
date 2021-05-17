package hu.bme.aut.android.classmanageractivity.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.*

@Entity(tableName = "eventitem")
data class EventItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "className") var className: String,
    @ColumnInfo(name = "eventName") val eventName: String,
    @ColumnInfo(name = "startDate") var startDate: String?,
    @ColumnInfo(name = "endDate") var endDate: String?,
    @ColumnInfo(name = "category") val category: EventCategory?
) {
    enum class EventCategory {
        HF, ZH, VIZSGA;

        companion object {
            @JvmStatic
            @TypeConverter
            fun getByOrdinal(ordinal: Int): EventCategory? {
                return values().find { it.ordinal == ordinal }
            }

            @JvmStatic
            @TypeConverter
            fun toInt(category: EventCategory): Int {
                return category.ordinal
            }
        }
    }
}


