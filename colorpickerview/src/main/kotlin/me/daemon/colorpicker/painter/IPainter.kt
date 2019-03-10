package me.daemon.colorpicker.painter

import android.view.View
import me.daemon.colorpicker.BrightnessView

/**
 * @author daemon
 * @since 2019-03-10 17:55
 */
interface IPainter<VIEW:View, VALUE> {


    /**
     * on view size changed
     *
     * @param view VIEW
     * @param w    width of this view
     * @param h    height of this view
     */
    fun onSizeChanged(
            view: VIEW,
            w: Int,
            h: Int
    )


}