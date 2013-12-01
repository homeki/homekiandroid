package com.homeki.android.view.chart;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Date;

public class TimeChartView extends ChartView {
	private static int[] CHANNEL_COLORS = new int[] { Color.RED, Color.CYAN, Color.BLUE };

	private SparseArray<TimeSeries> seriesMap;

	public TimeChartView(Context context) {
		super(context);
		seriesMap = new SparseArray<TimeSeries>(1);
	}

	@Override
	public void putValueToChart(int channel, Date time, double value) {
		TimeSeries series = seriesMap.get(channel);

		if (series == null) {
			series = new TimeSeries("Channel: " + channel);
			seriesMap.put(channel, series);
			dataSet.addSeries(series);

			XYSeriesRenderer renderer = new XYSeriesRenderer();
			if (channel < CHANNEL_COLORS.length) {
				renderer.setColor(CHANNEL_COLORS[channel]);
			}
			this.renderer.addSeriesRenderer(renderer);
		}

		series.add(time.getTime(), value);
	}

	@Override
	public GraphicalView onCreateGraphicalView(Context context, XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
		return ChartFactory.getTimeChartView(context, dataset, renderer, "HH:mm:ss");
	}
}
