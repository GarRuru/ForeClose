package cw;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.event.ChangeEvent;

public class UX {

	private JFrame frmParser;
	private JPanel panel0;
	private JPanel panel1;
	private JPanel panel2;
	private JTextField saveLocationField;
	private JFileChooser FolderPicker;
	private JTextField googlesearchTextField;
	private JTextField searchTextField;
	private JTable Data;
	//一般搜尋表格
	private String[] tableCol = {"拍賣案號","房屋地址","拍賣法院","拍賣日期"};
	private Object[][] tableData = {{"0001", "基隆市北寧路199號","北院", "108/5/12"}};
	private JTable historyTable;
	private DefaultTableModel hTmodel;
	//Google趨勢表格
	private String[] tableGTrendCol = {"拍賣地址","搜尋熱度"};
	private Object[][] tableGData = {{"北寧路2號", 73849}};
	private JTable historyGTable;
	private DefaultTableModel hGTmodel;

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
		initialize();
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
		
		//Panel0 : Search Craw. House
		panel0 = new JPanel();
		panel0.setBounds(35, 76, 726, 485);
		frmParser.getContentPane().add(panel0);
		panel0.setLayout(null);
		panel0.setVisible(true);
		
		JComboBox crawPicker = new JComboBox();
		crawPicker.setBounds(206, 77, 172, 27);
		panel0.add(crawPicker);

		
		JLabel p0TitleLabel = new JLabel("法拍屋追蹤");
		p0TitleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
		p0TitleLabel.setBounds(23, 20, 172, 53);
		panel0.add(p0TitleLabel);
		
		JRadioButton crnoRadioBtn = new JRadioButton("依字號");
		crnoRadioBtn.setBounds(106, 77, 78, 23);
		panel0.add(crnoRadioBtn);
		
		JRadioButton addrRadioBtn = new JRadioButton("依地址");
		addrRadioBtn.setBounds(23, 77, 78, 23);
		addrRadioBtn.setSelected(true);
		panel0.add(addrRadioBtn);

		ButtonGroup group = new ButtonGroup();
		group.add(addrRadioBtn);
		group.add(crnoRadioBtn);
		
		searchTextField = new JTextField();
		searchTextField.setBounds(23, 112, 571, 26);
		panel0.add(searchTextField);
		searchTextField.setColumns(10);
		
		JButton button = new JButton("加入追蹤清單");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//地址查詢
				if(addrRadioBtn.isSelected())
				{
					ArrayList<String> result;
					result=Crawler.searchCrno(searchTextField.getText(),String.valueOf(crawPicker.getSelectedItem()));
					if(!result.get(0).contains("Not"))
					{
						System.out.println("找到惹");
						for(String s: result)
							System.out.print("["+s+"]");
						hTmodel.addRow(new Object[] {result.get(0),result.get(1),result.get(2),result.get(3)});
					}
					else
					{
						System.out.println("找不到");
						JOptionPane.showMessageDialog(frmParser,"找不到地址","查詢結果",JOptionPane.WARNING_MESSAGE);
					}
						
				}
				//字號查詢
				else
				{
					try	//檢查是不是輸入數字，如果不是要跳錯誤視窗
					{
						ArrayList<String> result;
						result = Crawler.searchCrno(Integer.parseInt(searchTextField.getText()),String.valueOf(crawPicker.getSelectedItem()));
						if(!result.get(0).contains("Not"))
						{
							System.out.println("字號" + searchTextField.getText() + "有找到");
							for(String s: result)
								System.out.print("["+s+"]");
							hTmodel.addRow(new Object[] {result.get(0),result.get(1),result.get(2),result.get(3)});

						}
						else
						{
							System.out.println("字號" + searchTextField.getText() + "找不到");						
							JOptionPane.showMessageDialog(frmParser,"案號"+Integer.parseInt(searchTextField.getText())+ "找不到","查詢結果",JOptionPane.WARNING_MESSAGE);

						}
					}
					catch(NumberFormatException nfe)
					{
						System.out.println(nfe);
						JOptionPane.showMessageDialog(frmParser,"請輸入有效的數字","錯誤",JOptionPane.ERROR_MESSAGE);
					}
					catch(NullPointerException npe)
					{
						//不明錯誤，無正確回傳值
						System.out.println(npe);
						JOptionPane.showMessageDialog(frmParser,"發生不明錯誤，請稍後再試!","錯誤",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		button.setBounds(606, 112, 117, 29);
		panel0.add(button);
		
		//表格部分
		hTmodel = new DefaultTableModel(tableData,tableCol);
		historyTable = new JTable(hTmodel);
        historyTable.setBounds(23, 187, 684, 266);
        historyTable.getTableHeader().setFont(new Font("Lucida Grande", Font.PLAIN, 15));	//Header Style
        historyTable.setFont(new Font("Lucida Grande", Font.PLAIN, 16));		
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBounds(23, 187, 684, 266);
        panel0.add(scrollPane);

        TableColumn column = null;
        for (int i = 0; i < 4; i++) 
        {
            column = historyTable.getColumnModel().getColumn(i);
            if (i == 0) 
                column.setPreferredWidth(40); //sport column is bigger
            else if(i == 1)
                column.setPreferredWidth(250);
            else
                column.setPreferredWidth(80);
        }    
		
		//下拉式選單的選項
        crawPicker.addItem("臺灣台北地方法院");
        crawPicker.addItem("臺灣新北地方法院");
        crawPicker.addItem("臺灣士林地方法院");
        crawPicker.addItem("臺灣桃園地方法院");
        crawPicker.addItem("臺灣新竹地方法院");
        crawPicker.addItem("臺灣苗栗地方法院");
        crawPicker.addItem("臺灣臺中地方法院");
        crawPicker.addItem("臺灣南投地方法院");
        crawPicker.addItem("臺灣彰化地方法院");
        crawPicker.addItem("臺灣雲林地方法院");
        crawPicker.addItem("臺灣嘉義地方法院");
        crawPicker.addItem("臺灣臺南地方法院");
        crawPicker.addItem("臺灣橋頭地方法院");
        crawPicker.addItem("臺灣高雄地方法院");
        crawPicker.addItem("臺灣屏東地方法院");
        crawPicker.addItem("臺灣臺東地方法院");
        crawPicker.addItem("臺灣花蓮地方法院");
        crawPicker.addItem("臺灣宜蘭地方法院");
        crawPicker.addItem("臺灣基隆地方法院");
        crawPicker.addItem("臺灣澎湖地方法院");
        crawPicker.addItem("福建金門地方法院");
        crawPicker.addItem("福建連江地方法院");
		//Panel0-end

	
		
		
		//PANEL1: Excel Export
		panel1 = new JPanel();
		panel1.setBounds(35, 76, 726, 485);
		frmParser.getContentPane().add(panel1);
		panel1.setLayout(null);
		
		JLabel p1TitleLabel = new JLabel("法院報表匯出");
		p1TitleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
		p1TitleLabel.setBounds(23, 20, 172, 53);
		panel1.add(p1TitleLabel);
		
		
		ArrayList<JCheckBox> checkBoxList = new ArrayList<JCheckBox>();
		
		JCheckBox court0 = new JCheckBox("台北地方法院");
		court0.setBounds(33, 76, 128, 23);
		panel1.add(court0);
		checkBoxList.add(court0);	
		JCheckBox court1 = new JCheckBox("新北地方法院");
		court1.setBounds(33, 111, 128, 23);
		panel1.add(court1);
		checkBoxList.add(court1);
		JCheckBox court2 = new JCheckBox("士林地方法院");
		court2.setBounds(33, 146, 128, 23);
		panel1.add(court2);
		checkBoxList.add(court2);
		JCheckBox court3 = new JCheckBox("桃園地方法院");
		court3.setBounds(33, 181, 128, 23);
		panel1.add(court3);
		checkBoxList.add(court3);
		JCheckBox court4 = new JCheckBox("新竹地方法院");
		court4.setBounds(33, 215, 128, 23);
		panel1.add(court4);
		checkBoxList.add(court4);
		JCheckBox court5 = new JCheckBox("苗栗地方法院");
		court5.setBounds(33, 250, 128, 23);
		panel1.add(court5);
		checkBoxList.add(court5);
		JCheckBox court6 = new JCheckBox("台中地方法院");
		court6.setBounds(33, 285, 128, 23);
		panel1.add(court6);
		checkBoxList.add(court6);
		JCheckBox court7 = new JCheckBox("南投地方法院");
		court7.setBounds(33, 322, 128, 23);
		panel1.add(court7);
		checkBoxList.add(court7);
		JCheckBox court8 = new JCheckBox("彰化地方法院");
		court8.setBounds(282, 76, 128, 23);
		panel1.add(court8);
		checkBoxList.add(court8);
		JCheckBox court9 = new JCheckBox("雲林地方法院");
		court9.setBounds(282, 111, 128, 23);
		panel1.add(court9);
		checkBoxList.add(court9);
		JCheckBox court10 = new JCheckBox("嘉義地方法院");
		court10.setBounds(282, 146, 128, 23);
		panel1.add(court10);
		checkBoxList.add(court10);
		JCheckBox court11 = new JCheckBox("台南地方法院");
		court11.setBounds(282, 180, 128, 23);
		panel1.add(court11);
		checkBoxList.add(court11);
		JCheckBox court12 = new JCheckBox("橋頭地方法院");
		court12.setBounds(282, 215, 128, 23);
		panel1.add(court12);
		checkBoxList.add(court12);
		JCheckBox court13 = new JCheckBox("高雄地方法院");
		court13.setBounds(282, 250, 128, 23);
		panel1.add(court13);
		checkBoxList.add(court13);
		JCheckBox court14 = new JCheckBox("屏東地方法院");
		court14.setBounds(282, 285, 128, 23);
		panel1.add(court14);
		checkBoxList.add(court14);
		JCheckBox court15 = new JCheckBox("台東地方法院");
		court15.setBounds(525, 76, 128, 23);
		panel1.add(court15);
		checkBoxList.add(court15);
		JCheckBox court16 = new JCheckBox("花蓮地方法院");
		court16.setBounds(525, 111, 128, 23);
		panel1.add(court16);
		checkBoxList.add(court16);
		JCheckBox court17 = new JCheckBox("宜蘭地方法院");
		court17.setBounds(525, 146, 128, 23);
		panel1.add(court17);
		checkBoxList.add(court17);
		JCheckBox court18 = new JCheckBox("基隆地方法院");
		court18.setBounds(525, 180, 128, 23);
		panel1.add(court18);
		checkBoxList.add(court18);
		JCheckBox court19 = new JCheckBox("澎湖地方法院");
		court19.setBounds(525, 215, 128, 23);
		panel1.add(court19);
		checkBoxList.add(court19);
		JCheckBox court20 = new JCheckBox("金門地方法院");
		court20.setBounds(525, 250, 128, 23);
		panel1.add(court20);
		checkBoxList.add(court20);
		JCheckBox court21 = new JCheckBox("連江地方法院");
		court21.setBounds(525, 287, 128, 23);
		panel1.add(court21);
		checkBoxList.add(court21);
				
		
		JLabel saveLocationLabel = new JLabel("保存位置");
		saveLocationLabel.setBounds(23, 396, 61, 16);
		panel1.add(saveLocationLabel);
		
		saveLocationField = new JTextField();
		saveLocationField.setEditable(false);
		saveLocationField.setBounds(96, 391, 479, 26);
		panel1.add(saveLocationField);
		saveLocationField.setColumns(10);
		File file=new File(".");
		saveLocationField.setText(file.getAbsolutePath());
		
		JButton locationPickerBtn = new JButton("瀏覽...");
		locationPickerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FolderPicker = new JFileChooser();
				FolderPicker.setCurrentDirectory(new java.io.File("."));
				FolderPicker.setDialogTitle("選擇輸出檔案路徑");
				FolderPicker.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				FolderPicker.setAcceptAllFileFilterUsed(false);	//禁止選擇所有項目的分類器
				if(FolderPicker.showOpenDialog(frmParser) == JFileChooser.APPROVE_OPTION)
				{
					System.out.println("ready output");
					saveLocationField.setText(FolderPicker.getSelectedFile().toString());
				}
				else System.out.println("取消操作");
				
	
			}
		});
		locationPickerBtn.setBounds(587, 391, 82, 29);
		panel1.add(locationPickerBtn);
		
		JButton exportBtn = new JButton("匯出指定報表");
		exportBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int tempcount = 0,truecount = 0,result,errcount=0;
				for(JCheckBox cb: checkBoxList)
				{
					if(cb.isSelected())
					{
						truecount++;
						result = Crawler.downloadDistrictCourtData(districtCourtList[tempcount],saveLocationField.getText());
						if(result == -1)
							errcount++;
					}
					tempcount++;
				}
				
				if(errcount == 0)
					JOptionPane.showMessageDialog(frmParser,truecount + "份報表已保存到"+FolderPicker.getSelectedFile(),"匯出報表成功",JOptionPane.INFORMATION_MESSAGE);
				else
					JOptionPane.showMessageDialog(frmParser,"成功匯出"+truecount+"份報表，"+errcount+"份報表匯出失敗。","結果",JOptionPane.WARNING_MESSAGE);
			}
		});
		exportBtn.setBounds(211, 438, 117, 29);
		panel1.add(exportBtn);
		
		JButton pickallBtn = new JButton("全選");
		pickallBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(JCheckBox cb :checkBoxList)
				{
					cb.setSelected(true);
				}
			}
		});
		pickallBtn.setBounds(194, 35, 96, 29);
		panel1.add(pickallBtn);
		
		JButton clearallBtn = new JButton("清除全部");
		clearallBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(JCheckBox cb :checkBoxList)
				{
					cb.setSelected(false);
				}

			}
		});
		clearallBtn.setBounds(295, 35, 96, 29);
		panel1.add(clearallBtn);
		
		JButton openFolderBtn = new JButton("開啟輸出資料夾");
		openFolderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String os = System.getProperty("os.name").toLowerCase();
					if(os.indexOf("win")>=0)	//OS: Windows
						Runtime.getRuntime().exec("Explorer.exe " + saveLocationField.getText()) ;
					//macos or Linux
					else Runtime.getRuntime().exec("open " + saveLocationField.getText());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(frmParser,"發生不明錯誤，請稍後再試!!","錯誤",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		openFolderBtn.setBounds(340, 438, 117, 29);
		panel1.add(openFolderBtn);
		panel1.setVisible(false);	//先不要顯示
		//Panel1 -end
		
		
		
		
		  
		//Panel2: Google Trend
		panel2 = new JPanel();
		panel2.setBounds(35, 76, 726, 485);
		frmParser.getContentPane().add(panel2);
		panel2.setLayout(null);
		panel2.setVisible(false);
		
		JLabel p2TitleLabel = new JLabel("Google 趨勢分析");
		p2TitleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
		p2TitleLabel.setBounds(23, 20, 229, 53);
		panel2.add(p2TitleLabel);

		googlesearchTextField = new JTextField();
		googlesearchTextField.setBounds(129, 112, 465, 26);
		panel2.add(googlesearchTextField);
		googlesearchTextField.setColumns(10);
		
		JLabel titleLabel = new JLabel("輸入地址");
		titleLabel.setBounds(42, 117, 61, 16);
		panel2.add(titleLabel);

		
		JButton gobutton = new JButton("加入追蹤清單");
		gobutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String result[];
				if(googlesearchTextField.getText() == "")
					JOptionPane.showMessageDialog(frmParser,"搜尋目標不可以留空!","錯誤",JOptionPane.WARNING_MESSAGE);
				else
				{
					try {
						result = Crawler.searchGoogleDataResults(googlesearchTextField.getText());
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						result = new String[2];
					}
					System.out.println("[" + result[0] + "]:" + result[1]);
					hGTmodel.addRow(new Object[] {result[0],result[1]});

				}
			}
		});
		gobutton.setBounds(606, 112, 117, 29);
		panel2.add(gobutton);
		
		hGTmodel = new DefaultTableModel(tableGData,tableGTrendCol);
		historyGTable = new JTable(hGTmodel);
		historyGTable.setBounds(23, 187,684, 266);
		historyGTable.getTableHeader().setFont(new Font("Lucido Grande",Font.PLAIN,15));
		historyGTable.setFont(new Font("Lucido Grande",Font.PLAIN,16));
		JScrollPane scrollGPane = new JScrollPane(historyGTable);
		scrollGPane.setBounds(23, 187, 684, 266);
		panel2.add(scrollGPane);
		
		
		//關於我們
		JButton aboutBtn = new JButton("關於");
		aboutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame aboutUS = new JFrame("關於");
				aboutUS.setBounds(150,150,400,200);
				aboutUS.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				aboutUS.getContentPane().setLayout(null);
				JLabel intro = new JLabel("法拍屋爬蟲系統 1.1");
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
		aboutBtn.setBounds(728, 6, 66, 29);
		frmParser.getContentPane().add(aboutBtn);

	}
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
