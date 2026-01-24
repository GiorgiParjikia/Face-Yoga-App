package ru.netology.faceyoga.ui.common

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import coil.size.Dimension
import coil.size.Size
import coil.transform.Transformation
import kotlin.math.max

/**
 * Аналог centerCrop, но с вертикальным смещением фокуса.
 *
 * focusY:
 * 0.0  -> больше "к верху" (видно больше верх)
 * 0.5  -> обычный centerCrop
 * 1.0  -> больше "к низу" (видно больше низ)
 *
 * Для портретов обычно норм: 0.35..0.45
 */
class VerticalCropTransformation(
    private val focusY: Float = 0.42f
) : Transformation {

    override val cacheKey: String = "VerticalCropTransformation(focusY=$focusY)"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val outW = size.width.pxOr(input.width)
        val outH = size.height.pxOr(input.height)

        if (outW <= 0 || outH <= 0) return input

        // centerCrop scale
        val scale = max(outW.toFloat() / input.width, outH.toFloat() / input.height)
        val scaledW = input.width * scale
        val scaledH = input.height * scale

        // X по центру
        val dx = (outW - scaledW) / 2f

        // Y со смещением (0 -> верх, 0.5 -> центр, 1 -> низ)
        val maxDy = (outH - scaledH) // отрицательное или 0
        val dy = maxDy * focusY.coerceIn(0f, 1f)

        val matrix = Matrix().apply {
            postScale(scale, scale)
            postTranslate(dx, dy)
        }

        val out = Bitmap.createBitmap(outW, outH, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(out)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        canvas.drawBitmap(input, matrix, paint)

        return out
    }

    private fun Dimension.pxOr(fallback: Int): Int =
        (this as? Dimension.Pixels)?.px ?: fallback
}
