package com.kadence1.screens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.kadence.mvi.fragment.BaseFlowFragment
import com.kadence1.R
import com.kadence1.ResultActivity
import com.kadence.mvi.viewModels
import com.kadence1.vm.*
import com.kadencelibrary.extension.context.addToStack
import com.kadencelibrary.extension.context.extraNotNull
import com.kadencelibrary.extension.debug.log
import com.kadencelibrary.extension.debug.toast
import com.kadencelibrary.extension.postDelay
import com.kadencelibrary.extension.text.getDebugID
import kotlinx.android.synthetic.main.screen_main.*

class MainFragment : BaseFlowFragment<MainViewState, MainViewEffect, MainViewEvent, MainVm>() {


    override val viewModel: MainVm by viewModels()


    companion object {

        const val TAG = "RegistrationFlowFragment"

        const val KEY = "KEY"

        fun newInstance(title: String): MainFragment {

            val frag = MainFragment()
            val args = Bundle().also {
                it.putString(KEY, title)
            }

            frag.arguments = args

            return frag
        }
    }

    val titleItem by extraNotNull<String>(KEY, "DEFAULT")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.screen_main, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (childFragmentManager.fragments.isEmpty()) {
        }

        bt1.setOnClickListener {
            viewModel.process(MainViewEvent.AddFirstFragment)
        }

        bt2.setOnClickListener {
            viewModel.process(MainViewEvent.AddSecondFragment)
        }

        bt3.setOnClickListener {
//            (activity as MainActivity).replaceFragment(MainFragment.newInstance(), "AAA")
            startFlow(newInstance(titleItem + "x"), "asds")
            postDelay(1000) {
//                toast("delay event")

                val fragments: List<Fragment> = childFragmentManager.fragments
                fragments.mapNotNull { it as? TestChild2Fragment }
                    .firstOrNull()?.viewModel?.process(
                    TestViewEvent.ChildTestForParent("postDelay TestChild2Fragment")
                )

            }

        }


        bt4.setOnClickListener {
            viewModel.process(MainViewEvent.Result)
        }


        tv_title.setText("$titleItem")
        tv_fragment_name.setText(this.getDebugID())

    }

    override fun renderViewState(state: MainViewState) {
        toast("renderViewState state = $state")
        log("renderViewState state = $state")

        tv_count.setText(" ${viewModel}  ${state.counter + 1}")
        state.contentTypes.map {


        }

    }

    val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val t1 = it.data.extraNotNull("powerCheck", "RED_DEFAULT")
        toast("getContent $t1")
        // Handle the returned Uri
    }

    override fun renderViewEffect(effect: MainViewEffect) {
        when (effect) {

            is MainViewEffect.TestEffect -> {
                val intent = Intent(requireContext(), ResultActivity::class.java)
                intent.putExtra("powerCheck", "Red")
//                getContent.launch(intent)

                toast("TestEffect")


//                    second()
            }

            is MainViewEffect.Result -> {
                val intent = Intent(requireContext(), ResultActivity::class.java)
                intent.putExtra("powerCheck", "Red")
                getContent.launch(intent)
            }
            is MainViewEffect.RemoveLastFragment -> {
                back()
            }

            is MainViewEffect.ShowToast -> TODO()

            is MainViewEffect.AddFragment -> {
//                toast("AddFragment")

                val tag = "FRAG_TAG_${effect.position}"
                val frag = childFragmentManager.findFragmentByTag(tag)

                val newClassTAG = when (effect.type) {
                    0 -> TestChildFragment::class.java
                    else -> TestChild2Fragment::class.java
                }

                val class2 = frag?.let {
                    it::class.java
                } ?: ""


                val requiredDefferentFragment = !newClassTAG.equals(class2)

                if (frag == null || requiredDefferentFragment) {

                    val newFragment = when (effect.type) {
                        0 -> TestChildFragment.newInstance(tag)
                        else -> TestChild2Fragment.newInstance(tag)
                    }

                    when (effect.position) {
                        0 -> {
                            childFragmentManager.addToStack(newFragment, R.id.container_1, tag)
                        }
                        1 -> {
                            childFragmentManager.addToStack(newFragment, R.id.container_2, tag)
                        }
                        2 -> {
                            childFragmentManager.addToStack(newFragment, R.id.container_3, tag)
                        }
                        3 -> {
                            childFragmentManager.addToStack(newFragment, R.id.container_4, tag)
                        }
                        4 -> {
                            childFragmentManager.addToStack(newFragment, R.id.container_5, tag)
                        }
                        5 -> {
                            childFragmentManager.addToStack(newFragment, R.id.container_6, tag)
                        }
                        else -> {
                        }
                    }


                    val frag = childFragmentManager.findFragmentByTag(tag)


                } else {


                }

            }
            MainViewEffect.NavigationBack -> {
                super.onBackPressed()
            }
        }
    }


    override fun onBackPressed(): Boolean {
        viewModel.process(MainViewEvent.OnBackPressed)
        return true
    }
}