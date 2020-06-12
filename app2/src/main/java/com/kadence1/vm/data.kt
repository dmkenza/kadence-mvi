package com.kadence1.vm

sealed class MainViewEvent {
    data class NewsItemClicked(val newsItem: String) : MainViewEvent()
    object FabClicked : MainViewEvent()
    object OnSwipeRefresh : MainViewEvent()
    object FetchNews : MainViewEvent()
    object Result : MainViewEvent()
    data class Test(val x: String) : MainViewEvent()

    object AddFirstFragment : MainViewEvent()
    object AddSecondFragment : MainViewEvent()

    object OnBackPressed : MainViewEvent()


    data class ChildTest(val x: String) : MainViewEvent()
}


sealed class MainViewEffect {
    object RemoveLastFragment : MainViewEffect()
    object NavigationBack : MainViewEffect()
    object Result : MainViewEffect()
    data class AddFragment(val position: Int, val type: Int) : MainViewEffect()

    data class TestEffect(val x: String) : MainViewEffect()
    data class ShowToast(val message: String) : MainViewEffect()
}


data class MainViewState(val counter: Int, val contentTypes: HashMap<Int, ContentType>)


enum class ContentType {
    Test1, Test2
}

sealed class FetchStatus {
    object Fetching : FetchStatus()
    object Fetched : FetchStatus()
    object NotFetched : FetchStatus()
}


sealed class TestViewEvent {
    data class NewsItemClicked(val newsItem: String) : TestViewEvent()
    object FabClicked : TestViewEvent()
    object OnSwipeRefresh : TestViewEvent()
    object FetchNews : TestViewEvent()
    data class Test(val x: String) : TestViewEvent()

    object AddFirstFragment : TestViewEvent()
    object AddSecondFragment : TestViewEvent()

    object OnBackPressed : TestViewEvent()


    data class ChildTest(val x: String) : TestViewEvent()
    data class ChildTestForParent(val x: String) : TestViewEvent()
}
