package com.kadence.mvi

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.kadence.mvi.fragment.BaseScreen
import com.kadencelibrary.base.KadenceActivity
import com.kadencelibrary.base.KadenceFragment
import com.kadencelibrary.extension.context.transaction
import com.kadencelibrary.extension.view.setHeight
import com.kadencelibrary.extension.view.vis


abstract class KadenceMviActivity : KadenceActivity() {


    val container_fragment: ViewGroup by lazy {
        findViewById<ViewGroup>(R.id.container_fragment)
    }

    val container_system_bar: View by lazy {
        findViewById<ViewGroup>(R.id.container_system_bar)
    }

    private var systemBarNeedHide = false

    @JvmOverloads
    fun startFlow(fragment: BaseScreen<*, *, *, *>, tag: String, animate: Boolean = true) {
        supportFragmentManager.transaction {
            addToBackStack(R.id.container_fragment, fragment, tag, animate)
        }
    }

    /**
     *   Prepare systembar and navigation bar be transparent for Single Activity Application. Also required call [setApplyWindowInsetsListener] after setContentView.
     */

    fun prepareSystembar() {
        /** make system bar transparent */
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        systemBarNeedHide = true

        /** remove system bar */
        val attrib = window.attributes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            attrib.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

//        setStatusBarVisibility(false)

//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }


    fun hideSystemUI() {

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }


    fun Activity.setStatusBarVisibility(isVisible: Boolean) {
        //see details https://developer.android.com/training/system-ui/immersive
        if (isVisible) {
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }


    /**
     * Apply correct padding for fragment's container by WindowInsets.
     */

    fun applyWindowInsetsRequired(fragmentContainer: ViewGroup) {
        ViewCompat.setOnApplyWindowInsetsListener(fragmentContainer) { view, insets ->
            var consumed = false

            (view as ViewGroup).forEach { child ->
                // Dispatch the insets to the child
                val childResult = child.dispatchApplyWindowInsets(insets.toWindowInsets())
                // If the child consumed the insets, record it
                if (childResult.isConsumed) {
                    consumed = true
                }
            }

//            val fragment = fragmentContainer.getChildAt(0) as BaseFlowFragment<*,*,*,*>


//            val applyRoot = fragment.onApplyWindowInsets(insets)

//            if(applyRoot){

            fragmentContainer.getChildAt(0)?.setPadding(
                insets.systemWindowInsetLeft,
                insets.systemWindowInsetTop,
                insets.systemWindowInsetRight,
                insets.systemWindowInsetBottom + insets.systemWindowInsetTop
//                bottom = insets.stableInsetBottom
            )

            container_system_bar.setHeight(insets.systemWindowInsetTop)

//            }


            // If any of the children consumed the insets, return
            // an appropriate value
            if (consumed) insets.consumeSystemWindowInsets() else insets
        }
    }

    fun showCustomSystemBar(show: Boolean) {
        container_system_bar.vis(show)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && systemBarNeedHide) {
            hideSystemUI()
        }
    }


    /**
     * Send backpressed event to inner fragments
     */

    override fun onBackPressed() {
        if (notifyFragments()) {

        } else {
            super.onBackPressed()
        }
    }

    private fun notifyFragments(): Boolean {
        val fragments: List<Fragment> = supportFragmentManager.fragments
        if (fragments.isEmpty()) return false
        return fragments.mapNotNull { it as? KadenceFragment }.first().onBackPressed()

    }


}