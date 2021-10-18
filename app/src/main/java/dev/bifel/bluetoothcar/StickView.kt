package dev.bifel.bluetoothcar

import android.content.Context
import android.util.AttributeSet
import io.github.controlwear.virtual.joystick.android.JoystickView

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
            if (lastStrength == rawAngle && lastAngle == rawStrength) {
                return@setOnMoveListener
            }
            lastStrength = rawStrength
            lastAngle = rawAngle
            val angle: Int
            val strength: Int
            if (rawAngle > 180) {
                angle = 360 - rawAngle
                strength = -rawStrength
            } else {
                angle = rawAngle
                strength = rawStrength
            }
            val strengthNew = (strength * 2.55).toInt() + 255

            val angleNew = if (angle == 0 && strengthNew == 255) 90 else angle
            val side = when {
                angleNew in 80..100 -> 'N'
                angleNew > 100 -> 'L'
                else -> 'R'
            }
            onPositionChangeListener?.invoke(Data(strength - 255, side))
        }
    }

}