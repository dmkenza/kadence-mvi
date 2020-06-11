package com.kadence1

import BaseActivity
import android.content.Intent
import android.os.Bundle
import com.kadence1.screens.MainFragment
import com.kadence1.screens.ResultFragment
import com.kadencelibrary.extension.context.extraNotNull
import com.kadencelibrary.extension.context.into
import com.kadencelibrary.extension.context.popOrReplaceRoot
import com.kadencelibrary.extension.postDelay

class ResultActivity : BaseActivity(){


    val action : String by extraNotNull("powerCheck" , "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main)
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        if (!wasRestored) {
            supportFragmentManager.popOrReplaceRoot({ ResultFragment.newInstance() } into R.id.content, MainFragment.TAG)


            postDelay(3000) {
                val intent = Intent()
                intent.putExtra("asd", 123)

//            setFragmentResult("qwert", args)
                setResult(1, intent)
                finish()


            }



        }
    }







}