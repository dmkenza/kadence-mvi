package com.kadence.harmony.data

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.kadencelibrary.data.CachedDataItem

class TestRepo (context: Context) : CachedDataItem.CacheDelegate {


    val testItem = CachedDataItem<String>(context, this, "TestItem", "Start", object : TypeToken<String>() {})



    override fun getSharedPrefencesFileName(): String  = "TestRepo"
}