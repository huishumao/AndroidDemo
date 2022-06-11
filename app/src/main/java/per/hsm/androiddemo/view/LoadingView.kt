package per.hsm.androiddemo.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import per.hsm.androiddemo.R

/**
 * TODO: document your custom view class.
 */
class LoadingView : View {

    private var _circleColor: Int = Color.WHITE
    private var _exampleDimension: Float = 10f
    private var circleRadius: Float = 50f //

    private lateinit var mPaint: Paint
    private lateinit var  mPath: Path
    private lateinit var  mPathMeasure: PathMeasure


    /**
     * The font color
     */
    var exampleColor: Int
        get() = _circleColor
        set(value) {
            _circleColor = value
            invalidatePaintAndMeasurements()
        }

    /**
     * In the example view, this dimension is the font size.
     */
    var exampleDimension: Float
        get() = _exampleDimension
        set(value) {
            _exampleDimension = value
            invalidatePaintAndMeasurements()
        }



    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.LoadingView, defStyle, 0
        )

        _circleColor = a.getColor(
            R.styleable.LoadingView_exampleColor,
            exampleColor
        )
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        _exampleDimension = a.getDimension(
            R.styleable.LoadingView_exampleDimension,
            exampleDimension
        )
        a.recycle()

        // Set up a default TextPaint object
        this.mPaint = Paint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }

        mPath = Path()
        invalidatePaintAndMeasurements()
    }

    private fun invalidatePaintAndMeasurements() {
        this.mPaint?.let {
            it.color = _circleColor
            it.strokeWidth = _exampleDimension
        }
        mPath.reset()
        mPath.addCircle(circleRadius, circleRadius, circleRadius, Path.Direction.CW)
        mPathMeasure = PathMeasure(mPath, true)
        startAnimator()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(200F, 200F)

        canvas.drawPath(tempPath1, mPaint)
        canvas.drawPath(tempPath2, mPaint)
        canvas.drawPath(tempPath3, mPaint)

    }


    val tempPath1 = Path()
    val tempPath2 = Path()
    val tempPath3 = Path()


    var temp = 0F

    fun startAnimator() {
        val animator = ValueAnimator.ofFloat(0F, 1F)
        animator.duration = 3000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.RESTART
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            val length = mPathMeasure.length

            if ((it.animatedValue as Float)% 0.2F == 0F){
                temp = 0F
            }else {
                temp = (it.animatedValue as Float)/3
            }
            tempPath1.reset()
            tempPath2.reset()
            tempPath3.reset()
            if((it.animatedValue as Float) < 0.33F) {
                mPathMeasure.getSegment(
                    0F,
                    length.times(it.animatedValue as Float),
                    tempPath1,
                    true
                )

                mPathMeasure.getSegment(
                    length / 3,
                    length / 3 + length.times(it.animatedValue as Float),
                    tempPath2,
                    true
                )

                mPathMeasure.getSegment(length * 2/ 3,
                    length*2 / 3 + length.times(it.animatedValue as Float),tempPath3, true)


            }else if((it.animatedValue as Float) < 0.66F){
                mPathMeasure.getSegment(
                    length.times(it.animatedValue as Float) - length/3,
                    length /3,
                    tempPath1,
                    true
                )

                mPathMeasure.getSegment(
                    length.times(it.animatedValue as Float),
                    length *2 /3,
                    tempPath2,
                    true
                )

                mPathMeasure.getSegment(length/3 +  length.times(it.animatedValue as Float), length ,tempPath3, true)


            }else {
//                mPathMeasure.getSegment(
//                    0F,
//                    length.times(it.animatedValue as Float) - length*2/3,
//                    tempPath1,
//                    true
//                )
//
//                mPathMeasure.getSegment(
//                    length/3,
//                    length.times(it.animatedValue as Float) - length/3,
//                    tempPath2,
//                    true
//                )
//
                mPathMeasure.getSegment(length*2/3, length.times(it.animatedValue as Float),tempPath3, true)

            }



            invalidate()
        }
        animator.start()
    }

}