package me.daemon.colorpicker

import me.daemon.colorpicker.internal.Factor

/**
 * @author yc
 * @since 2019-04-12 22:48
 */
interface IFactor {

    interface ICenter {

        fun changeFactor(factor: Factor, value: Float)
    }

    var colorCenter1: ICenter?

    fun onFactorChange(value: Float)

}