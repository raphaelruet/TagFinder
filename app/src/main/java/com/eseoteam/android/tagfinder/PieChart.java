package com.eseoteam.android.tagfinder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

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
    private static final int PIECHART_SIZE = 400;

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
     * Accessor to set the angles of the Pie Chart
     * @param angles the angles to set
     */
    public void setAngles(int[] angles) {
        this.angles[0] = angles[0];
        this.angles[1] = angles[1];
    }

    // Methods //

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
        this.rectf.set((getWidth() - PIECHART_SIZE)/2,
                (getHeight() - PIECHART_SIZE)/2,
                (getWidth() + PIECHART_SIZE)/2,
                (getHeight() + PIECHART_SIZE)/2);

        // PieChart creation
        canvas.drawArc(rectf, 0, 360, true, backgroundPaint);
        canvas.drawArc(rectf, computedAngles[0], computedAngles[1], true, pieChartPaint);
    }

}
