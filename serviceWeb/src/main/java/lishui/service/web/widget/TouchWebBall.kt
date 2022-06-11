package lishui.service.web.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.lib.base.util.Utilities
import android.util.AttributeSet
import android.util.FloatProperty
import android.util.Property
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.IntRange
import lishui.android.ui.extensions.dp
import lishui.service.web.R
import kotlin.math.absoluteValue
import kotlin.math.min

/**
 * @author lishui.lin
 * Created it on 2021/8/19
 */
class TouchWebBall(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    companion object {
        private const val CLICK_TIME_THRESHOLD = 100L
        private const val LONG_CLICK_TIME_THRESHOLD = 700L
    }

    private val mProgressProperty: Property<TouchWebBall, Float> =
        object : FloatProperty<TouchWebBall>("progress") {
            override fun get(view: TouchWebBall): Float = view.mProgress

            override fun setValue(view: TouchWebBall, value: Float) {
                view.mProgress = value
                invalidate()
            }
        }

    private val mHitRect = Rect()
    private val mDownPoint: PointF = PointF()
    private val mEventPoint: PointF = PointF()
    private val mBgPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mCirclePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mTouchSlop: Int by lazy {
        ViewConfiguration.get(context).scaledTouchSlop
    }

    private val mSizeThreshold: Int by lazy {
        dp(56)
    }

    private val mMarginThreshold: Int by lazy {
        dp(16)
    }

    private var mCircleSize: Int = 0
    private var mCircleColor: Int = 0
    private var mBackgroundColor: Int = 0

    private var mProgress: Float = 0F
    private var isShowProgressCircle: Boolean = true

    override fun onFinishInflate() {
        super.onFinishInflate()
        mCircleSize = dp(3)
        mCircleColor = resources.getColor(R.color.uikit_accent_primary, null)
        mBackgroundColor = resources.getColor(R.color.uikit_bg_layout, null)

        mBgPaint.color = mBackgroundColor
        mBgPaint.style = Paint.Style.FILL
        val shadowSize = dp(3)
        val shadowColor = resources.getColor(R.color.uikit_mask, null)
        mBgPaint.setShadowLayer(shadowSize.toFloat(), 0F, 0F, shadowColor)

        mCirclePaint.color = mCircleColor
        mCirclePaint.style = Paint.Style.STROKE
        mCirclePaint.strokeWidth = mCircleSize.toFloat()

//        ObjectAnimator.ofFloat(this, mProgressProperty, 270F).apply {
//            duration = 5000
//            start()
//        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val resultSize = min(min(widthSize, heightSize), mSizeThreshold)
        setMeasuredDimension(resultSize, resultSize)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //LogUtils.d("TouchWebBall event=${event.action}")

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                animate().cancel()
                mDownPoint.x = event.x
                mDownPoint.y = event.y
                mEventPoint.x = event.x
                mEventPoint.y = event.y
                //LogUtils.d("TouchWebBall ACTION_DOWN x= $x, y=$y")
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = event.x - mEventPoint.x
                val deltaY = event.y - mEventPoint.y
                if (deltaX.absoluteValue < mTouchSlop && deltaY.absoluteValue < mTouchSlop) {
                    return false
                }
                x += deltaX
                y += deltaY
                mEventPoint.x = event.x
                mEventPoint.y = event.y
                return true
            }
            MotionEvent.ACTION_UP -> {
                //LogUtils.d("TouchWebBall ACTION_UP x= $x, y=$y, eventTime=${event.eventTime}, downTime=${event.downTime}")
                if (mDownPoint.equals(event.x, event.y)) {
                    val touchDuration = (event.eventTime - event.downTime) // ms
                    if (touchDuration <= CLICK_TIME_THRESHOLD) {
                        performClick()
                    } else if (touchDuration >= LONG_CLICK_TIME_THRESHOLD) {
                        performLongClick()
                    }
                } else {
                    checkBallLayout()
                }
                return true
            }
        }
        return false
    }

    fun shouldInterceptTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            getHitRect(mHitRect)
            if (mHitRect.contains(event.x.toInt(), event.y.toInt())) {
                return true
            }
        }
        return false
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 1. draw background
        val radius = width / 2f - mCircleSize
        val cx = width / 2f
        val cy = height / 2f
        canvas.drawCircle(cx, cy, radius, mBgPaint)

        // 2. draw progress circle if need
        if (isShowProgressCircle) {
            canvas.drawArc(
                mCircleSize.toFloat(),
                mCircleSize.toFloat(),
                width.toFloat() - mCircleSize,
                height.toFloat() - mCircleSize,
                -90F,
                mProgress,
                false,
                mCirclePaint
            )
        }
    }

    fun setCircleProgress(@IntRange(from = 0, to = 100) progress: Int) {
        mProgress = (progress * 0.01 * 360).toFloat()
        invalidate()
    }

    fun showProgress() {
        isShowProgressCircle = true
        invalidate()
    }

    fun hideProgress() {
        isShowProgressCircle = false
        mProgress = 0F
        invalidate()
    }

    private fun checkBallLayout() {
        if (parent != null) {
            val curX = x
            val curY = y
            val parentLayout = parent as ViewGroup
            val parentWidth = parentLayout.width
            val parentHeight = parentLayout.height
            val tempX = Utilities.boundToRange(
                curX, mMarginThreshold.toFloat(),
                (parentWidth - mMarginThreshold - width).toFloat()
            )
            val tempY = Utilities.boundToRange(
                curY, mMarginThreshold.toFloat(),
                (parentHeight - mMarginThreshold - height).toFloat()
            )
            if (curX != tempX || curY != tempY) {
                animate().x(tempX).y(tempY).apply {
                    interpolator = AccelerateDecelerateInterpolator()
                    duration = 500
                    start()
                }
            }
        }
    }

}