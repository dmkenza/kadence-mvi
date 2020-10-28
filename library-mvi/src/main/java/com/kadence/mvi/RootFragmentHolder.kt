package com.kadence.mvi

import androidx.fragment.app.Fragment
import com.kadence.mvi.fragment.BaseScreen


/** help return root parent fragment for nested fragments */
interface RootFragmentHolder {
    fun getRootFragment() : BaseScreen<*, *, *, *>?
}