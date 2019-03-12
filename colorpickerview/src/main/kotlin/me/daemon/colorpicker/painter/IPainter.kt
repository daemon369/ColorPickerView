package me.daemon.colorpicker.painter

import android.graphics.Canvas
import me.daemon.colorpicker.view.IView

/**
 * @author daemon
 * @since 2019-03-10 17:55
 */
interface IPainter<VIEW : IView<VALUE>, VALUE : IView.Value> {


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

    /**
     * draw view
     *
     * 绘制VIEW
     *
     * @param view       VIEW
     * @param canvas     canvas to draw
     * @param isChanging whether view is changing
     */
    fun onDraw(
            view: VIEW,
            canvas: Canvas,
            isChanging: Boolean
    )

    /**
     * update view value based on touch event coordinate
     *
     * @param view  VIEW
     * @param x     touch event x
     * @param y     touch event y
     */
    fun onUpdate(
            view: VIEW,
            x: Float,
            y: Float
    )

    /**
     * update view by value
     *
     * @param view  VIEW
     */
    fun updateByValue(
            view: VIEW
    )

}