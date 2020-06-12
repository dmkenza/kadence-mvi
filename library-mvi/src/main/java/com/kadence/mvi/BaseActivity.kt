
import com.kadence.mvi.fragment.BaseFlowFragment
import com.kadence.mvi.R
import com.kadencelibrary.base.KadenceActivity
import com.kadencelibrary.extension.context.transaction


abstract class BaseActivity :KadenceActivity(){

    @JvmOverloads
    fun startFlow(fragment: BaseFlowFragment<*, *, *, *>, tag: String, animate: Boolean = true) {
        supportFragmentManager.transaction {
            addToBackStack(R.id.content, fragment, tag, animate)
        }
    }

}