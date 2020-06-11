package com.kadence1.vm

import android.app.Application
import com.kadencelibrary.extension.ChildViewModelContract
import com.kadencelibrary.extension.debug.toast

class MainVm(application: Application) :
    BaseVm<MainViewState, MainViewEffect, MainViewEvent>(application),
    ChildViewModelContract<TestViewEvent> {

//    val childVm = TestChildVm(application, this)

    init {
        viewState = MainViewState(counter = 0, contentTypes = HashMap())
//        childVMs.add(childVm)


//        childVm.viewStates().observe {
//
//        }
    }

    var counter = 0
    val contentTypes: HashMap<Int, ContentType> = HashMap()


    override fun process(viewEvent: MainViewEvent) {
        super.process(viewEvent)

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

                contentTypes.put(counter, ContentType.Test1)
                viewState = viewState.copy(counter = counter, contentTypes = contentTypes)

                viewEffect = MainViewEffect.AddFragment(counter, 0)


                counter++


            }
            MainViewEvent.AddSecondFragment -> {

                if (counter >= 5) {
                    counter = 0
                }

                contentTypes.put(counter, ContentType.Test2)
                viewState = viewState.copy(counter = counter, contentTypes = contentTypes)
                viewEffect = MainViewEffect.AddFragment(counter, 1)

                counter++
            }
            MainViewEvent.OnBackPressed -> {
                val last = contentTypes.keys.lastOrNull()
                if (last == null) {
                    viewEffect = MainViewEffect.NavigationBack
                }
                contentTypes.remove(last)
                viewState = viewState.copy(counter = counter, contentTypes = contentTypes)
                viewEffect = MainViewEffect.RemoveLastFragment
            }
        }

    }


    override fun onCleared() {
//        childVMs.map {  it.value }
        childVMs.clear()
        super.onCleared()
    }

    override fun <T> popUpOrCreateChildViewModel(
        clazz: Class<T>,
        modelKey: String
    ): ChildVm<*, *, *>? {


        if (clazz == TestChildVm::class.java) {
//        if (clazz.canonicalName == TestChildVm::class.java.canonicalName) {
            var model = childVMs.get(modelKey)
            if (model == null) {
                model = TestChildVm(this.getApplication(), this)
                childVMs.put(modelKey, model)
            }
            return model
        }

        return null
    }


    override fun <T> removeChildViewModel(clazz: Class<T>, modelKey: String) {
        if (clazz.canonicalName == TestChildVm::class.java.canonicalName) {
            val model = childVMs.get(modelKey)

            model?.clear()
            childVMs.remove(modelKey)

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