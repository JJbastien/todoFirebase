package com.example.todofirebase

interface UpdateAndDelete {
    fun modifyItem(itemUID: String, isDone: Boolean)
    fun onItemDelete(itemUID: String)
}