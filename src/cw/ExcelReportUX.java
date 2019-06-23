package cw;

import excel.ExcelFilter;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExcelReportUX extends JPanel{
	private JTextField saveLocationField;
	private JFileChooser FolderPicker;
	private String currentPath = ".";
    public final String[] districtCourtList = {"臺灣台北地方法院", "臺灣新北地方法院", "臺灣士林地方法院", "臺灣桃園地方法院", "臺灣新竹地方法院", "臺灣苗栗地方法院",
    		"臺灣臺中地方法院", "臺灣南投地方法院", "臺灣彰化地方法院", "臺灣雲林地方法院", "臺灣嘉義地方法院", "臺灣臺南地方法院", "臺灣橋頭地方法院", "臺灣高雄地方法院", 
    		"臺灣屏東地方法院", "臺灣臺東地方法院", "臺灣花蓮地方法院", "臺灣宜蘭地方法院", "臺灣基隆地方法院", "臺灣澎湖地方法院", "福建金門地方法院", "福建連江地方法院"};

	public ExcelReportUX() 
	{
		super();
		setBounds(35, 76, 726, 485);
		setLayout(null);
		setVisible(false);
		setBorder(BorderFactory.createTitledBorder("Border"));
		setBorder(BorderFactory.createLineBorder(Color.black));

		
		JLabel p1TitleLabel = new JLabel("法院報表匯出");
		p1TitleLabel.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		p1TitleLabel.setBounds(23, 20, 172, 53);
		add(p1TitleLabel);
		
		
		ArrayList<JCheckBox> checkBoxList = new ArrayList<JCheckBox>();
		
		JCheckBox court0 = new JCheckBox("台北地方法院");
		court0.setBounds(33, 76, 128, 23);
		add(court0);
		checkBoxList.add(court0);	
		JCheckBox court1 = new JCheckBox("新北地方法院");
		court1.setBounds(33, 111, 128, 23);
		add(court1);
		checkBoxList.add(court1);
		JCheckBox court2 = new JCheckBox("士林地方法院");
		court2.setBounds(33, 146, 128, 23);
		add(court2);
		checkBoxList.add(court2);
		JCheckBox court3 = new JCheckBox("桃園地方法院");
		court3.setBounds(33, 181, 128, 23);
		add(court3);
		checkBoxList.add(court3);
		JCheckBox court4 = new JCheckBox("新竹地方法院");
		court4.setBounds(33, 215, 128, 23);
		add(court4);
		checkBoxList.add(court4);
		JCheckBox court5 = new JCheckBox("苗栗地方法院");
		court5.setBounds(33, 250, 128, 23);
		add(court5);
		checkBoxList.add(court5);
		JCheckBox court6 = new JCheckBox("台中地方法院");
		court6.setBounds(33, 285, 128, 23);
		add(court6);
		checkBoxList.add(court6);
		JCheckBox court7 = new JCheckBox("南投地方法院");
		court7.setBounds(33, 322, 128, 23);
		add(court7);
		checkBoxList.add(court7);
		JCheckBox court8 = new JCheckBox("彰化地方法院");
		court8.setBounds(282, 76, 128, 23);
		add(court8);
		checkBoxList.add(court8);
		JCheckBox court9 = new JCheckBox("雲林地方法院");
		court9.setBounds(282, 111, 128, 23);
		add(court9);
		checkBoxList.add(court9);
		JCheckBox court10 = new JCheckBox("嘉義地方法院");
		court10.setBounds(282, 146, 128, 23);
		add(court10);
		checkBoxList.add(court10);
		JCheckBox court11 = new JCheckBox("台南地方法院");
		court11.setBounds(282, 180, 128, 23);
		add(court11);
		checkBoxList.add(court11);
		JCheckBox court12 = new JCheckBox("橋頭地方法院");
		court12.setBounds(282, 215, 128, 23);
		add(court12);
		checkBoxList.add(court12);
		JCheckBox court13 = new JCheckBox("高雄地方法院");
		court13.setBounds(282, 250, 128, 23);
		add(court13);
		checkBoxList.add(court13);
		JCheckBox court14 = new JCheckBox("屏東地方法院");
		court14.setBounds(282, 285, 128, 23);
		add(court14);
		checkBoxList.add(court14);
		JCheckBox court15 = new JCheckBox("台東地方法院");
		court15.setBounds(525, 76, 128, 23);
		add(court15);
		checkBoxList.add(court15);
		JCheckBox court16 = new JCheckBox("花蓮地方法院");
		court16.setBounds(525, 111, 128, 23);
		add(court16);
		checkBoxList.add(court16);
		JCheckBox court17 = new JCheckBox("宜蘭地方法院");
		court17.setBounds(525, 146, 128, 23);
		add(court17);
		checkBoxList.add(court17);
		JCheckBox court18 = new JCheckBox("基隆地方法院");
		court18.setBounds(525, 180, 128, 23);
		add(court18);
		checkBoxList.add(court18);
		JCheckBox court19 = new JCheckBox("澎湖地方法院");
		court19.setBounds(525, 215, 128, 23);
		add(court19);
		checkBoxList.add(court19);
		JCheckBox court20 = new JCheckBox("金門地方法院");
		court20.setBounds(525, 250, 128, 23);
		add(court20);
		checkBoxList.add(court20);
		JCheckBox court21 = new JCheckBox("連江地方法院");
		court21.setBounds(525, 287, 128, 23);
		add(court21);
		checkBoxList.add(court21);
				
		
		for(JCheckBox jcb: checkBoxList)
		{
			jcb.setFont(new Font("微軟正黑體",Font.BOLD,16));
		}
		
		JLabel saveLocationLabel = new JLabel("保存位置");
		saveLocationLabel.setBounds(23, 396, 61, 16);
		add(saveLocationLabel);
		
		saveLocationField = new JTextField();
		saveLocationField.setEditable(false);
		saveLocationField.setBounds(96, 391, 479, 26);
		add(saveLocationField);
		saveLocationField.setColumns(10);
		if(currentPath.equals(""))
		{
			File file=new File("");
			saveLocationField.setText(file.getAbsolutePath());
		}
		else
			saveLocationField.setText(currentPath);
		JButton locationPickerBtn = new JButton("瀏覽...");
		locationPickerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FolderPicker = new JFileChooser();
				FolderPicker.setCurrentDirectory(new java.io.File(currentPath));
				FolderPicker.setDialogTitle("選擇輸出檔案路徑");
				FolderPicker.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				FolderPicker.setAcceptAllFileFilterUsed(false);	//禁止選擇所有項目的分類器
				if(FolderPicker.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				{
					System.out.println("ready output");
					saveLocationField.setText(FolderPicker.getSelectedFile().toString());
					currentPath = FolderPicker.getSelectedFile().toString();
					WriteConfig();
				}
				else System.out.println("取消操作");
				
	
			}
		});
		locationPickerBtn.setBounds(587, 391, 82, 29);
		add(locationPickerBtn);
		
		JButton exportBtn = new JButton("匯出指定報表");
		exportBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int tempcount = 0,truecount = 0,result,errcount=0;
				for(JCheckBox cb: checkBoxList)
				{
					if(cb.isSelected())
					{
						truecount++;
						System.out.print(saveLocationField.getText());
						result = Crawler.downloadDistrictCourtData(districtCourtList[tempcount],saveLocationField.getText());
						if(result == -1)
							errcount++;
						java.util.Date today = new java.util.Date();
						String fullDestPath = saveLocationField.getText() + "/" + 
											  districtCourtList[tempcount] + "-" 
						                        + String.format("%04d", today.getYear() + 1900)
						                        + String.format("%02d", today.getMonth() + 1)
						                        + String.format("%02d", today.getDate()) + ".xls";

										
						ExcelFilter eft = new ExcelFilter(fullDestPath);
						eft.filter();
					}
					tempcount++;
				}
				
				if(errcount == 0)
					JOptionPane.showMessageDialog(null,truecount + "份報表已保存到"+saveLocationField.getText(),"匯出報表成功",JOptionPane.INFORMATION_MESSAGE);
				else
					JOptionPane.showMessageDialog(null,"成功匯出"+truecount+"份報表，"+errcount+"份報表匯出失敗。","結果",JOptionPane.WARNING_MESSAGE);
			}
		});
		exportBtn.setBounds(211, 438, 117, 29);
		add(exportBtn);
		
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
		add(pickallBtn);
		
		JButton clearallBtn = new JButton("取消全選");
		clearallBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(JCheckBox cb :checkBoxList)
				{
					cb.setSelected(false);
				}

			}
		});
		clearallBtn.setBounds(295, 35, 96, 29);
		add(clearallBtn);
		
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
					JOptionPane.showMessageDialog(null,"發生不明錯誤，請稍後再試!!","錯誤",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		openFolderBtn.setBounds(370, 438, 140, 29);
		add(openFolderBtn);
		
		//介面先準備好再讀路徑
		LoadData();

	}
	
	private void LoadData()
	{
		String jsonBuffer = "";
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("./path.json"),"UTF-8"));
			String tempString = null;
			while((tempString = reader.readLine()) != null)
				jsonBuffer += tempString;
			reader.close();
			//jsonBuffer.substring(1);	//去除JSON檔案裡因編輯器塞入的header 字元(BOM)
		}catch (java.io.FileNotFoundException ffe){
			ffe.printStackTrace();
			File file=new File("");
			saveLocationField.setText(file.getAbsolutePath());
			System.out.println("重新定向路徑完成");
			return;
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			if(reader != null){
				try {
					reader.close();
				}catch(IOException e1){
					e1.printStackTrace();
				}}
		}
		JSONObject J;
		try{
			J = new JSONObject(jsonBuffer);
			currentPath = J.getString("LastSavedLocation");
			saveLocationField.setText(currentPath);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void WriteConfig() //throws IOException
	{
		try{
			Writer ff = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./path.json"),"UTF-8"));
			JSONObject json = new JSONObject();				//JSON元件
			try{
				json.put("LastSavedLocation", currentPath);		
				String writeString = json.toString();
				ff.write(writeString);
				ff.close();
				System.out.println("成功寫入path.json");	
			}catch(JSONException jse){
				jse.printStackTrace();
		}}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
	}


}
