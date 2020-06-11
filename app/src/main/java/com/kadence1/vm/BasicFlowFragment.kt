package com.kadence1.vm

import BaseActivity
import android.content.Context
import androidx.lifecycle.Observer
import com.kadencelibrary.base.KadenceActivity
import com.kadencelibrary.base.KadenceFragment
import com.kadencelibrary.extension.debug.log
import com.kadencelibrary.interfaces.HasContextExtensions

abstract class BasicFlowFragment<STATE, EFFECT, EVENT, ViewModel : BaseVm<STATE, EFFECT, EVENT>> :
    KadenceFragment(), HasContextExtensions {

    abstract val viewModel: ViewModel

    var flowTAG = ""




    private val viewStateObserver = Observer<STATE> {
        log( "observed viewState : $it")
        renderViewState(it)
    }

    private val viewEffectObserver = Observer<EFFECT> {
        log( "observed viewEffect : $it")
        renderViewEffect(it)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.viewStates().observe(this, viewStateObserver)
        viewModel.viewEffects().observe(this, viewEffectObserver)



    }

    override fun onDetach() {
        super.onDetach()
        viewModel.viewStates().removeObserver(viewStateObserver)
        viewModel.viewEffects().removeObserver(viewEffectObserver)
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
    fun startFlow(fragment: BasicFlowFragment<*, *, *, *>, tag: String, animate: Boolean = true) {
        (activity as BaseActivity).startFlow(fragment , tag, animate)
    }


    @JvmOverloads
    fun startFlowForResult(
        fragment: BasicFlowFragment<*, *, *, *>,
        tag: String,
        requestCode: Int,
        animate: Boolean = true
    ) {
        fragment.setTargetFragment(this, requestCode)
        startFlow(fragment, tag, animate)
    }


    fun back(): Boolean {
        if (childFragmentManager.popBackStackImmediate()) return true
        if (parentFragmentManager.popBackStackImmediate()) return true

        val parentFragment = this.parentFragment
        return when {
            (parentFragment != null && parentFragment is BasicFlowFragment<*, *, *, *>) -> parentFragment.back()
            else -> {
                (requireActivity() as? KadenceActivity)?.back()
                false
            }
        }
    }

    override fun onBackPressed(): Boolean = back()

    override fun getContextForExtensions() = requireContext()


    @Suppress("UNCHECKED_CAST")
    inline fun <reified T>popUpOrCreateChildViewModel(clazz: Class<T>, modelKey: String): T? {
        return viewModel.popUpOrCreateChildViewModel<T>(clazz, modelKey) as? T
    }

    fun <T> removeChildViewModel(clazz: Class<T>, modelKey: String): T? {
        return viewModel.removeChildViewModel<T>(clazz, modelKey) as? T
    }


}