package com.kadence1.screens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kadence.mvi.viewModels
import com.kadence.mvi.fragment.BaseFlowFragment
import com.kadence1.R
import com.kadence1.vm.*
import kotlinx.android.synthetic.main.screen_result.*


class ResultFragment : BaseFlowFragment<MainViewState, MainViewEffect, MainViewEvent, MainVm>() {


    override val viewModel: MainVm by viewModels()


    companion object {
        fun newInstance(): ResultFragment = ResultFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.screen_result, container, false)


    override fun renderViewState(state: MainViewState) {

    }

    override fun renderViewEffect(effect: MainViewEffect) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bt.setOnClickListener {

            val args = Bundle().also {
                it.putString(MainFragment.KEY, "asd")
            }

            val intent = Intent()
            intent.putExtra("powerCheck", "RED_FRAGMENT")

//            setFragmentResult("qwert", args)
            activity?.setResult(1, intent)


            activity?.finish()


//            targetFragment!!.onActivityResult(targetRequestCode, 123, intent)


//            targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)

        }

    }
}