package com.homeki.android.view.chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.widget.LinearLayout;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.Date;

public abstract class ChartView extends LinearLayout {
	protected XYMultipleSeriesDataset dataSet;
	protected XYMultipleSeriesRenderer renderer;

	protected double maxValue, minValue;
	protected Date startValue, endValue;

	private GraphicalView graphView;

	public ChartView(Context context) {
		super(context);
		
		maxValue = 0;
		minValue = 0;
		startValue = new Date();
		endValue = new Date(0);

		dataSet = new XYMultipleSeriesDataset();
		renderer = new XYMultipleSeriesRenderer();
		graphView = onCreateGraphicalView(context, dataSet, renderer);
		this.addView(graphView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	protected void updateScale() {
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(48);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setAxesColor(Color.argb(255, 0, 128, 172));
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setShowLegend(false);
		renderer.setYAxisMax(maxValue);
		renderer.setYAxisMin(minValue);
		renderer.setXAxisMax(endValue.getTime());
		renderer.setXAxisMin(startValue.getTime());
	}

	public void putValue(int channel, Date time, double value) {
		maxValue = value > maxValue ? value : maxValue;
		minValue = value < minValue ? value : minValue;
		startValue = startValue.after(time) ? time : startValue;
		endValue = endValue.before(time) ? time : endValue;
		
		putValueToChart(channel, time, value);

		updateScale();
		if (graphView != null) {
			if (dataSet.getSeriesCount() > 0) {
				graphView.repaint();
			}
		}
	}

	protected abstract void putValueToChart(int channel, Date time, double value);

	public abstract GraphicalView onCreateGraphicalView(Context context, XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer);
}
