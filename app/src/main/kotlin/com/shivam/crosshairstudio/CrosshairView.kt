package com.shivam.crosshairstudio

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class CrosshairView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var a1: Int = 0
    var a2: Int = 90
    var a3: Int = 180
    var a4: Int = 270
    var radius: Int = 10
    var length: Int = 20
    var thickness: Int = 3
    var activeColor: Int = Color.parseColor("#00E5FF")

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#060810")
        style = Paint.Style.FILL
    }

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#0E1120")
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }

    private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#0E1120")
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }

    private val outerGlowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        alpha = 46  // ~18%
    }

    private val midGlowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        alpha = 89  // ~35%
    }

    private val corePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)
    }

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#00E5FF")
        maskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.NORMAL)
    }

    private val dotCorePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    fun updateAndRedraw() {
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()
        val size = min(w, h)
        val cx = w / 2f
        val cy = h / 2f
        val scale = size / 600f

        // Background
        canvas.drawRect(0f, 0f, w, h, bgPaint)

        // Grid
        val step = size / 15f
        var x = cx - size / 2f
        while (x <= cx + size / 2f) {
            canvas.drawLine(x, cy - size / 2f, x, cy + size / 2f, gridPaint)
            x += step
        }
        var y = cy - size / 2f
        while (y <= cy + size / 2f) {
            canvas.drawLine(cx - size / 2f, y, cx + size / 2f, y, gridPaint)
            y += step
        }

        // Rings
        for (ringR in listOf(size * 0.13f, size * 0.27f, size * 0.40f)) {
            canvas.drawCircle(cx, cy, ringR, ringPaint)
        }

        // Crosshair arms
        val angles = listOf(a1, a2, a3, a4)
        val r = radius * scale
        val l = length * scale
        val t = maxOf(thickness, 1) * scale

        outerGlowPaint.color = activeColor
        outerGlowPaint.alpha = 46
        midGlowPaint.color = activeColor
        midGlowPaint.alpha = 89
        corePaint.color = if (thickness < 3) Color.WHITE else activeColor

        for (ang in angles) {
            val rd = Math.toRadians((ang + 90).toDouble())
            val cosA = cos(rd).toFloat()
            val sinA = sin(rd).toFloat()

            val sx = cx + cosA * r
            val sy = cy + sinA * r
            val ex = cx + cosA * (r + l)
            val ey = cy + sinA * (r + l)

            // Outer glow
            outerGlowPaint.strokeWidth = t + 6f * scale
            canvas.drawLine(sx, sy, ex, ey, outerGlowPaint)

            // Mid glow
            midGlowPaint.strokeWidth = t + 2f * scale
            canvas.drawLine(sx, sy, ex, ey, midGlowPaint)

            // Core
            corePaint.strokeWidth = t
            canvas.drawLine(sx, sy, ex, ey, corePaint)
        }

        // Center dot glow
        dotPaint.color = Color.parseColor("#00E5FF")
        canvas.drawCircle(cx, cy, 4f * scale, dotPaint)

        // Center dot core
        canvas.drawCircle(cx, cy, 2f * scale, dotCorePaint)
    }
}
