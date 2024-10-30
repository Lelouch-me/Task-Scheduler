package com.LRG.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
public class ChartData {
	
	private String[] xMin;
	private String[] xMax;
	private String[] yMin;
	private String[] yMax;
	private List<List<String>> series;
	private List<String> seriesNames;
	private Map<String, String> params;
	private List<String> seriesYears;
	private String chartBenchmarkSymbol;
	private String[] medianSeries;
	
	@JsonIgnore
	private Map<String, String> internals;
	
	public String[] getxMin() {
		return xMin;
	}

	public void setxMin(String[] xMin) {
		this.xMin = xMin;
	}

	public String[] getxMax() {
		return xMax;
	}

	public void setxMax(String[] xMax) {
		this.xMax = xMax;
	}

	public String[] getyMin() {
		return yMin;
	}

	public void setyMin(String[] yMin) {
		this.yMin = yMin;
	}

	public String[] getyMax() {
		return yMax;
	}

	public void setyMax(String[] yMax) {
		this.yMax = yMax;
	}

	public List<List<String>> getSeries() {
		return series;
	}

	public void setSeries(List<List<String>> series) {
		this.series = series;
	}

	public List<String> getSeriesNames() {
		return seriesNames;
	}

	public void setSeriesNames(List<String> seriesNames) {
		this.seriesNames = seriesNames;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public Map<String, String> getInternals() {
		return internals;
	}

	public void setInternals(Map<String, String> internals) {
		this.internals = internals;
	}

	public List<String> getSeriesYears() {
		return seriesYears;
	}

	public void setSeriesYears(List<String> seriesYears) {
		this.seriesYears = seriesYears;
	}

	public String getChartBenchmarkSymbol() {
		return chartBenchmarkSymbol;
	}

	public void setChartBenchmarkSymbol(String chartBenchmarkSymbol) {
		this.chartBenchmarkSymbol = chartBenchmarkSymbol;
	}

	public String[] getMedianSeries() {
		return medianSeries;
	}

	public void setMedianSeries(String[] medianSeries) {
		this.medianSeries = medianSeries;
	}

	@Override
	public String toString() {
		return "ChartData [xMin=" + xMin + ", xMax=" + xMax + ", yMin=" + yMin
				+ ", yMax=" + yMax + ", series=" + series + ", seriesNames="
				+ seriesNames + ", params=" + params + "]";
	}

}
