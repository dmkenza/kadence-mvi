package com.kadence.mvi.fragment

import com.kadence.mvi.KadenceMviActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.kadence.mvi.RootFragmentHolder
import com.kadence.mvi.vm.BaseScreenViewModel
import com.kadence.mvi.vm.BaseFragViewModel
import com.kadencelibrary.base.KadenceActivity
import com.kadencelibrary.base.KadenceFragment
import com.kadencelibrary.extension.TAG0
import com.kadencelibrary.extension.debug.log
import com.kadencelibrary.interfaces.HasContextExtensions

abstract class BaseScreen<STATE, EFFECT, EVENT, ViewModel : BaseScreenViewModel<STATE, EFFECT, EVENT>> :
    KadenceFragment(), HasContextExtensions , RootFragmentHolder{

    abstract val viewModel: ViewModel

    var flowTAG = TAG0

    var wasRestored: Boolean = false

    private val viewStateObserver = Observer<STATE> {
//        log("observed viewState : $it")
        renderViewState(it)
    }

    private val viewEffectObserver = Observer<EFFECT> {
//        log("observed viewEffect : $it")
        renderViewEffect(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewStates().observe(this, viewStateObserver)
        viewModel.viewEffects().observe(this, viewEffectObserver)

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            wasRestored = true
        }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.viewStates().removeObserver(viewStateObserver)
        viewModel.viewEffects().removeObserver(viewEffectObserver)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        viewModel.onSaveState()
    }


    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        super.onStop()

    }


    abstract fun renderViewState(state: STATE)

    abstract fun renderViewEffect(effect: EFFECT)


    @JvmOverloads
    fun startFlow(fragment: BaseScreen<*, *, *, *>, tag: String, animate: Boolean = true) {
        (activity as KadenceMviActivity).startFlow(fragment, tag, animate)
    }


    fun back(): Boolean {
        log("back  - try remove fragment from child")
        if (childFragmentManager.popBackStackImmediate()) return true
        log("back  - try remove fragment from parent")
        if (parentFragmentManager.popBackStackImmediate()) return true

        val parentFragment = this.parentFragment
        return when {
            (parentFragment != null && parentFragment is BaseScreen<*, *, *, *>) -> parentFragment.back()
            else -> {
                (requireActivity() as? KadenceActivity)?.back()
                false
            }
        }
    }

    override fun onBackPressed(): Boolean = back()

    override fun getContextForExtensions() = requireContext()


    @Suppress("UNCHECKED_CAST")
    fun <T : BaseFragViewModel<*, *, *>> popUpOrCreateChildViewModel(
        clazz: Class<T>,
        modelKey: String
    ): T? {
        return viewModel.popUpOrCreateChildViewModel<T>(clazz, modelKey) as? T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> removeChildViewModel(clazz: Class<T>, modelKey: String): T? {
        return viewModel.removeChildViewModel<T>(clazz, modelKey) as? T
    }


    /**
     * Function allow use  WindowInsets for change  UI element when systembar has been hided.
     * @return Boolean - true for insets need apply in Parent Activity too.
     */

    open fun onApplyWindowInsets(insets: WindowInsetsCompat): Boolean {
        return true
    }

    override fun getRootFragment(): BaseScreen<*, *, *, *>? {
         return this
    }
}