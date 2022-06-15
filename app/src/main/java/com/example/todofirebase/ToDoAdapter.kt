package com.example.todofirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.content.Context
import android.text.style.UpdateAppearance
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView

class ToDoAdapter( context: Context , todoList:MutableList<TodoModel>):BaseAdapter()  {
    private val itemList = todoList
    private val inflater:LayoutInflater = LayoutInflater.from(context)
    private var updateAndDelete:UpdateAndDelete=context as UpdateAndDelete

    override fun getCount(): Int {
        return itemList.size
    }

    override fun getItem(position: Int): Any {
      return itemList.get(position)
    }

    override fun getItemId(position: Int): Long {
       return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val UID: String = itemList.get(position).UID as String
        val itemTextData = itemList.get(position).itemDataText as String
        val done: Boolean = itemList.get(position).done as Boolean
        val view: View
        val viewHolder: ListViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.row_items_layout, convertView, false)
            viewHolder = ListViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ListViewHolder
        }
        viewHolder.textlabel.text = itemTextData
        viewHolder.isDone.isChecked = done
        viewHolder.isDone.setOnClickListener {
            updateAndDelete.modifyItem(UID, !done)
        }

        viewHolder.idDeleted.setOnClickListener{
            updateAndDelete.onItemDelete(UID)
        }
        return view
    }

   private class ListViewHolder(row: View?) {
    val textlabel: TextView = row!!.findViewById(R.id.item_textView) as TextView
        val isDone: CheckBox = row!!.findViewById(R.id.checkbox) as  CheckBox
        val idDeleted: ImageButton = row!!.findViewById(R.id.close) as ImageButton
    }
}