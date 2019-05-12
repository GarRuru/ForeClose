package cw;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JSeparator;
import java.awt.Color;
import javax.swing.JTextPane;
import java.text.DateFormat;
import java.util.Date;


public class UI {
	//全域UI元件
	private JFrame frmParser;
	private JTextField inputTextBox;
	private JComboBox<String> PlaceComboBox = new JComboBox();
	private JFileChooser FolderPicker;
	private JTextArea logPane;
	private static Date date;
	private static DateFormat mediumFormat;
	
	private int crmnoResult = -2;
	private int addressResult = -2;
	private int exportResult = -2;
	private int googleResult = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		mediumFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM); 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI window = new UI();
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
	public UI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmParser = new JFrame();
		frmParser.setTitle("法拍屋爬蟲系統");
		frmParser.setResizable(false);
		frmParser.setBounds(100, 100, 800, 600);
		frmParser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmParser.getContentPane().setLayout(null);
		
		inputTextBox = new JTextField();
		inputTextBox.setColumns(10);
		inputTextBox.setBounds(207, 83, 370, 37);
		frmParser.getContentPane().add(inputTextBox);
		
		JLabel label = new JLabel("搜尋目標");
		label.setBounds(85, 93, 61, 16);
		frmParser.getContentPane().add(label);
		
		JRadioButton addressRadioBtn = new JRadioButton("地址");
		addressRadioBtn.setSelected(true);
		addressRadioBtn.setBounds(208, 37, 141, 23);
		frmParser.getContentPane().add(addressRadioBtn);
		
		JRadioButton crnoRadioBtn = new JRadioButton("法拍字號");
		crnoRadioBtn.setBounds(343, 37, 141, 23);
		frmParser.getContentPane().add(crnoRadioBtn);
		
		//Radio Button Grouped
		ButtonGroup group = new ButtonGroup();
		group.add(addressRadioBtn);
		group.add(crnoRadioBtn);
		
		JButton searchBtn = new JButton("搜尋");
		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//地址查詢
				if(addressRadioBtn.isSelected())
				{
					
					addressResult = Crawler.searchCrno(inputTextBox.getText(),String.valueOf(PlaceComboBox.getSelectedItem()));
					if(addressResult == 1)
					{
						date = new Date();
						JOptionPane.showMessageDialog(frmParser,"地址："+inputTextBox.getText()+"已找到","查詢結果",JOptionPane.INFORMATION_MESSAGE);
						logPane.append("[" + mediumFormat.format(date) + "]  " + "已找到：" +inputTextBox.getText() + "\n");
					}
					else if(addressResult == 0)
					{
						date = new Date();
						JOptionPane.showMessageDialog(frmParser,"找不到此地址","查詢結果",JOptionPane.WARNING_MESSAGE);
						logPane.append("[" + mediumFormat.format(date) + "]  " + "找不到：" +inputTextBox.getText() + "\n");
					}
					else
					{
						date = new Date();
						JOptionPane.showMessageDialog(frmParser,"發生不明錯誤，請稍後再試!!","錯誤",JOptionPane.ERROR_MESSAGE);
						logPane.append("[" + mediumFormat.format(date) + "]  " + "發生錯誤，請重試\n");

					}
				}
				else	//字號查詢
				{
					try	//防爆：Parse不到數字
					{
						crmnoResult = Crawler.searchCrno(Integer.parseInt(inputTextBox.getText()),String.valueOf(PlaceComboBox.getSelectedItem()));
						if(crmnoResult == 1)
						{
							date = new Date();
							JOptionPane.showMessageDialog(frmParser,"案號："+inputTextBox.getText()+"已找到","查詢結果",JOptionPane.INFORMATION_MESSAGE);
							logPane.append("[" + mediumFormat.format(date) + "]  " + "案號：" +inputTextBox.getText() + "已找到\n");

						}
						else if(crmnoResult == 0)
						{
							date = new Date();
							JOptionPane.showMessageDialog(frmParser,"找不到此案號","查詢結果",JOptionPane.WARNING_MESSAGE);
							logPane.append("[" + mediumFormat.format(date) + "]  " + "案號：" +inputTextBox.getText() + "找不到\n");
						}
						else
						{
							date = new Date();
							JOptionPane.showMessageDialog(frmParser,"發生不明錯誤，請稍後再試!!","錯誤",JOptionPane.ERROR_MESSAGE);
							logPane.append("[" + mediumFormat.format(date) + "]  " + "發生錯誤，請重試\n");
							
						}
					
						logPane.setCaretPosition(logPane.getText().length());

					}
					
					catch (NumberFormatException nfe)
					{
						JOptionPane.showMessageDialog(frmParser,"請輸入有效的數字","錯誤",JOptionPane.ERROR_MESSAGE);
						
					}
					
			}
			}
		});
		searchBtn.setBounds(207, 205, 117, 29);
		frmParser.getContentPane().add(searchBtn);
		
		PlaceComboBox.setBounds(208, 138, 161, 37);
		frmParser.getContentPane().add(PlaceComboBox);
		
		JLabel lblNewLabel = new JLabel("執行拍賣法院");
		lblNewLabel.setBounds(85, 147, 88, 16);
		frmParser.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("查詢方式");
		lblNewLabel_1.setBounds(85, 41, 61, 16);
		frmParser.getContentPane().add(lblNewLabel_1);
		
		JButton searchAndExportBtn = new JButton("匯出指定法院之Excel報表");
		searchAndExportBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FolderPicker = new JFileChooser();
				FolderPicker.setCurrentDirectory(new java.io.File("."));
				FolderPicker.setDialogTitle("選擇輸出檔案路徑");
				FolderPicker.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				FolderPicker.setAcceptAllFileFilterUsed(false);	//禁止選擇所有項目的分類器
				if(FolderPicker.showOpenDialog(frmParser) == JFileChooser.APPROVE_OPTION)
				{
					System.out.println("get Folder:" + FolderPicker.getSelectedFile());
					exportResult = Crawler.downloadDistrictCourtData(String.valueOf(PlaceComboBox.getSelectedItem()),FolderPicker.getSelectedFile().toString());
					if(exportResult == 1)
					{
						date = new Date();
						JOptionPane.showMessageDialog(frmParser,"報表已保存到"+FolderPicker.getSelectedFile(),"匯出報表成功",JOptionPane.INFORMATION_MESSAGE);
						logPane.append("[" + mediumFormat.format(date) + "]  " + "成功匯出\"" +String.valueOf(PlaceComboBox.getSelectedItem()) + "\"的Excel報表。"
								+ "存放路徑:" + FolderPicker.getSelectedFile().toString() + "/" + String.valueOf(PlaceComboBox.getSelectedItem()) + "-"
								+ String.format("%04d", date.getYear() + 1900) + String.format("%02d", date.getMonth() + 1) + String.format("%02d", date.getDate())
								+ ".xls\n");
					}
					
					else 
					{
						date = new Date();
						JOptionPane.showMessageDialog(frmParser,"發生不明錯誤，請稍後再試!!","錯誤",JOptionPane.ERROR_MESSAGE);
						logPane.append("[" + mediumFormat.format(date) + "]  " + "無法匯出" +String.valueOf(PlaceComboBox.getSelectedItem()) + "\"的Excel報表。\n");						logPane.setCaretPosition(logPane.getText().length());
					}
					logPane.setCaretPosition(logPane.getText().length());
				}
				else System.out.println("取消操作");
				
			}
		});
		searchAndExportBtn.setBounds(349, 205, 194, 29);
		frmParser.getContentPane().add(searchAndExportBtn);
		
		JSeparator separator = new JSeparator();
		separator.setBackground(Color.BLACK);
		separator.setForeground(Color.BLACK);
		separator.setBounds(6, 302, 788, -22);
		frmParser.getContentPane().add(separator);
		
		JButton btnGoogle = new JButton("Google 趨勢統計");
		btnGoogle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				googleResult = Crawler.searchGoogleDataResults(inputTextBox.getText());
				logPane.append("[" + mediumFormat.format(date) + "]  \"" + inputTextBox.getText() + "\"的趨勢:" + googleResult + "筆\n");
				logPane.setCaretPosition(logPane.getText().length());
			}
		});
		btnGoogle.setBounds(571, 205, 162, 29);
		frmParser.getContentPane().add(btnGoogle);
		
		logPane = new JTextArea();
		logPane.setLineWrap(true);
		logPane.setEditable(false);
		logPane.setBounds(85, 274, 648, 265);
		frmParser.getContentPane().add(logPane);
		
		JButton aboutBtn = new JButton("關於");
		aboutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame aboutUS = new JFrame("關於");
				aboutUS.setBounds(150,150,400,200);
				aboutUS.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				aboutUS.getContentPane().setLayout(null);
				JLabel intro = new JLabel("法拍屋爬蟲系統 1.00");
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
		aboutBtn.setBounds(6, 549, 66, 29);
		frmParser.getContentPane().add(aboutBtn);
        PlaceComboBox.addItem("臺灣台北地方法院");
        PlaceComboBox.addItem("臺灣新北地方法院");
        PlaceComboBox.addItem("臺灣士林地方法院");
        PlaceComboBox.addItem("臺灣桃園地方法院");
        PlaceComboBox.addItem("臺灣新竹地方法院");
        PlaceComboBox.addItem("臺灣苗栗地方法院");
        PlaceComboBox.addItem("臺灣臺中地方法院");
        PlaceComboBox.addItem("臺灣南投地方法院");
        PlaceComboBox.addItem("臺灣彰化地方法院");
        PlaceComboBox.addItem("臺灣雲林地方法院");
        PlaceComboBox.addItem("臺灣嘉義地方法院");
        PlaceComboBox.addItem("臺灣臺南地方法院");
        PlaceComboBox.addItem("臺灣橋頭地方法院");
        PlaceComboBox.addItem("臺灣高雄地方法院");
        PlaceComboBox.addItem("臺灣屏東地方法院");
        PlaceComboBox.addItem("臺灣臺東地方法院");
        PlaceComboBox.addItem("臺灣花蓮地方法院");
        PlaceComboBox.addItem("臺灣宜蘭地方法院");
        PlaceComboBox.addItem("臺灣基隆地方法院");
        PlaceComboBox.addItem("臺灣澎湖地方法院");
        PlaceComboBox.addItem("福建金門地方法院");
        PlaceComboBox.addItem("福建連江地方法院");
		
		
	}
}
