package lishui.module.media.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.lib.base.util.CommonUtils
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Scroller
import androidx.annotation.ColorInt
import kotlinx.coroutines.*
import lishui.service.imager.ImageLoader
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

/**
 * @author lishui.lin
 * Created it on 2021/6/3
 */
class MediaImagePreview(
    context: Context,
    attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    private var exitBlock: (() -> Unit)? = null

    private var mJob: Job? = null

    private val mEventPoint: PointF = PointF()
    private val mClosedThreshold: Int
    @ColorInt
    private val mBackgroundColor: Int
    private var mStatusBarColor: Int = 0
    private var mNavBarColor: Int = 0

    private val mHostActivity: Activity

    private val mScroller: Scroller by lazy {
        Scroller(context, AccelerateDecelerateInterpolator())
    }

    private val mTouchSlop: Int by lazy {
        ViewConfiguration.get(context).scaledTouchSlop
    }

    private val viewScope: CoroutineScope by lazy {
        CoroutineScope(
            SupervisorJob() + Dispatchers.Main.immediate
        )
    }

    init {
        scaleType = ScaleType.CENTER_INSIDE
        mHostActivity = CommonUtils.findActivity(context)
        mClosedThreshold = (context.resources.displayMetrics.density * 200).roundToInt()
        mBackgroundColor = Color.BLACK
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mStatusBarColor = mHostActivity.window.statusBarColor
        mNavBarColor = mHostActivity.window.navigationBarColor
        mHostActivity.window.statusBarColor = Color.BLACK
        mHostActivity.window.navigationBarColor = Color.BLACK
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawColor(mBackgroundColor)
        super.onDraw(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mEventPoint.x = event.x
                mEventPoint.y = event.y
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = (mEventPoint.x - event.x).toInt()
                val deltaY = (mEventPoint.y - event.y).toInt()
                if (deltaX.absoluteValue < mTouchSlop && deltaY.absoluteValue < mTouchSlop) {
                    return false
                }
                scrollTo(deltaX, deltaY)
                return true
            }
            MotionEvent.ACTION_UP -> {
                val deltaY = Math.abs(mEventPoint.y - event.y)
                if (deltaY > mClosedThreshold) {
                    scrollTo(0, 0)
                    exitBlock?.invoke()
                } else {
                    smoothScrollTo()
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            invalidate()
        }
    }

    fun loadImageFromUri(uri: Uri) {
        ImageLoader.with(this)
            .load(uri)
            .into(this)

//        job = viewScope.launch {
//            val bitmap = loadImageWork(uri)
//            bitmap?.apply {
//                setImageBitmap(this)
//            } ?: Glide.with(context).load(uri).into(this@MediaImagePreview)
//        }
    }

    fun setExitListener(block: (() -> Unit)?) {
        exitBlock = block
    }

    private fun smoothScrollTo(destX: Int = 0, destY: Int = 0) {
        val deltaX = destX - scrollX
        val deltaY = destY - scrollY
        mScroller.startScroll(scrollX, scrollY, deltaX, deltaY)
        invalidate()
    }

    private suspend fun loadImageWork(uri: Uri): Bitmap? = withContext(Dispatchers.IO) {
        var bitmap: Bitmap? = null

        try {
            val parcelFileDescriptor: ParcelFileDescriptor? =
                context.contentResolver.openFileDescriptor(uri, "r")
            parcelFileDescriptor?.use {
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeFileDescriptor(it.fileDescriptor, null, options)
                // calculate sample size

                options.inJustDecodeBounds = false
                bitmap = BitmapFactory.decodeFileDescriptor(it.fileDescriptor, null, options)
            }
        } catch (ex: FileNotFoundException) {
            // no-op
        } catch (ioEx: IOException) {
            // no-op
        }
        return@withContext bitmap
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHostActivity.window.statusBarColor = mStatusBarColor
        mHostActivity.window.navigationBarColor = mNavBarColor
        mJob?.cancel()
    }
}