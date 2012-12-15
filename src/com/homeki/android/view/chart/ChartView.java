package com.homeki.android.view.chart;

import java.util.Date;

import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;
import android.widget.LinearLayout;

public abstract class ChartView extends LinearLayout {

	protected XYMultipleSeriesDataset mDataSet;
	protected XYMultipleSeriesRenderer mRenderer;

	protected double mMaxValue, mMinValue;
	protected Date mStartValue, mEndValue;

	private GraphicalView mGraphView;

	public ChartView(Context context) {
		super(context);
		
		mMaxValue = 0;
		mMinValue = 0;
		mStartValue = new Date();
		mEndValue = new Date(0);

		mDataSet = new XYMultipleSeriesDataset();
		mRenderer = new XYMultipleSeriesRenderer();
		mGraphView = onCreateGraphicalView(context, mDataSet, mRenderer);
		this.addView(mGraphView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	protected void updateScale() {
		mRenderer.setAxisTitleTextSize(16);
		mRenderer.setChartTitleTextSize(48);
		mRenderer.setLabelsTextSize(15);
		mRenderer.setLegendTextSize(15);
		mRenderer.setAxesColor(Color.argb(255, 0, 128, 172));
		mRenderer.setXLabelsAlign(Align.LEFT);
		mRenderer.setShowLegend(false);
		mRenderer.setYAxisMax(mMaxValue);
		mRenderer.setYAxisMin(mMinValue);
		mRenderer.setXAxisMax(mEndValue.getTime());
		mRenderer.setXAxisMin(mStartValue.getTime());
	}

	public void putValue(int channel, Date time, double value) {
		mMaxValue = value > mMaxValue ? value : mMaxValue;
		mMinValue = value < mMinValue ? value : mMinValue;
		mStartValue = mStartValue.after(time) ? time : mStartValue;
		mEndValue = mEndValue.before(time) ? time : mEndValue;
		
		putValueToChart(channel, time, value);

		updateScale();
		if (mGraphView != null) {
			if (mDataSet.getSeriesCount() > 0) {
				mGraphView.repaint();
			}
		}
	}

	protected abstract void putValueToChart(int channel, Date time, double value);

	public abstract GraphicalView onCreateGraphicalView(Context context, XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer);
}
