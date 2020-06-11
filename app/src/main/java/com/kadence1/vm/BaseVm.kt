package com.kadence1.vm

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kadencelibrary.data.ActiveMutableLiveData
import com.kadencelibrary.extension.ViewModelContract
import com.kadencelibrary.extension.debug.d
import com.shopify.livedataktx.SupportMediatorLiveData
import io.reactivex.disposables.CompositeDisposable

abstract class BaseVm<STATE, EFFECT, EVENT>(application: Application) :
    AndroidViewModel(application), ViewModelContract<EVENT> {


    var childVMs: HashMap<String, ChildVm<*, *, *>> = HashMap()
    val disposables = CompositeDisposable()


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
        d("onCleared")
    }


    open fun <T> popUpOrCreateChildViewModel(clazz: Class<T>, modelKey: String): ChildVm<*, *, *>? {
        return null
    }

    open fun <T> removeChildViewModel(clazz: Class<T>, modelKey: String) {

    }


}