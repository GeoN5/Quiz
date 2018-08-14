package com.example.geonho.codeandroidasynctask

import android.content.Context
import android.content.SharedPreferences

fun Context.getData(key : String) : Int {
    val sharedPreferences : SharedPreferences = getSharedPreferences("test",Context.MODE_PRIVATE)
    return sharedPreferences.getInt(key, -1)
}

fun Context.saveData(key : String, value : Int) {
    val sharedPreferences : SharedPreferences = getSharedPreferences("test", Context.MODE_PRIVATE)
    val editor : SharedPreferences.Editor = sharedPreferences.edit()
    editor.putInt(key, value).apply()
}