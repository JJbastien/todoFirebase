package com.example.todofirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.todofirebase.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*


class MainActivity : AppCompatActivity(), UpdateAndDelete {

 lateinit var database : DatabaseReference
    var toDOLIst: MutableList<TodoModel>? = null
    lateinit var adapter: ToDoAdapter
    private var listViewItem: ListView?=null




   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        listViewItem=findViewById(R.id.item_listView) as ListView
        database = FirebaseDatabase.getInstance().reference
        fab.setOnClickListener {view ->

            val alertDialog = AlertDialog.Builder(this)
            val textEditText = EditText(this)
            alertDialog.setMessage("add todo item")
            alertDialog.setTitle("enter todo item")
            alertDialog.setView(textEditText)
            alertDialog.setPositiveButton("Add"){dialog, i->
                val todoItemData = TodoModel.createList()
                todoItemData.itemDataText = textEditText.text.toString()
                todoItemData.done = false

                val newItemData = database.child("todo").push()
                todoItemData.UID = newItemData.key
                newItemData.setValue(todoItemData)
                dialog.dismiss()
                Toast.makeText(this, "item saved", Toast.LENGTH_SHORT).show()
                Log.d("TAG", todoItemData.toString())
            }
            alertDialog.show()
        }
        toDOLIst = mutableListOf<TodoModel>()
        adapter = ToDoAdapter(this, toDOLIst!!)
        listViewItem!!.adapter=adapter

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                toDOLIst!!.clear()
                addItemToList(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
               Toast.makeText(applicationContext, "no Item added", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun addItemToList(snapshot: DataSnapshot){
    val items= snapshot.children.iterator()
        if(items.hasNext()){
            val toDoIndexedValue = items.next()
            val itemsIterator = toDoIndexedValue.children.iterator()

            while (itemsIterator.hasNext()){
                val currentItem = itemsIterator.next()
                val toDoItemData = TodoModel.createList()
                val map = currentItem.getValue() as HashMap<String, Any>

                toDoItemData.UID = currentItem.key
                toDoItemData.done= map.get("done") as  Boolean?
                toDoItemData.itemDataText = map.get("itemDataText") as String?
                toDOLIst!!.add(toDoItemData)
            }
        }
            adapter.notifyDataSetChanged()
    }

    override fun modifyItem(itemUID: String, isDone: Boolean) {
        val itemReference =database.child("todo").child(itemUID)
        itemReference.child("done").setValue(isDone)

    }

    override fun onItemDelete(itemUID: String) {
        val itemReference=database.child("todo").child(itemUID)
        itemReference.removeValue()
        adapter.notifyDataSetChanged()
    }
}