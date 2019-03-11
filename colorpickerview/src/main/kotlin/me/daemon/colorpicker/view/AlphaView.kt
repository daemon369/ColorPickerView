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
) : View(context, attrs, defStyleAttr), IView<Float> {

    override fun setValue(value: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}