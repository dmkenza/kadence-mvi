package com.kadence1.vm

import android.content.Context
import androidx.lifecycle.Observer
import com.kadencelibrary.base.KadenceFragment
import com.kadencelibrary.extension.TAG0
import com.kadencelibrary.extension.context.extraNotNull
import com.kadencelibrary.extension.debug.log
import com.kadencelibrary.interfaces.HasContextExtensions
import java.lang.Exception

abstract class BasicChildFragment<STATE, EFFECT, EVENT, ViewModel : ChildVm<STATE, EFFECT, EVENT>> :
    KadenceFragment(), HasContextExtensions {

    open lateinit var viewModel: ViewModel

    companion object {
        val MODEL_KEY = "MODEL_KEY"
    }

    val modelTag by extraNotNull<String>(MODEL_KEY, TAG0)


    private val viewStateObserver = Observer<STATE> {
        log("observed viewState : $it")
        renderViewState(it)
    }

    private val viewEffectObserver = Observer<EFFECT> {
        log("observed viewEffect : $it")
        renderViewEffect(it)
    }

    abstract fun renderViewState(state: STATE)

    abstract fun renderViewEffect(effect: EFFECT)



    inline fun <reified T : ViewModel > attachChildViewModel() {
        val parentFragment = parentFragment as BasicFlowFragment<*, *, *, *>
        val clazz = T::class.java
        viewModel = parentFragment.popUpOrCreateChildViewModel(clazz, modelTag) ?: throw Exception("Parent's ViewModel should have childViewModel for this fragment.")
    }

    inline fun <reified T : ViewModel> detachChildViewModel() {
        val parentFragment = parentFragment as BasicFlowFragment<*, *, *, *>
        val clazz = T::class.java
        parentFragment.removeChildViewModel(clazz, modelTag)

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        log("onAttach ${this.toString()}")
        if (parentFragment !is BasicFlowFragment<*, *, *, *>) {
            throw IllegalStateException("This fragment should be used only as a child fragment of parent fragment")
        }

        viewModel.viewStates().observe(this, viewStateObserver)
        viewModel.viewEffects().observe(this, viewEffectObserver)


//        val parentFragment = parentFragment as BasicFlowFragment<*, *, *, *>
//        viewModel = parentFragment.requestChildModel<ViewModel>()
//            ?: throw IllegalStateException("The ViewModel of parent fragment should has child model in list childViewModels ")
    }

    override fun onDetach() {
        super.onDetach()
        log("onDetach ${this.toString()}")

        viewModel.viewStates().removeObserver(viewStateObserver)
        viewModel.viewEffects().removeObserver(viewEffectObserver)
    }

    open fun back(): Boolean =
        (parentFragment as BasicFlowFragment<*, *, *, *>).back()

    override fun getContextForExtensions() = requireContext()

}