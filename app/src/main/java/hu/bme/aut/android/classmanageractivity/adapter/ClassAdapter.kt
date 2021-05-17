package hu.bme.aut.android.myclasses.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.classmanageractivity.ClassActivity
import hu.bme.aut.android.classmanageractivity.R
import hu.bme.aut.android.myclasses.data.ClassItem
import kotlinx.android.synthetic.main.item_class_list.view.*


class ClassAdapter(private val listener: ClassItemClickListener, private val context: Context) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {
    private val items = mutableListOf<ClassItem>()

    inner class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val iconImageView: ImageButton
        val nameTextView: TextView
        val removeButton: Button

        var item: ClassItem? = null

        init {
            nameTextView = itemView.ClassNameText
            iconImageView = itemView.findViewById(R.id.classImg)
            iconImageView.setOnClickListener(){
                item?.let {
                    val classIntent =  Intent(context, ClassActivity::class.java)
                    classIntent.putExtra("Title", nameTextView.text)
                    context.startActivity(classIntent)
                }
            }
            removeButton = itemView.findViewById(R.id.ClassItemRemoveButton)
             removeButton.setOnClickListener(){
                 item?.let {
                     listener.onItemDeleted(it)
                 }
             }
        }
    }


    interface ClassItemClickListener {
        fun onItemChanged(item: ClassItem)
        fun onItemDeleted(item: ClassItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_class_list, parent, false)
        return ClassViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val item = items[position]
        holder.nameTextView.text = item.name
        holder.iconImageView.setImageResource(R.drawable.class_img)
        holder.item = item
    }

    fun addItem(item : ClassItem){
        Log.i("tag",item.name)
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(classItems: List<ClassItem>){
        items.clear()
        items.addAll(classItems)
        notifyDataSetChanged()
    }

    fun deleteItem(item : ClassItem){
        items.remove(item)
        notifyDataSetChanged()
    }
}