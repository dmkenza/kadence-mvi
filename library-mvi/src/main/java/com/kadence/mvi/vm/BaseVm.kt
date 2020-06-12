package com.kadence.mvi.vm

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.kadencelibrary.data.ActiveMutableLiveData
import com.kadencelibrary.extension.ViewModelContract
import com.kadencelibrary.extension.debug.d
import com.kadencelibrary.extension.text.toJson
import com.kadencelibrary.extension.text.toObject
import com.shopify.livedataktx.SupportMediatorLiveData
import io.reactivex.disposables.CompositeDisposable

abstract class BaseVm<STATE, EFFECT, EVENT>(
    application: Application,
    open val savedStateHandle: SavedStateHandle
) :
    AndroidViewModel(application), ViewModelContract<EVENT> {


    var childs: HashMap<String, ChildVm<*, *, *>> = HashMap()
    val disposables = CompositeDisposable()

    protected val SAVE_STATE_KEY = "SAVE_STATE_KEY"


    private val _viewStates: MutableLiveData<STATE> = SupportMediatorLiveData()
    fun viewStates(): LiveData<STATE> = _viewStates

    private var _viewState: STATE? = null
    protected var viewState: STATE
        get() = _viewState
            ?: throw UninitializedPropertyAccessException("\"viewState\" was queried before being initialized")
        set(value) {


            d("setting viewState : $value")
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
            d("setting viewEffect : $value")
            _viewEffect = value
            _viewEffects.setValue(value)
        }


    private val _singleViewEffects: SupportMediatorLiveData<EFFECT> = SupportMediatorLiveData(true)
    fun viewSingleEffect(): LiveData<EFFECT> = _singleViewEffects

    private var _singleViewEffect: EFFECT? = null
    protected var singleViewEffect: EFFECT
        get() = _singleViewEffect
            ?: throw UninitializedPropertyAccessException("\"singleViewEffect\" was queried before being initialized")
        set(value) {
            _singleViewEffect = value
            _singleViewEffects.setValue(value)
        }


    @CallSuper
    override fun process(viewEvent: EVENT) {
//        if (!viewStates().hasObservers()) {
//            throw NoObserverAttachedException("No observer attached. In case of custom View \"startObserving()\" function needs to be called manually.")
//        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        disposables.clear()
        childs.clear()
        d("onCleared")
    }


    protected open fun restoreViewState(
        clazz: Class<STATE>,
        savedStateHandle: SavedStateHandle
    ): STATE? {
        val stateJson = savedStateHandle.get<String>(SAVE_STATE_KEY)
//        toast(getApplication(), "restoreViewState = ${stateJson}")
        return stateJson?.toObject(clazz)

    }

    fun onSaveState() {
        val json = viewState?.toJson() ?: ""
//        toast(getApplication(), "onSaveState $json" )

        savedStateHandle.set(SAVE_STATE_KEY, json)
        childs.map {  it.value.onSaveState(savedStateHandle) }
    }


    /**
     * Generate instance for [ChildVm]
     */

    private fun <T : ChildVm<*, *, *>> createChildViewModel(entityClass: Class<T>): T {
//        var entity: T = entityClass.newInstance()
        val constructor = entityClass.constructors.firstOrNull()
        val childVm = constructor?.newInstance(this.getApplication(), this)
        return childVm as T

    }


    open fun <T : ChildVm<*, *, *>> popUpOrCreateChildViewModel(
        clazz: Class<T>,
        modelKey: String
    ): ChildVm<*, *, *>? {

        var model = childs.get(modelKey)
        if (model == null) {
            model = createChildViewModel(clazz)
            model.initViewModel(savedStateHandle, modelKey)
            childs.put(modelKey, model)
        }
        return model

    }



    open fun <T> removeChildViewModel(clazz: Class<T>, modelKey: String) {
        val model = childs.get(modelKey)
        model?.clear()
        childs.remove(modelKey)
    }


}