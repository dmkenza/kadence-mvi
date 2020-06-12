package com.kadence1.vm

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.kadence.mvi.vm.ChildVm
import com.kadencelibrary.extension.ChildViewModelContract
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class TestChildVm(
    application: Application,
    private val contract: ChildViewModelContract<TestViewEvent>
) :
    ChildVm<MainViewState, MainViewEffect, TestViewEvent>(application) {



    init {


//        viewState = MainViewState()
    }

    override fun process(viewEvent: TestViewEvent): Boolean {
        super.process(viewEvent)

        if (!contract.process(viewEvent)) {
            when (viewEvent) {
                is TestViewEvent.NewsItemClicked -> TODO()
                TestViewEvent.FabClicked -> TODO()
                TestViewEvent.OnSwipeRefresh -> TODO()
                TestViewEvent.FetchNews -> TODO()
                is TestViewEvent.ChildTest -> {
                    viewEffect = MainViewEffect.TestEffect("Test child")
                }
            }
        }

        return true
    }


    override fun  initViewModel(savedStateHandle: SavedStateHandle, modelKey : String  ) {

        val default  = MainViewState(counter = 0, contentTypes = HashMap())
        onRestoreState(savedStateHandle, MainViewState::class.java, modelKey, default)


        Observable.interval(1000, 1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

                var counter = viewState.counter

                counter++
                viewState = viewState.copy(counter = counter)
                viewEffect = MainViewEffect.TestEffect("counter = $counter")
            }.addTo(disposables)

    }


}