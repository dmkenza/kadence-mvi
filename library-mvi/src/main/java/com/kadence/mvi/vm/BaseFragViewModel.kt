package com.kadence.mvi.vm

import androidx.annotation.CallSuper
import androidx.lifecycle.*
import com.kadence.mvi.fragment.BaseScreen
import com.kadencelibrary.data.ActiveMutableLiveData
import com.kadencelibrary.extension.isMainThread
import com.shopify.livedataktx.SingleLiveData
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import java.lang.Exception
import java.lang.reflect.ParameterizedType


abstract class BaseFragViewModel<STATE, EFFECT, EVENT>(open val parent: AndroidViewModel)  {

//    val context : Application = parent.getApplication()

    val viewModelScope: CoroutineScope
        get() {
            return  parent.viewModelScope
        }


    protected inline fun <reified T: Any> requestSharedRepo(): T{
        return (parent as? BaseScreenViewModel<*,*,*>)?.onSharedRepoRequested(T::class.java) as? T ?: throw Exception("Parent ViewModel must hold SharedRepo : ${T::class.java}")
    }

    init {

    }

    /** Define association between [BaseFragViewModel] and [BaseChildFragment] */
    var modelKey = ""

    /** Base disposables for Rx operations with auto clear.*/
    val disposables = CompositeDisposable()

    /** Last saved unparsed state.*/
    open var savedStateHandle: SavedStateHandle? = null

    /** Type for detecting class of State */
    private var typeOfViewState: Class<STATE>? = null

    /** Key for saved and restore last state in savedStateHandle. */
    protected val SAVE_CHILD_STATE_KEY = "SAVE_CHILD_STATE_KEY"


    /** Base functions for viewStates. */
    private val _viewStates: MutableLiveData<STATE> = MutableLiveData()
    fun viewStates(): LiveData<STATE> = _viewStates

    private var _viewState: STATE? = null
    protected var viewState: STATE
        get() = _viewState
            ?: throw UninitializedPropertyAccessException("\"viewState\" was queried before being initialized")
        set(value) {

//            d("setting viewState : $value")
            _viewState = value
            if(isMainThread()){
                _viewStates.value = value
            }else{
                _viewStates.postValue( value )
            }
        }


    /** Base functions for viewEffects. */
    private val _viewEffects: ActiveMutableLiveData<EFFECT> = ActiveMutableLiveData()
//    private val _viewEffects: SingleLiveData<EFFECT> = SingleLiveData()
    fun viewEffects(): LiveData<EFFECT> = _viewEffects

    private var _viewEffect: EFFECT? = null
    protected var viewEffect: EFFECT
        get() = _viewEffect
            ?: throw UninitializedPropertyAccessException("\"viewEffect\" was queried before being initialized")
        set(value) {
//            d("setting viewEffect : $value")
            _viewEffect = value

            if (isMainThread()) {
                _viewEffects.setValue(value)
            } else {
                _viewEffects.postValue(value)
            }
        }


    fun sendEventToParent(viewEvent: Any): Boolean {
        return (parent as BaseScreenViewModel<*,*,*>).processChildEvent(viewEvent)
    }


    //@CallSuper
    open fun process(viewEvent: EVENT) : Any {

        return  Any()
    }

    open fun clear() {
        disposables.dispose()
        disposables.clear()

    }


    @Suppress("UNCHECKED_CAST")
    open fun getTypeViewState(): Class<STATE>? {
        typeOfViewState =
            (javaClass.genericSuperclass as? ParameterizedType)?.getActualTypeArguments()
                ?.get(0) as Class<STATE>?
        return typeOfViewState
    }


    @CallSuper
    open fun initViewModel( modelKey: String) {
        this.modelKey = modelKey
    }




    private fun getSaveStateKey() = SAVE_CHILD_STATE_KEY + "_" + modelKey
}