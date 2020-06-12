package com.kadence1.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kadence.mvi.fragment.BaseChildFragment
import com.kadence.mvi.fragment.BaseChildFragment.Companion.MODEL_KEY
import com.kadence1.R
import com.kadence1.vm.*
import com.kadencelibrary.extension.debug.d
import kotlinx.android.synthetic.main.frag_test1.*


class TestChild2Fragment :
    BaseChildFragment<MainViewState, MainViewEffect, TestViewEvent, TestChildVm>() {

//    override val viewModel: MainVm by viewModels()

    companion object {
        fun newInstance(modelKey : String): TestChild2Fragment {

            val frag = TestChild2Fragment()
            val args = Bundle().also {
                it.putString(MODEL_KEY, modelKey)
            }

            frag.arguments = args

            return  frag

        }
    }

//    private var callbacks: Callbacks? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.frag_test1, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        tv_main.setText("2 ${viewModel.toString()}")

        tv_main.setOnClickListener {
            viewModel.process(TestViewEvent.ChildTest("sdsa"))
        }
    }

    override fun onAttach(context: Context) {
        attachChildViewModel<TestChildVm>()
        super.onAttach(context)
    }

    override fun onDetach() {
//        callbacks = null
        detachChildViewModel<TestChildVm>()
        super.onDetach()

    }

    override fun renderViewState(state: MainViewState) {
        tv_main.setText("2 - ${state.counter}  + $viewModel + $modelTag")

//        d("run renderViewState from TestChild2Fragment + $state")
    }

    override fun renderViewEffect(effect: MainViewEffect) {
//        toast("TestChild2Fragment $effect ")

        d("run renderViewEffect from TestChild2Fragment + $effect")
    }
}