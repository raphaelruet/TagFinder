package com.eseoteam.android.tagfinder;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Map;

/**
 * Manage the Creation of the PieChart
 * Created on 12/01/2014.
 * @author Raphael RUET.
 * @version 0.1.
 */
public class PieChart extends View
{
    // Attributes //

    /**
     * The angles of the PieChart
     * (Initialized with default values)
     */
    private int[] angles = {0,360};

    /**
     * The size of the drawn PieChart
     * (Initialized with default values)
     */
    private int piechartSize = 400;

    /**
     * The rectF used to create the PieChart
     */
    private RectF rectf = new RectF();

    /**
     * The paint used to set the color of the PieChart
     */
    private static final Paint pieChartPaint = new Paint();

    /**
     * The paint used to set the color of the background of the PieChart
     */
    private static final Paint backgroundPaint = new Paint();

    // Constructors //

    /**
     * Constructor for the PieChart
     * @param context the ApplicationContext
     */
    public PieChart(Context context) {
        super(context);
    }

    /**
     * Constructor for the PieChart
     * @param context the ApplicationContext
     */
    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupAttributes(attrs);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupAttributes(attrs);
    }

    // Accessor //

    /**
     * Accessor to the angles array of the Pie Chart
     * @return the angles of the Pie Chart
     */
    public int[] getAngles() {
        return this.angles;
    }

    /**
     * Accessor to the size of the Pie Chart
     * @return the size of the Pie Chart
     */
    public int[] getSize() {
        return this.angles;
    }

    /**
     * Accesssor to set the size of the PieChart
     * @param size the new size of the PieChart
     */
    public void setSize(int size) {
        this.piechartSize = size;
        invalidate();
        requestLayout();
    }

    /**
     * Accessor to set the angles of the Pie Chart
     * @param minAngle the min angle of the Pie Chart
     * @param maxAngle the max angle of the Pie Chart
     */
    public void setAngles(int minAngle, int maxAngle) {
        this.angles[0] = minAngle;
        this.angles[1] = maxAngle;
        invalidate();
        requestLayout();
    }

    // Methods //

    /**
     * Sets the colors of the PieChart (background and foreground)
     */
    private void setPieChartPaint() {
        pieChartPaint.setColor(getResources().getColor(R.color.main_theme_blue));
        backgroundPaint.setColor(getResources().getColor(R.color.main_theme_grey));
    }

    /**
     * Computes the angles of the PieChart
     * @return the computed angles
     */
    private float[] computeAngles() {
        return new float[]{angles[0]-90, angles[1] - angles[0]};
    }


    private void setupAttributes(AttributeSet attrs) {
        // Obtain a typed array of attributes
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PieChartViewStyle, 0, 0);
        // Extract custom attributes into member variables
        try {
            angles[0] = a.getInteger(R.styleable.PieChartViewStyle_startAngle, 0);
            angles[1] = a.getInteger(R.styleable.PieChartViewStyle_stopAngle, 90);
            piechartSize = a.getInteger(R.styleable.PieChartViewStyle_pieChartSize, 400);
        } finally {
            // TypedArray objects are shared and must be recycled.
            a.recycle();
        }
    }

    /**
     * Fill a canvas with a PieChart
     * @param canvas the canvas filled
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // OnDraw creation
        super.onDraw(canvas);

        // Angles calculation
        float computedAngles[] = computeAngles();

        // Colorization
        this.setPieChartPaint();

        // Rectangle creation
        this.rectf.set((getWidth() - piechartSize)/2,
                (getHeight() - piechartSize)/2,
                (getWidth() + piechartSize)/2,
                (getHeight() + piechartSize)/2);

        // PieChart creation
        canvas.drawArc(rectf, 0, 360, true, backgroundPaint);
        canvas.drawArc(rectf, computedAngles[0], computedAngles[1], true, pieChartPaint);
    }

}
