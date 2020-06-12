package com.kadence1

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.kadencelibrary.extension.debug.log
import com.kadencelibrary.extension.debug.toast
import com.kadencelibrary.view.KadenceView


class TestView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : KadenceView(context, attrs, defStyleAttr) {



    init {
        LayoutInflater.from(context).inflate(R.layout.custom_test, this, true)
//        setttingPreview("BaseMviView")

//        addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
//            override fun onViewDetachedFromWindow(v: View?) {
//                log("onViewDetachedFromWindow ${this.toString()}")
//                toast("onViewDetachedFromWindow id = $id")
//
//            }
//
//            override fun onViewAttachedToWindow(v: View?) {
//                log("onViewAttachedToWindow ${this.toString()}")
//                toast("onViewAttachedToWindow id = $id")
//            }
//        })


    }



    fun attachChildViewModel(){

    }

    fun detachChildViewModel(){

    }







}