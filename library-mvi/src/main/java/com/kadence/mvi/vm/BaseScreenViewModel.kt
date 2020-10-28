package com.kadence.mvi.vm

import android.app.Application
import androidx.lifecycle.*
import com.kadencelibrary.data.ActiveMutableLiveData
import com.kadencelibrary.extension.ChildViewModelContract
import com.kadencelibrary.extension.ViewModelContract
import com.kadencelibrary.extension.debug.d
import com.shopify.livedataktx.Removable
import com.shopify.livedataktx.SupportMediatorLiveData
import io.reactivex.disposables.CompositeDisposable
import java.lang.Exception
import java.lang.reflect.ParameterizedType

abstract class BaseScreenViewModel<STATE, EFFECT, EVENT>(
    application: Application,
    open val savedStateHandle: SavedStateHandle
) :
    AndroidViewModel(application), ViewModelContract<EVENT>, ChildViewModelContract<Any> {


    var childs: HashMap<String, BaseFragViewModel<*, *, *>> = HashMap()
    val disposables = CompositeDisposable()
    val removables = ArrayList<Removable<*>>()

    protected val SAVE_STATE_KEY = "SAVE_STATE_KEY"
    private var typeOfViewState: Class<STATE>? = null


    private val _viewStates: MutableLiveData<STATE> = SupportMediatorLiveData()
    fun viewStates(): LiveData<STATE> = _viewStates

    private var _viewState: STATE? = null
    protected var viewState: STATE
        get() = _viewState
            ?: throw UninitializedPropertyAccessException("\"viewState\" was queried before being initialized")
        set(value) {

//            d("setting viewState : $value")
            _viewState = value
            _viewStates.value = value
        }


    private val _viewEffects: ActiveMutableLiveData<EFFECT> = ActiveMutableLiveData()
    fun viewEffects(): LiveData<EFFECT> = _viewEffects

    private var _viewEffect: EFFECT? = null
    protected var viewEffect: EFFECT
        get() = _viewEffect
            ?: throw UninitializedPropertyAccessException("\"viewEffect\" was queried before being initialized")
        set(value) {
//            d("setting viewEffect : $value")
            _viewEffect = value
            _viewEffects.setValue(value)
        }


//    private val _singleViewEffects: SupportMediatorLiveData<EFFECT> = SupportMediatorLiveData(true)
//    fun viewSingleEffect(): LiveData<EFFECT> = _singleViewEffects
//
//    private var _singleViewEffect: EFFECT? = null
//    protected var singleViewEffect: EFFECT
//        get() = _singleViewEffect
//            ?: throw UninitializedPropertyAccessException("\"singleViewEffect\" was queried before being initialized")
//        set(value) {
//            _singleViewEffect = value
//            _singleViewEffects.setValue(value)
//        }


    @Suppress("UNCHECKED_CAST")
    open fun getTypeViewState(): Class<STATE>? {
        typeOfViewState =
            (javaClass.genericSuperclass as? ParameterizedType)?.getActualTypeArguments()
                ?.get(0) as Class<STATE>?
        return typeOfViewState
    }


    override fun process(viewEvent: EVENT) {
//        if (!viewStates().hasObservers()) {
//            throw NoObserverAttachedException("No observer attached. In case of custom View \"startObserving()\" function needs to be called manually.")
//        }
    }

    override fun processChildEvent(viewEvent: Any): Boolean {
        return true
    }


    override fun onCleared() {
        disposables.dispose()
        disposables.clear()
        childs.clear()

        removables.map {
            it.removeObserver()
        }
        removables.clear()
        super.onCleared()

        d("onCleared")
    }


    inline fun <reified T> findChild(): T {
        return childs.toList().find { it.second is T }?.second as T
    }


    /**
     * Generate instance for [BaseFragViewModel]
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T : BaseFragViewModel<*, *, *>> createChildViewModel(entityClass: Class<T>): T {
//        var entity: T = entityClass.newInstance()
        val constructor = entityClass.constructors.firstOrNull()
//        val childVm = constructor?.newInstance(this.getApplication(), this )
        val parent = this as AndroidViewModel
        try {
            val childVm = constructor?.newInstance(parent)
            return childVm as T
        } catch (e: Exception) {
            d()
            e.printStackTrace()
            throw  Exception("")
        }

    }


    open fun <T : BaseFragViewModel<*, *, *>> popUpOrCreateChildViewModel(
        clazz: Class<T>,
        modelKey: String
    ): BaseFragViewModel<*, *, *>? {

        var model = childs.get(modelKey)
        if (model == null) {
            model = createChildViewModel(clazz)
            model.initViewModel(modelKey)
            childs.put(modelKey, model)
        }
        return model

    }


    open fun <T> removeChildViewModel(clazz: Class<T>, modelKey: String) {
        val model = childs.get(modelKey)
        model?.clear()
        childs.remove(modelKey)
    }


    open fun <T : Any> onSharedRepoRequested(clazz: Class<T>): Any? {
        return null
    }


}

fun <T> Removable<T>.addTo(removables: ArrayList<Removable<*>>): Removable<T> {
    removables.add(this)
    return this
}
