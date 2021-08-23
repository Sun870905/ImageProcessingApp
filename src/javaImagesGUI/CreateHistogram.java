/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaImagesGUI;

import java.awt.Color;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Luis Marcos
 */
public class CreateHistogram {

	/**
	 * Enumeration with all available channels
	 */
	public enum availableChannel {
		red, green, blue, alpha, grayscale
	}

	private void BarColor(availableChannel chanel, JFreeChart chart) {
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		Color barColor = Color.BLACK;
		switch (chanel) {
		case red:
			barColor = Color.red;
			break;
		case green:
			barColor = Color.green;
			break;
		case blue:
			barColor = Color.blue;
			break;
		case alpha:
			barColor = Color.white;
			break;
		case grayscale:
			barColor = Color.gray;
			break;
		}
		renderer.setSeriesPaint(0, barColor);
	}

	/**
	 * Create a bar graph with the histogram and draws on the Jpanel
	 * (jPanelHistogram)
	 * 
	 * @param histogram
	 *            frequency histogram of an image (int[256])
	 * @param jPanelHistogram
	 *            JPanel where the histogram will be drawn
	 * @param chanel
	 *            Histogram Channel (depending on the selected channel, the bar
	 *            graph will be drawn to that color)
	 * @see javaImagesGUI.CreateHistogram.availableChannel
	 */
	public void createHistogramBarChart(int[] histogram, JPanel jPanelHistogram, availableChannel chanel) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		String serie = "Number of píxels (" + chanel.toString() + ")";
		for (int i = 0; i < histogram.length; i++) {
			dataset.addValue(histogram[i], serie, "" + i);
		}
		JFreeChart chart = ChartFactory.createBarChart("Frequency Histogram", null, null, dataset,
				PlotOrientation.VERTICAL, true, true, false);
		BarColor(chanel, chart);
		chart.setAntiAlias(true);
		chart.setBackgroundPaint(new Color(214, 217, 223));
		jPanelHistogram.removeAll();
		jPanelHistogram.repaint();
		jPanelHistogram.setLayout(new java.awt.BorderLayout());
		jPanelHistogram.add(new ChartPanel(chart));
		jPanelHistogram.validate();
	}
}
