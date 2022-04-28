package lishui.module.main.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import lishui.module.main.R

/**
 *  author : linlishui
 *  time   : 2021/11/26
 *  desc   : 带有阴影的TextView
 */
class TextShadowView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttr: Int = 0
) : AppCompatTextView(context, attributeSet, defAttr) {

    private val mMaskPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {

//        if (layerType != LAYER_TYPE_SOFTWARE) {
//            setLayerType(LAYER_TYPE_SOFTWARE, null)
//        }

        mMaskPaint.color = context.getColor(R.color.uikit_bg_layout)
        //mMaskPaint.style = Paint.Style.FILL_AND_STROKE
        //mMaskPaint.maskFilter = BlurMaskFilter(16f, BlurMaskFilter.Blur.SOLID)
        //mMaskPaint.setShadowLayer(16f, 0f, 16f, Color.argb(1f,1f,0f,0f))
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawRoundRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            16f,
            16f,
            mMaskPaint
        )
        super.onDraw(canvas)
    }
}