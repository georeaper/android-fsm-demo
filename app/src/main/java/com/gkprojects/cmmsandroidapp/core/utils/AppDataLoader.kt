package com.gkprojects.cmmsandroidapp.core.utils

import android.content.Context
import com.gkprojects.cmmsandroidapp.data.local.dto.EventItem
import org.json.JSONArray
import java.io.File

class AppDataLoader(private val context: Context) {

     fun getDataFromJson(filename: String): Array<String> {
        try {
            val file = File(context.filesDir, filename)
            if (!file.exists()) return emptyArray()

            val jsonString = file.readText()
            val jsonArray = JSONArray(jsonString)
            val names = mutableListOf<String>()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val name = jsonObject.optString("name")
                names.add(name)
            }

            return names.toTypedArray()
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyArray() // Return an empty array in case of any exception
        }
    }
    fun getDataColorsFromJson(filename: String): ArrayList<EventItem> {
        try {
            val file = File(context.filesDir, filename)
            if (!file.exists()) return ArrayList<EventItem>()

            val jsonString = file.readText()
            val jsonArray = JSONArray(jsonString)
            val names = ArrayList<EventItem>()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val name = jsonObject.optString("name")
                val color =jsonObject.optString("color")
                names.add(EventItem(name,color))
            }

            return names
        } catch (e: Exception) {
            e.printStackTrace()
            return ArrayList<EventItem>() // Return an empty array in case of any exception
        }
    }
}