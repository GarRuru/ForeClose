package cw;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.json.JSONArray;
import org.json.JSONObject;

public class UX {
	public String ConfigLoadString;
	
	//設定類參數
	private boolean autoRefresh;
	private int updateFreq;	
	
	//UI元件
	private JFrame frmParser;
	private CRAWUX panel0;
	private ExcelReportUX panel1;
	private GTrendTrackUX panel2;
	
	
	private JTable Data;
	//Google趨勢表格
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UX window = new UX();
					window.frmParser.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UX() {
		ConfigLoadString = LoadData();
		JSONParse(ConfigLoadString);
		initialize();
		

	}

	private String LoadData()
	{
		String jsonBuffer = "";
		//File file = new File("./config.json");	
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("./config.json"),"UTF-8"));
			String tempString = null;
			int line = 1;
			while((tempString = reader.readLine()) != null)
			{
				//System.out.println("Line #" + line + ": " + tempString);
				jsonBuffer += tempString;
				line++;
			}
			reader.close();
			//jsonBuffer.substring(1);	//去除JSON檔案裡因編輯器塞入的header 字元(BOM)
		}
		catch (java.io.FileNotFoundException ffe)
		{
			ffe.printStackTrace();
			System.out.println("找不到config.json，程式初始化...");
			autoRefresh = false;
			updateFreq = 0;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{

			if(reader != null)
			{
				try 
				{
					reader.close();
				}
				catch(IOException e1)
				{
					e1.printStackTrace();
				}			
			}
		}
		return jsonBuffer;
	}
	
	private void JSONParse(String input)
	{
		JSONObject J;
		try
		{
			J = new JSONObject(input);
			//設定參數
			autoRefresh = (boolean)J.get("AutoRefresh");
			updateFreq = (int)J.getInt("UpdateFreq");
		
			//System.out.println("Load File From Config Complete.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
	    final String[] districtCourtList = {"臺灣台北地方法院", "臺灣新北地方法院", "臺灣士林地方法院", "臺灣桃園地方法院", "臺灣新竹地方法院", "臺灣苗栗地方法院",
	    		"臺灣臺中地方法院", "臺灣南投地方法院", "臺灣彰化地方法院", "臺灣雲林地方法院", "臺灣嘉義地方法院", "臺灣臺南地方法院", "臺灣橋頭地方法院", "臺灣高雄地方法院", 
	    		"臺灣屏東地方法院", "臺灣臺東地方法院", "臺灣花蓮地方法院", "臺灣宜蘭地方法院", "臺灣基隆地方法院", "臺灣澎湖地方法院", "福建金門地方法院", "福建連江地方法院"};
	
		frmParser = new JFrame();
		frmParser.setTitle("法拍屋爬蟲系統");
		frmParser.setResizable(false);
		frmParser.setBounds(100, 100, 800, 600);
		frmParser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmParser.getContentPane().setLayout(null);
		frmParser.setVisible(true);
		
		
		//分頁標籤
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JTabbedPane tb = (JTabbedPane) e.getSource();
				System.out.println("切換到Tab" + tb.getSelectedIndex() + " " + tb.getTitleAt(tb.getSelectedIndex()));
				//Avoid Panel is NULL.
				if(panel0 != null)
				{
					panel0.setVisible(false);
					panel1.setVisible(false);
					panel2.setVisible(false);
					switch(tb.getSelectedIndex())
					{
					case 0:
						panel0.setVisible(true);
						break;
					case 1:
						panel1.setVisible(true);
						break;
					case 2:
						panel2.setVisible(true);
						break;
					}

				}
			}
		});
		tabbedPane.setBounds(35, 30, 726, 21);
		frmParser.getContentPane().add(tabbedPane);
		tabbedPane.addTab("法拍屋查詢"	,panel0);
		tabbedPane.addTab("法院報表匯出"	,panel1);
		tabbedPane.addTab("趨勢查詢"		,panel2);
		
		Container cp = frmParser.getContentPane();
		cp.add(tabbedPane,BorderLayout.CENTER);
		
		//加入Panel到視窗(frame)元件
		panel0 = new CRAWUX();
		frmParser.getContentPane().add(panel0);
		panel1 = new ExcelReportUX();
		frmParser.getContentPane().add(panel1);
		panel2 = new GTrendTrackUX();
		frmParser.getContentPane().add(panel2);
		
		//關於我們
		JButton aboutBtn = new JButton("關於");
		aboutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame aboutUS = new JFrame("關於");
				aboutUS.setBounds(150,150,400,200);
				aboutUS.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				aboutUS.getContentPane().setLayout(null);
				JLabel intro = new JLabel("法拍屋爬蟲系統 2.1");
				intro.setBounds(10,0,400,100);
				intro.setFont(new Font("微軟正黑體",Font.PLAIN,13));
				JLabel intro2 = new JLabel("作者：鄒承皓、陳育祥、陳育軒\n");
				intro2.setBounds(10,30,400,100);
				intro2.setFont(new Font("微軟正黑體",Font.PLAIN,13));

				aboutUS.getContentPane().add(intro);
				aboutUS.getContentPane().add(intro2);
				JButton closeBtn = new JButton("Close");
				closeBtn.setBounds(150,120,77,39);
				aboutUS.getContentPane().add(closeBtn);
				closeBtn.addActionListener(new ActionListener()
				{
				public void actionPerformed(ActionEvent e) {
					aboutUS.dispose();
				}
				});
				aboutUS.setVisible(true);
			}
		});
		aboutBtn.setBounds(686, 6, 66, 29);
		frmParser.getContentPane().add(aboutBtn);
		
		JButton helpBtn = new JButton("?");
		helpBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch(tabbedPane.getSelectedIndex())
				{
				case 0:
					JOptionPane.showMessageDialog(frmParser,"法拍屋爬蟲" ,"提示",JOptionPane.PLAIN_MESSAGE);
					break;
				case 1:
					JOptionPane.showMessageDialog(frmParser,"報表匯出" ,"提示",JOptionPane.PLAIN_MESSAGE);
					break;
				case 2:
					JOptionPane.showMessageDialog(frmParser,"Google趨勢搜尋" ,"提示",JOptionPane.PLAIN_MESSAGE);
					break;

				}
			}
		});
		helpBtn.setBounds(748, 6, 46, 29);
		frmParser.getContentPane().add(helpBtn);

	}
	
	//將資料存入TotalCrawData中 
	//ifAddress=true是用地址查詢    false則是用字號
}




/*
class WindowHandler extends WindowAdapter {
	  JFrame f;
	  public WindowHandler(JFrame f) {this.f=f;}
	  public void windowClosing(WindowEvent e) {
	    int result=JOptionPane.showConfirmDialog(f,
	               "確定要結束程式嗎?",
	               "確認訊息",
	               JOptionPane.YES_NO_OPTION,
	               JOptionPane.WARNING_MESSAGE);
	    if (result==JOptionPane.YES_OPTION) {System.exit(0);}
	    }   
	  }
	  */
