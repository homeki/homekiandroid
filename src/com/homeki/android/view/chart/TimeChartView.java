package com.homeki.android.view.chart;

import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;

public class TimeChartView extends ChartView {
	private static int[] CHANNEL_COLORS = new int[] { Color.RED, Color.CYAN, Color.BLUE };

	private SparseArray<TimeSeries> mSeriesMap;

	public TimeChartView(Context context) {
		super(context);

		mSeriesMap = new SparseArray<TimeSeries>(1);
	}

	@Override
	public void putValueToChart(int channel, Date time, double value) {
		TimeSeries series = mSeriesMap.get(channel);

		if (series == null) {
			series = new TimeSeries("Channel: " + channel);
			mSeriesMap.put(channel, series);
			mDataSet.addSeries(series);

			XYSeriesRenderer renderer = new XYSeriesRenderer();
			if (channel < CHANNEL_COLORS.length) {
				renderer.setColor(CHANNEL_COLORS[channel]);
			}
			mRenderer.addSeriesRenderer(renderer);
		}

		series.add(time.getTime(), value);
	}

	@Override
	public GraphicalView onCreateGraphicalView(Context context, XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
		return ChartFactory.getTimeChartView(context, dataset, renderer, "HH:mm:ss");
	}
}
