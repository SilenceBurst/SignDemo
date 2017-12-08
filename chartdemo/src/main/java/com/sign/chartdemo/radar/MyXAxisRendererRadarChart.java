package com.sign.chartdemo.radar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.renderer.XAxisRendererRadarChart;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * 项目名:    ChartDemo
 * 包名:      com.sign.chartdemo
 * 文件名:    MyXAxisRendererRadarChart
 * 创建者:    CYS
 * 创建时间:  2017/12/5 0005 on 17:54
 * 描述:     TODO
 */
public class MyXAxisRendererRadarChart extends XAxisRendererRadarChart {

    private RadarChart mChart;

    public MyXAxisRendererRadarChart(ViewPortHandler viewPortHandler, XAxis xAxis, RadarChart chart) {
        super(viewPortHandler, xAxis, null);

        mChart = chart;
    }

    @Override
    public void renderAxisLabels(Canvas c) {

        if (!mXAxis.isEnabled() || !mXAxis.isDrawLabelsEnabled())
            return;

        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
        final MPPointF drawLabelAnchor = MPPointF.getInstance(0.5f, 0.25f);

        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());
        mAxisLabelPaint.setColor(mXAxis.getTextColor());

        float sliceangle = mChart.getSliceAngle();

        // calculate the factor that is needed for transforming the value to
        // pixels
        float factor = mChart.getFactor();

        MPPointF center = mChart.getCenterOffsets();
        MPPointF pOut = MPPointF.getInstance(0, 0);
        for (int i = 0; i < mChart.getData().getMaxEntryCountSet().getEntryCount(); i++) {

            String label = mXAxis.getValueFormatter().getFormattedValue(i, mXAxis);

            float angle = (sliceangle * i + mChart.getRotationAngle()) % 360f;

            Utils.getPosition(center, mChart.getYRange() * factor
                    + mXAxis.mLabelRotatedWidth / 2f, angle, pOut);

            drawLabel(c, label, pOut.x, pOut.y - mXAxis.mLabelRotatedHeight / 2.f,
                    drawLabelAnchor, labelRotationAngleDegrees);
        }

        MPPointF.recycleInstance(center);
        MPPointF.recycleInstance(pOut);
        MPPointF.recycleInstance(drawLabelAnchor);
    }

    @Override
    protected void drawLabel(Canvas c, String formattedLabel, float x, float y, MPPointF anchor, float angleDegrees) {
        float labelHeight = mXAxis.getTextSize();
        float labelInterval = 25f;
        String[] labels = formattedLabel.split(" ");

        Paint mFirstLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFirstLinePaint.setColor(Color.WHITE);
        mFirstLinePaint.setTextAlign(Paint.Align.CENTER);
        mFirstLinePaint.setTextSize(Utils.convertDpToPixel(15f));
        mFirstLinePaint.setTypeface(mXAxis.getTypeface());

        Paint mSecondLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondLinePaint.setColor(0xFF9b9b9b);
        mSecondLinePaint.setTextAlign(Paint.Align.CENTER);
        mSecondLinePaint.setTextSize(Utils.convertDpToPixel(13f));
        mSecondLinePaint.setTypeface(mXAxis.getTypeface());
        float move = Utils.convertDpToPixel(22f);
//        Utils.drawXAxisValue(c, formattedLabel, x, y, mAxisLabelPaint, anchor, angleDegrees);
        if (labels.length > 1) {
            Utils.drawXAxisValue(c, labels[0], x, y - move, mFirstLinePaint, anchor, angleDegrees);
            Utils.drawXAxisValue(c, labels[1], x, y + labelHeight + labelInterval - move, mSecondLinePaint, anchor, angleDegrees);
        } else {
            Utils.drawXAxisValue(c, formattedLabel, x, y, mFirstLinePaint, anchor, angleDegrees);
        }
    }

    /**
     * XAxis LimitLines on RadarChart not yet supported.
     *
     * @param c
     */
    @Override
    public void renderLimitLines(Canvas c) {
        // this space intentionally left blank
    }
}
