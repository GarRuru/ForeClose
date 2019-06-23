package cw;

import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class jChart extends JFrame
{
	private String[] series;	//資料數量，可能有多個
	private String[] category;	//橫坐標：標示日期
	private GTrendData GData;
	private int MIN,MAX;		//搜尋次數的最大、最小值（控制座標軸Range）
	
	private void findMinMax(int[] data)
	{
		int[] dataCopy = new int[data.length];
		System.arraycopy(data, 0, dataCopy, 0, data.length);
		Arrays.sort(dataCopy);
		//取上下界
		MIN = (dataCopy[0] / 100) *100;
		MAX = ((dataCopy[dataCopy.length-1] / 100) + 1 )*100;
	}
	
	public jChart(GTrendData g) 
	{
		GData = g;
		// 步驟1. 建立Dataset  
        CategoryDataset dataset = setDataset();  
        // 步驟2. 建立BarChart  
        JFreeChart chart = createChart(dataset);  
          
        // 步驟3. 將所建立的 BarChart 放在 ChartPanel 上並設定大小.  
        ChartPanel chartPanel = new ChartPanel(chart);  
        chartPanel.setPreferredSize(new Dimension(800, 570));  
        getContentPane().add(chartPanel); 
        pack();
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  
        setVisible(true);
        setBounds(150, 150, 820, 620);
        
	}
	
	
	//要改的函式，傳入要放在表格上的資料
	private CategoryDataset setDataset() 
	{  
		//String[] series = {"100", "200", "300","400"};  //row分類，放不同的搜尋結果
        //String[] category = {"z", "x", "c", "v", "b"};		//colum分類，放日期  
		
        //double[][] data = {{8.0, 5.5, 10.2,20}, {4.2, 7.8, 30,15}, {3.5, 5.3, 6.8,26}  
        //,{9.2, 14.5, 10.7,8}, {6.6, 8.3, 7.9,13}};  //放搜尋的數量
        //DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
        //for(int i=0; i<category.length; i++)  
        //    for(int j=0; j<series.length; j++)  
        //        dataset.addValue(data[i][j], series[j], category[i]); 
        
		series = new String[1];
		series[0] = GData.getAddress();
		int data[] = new int[GData.getFullSearchCount().size()];	//縱座標：表示搜尋數量
		category = new String[GData.getFullDate().size()];
		for(int i=0;i<data.length;i++)
		{
			data[i] = GData.getFullSearchCount().get(i);
			category[i] = GData.getFullDate().get(i);
		}
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
        for(int i=0;i<data.length;i++)
        	dataset.addValue(data[i], series[0], category[i]);
        
        findMinMax(data);
        return dataset;  
    }  
  
    private JFreeChart createChart(final CategoryDataset dataset) 
    {
    	////設定字體
    	
    	//設定主題
		StandardChartTheme mChartTheme = new StandardChartTheme("CN");
		//設定標題字體
		mChartTheme.setExtraLargeFont(new Font("微軟正黑體", Font.PLAIN, 18));
		//設定row字體
		mChartTheme.setLargeFont(new Font("微軟正黑體", Font.PLAIN, 18));
		//設定column字體
		mChartTheme.setRegularFont(new Font("微軟正黑體", Font.PLAIN, 18));
		//設定chart類別的主題
		ChartFactory.setChartTheme(mChartTheme);

    	//建立圖表：標題、X軸標題、Y軸標題、資料集、Legend、Tooltips、URLS
    	JFreeChart jfreechart = ChartFactory.createLineChart("Google趨勢表","日期","搜尋數",
    					dataset,PlotOrientation.VERTICAL, true, false, false); 
    	// 圖表參數
    	CategoryPlot plot = (CategoryPlot)jfreechart.getPlot();
    	// 背景與透明度
    	plot.setBackgroundAlpha(1f);
    	// 前景與透明度
    	plot.setForegroundAlpha(1f);
    	// 其他參數 
    	LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();
    	renderer.setBaseShapesVisible(true);	//數據的每個點可見
    	renderer.setBaseLinesVisible(true); 	//數據之間的連線可見
    	renderer.setUseSeriesOffset(true);		//數據偏移量(?)
    	renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
    	renderer.setBaseItemLabelsVisible(true);
    	//取得縱座標並修改範圍
    	ValueAxis range = plot.getRangeAxis();
    	range.setRange(MIN,MAX);
    	
    	return jfreechart;
    
         
    } 
    
    
    /*
    public static void main(String args[]) { 
    		new jfree(); 
    }*/
}



/*jfree a=new jfree(); 

// 步驟1. 建立Dataset  
CategoryDataset dataset = a.setDataset();  
// 步驟2. 建立BarChart  
JFreeChart chart = a.createChart(dataset);  
  
// 步驟3. 將所建立的 BarChart 放在 ChartPanel 上並設定大小.  

ChartPanel chartPanel = new ChartPanel(chart);  
chartPanel.setPreferredSize(new Dimension(1000, 600)); 
System.out.println(chartPanel);
JFrame fk=new JFrame();
fk.getContentPane().add(chartPanel);  
fk.setLayout(null);
fk.pack();
fk.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
fk.setVisible(true);
fk.setBounds(100, 100, 1200, 800);
 */


//String[] series = {"100", "200", "300","400"};  //row分類，放不同的搜尋結果
//String[] category = {"z", "x", "c", "v", "b"};		//colum分類，放日期  

//double[][] data = {{8.0, 5.5, 10.2,20}, {4.2, 7.8, 30,15}, {3.5, 5.3, 6.8,26}  
//,{9.2, 14.5, 10.7,8}, {6.6, 8.3, 7.9,13}};  //放搜尋的數量
//DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
//for(int i=0; i<category.length; i++)  
//    for(int j=0; j<series.length; j++)  
//        dataset.addValue(data[i][j], series[j], category[i]); 