import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    protected fun applyStatusBarPadding(view: View) {
        val statusBarHeight = resources.getDimensionPixelSize(
            resources.getIdentifier("status_bar_height", "dimen", "android")
        )
        val mainPadding = resources.getDimensionPixelSize(R.dimen.main_padding)

        view.setPadding(
            view.paddingLeft,
            statusBarHeight + mainPadding,
            view.paddingRight,
            view.paddingBottom
        )
    }
}