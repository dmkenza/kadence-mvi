package com.kadence1.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kadence1.R
import com.kadence1.vm.*
import com.kadencelibrary.extension.debug.log
import com.kadencelibrary.extension.debug.toast
import kotlinx.android.synthetic.main.frag_test_0.*

class TestChildFragment : BasicChildFragment<MainViewState, MainViewEffect, TestViewEvent, TestChildVm>() {

//            by viewModels()

    companion object {

        fun newInstance(modelKey: String): TestChildFragment {

            val frag = TestChildFragment()
            val args = Bundle().also {
                it.putString(MODEL_KEY, modelKey)
            }

            frag.arguments = args

            return frag
        }

    }




    override fun onAttach(context: Context) {
        attachChildViewModel<TestChildVm>()
        super.onAttach(context)

//        callbacks = parentFragment as Callbacks



//        toast("onAttach" + this.toString())
    }


    override fun onDetach() {
        detachChildViewModel<TestChildVm>()
        super.onDetach()
//        toast("onDetach" + this.toString())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.frag_test_0, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_main.setOnClickListener {
            viewModel.process(TestViewEvent.ChildTestForParent("ChildTestForParent"))

        }
        tv_main.setText("11 ${viewModel}")


//        signUpWithEmail.setOnClickListener { callbacks?.onSignUpWithEmailClick() }
    }

    override fun renderViewState(state: MainViewState) {
    }

    override fun renderViewEffect(effect: MainViewEffect) {
        log("TestChildFragment  renderViewEffect $effect")
        toast("TestChildFragment $effect ")

    }


}