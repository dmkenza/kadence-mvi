package com.kadence1.vm

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kadencelibrary.data.ActiveMutableLiveData
import com.kadencelibrary.extension.ChildViewModelContract
import com.kadencelibrary.extension.debug.d
import com.shopify.livedataktx.SupportMediatorLiveData
import io.reactivex.disposables.CompositeDisposable


abstract class ChildVm<STATE, EFFECT, EVENT>(application: Application) : ChildViewModelContract<EVENT> {

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
            _viewEffects.setValue( value )
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
        return false
    }

    open fun clear() {
        disposables.dispose()
        disposables.clear()

    }






}