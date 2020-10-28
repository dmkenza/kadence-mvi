package com.kadence.mvi.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.kadence.mvi.RootFragmentHolder
import com.kadence.mvi.vm.BaseFragViewModel
import com.kadencelibrary.base.KadenceFragment
import com.kadencelibrary.extension.TAG0
import com.kadencelibrary.extension.context.extraNotNull
import com.kadencelibrary.interfaces.HasContextExtensions
import com.shopify.livedataktx.debounce
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<STATE, EFFECT, EVENT, ViewModel : BaseFragViewModel<STATE, EFFECT, EVENT>> :
    KadenceFragment(), HasContextExtensions, RootFragmentHolder {


    var viewState: STATE
        set(value) {}
        get() {
            return viewModel.viewStates().value!!
        }

    var prevViewState: STATE? = null

    private var _viewState: STATE? = null




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
//        log("observed viewState : $it")
        prevViewState = _viewState
        _viewState = it

        renderViewState(it)
    }

    private val viewEffectObserver = Observer<EFFECT> {
//        log("observed viewEffect : $it")
        renderViewEffect(it)
    }

    abstract fun renderViewState(state: STATE): Any

    abstract fun renderViewEffect(effect: EFFECT)



    inline fun attachChildViewModel() {
        val parentFragment = (parentFragment as RootFragmentHolder).getRootFragment()
        val clazz = getTypeViewModel()!!
        viewModel = parentFragment?.popUpOrCreateChildViewModel(clazz, modelTag) ?: throw Exception("Parent's ViewModel should have childViewModel for this fragment.")
    }

    inline fun detachChildViewModel() {
        val parentFragment = (parentFragment as RootFragmentHolder).getRootFragment()
        val clazz = getTypeViewModel()!!
        parentFragment?.removeChildViewModel(clazz, modelTag)

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
        if (getRootFragment() !is BaseScreen<*, *, *, *>) {
            throw IllegalStateException("This fragment should be used only as a child fragment of parent fragment")
        }

//        val x1 = viewModel.viewStates()
//        val x2 = viewModel.viewEffects()
//
//        x1.combineWith(x2){ x1, x2 ->
//            x1 to x2
//        }.debounce(1).map {
//            viewModel.viewStates().value == it?.first
//            viewModel.viewEffects().value == it?.second
//        }

        viewModel.viewStates().debounce(1).observe(this, viewStateObserver)
        viewModel.viewEffects().observe(this, viewEffectObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.viewStates().removeObserver(viewStateObserver)
        viewModel.viewEffects().removeObserver(viewEffectObserver)
    }



    open fun back(): Boolean =
        (getRootFragment() as BaseScreen<*, *, *, *>).back()

    override fun getContextForExtensions() = requireContext()


    override fun getRootFragment(): BaseScreen<*,*,*,*>? {
        if(parentFragment is BaseScreen<*,*,*,*>){
            return parentFragment as BaseScreen<*, *, *, *>
        }

        if(parentFragment is BaseFragment<*,*,*,*>){
            return (parentFragment as BaseFragment<*, *, *, *>).getRootFragment()
        }

        return null
    }
}