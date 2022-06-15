package com.example.todofirebase

class TodoModel {
    companion object{
        fun createList(): TodoModel = TodoModel()
    }
    var UID :String? = null
    var itemDataText: String? = null
    var done:  Boolean ? = false
}