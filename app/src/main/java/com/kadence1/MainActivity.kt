package com.kadence1

import BaseActivity
import android.os.Bundle
import com.kadence1.R
import com.kadence1.screens.MainFragment
import com.kadencelibrary.base.KadenceActivity
import com.kadencelibrary.extension.context.into
import com.kadencelibrary.extension.context.popOrReplaceRoot
import com.kadencelibrary.extension.context.transaction
import com.kadencelibrary.extension.debug.d
import com.kadencelibrary.extension.debug.toast


open class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main)

    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        if (!wasRestored) {
            supportFragmentManager.popOrReplaceRoot({ MainFragment.newInstance("startM") } into R.id.content,
                MainFragment.TAG)
        }


    }







}