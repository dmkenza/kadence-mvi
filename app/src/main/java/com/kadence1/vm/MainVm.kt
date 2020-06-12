package com.kadence1.vm

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.kadence.mvi.vm.BaseVm
import com.kadence.mvi.vm.ChildVm
import com.kadencelibrary.extension.ChildViewModelContract
import com.kadencelibrary.extension.debug.d
import com.kadencelibrary.extension.debug.toast

class MainVm(application: Application, override val savedStateHandle: SavedStateHandle) :
    BaseVm<MainViewState, MainViewEffect, MainViewEvent>(application, savedStateHandle),
    ChildViewModelContract<TestViewEvent> {

//    val childVm = TestChildVm(application, this)

    init {
        val restoredState = restoreViewState(MainViewState::class.java, savedStateHandle)
        viewState = restoredState ?: MainViewState(counter = -1, contentTypes = HashMap())
    }


    override fun process(viewEvent: MainViewEvent) {
        super.process(viewEvent)


        var counter = viewState.counter
        val contentTypes: HashMap<Int, ContentType> = viewState.contentTypes


        when (viewEvent) {
            is MainViewEvent.Test -> {
                toast(getApplication(), "test event")
                viewEffect = MainViewEffect.TestEffect("test x")
            }
            is MainViewEvent.Result -> {
                viewEffect = MainViewEffect.Result
            }
            is MainViewEvent.NewsItemClicked -> {

            }
            MainViewEvent.FabClicked -> {

            }
            MainViewEvent.OnSwipeRefresh -> {

            }
            MainViewEvent.FetchNews -> {

            }
            is MainViewEvent.ChildTest -> {

                viewEffect = MainViewEffect.TestEffect("Get event from childVM")
            }
            MainViewEvent.AddFirstFragment -> {

                if (counter >= 5) {
                    counter = 0
                }
                counter++

                contentTypes.put(counter, ContentType.Test1)
                viewState = viewState.copy(counter = counter, contentTypes = contentTypes)
                viewEffect = MainViewEffect.AddFragment(counter, 0)

            }
            MainViewEvent.AddSecondFragment -> {

                if (counter >= 5) {
                    counter = 0
                }
                counter++


                contentTypes.put(counter, ContentType.Test2)
                viewState = viewState.copy(counter = counter, contentTypes = contentTypes)
                viewEffect = MainViewEffect.AddFragment(counter, 1)

            }
            MainViewEvent.OnBackPressed -> {
                val last = contentTypes.keys.lastOrNull()
//                if (last == null) {
//                    viewEffect = MainViewEffect.NavigationBack
//                }
                contentTypes.remove(last)
                viewState = viewState.copy(counter = counter, contentTypes = contentTypes)
                viewEffect = MainViewEffect.RemoveLastFragment
            }
        }

    }


    override fun process(viewEvent: TestViewEvent): Boolean {

        when (viewEvent) {
            is TestViewEvent.Test -> {

                toast(getApplication(), "test event")
                viewEffect = MainViewEffect.TestEffect("test x")

            }
            is TestViewEvent.NewsItemClicked -> {
            }
            TestViewEvent.FabClicked -> {
            }
            TestViewEvent.OnSwipeRefresh -> {
            }
            TestViewEvent.FetchNews -> {
            }
            is TestViewEvent.ChildTest -> {
                return false
            }

            is TestViewEvent.ChildTestForParent -> {
                viewEffect = MainViewEffect.TestEffect("Get ChildTestForParent from Parent VM")
                return true
            }

        }

        return false
    }
}