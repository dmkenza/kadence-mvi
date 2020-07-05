package com.kadence.mvi.fragment

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import com.kadence.mvi.vm.BaseChildViewModel
import com.kadencelibrary.base.KadenceFragment
import com.kadencelibrary.extension.TAG0
import com.kadencelibrary.extension.context.extraNotNull
import com.kadencelibrary.extension.debug.log
import com.kadencelibrary.interfaces.HasContextExtensions
import java.lang.reflect.ParameterizedType

abstract class BaseChildFragment<STATE, EFFECT, EVENT, ViewModel : BaseChildViewModel<STATE, EFFECT, EVENT>> :
    KadenceFragment(), HasContextExtensions {


    open lateinit var viewModel: ViewModel

    companion object {
        val MODEL_KEY = "MODEL_KEY"
    }

    val modelTag by extraNotNull<String>(MODEL_KEY, TAG0)



    fun addModelKey(key : String){
        this.arguments?.putString(MODEL_KEY, key )
    }


    private var typeOfViewModel: Class<ViewModel>? = null


    @Suppress("UNCHECKED_CAST")
    open fun getTypeViewModel(): Class<ViewModel>? {
        typeOfViewModel = (javaClass.genericSuperclass as? ParameterizedType)?.getActualTypeArguments()?.get(3) as Class<ViewModel>?
        return typeOfViewModel
    }



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



    inline fun attachChildViewModel() {
        val parentFragment = parentFragment as BaseFlowFragment<*, *, *, *>
        val clazz = getTypeViewModel()!!
        viewModel = parentFragment.popUpOrCreateChildViewModel(clazz, modelTag) ?: throw Exception("Parent's ViewModel should have childViewModel for this fragment.")
    }

    inline fun detachChildViewModel() {
        val parentFragment = parentFragment as BaseFlowFragment<*, *, *, *>
        val clazz = getTypeViewModel()!!
        parentFragment.removeChildViewModel(clazz, modelTag)

    }


    override fun onAttach(context: Context) {
        attachChildViewModel()
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (parentFragment !is BaseFlowFragment<*, *, *, *>) {
            throw IllegalStateException("This fragment should be used only as a child fragment of parent fragment")
        }

        viewModel.viewStates().observe(this, viewStateObserver)
        viewModel.viewEffects().observe(this, viewEffectObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.viewStates().removeObserver(viewStateObserver)
        viewModel.viewEffects().removeObserver(viewEffectObserver)
    }



//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        log("onAttach ${this.toString()}")
//        if (parentFragment !is BaseFlowFragment<*, *, *, *>) {
//            throw IllegalStateException("This fragment should be used only as a child fragment of parent fragment")
//        }
//
//        viewModel.viewStates().observe(this, viewStateObserver)
//        viewModel.viewEffects().observe(this, viewEffectObserver)
//
//
////        val parentFragment = parentFragment as BasicFlowFragment<*, *, *, *>
////        viewModel = parentFragment.requestChildModel<ViewModel>()
////            ?: throw IllegalStateException("The ViewModel of parent fragment should has child model in list childViewModels ")
//    }

//    override fun onDetach() {
//        super.onDetach()
//        log("onDetach ${this.toString()}")
//
//        viewModel.viewStates().removeObserver(viewStateObserver)
//        viewModel.viewEffects().removeObserver(viewEffectObserver)
//    }

    open fun back(): Boolean =
        (parentFragment as BaseFlowFragment<*, *, *, *>).back()

    override fun getContextForExtensions() = requireContext()

}