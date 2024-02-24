package com.hellcorp.customviewtest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import java.lang.Double.max
import kotlin.math.atan2
import kotlin.math.min

class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private val minViewSize = resources.getDimensionPixelSize(R.dimen.circularProgressViewMinSize)
    private val strokeWidthPx = 20f

    val progressPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.BLUE
        strokeWidth = strokeWidthPx
    }


    val trackPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.GRAY
        strokeWidth = strokeWidthPx
    }


    private var maxProgress = 100f
    private var currentProgress = 35f


    // region Расчёты
    // Рассчитываем координаты центра круга
    private var centerX = 0f
    private var centerY = 0f

    // Рассчитываем радиус круга
    private var radius = 0f

    private val arcRect = RectF()
    // endregion

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
//        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
//
//        val contentWidth = when(widthMode) {
//            MeasureSpec.UNSPECIFIED -> minViewSize
//            MeasureSpec.EXACTLY -> widthSize
//            MeasureSpec.AT_MOST -> widthSize
//            else -> error("Unexpected mode for width $widthMode")
//        }
//
//        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
//        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
//
//        val contentHeight = when(heightMode) {
//            MeasureSpec.UNSPECIFIED -> minViewSize
//            MeasureSpec.EXACTLY -> heightSize
//            MeasureSpec.AT_MOST -> heightSize
//            else -> error("Unexpected mode for width $widthMode")
//        }
//
//        val size = min(contentHeight, contentWidth)
//
//        setMeasuredDimension(size, size)
//    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

    /*
    //! Упрощенный расчет минимума
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }
     */

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Рассчитываем центр и радиус с учётом новых размеров
        centerX = measuredWidth / 2f
        centerY = measuredHeight / 2f

        // Рассчитываем радиус круга
        radius = (measuredWidth - strokeWidthPx) / 2f

        // устанавливаем координаты прямоугольника для
        // отрисовки дуги прогресса
        arcRect.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
    }

    override fun onDraw(canvas: Canvas) = with(canvas) {
        drawTrack(centerX, centerY, radius)
        drawProgress()
    }

    private fun Canvas.drawTrack(centerX: Float, centerY: Float, radius: Float) {
        drawCircle(centerX, centerY, radius, trackPaint)
    }

    private fun Canvas.drawProgress() {
        val sweepAngle = (currentProgress / maxProgress) * 360
        drawArc(arcRect, -90f, sweepAngle, false, progressPaint)
    }

    fun setCurrentProgress(newCurrentProgress: Float) {
        if (newCurrentProgress < 0f || newCurrentProgress > 100f) {
            return
        }

        this.currentProgress = newCurrentProgress
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                updateProgress(event.x, event.y)
                return true
            }
        }
        return gestureDetector.onTouchEvent(event)
    }

    private fun updateProgress(pX: Float, pY: Float) {
        val dy = (pY - centerY).toDouble()
        val dx = (pX - centerX).toDouble()

        var angle = Math.toDegrees(atan2(dy, dx) + (Math.PI / 2))

        if (angle < 0) {
            angle += 360
        }

        val progress = max(0.0, min(1.0, angle / 360.0))

        // Вызываем наш метод изменения переменной прогресса.
        setCurrentProgress(
            newCurrentProgress = (progress * maxProgress).toFloat()
        )
    }

    private val gestureDetector = GestureDetector(
        context,
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                super.onLongPress(e)
            }

            override fun onDoubleTap(e: MotionEvent): Boolean {
                return true
            }
        }
    )
}