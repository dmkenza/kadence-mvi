package com.kadence.mvi.vm

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.kadencelibrary.data.ActiveMutableLiveData
import com.kadencelibrary.extension.ChildViewModelContract
import com.kadencelibrary.extension.debug.d
import com.kadencelibrary.extension.text.toJson
import com.kadencelibrary.extension.text.toObject
import com.shopify.livedataktx.SupportMediatorLiveData
import io.reactivex.disposables.CompositeDisposable


abstract class ChildVm<STATE, EFFECT, EVENT>(application: Application) :
    ChildViewModelContract<EVENT> {

    /** Define association between [ChildVm] and [BaseChildFragment] */
    var modelKey = ""

    val disposables = CompositeDisposable()

    protected val SAVE_CHILD_STATE_KEY = "SAVE_CHILD_STATE_KEY"

    private val _viewStates: MutableLiveData<STATE> = MutableLiveData()
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
    fun viewSingleEffects(): LiveData<EFFECT> = _singleViewEffects

    private var _singleViewEffect: EFFECT? = null
    protected var singleViewEffect: EFFECT
        get() = _singleViewEffect
            ?: throw UninitializedPropertyAccessException("\"singleViewEffect\" was queried before being initialized")
        set(value) {
            _singleViewEffect = value
            _singleViewEffects.setValue(value)
        }


    @CallSuper
    override fun process(viewEvent: EVENT): Boolean {
//        if (!viewStates().hasObservers()) {
//            throw NoObserverAttachedException("No observer attached. In case of custom View \"startObserving()\" function needs to be called manually.")
//        }
        return true
    }

    open fun clear() {
        disposables.dispose()
        disposables.clear()

    }

    abstract fun  initViewModel(savedStateHandle: SavedStateHandle, modelKey: String)



    protected fun onRestoreState(
        savedStateHandle: SavedStateHandle,
        clazz: Class<STATE>,
        modelKey: String,
        default: STATE? = null
    ) {
        this.modelKey = modelKey

        val stateJson = savedStateHandle.get<String>(getSaveStateKey())
//        toast(getApplication(), "restoreViewState = ${stateJson}")
        val stateOld =  stateJson?.toObject(clazz)

        if(stateOld!= null){
            viewState = stateOld
        }else if(default != null){
            viewState = default
        }

    }

    fun onSaveState(savedStateHandle: SavedStateHandle) {

        val json = viewState?.toJson() ?: ""
//        toast(getApplication(), "onSaveState $json" )
        savedStateHandle.set(getSaveStateKey(), json)

    }


    private fun getSaveStateKey () = SAVE_CHILD_STATE_KEY + "_" + modelKey
}