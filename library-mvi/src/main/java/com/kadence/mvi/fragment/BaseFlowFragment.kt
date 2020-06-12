package com.kadence.mvi.fragment

import BaseActivity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.kadence.mvi.vm.BaseVm
import com.kadence.mvi.vm.ChildVm
import com.kadencelibrary.base.KadenceActivity
import com.kadencelibrary.base.KadenceFragment
import com.kadencelibrary.extension.TAG0
import com.kadencelibrary.extension.debug.log
import com.kadencelibrary.interfaces.HasContextExtensions

abstract class BaseFlowFragment<STATE, EFFECT, EVENT, ViewModel : BaseVm<STATE, EFFECT, EVENT>> :
    KadenceFragment(), HasContextExtensions {

    abstract val viewModel: ViewModel

    var flowTAG = TAG0

    private val viewStateObserver = Observer<STATE> {
        log( "observed viewState : $it")
        renderViewState(it)
    }

    private val viewEffectObserver = Observer<EFFECT> {
        log( "observed viewEffect : $it")
        renderViewEffect(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewStates().observe(this, viewStateObserver)
        viewModel.viewEffects().observe(this, viewEffectObserver)
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
        viewModel.onSaveState()
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
    fun startFlow(fragment: BaseFlowFragment<*, *, *, *>, tag: String, animate: Boolean = true) {
        (activity as BaseActivity).startFlow(fragment , tag, animate)
    }


    @JvmOverloads
    fun startFlowForResult(
        fragment: BaseFlowFragment<*, *, *, *>,
        tag: String,
        requestCode: Int,
        animate: Boolean = true
    ) {
        fragment.setTargetFragment(this, requestCode)
        startFlow(fragment, tag, animate)
    }


    fun back(): Boolean {
        log("back  - try remove fragment from child")
        if (childFragmentManager.popBackStackImmediate()) return true
        log("back  - try remove fragment from parent")
        if (parentFragmentManager.popBackStackImmediate()) return true

        val parentFragment = this.parentFragment
        return when {
            (parentFragment != null && parentFragment is BaseFlowFragment<*, *, *, *>) -> parentFragment.back()
            else -> {
                (requireActivity() as? KadenceActivity)?.back()
                false
            }
        }
    }

    override fun onBackPressed(): Boolean = back()

    override fun getContextForExtensions() = requireContext()


    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : ChildVm<*, *, *>>popUpOrCreateChildViewModel(clazz: Class<T>, modelKey: String): T? {
        return viewModel.popUpOrCreateChildViewModel<T>(clazz, modelKey) as? T
    }

    fun <T> removeChildViewModel(clazz: Class<T>, modelKey: String): T? {
        return viewModel.removeChildViewModel<T>(clazz, modelKey) as? T
    }


}