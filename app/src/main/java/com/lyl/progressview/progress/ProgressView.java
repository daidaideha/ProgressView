package com.lyl.progressview.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.lyl.progressview.R;

/**
 * Created by Mr.Pro.Lin on 2016/7/7.
 * </p>
 * 左下角到右上角的渐变圆形进度条
 */
public class ProgressView extends View {

    /***
     * 内边距
     */
    private static final int PADDING = 20;

    /***
     * 渐变颜色 默认为2种 可以通过
     * {@link #setShaderColors(int[])}(来设置更多的颜色)
     */
    private int[] mShaderColors = new int[2];
    /***
     * mEmptyStrokeWidth 空圈宽度
     * mFullStrokeWidth 进度圈宽度
     */
    private float mEmptyStrokeWidth, mFullStrokeWidth;
    /***
     * mMaxCount 最大进度
     * mCurrentCount 当前进度
     */
    private float mMaxCount, mCurrentCount;
    /***
     * mWidth 控件宽度
     * mHeight 控件高度
     */
    private int mWidth, mHeight;
    /***
     * 文字大小
     */
    private int mTextSize;
    /***
     * 初始角度
     */
    private int mStartAngle;
    /***
     * 三种画笔
     */
    private Paint mFullPaint, mEmptyPaint, mTextPaint;
    /***
     * 背景矩形
     */
    private RectF mRectBackground;
    /***
     * 渐变方向
     */
    private ColorOrientation mColorOrientation;

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgressView(Context context) {
        this(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
            mShaderColors[0] = ta.getInteger(R.styleable.ProgressView_shaderStartColor, 0xFF17EEE1);
            mShaderColors[1] = ta.getInteger(R.styleable.ProgressView_shaderEndColor, 0xFF18D06A);
            mTextSize = ta.getDimensionPixelSize(R.styleable.ProgressView_textSize, dip2Px(18));
            mStartAngle = ta.getInt(R.styleable.ProgressView_startAngle, 0);
            mEmptyStrokeWidth = ta.getDimension(R.styleable.ProgressView_emptyStrokeWidth, (float) dip2Px(3));
            mFullStrokeWidth = ta.getDimension(R.styleable.ProgressView_fullStrokeWidth, (float) dip2Px(5));
            mColorOrientation = ColorOrientation.values()[ta.getInt(R.styleable.ProgressView_colorOrientation, 0)];
            ta.recycle();
        } else {
            mShaderColors[0] = ContextCompat.getColor(context, R.color.progress_start);
            mShaderColors[1] = ContextCompat.getColor(context, R.color.progress_end);
            mTextSize = dip2Px(18);
            mStartAngle = 0;
            mColorOrientation = ColorOrientation.HORIZONTAL;
            mEmptyStrokeWidth = (float) dip2Px(3);
            mFullStrokeWidth = (float) dip2Px(5);
        }
        mRectBackground = new RectF();
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = measureDimension(dip2Px(200), widthMeasureSpec);
        mHeight = measureDimension(dip2Px(200), heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        mRectBackground.set(PADDING, PADDING, mWidth - PADDING, mHeight - PADDING);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LinearGradient shader;
        switch (mColorOrientation) {
            case VERTICAL:
                shader = new LinearGradient(w / 2, 0, w / 2, h, mShaderColors, null, Shader.TileMode.MIRROR);
                break;
            case LEFT_TOP_RIGHT:
                shader = new LinearGradient(0, h, w, 0, mShaderColors, null, Shader.TileMode.MIRROR);
                break;
            case LEFT_BOTTOM_RIGHT:
                shader = new LinearGradient(0, 0, w, h, mShaderColors, null, Shader.TileMode.MIRROR);
                break;
            case HORIZONTAL:
            default:
                shader = new LinearGradient(0, h / 2, w, h / 2, mShaderColors, null, Shader.TileMode.MIRROR);
                break;
        }
        mFullPaint.setShader(shader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float section = mCurrentCount / mMaxCount;
        // 绘制空圈
        canvas.drawArc(mRectBackground, 0, 360, false, mEmptyPaint);
        // 绘制当前进度圈
        canvas.drawArc(mRectBackground, -90 + mStartAngle, section * 360, false, mFullPaint);
        // 绘制中间的百分比文字
        String text = (int) (section * 100) + "%";
        canvas.drawText(text, (mWidth - mTextPaint.measureText(text)) / 2, mHeight / 2, mTextPaint);
    }

    /***
     * dip转换成px
     *
     * @param dip dip
     * @return px
     */
    private int dip2Px(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /***
     * 计算控件宽高
     *
     * @param defaultSize 默认大小
     * @param measureSpec
     * @return 最终大小
     */
    private int measureDimension(int defaultSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        } else {
            result = defaultSize;
        }
        return result;
    }

    /***
     * 设置渐变色
     *
     * @param colors 渐变色数组
     */
    public void setShaderColors(int[] colors) {
        this.mShaderColors = colors;
        requestLayout();
    }

    /***
     * 设置文字大小
     *
     * @param textSize 文字大小
     */
    public void setTextSize(int textSize) {
        if (mTextPaint == null)
            throw new NullPointerException("The TextPaint is null");
        if (textSize != mTextPaint.getTextSize()) {
            mTextPaint.setTextSize(mTextSize);
            requestLayout();
            invalidate();
        }
    }

    /***
     * 设置初始点
     *
     * @param startAngle 初始点角度 0为正上方
     */
    public void setStartAngle(int startAngle) {
        if (mStartAngle != startAngle) {
            this.mStartAngle = startAngle;
            invalidate();
        }
    }

    /***
     * 设置渐变方向
     *
     * @param colorOrientation {@link ColorOrientation#HORIZONTAL} (水平方向 从左到右)
     *                         {@link ColorOrientation#VERTICAL} (垂直方向 从上到下)
     *                         {@link ColorOrientation#LEFT_TOP_RIGHT} (从左下角到右上角)
     *                         {@link ColorOrientation#LEFT_BOTTOM_RIGHT} (从左上角到右下角)
     */
    public void setColorOrientation(ColorOrientation colorOrientation) {
        if (mColorOrientation != colorOrientation) {
            this.mColorOrientation = colorOrientation;
            requestLayout();
        }
    }

    /***
     * 设置空全圆环宽度
     *
     * @param emptyStrokeWidth 圆环宽度
     */
    public void setEmptyStrokeWidth(float emptyStrokeWidth) {
        if (mEmptyPaint == null)
            throw new NullPointerException("The EmptyPaint is null");
        if (emptyStrokeWidth != mEmptyPaint.getStrokeWidth()) {
            mEmptyPaint.setStrokeWidth(emptyStrokeWidth);
            requestLayout();
            invalidate();
        }
    }

    /***
     * 设置进度圈圆环宽度
     *
     * @param fullStrokeWidth 圆环宽度
     */
    public void setFullStrokeWidth(float fullStrokeWidth) {
        if (mFullPaint == null)
            throw new NullPointerException("The FullPaint is null");
        if (fullStrokeWidth != mFullPaint.getStrokeWidth()) {
            mFullPaint.setStrokeWidth(fullStrokeWidth);
            requestLayout();
            invalidate();
        }
    }

    /***
     * 初始化画笔
     */
    private void initPaint() {
        mFullPaint = new Paint();
        mFullPaint.setAntiAlias(true);
        mFullPaint.setStrokeWidth(mFullStrokeWidth);
        mFullPaint.setStyle(Paint.Style.STROKE);
        mFullPaint.setStrokeCap(Paint.Cap.ROUND);
        mFullPaint.setColor(Color.BLACK);

        mEmptyPaint = new Paint();
        mEmptyPaint.setAntiAlias(true);
        mEmptyPaint.setStrokeWidth(mEmptyStrokeWidth);
        mEmptyPaint.setStyle(Paint.Style.STROKE);
        mEmptyPaint.setStrokeCap(Paint.Cap.ROUND);
        mEmptyPaint.setColor(ContextCompat.getColor(getContext(), R.color.progress_empty));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(mTextSize);
    }

    /***
     * 获取最大进度值
     *
     * @return 最大进度
     */
    public float getMaxCount() {
        return mMaxCount;
    }

    /***
     * 获取当前进度值
     *
     * @return 当前进度
     */
    public float getCurrentCount() {
        return mCurrentCount;
    }

    /***
     * 设置最大的进度值
     *
     * @param maxCount 最大进度
     */
    public void setMaxCount(float maxCount) {
        this.mMaxCount = maxCount;
    }

    /***
     * 设置当前的进度值
     *
     * @param currentCount 当前进度
     */
    public void setCurrentCount(float currentCount) {
        this.mCurrentCount = currentCount > mMaxCount ? mMaxCount : currentCount;
        invalidate();
    }
}
