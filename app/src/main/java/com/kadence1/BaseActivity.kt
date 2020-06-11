
import com.kadence1.vm.BasicFlowFragment
import com.kadence1.R
import com.kadencelibrary.base.KadenceActivity
import com.kadencelibrary.extension.context.transaction


abstract class BaseActivity :KadenceActivity(){

    @JvmOverloads
    fun startFlow(fragment: BasicFlowFragment<*, *, *, *>, tag: String, animate: Boolean = true) {
        supportFragmentManager.transaction {
            addToBackStack(R.id.content, fragment, tag, animate)
        }
    }

}