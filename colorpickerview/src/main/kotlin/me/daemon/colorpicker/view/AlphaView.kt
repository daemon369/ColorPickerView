package me.daemon.colorpicker.view

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * @author daemon
 * @since 2019-03-11 08:24
 */
class AlphaView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), IView<AlphaView.AlphaValue> {

    class AlphaValue : IView.Value() {

        var alpha: Float = 1f
            private set

        fun setValue(alpha: Float) {
            this.alpha = alpha
        }
    }

}