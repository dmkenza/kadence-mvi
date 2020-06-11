package com.kadence1.vm

import android.app.Application
import com.kadence1.vm.ChildVm
import com.kadence1.vm.MainViewEffect
import com.kadence1.vm.MainViewState
import com.kadence1.vm.TestViewEvent
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


    var counter = 0

    init {
        viewState = MainViewState(counter = 0, contentTypes = HashMap())

        Observable.interval(1000, 1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                counter++
                viewState = viewState.copy(counter = counter )
                viewEffect = MainViewEffect.TestEffect("counter = $counter")
            }).addTo(disposables)

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

    override fun clear() {}


}