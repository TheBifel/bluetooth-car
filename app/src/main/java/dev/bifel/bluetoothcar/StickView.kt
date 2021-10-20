package dev.bifel.bluetoothcar

import android.content.Context
import android.util.AttributeSet
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin

/**
 * Date: 18.10.2021
 * Time: 18:22
 *
 * @author Bohdan Ishchenko
 */
class StickView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : JoystickView(context, attrs) {

    var onPositionChangeListener: ((Data) -> Unit)? = null

    init {
        var lastStrength = 0
        var lastAngle = 0
        setOnMoveListener { rawAngle: Int, rawStrength: Int ->
            if (lastStrength == rawAngle && lastAngle == rawStrength) return@setOnMoveListener
            lastStrength = rawStrength
            lastAngle = rawAngle

            val radians = Math.toRadians(rawAngle.toDouble())
            val strength = ceil(rawStrength * if (sin(radians) < 0) -2.55 else 2.55).toInt()
            val cos = cos(radians)
            val side = when {
                strength == 0 -> 'N'
                cos > 0.3 -> 'R'
                cos < -0.3 -> 'L'
                else -> 'N'
            }

            onPositionChangeListener?.invoke(Data(strength + 255, side))
        }
    }

}