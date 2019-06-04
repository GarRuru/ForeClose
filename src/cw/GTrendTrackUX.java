package cw;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//Panel 2:Google趨勢追蹤
public class GTrendTrackUX extends JPanel{
	public Timer Gtimer = new Timer();
	public AutoCrawler AutoCW = new AutoCrawler();

	
	private String[] tableGTrendCol = {"","拍賣地址","上次搜尋熱度", "上次搜尋日期","第一次搜尋日期"};
	private Object[][] tableGData;// = {{"北寧路2號", 73849,"2018/05/22"},{"新豐街",44700,"2019/04/13"},{"祥豐街",324242,"2018/10/13"}};
	private JTable historyGTable;
	private DefaultTableModel hGTmodel;
	private ArrayList<GTrendData> GTD;
	private JTextField googlesearchTextField;
		
	public GTrendTrackUX()
	{
		super();
		LoadConfig();	//從檔案讀取
		setBounds(35, 76, 726, 485);
		setLayout(null);
		setVisible(false);
		
		JLabel p2TitleLabel = new JLabel("Google 趨勢分析");
		p2TitleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
		p2TitleLabel.setBounds(23, 20, 229, 53);
		add(p2TitleLabel);

		googlesearchTextField = new JTextField();
		googlesearchTextField.setBounds(129, 112, 465, 26);
		add(googlesearchTextField);
		googlesearchTextField.setColumns(10);
		
		JLabel titleLabel = new JLabel("輸入地址");
		titleLabel.setBounds(42, 117, 61, 16);
		add(titleLabel);

		//TODO: 介面上要有刷新的button嗎？
		JButton gobutton = new JButton("加入追蹤清單");
		gobutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				Date dd = new Date();
				String result[];
				String curDate = (dd.getYear()+1900) + "/" + (dd.getMonth()+1) + "/" + dd.getDate();
				if(googlesearchTextField.getText() == "")
					JOptionPane.showMessageDialog(null,"搜尋目標不可以留空!","錯誤",JOptionPane.WARNING_MESSAGE);
				else
				{
					try {
						result = Crawler.searchGoogleDataResults(googlesearchTextField.getText());
					} catch (InterruptedException e1) {
						e1.printStackTrace();
						result = new String[2];
					}
					System.out.println("[" + result[0] + "]:" + result[1]);
					hGTmodel.addRow(new Object[] {Boolean.FALSE,result[0],result[1],curDate,curDate});
					//TODO: 怎麼產生GTrendData的格式並且插入GTD
					GTrendData gted = new GTrendData();
					gted.setAddress(googlesearchTextField.getText());
					gted.addSearchCount(Integer.parseInt(result[1]));
					gted.addDate(curDate);
					GTD.add(gted);
					
				}
				if(tableGData==null) {
					tableGData = new Object[1][5];
					tableGData[0][0] = Boolean.FALSE; 
					tableGData[0][1] = ""; 
					tableGData[0][2] = ""; 
					tableGData[0][3] = ""; 
					tableGData[0][4] = ""; 
				}
			}
		});
		gobutton.setBounds(606, 112, 117, 29);
		add(gobutton);
		
		hGTmodel = new DefaultTableModel(tableGData,tableGTrendCol){
			 private boolean ImInLoop = false;
			 @Override
			 public Class<?> getColumnClass(int columnIndex) {
				 if(tableGData==null)
					 return String.class;
				 return tableGData[0][columnIndex].getClass();
			 }
			 @Override
			 public boolean isCellEditable(int rowIndex, int columnIndex) {
				 if(columnIndex==0)
					 return true;
				 return false;
			 }
		 };
		historyGTable = new JTable(hGTmodel);
		historyGTable.setBounds(23, 187,684, 236);
		historyGTable.getTableHeader().setFont(new Font("Lucido Grande",Font.PLAIN,15));
		historyGTable.setFont(new Font("Lucido Grande",Font.PLAIN,16));
		historyGTable.setRowHeight(30);			//設定欄位高度
		JScrollPane scrollGPane = new JScrollPane(historyGTable);
		scrollGPane.setBounds(23, 187, 684, 236);
		add(scrollGPane);
		
		JButton TimerBtn = new JButton("<html>定時<br>重新整理</html>");
		TimerBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		GTrendTimerSetUX gtsu= new GTrendTimerSetUX();
        		//TODO: 如何接起來？
        	}
        });
		TimerBtn.setBounds(63, 430, 100, 58);
        add(TimerBtn);
		
		
		
		
		JButton RefreshNowBtn = new JButton("<html>立即刷新<br>所選項目</html>");
		RefreshNowBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Date dd = new Date();
        		String curDate = (dd.getYear()+1900) + "/" + (dd.getMonth()+1) + "/" + dd.getDate();

        		for(int i=0;i<historyGTable.getRowCount();i++)
        		{
        			if(historyGTable.getValueAt(i, 0)==Boolean.TRUE)
        			{
        				int tempResult = Crawler.UpdateGoogleDataResults(GTD.get(i).getAddress());
        				GTD.get(i).addSearchCount(tempResult);
        				GTD.get(i).addDate(curDate);
        				hGTmodel.setValueAt(tempResult, i , 2);
        				hGTmodel.setValueAt(curDate, i, 3);
        			}
        		}
        		WriteConfig();
        	}
        });
		RefreshNowBtn.setBounds(183, 430, 100, 58);
        add(RefreshNowBtn);
		
		
		
		
		
        JButton deleteGSelItemBtn = new JButton("刪除已選的項目");
        //deleteGSelItemBtn.setEnabled(false);
        deleteGSelItemBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		/*
        		//TODO:config還沒動
				System.out.println("刪除清單項目：選到第 "+historyGTable.getSelectedRow() +"個row");

        		if(historyGTable.getSelectedRow() != -1)
				{
        			GTD.remove(historyGTable.getSelectedRow());
					hGTmodel.removeRow(historyGTable.getSelectedRow());
				}
				*/
        		for(int i=0;i<historyGTable.getRowCount();i++)
        		{
        			if(historyGTable.getValueAt(i, 0)==Boolean.TRUE)
        			{
        				GTD.remove(i);
    					hGTmodel.removeRow(i);
            			i--;
        			}
        		}
        		WriteConfig();
        	}
        });
        deleteGSelItemBtn.setBounds(495, 450, 154, 29);
        add(deleteGSelItemBtn);

        

        JButton showChartBtn = new JButton("顯示趨勢圖表");
        showChartBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//沒有選到任何一列的時候：會回傳-1
				System.out.println("顯示圖表：選到第 "+historyGTable.getSelectedRow() +"個row");
				if(historyGTable.getSelectedRow() != -1)
				{
					jChart C = new jChart(GTD.get(historyGTable.getSelectedRow()));
				}
			}
		});
        showChartBtn.setBounds(333, 450, 117, 29);
        add(showChartBtn);
        
        //表格寬度設定
		TableColumn column = null;
		for (int i = 0; i < 5; i++) {
			column = historyGTable.getColumnModel().getColumn(i);
			if (i == 0)
				column.setPreferredWidth(15);
			else if (i == 1)
				column.setPreferredWidth(265);
			else if (i == 2)
				column.setPreferredWidth(100);
			else if (i == 3)
				column.setPreferredWidth(100);
			else
				column.setPreferredWidth(100);
		}
        
        tableView();
        
        /*
		//timer 
		//Gtimer.schedule(AutoCW, 1000, 60000);	//60000 ms
		Gtimer.schedule(new TimerTask() {
			@Override
			public void run()
			{
				System.out.println("我又爬蟲了喔～");
        		Date dd = new Date();
        		String curDate = (dd.getYear()+1900) + "/" + (dd.getMonth()+1) + "/" + dd.getDate();
        		
        		for(int i=0;i<historyGTable.getRowCount();i++)
        		{
        			int tempResult = Crawler.UpdateGoogleDataResults(GTD.get(i).getAddress());
        			GTD.get(i).addSearchCount(tempResult);
        			GTD.get(i).addDate(curDate);
        			hGTmodel.setValueAt(tempResult, i , 2);
        			hGTmodel.setValueAt(curDate, i, 3);
        			
        		}
        		WriteConfig();

			}
		},0,60000);
        */
	}
	
	
	private void LoadConfig()
	{
		String jsonBuffer = "";
		//File file = new File("./config.json");	
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("./GTrendHistory.json"),"UTF-8"));
			String tempString = null;
			while((tempString = reader.readLine()) != null)
			{
				jsonBuffer += tempString;
			}
			reader.close();
		}catch (java.io.FileNotFoundException ffe){
			ffe.printStackTrace();
			System.out.println("找不到趨勢資料庫，程式初始化...");
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			if(reader != null)
			{
				try {
					reader.close();
				}catch(IOException e1){
					e1.printStackTrace();
				}			
			}
		}
		JSONParse(jsonBuffer);
	}
	
	
	private void JSONParse(String inputStream)
	{
		GTD = new ArrayList<GTrendData>();
		JSONObject J = new JSONObject(inputStream);
		JSONArray t = J.getJSONArray("GTrend");
		for(int i=0;i<t.length();i++)
		{
			JSONObject temp = (JSONObject) t.get(i);
			//System.out.println(temp);
			GTrendData gtd = new GTrendData();
			gtd.setAddress(temp.getString("Address"));
			for(int j=0;j<temp.getJSONArray("Date").length();j++)
			{
				System.out.println("Load: " + temp.getJSONArray("Date").get(j).toString() + " " +temp.getJSONArray("Count").get(j));
				gtd.addDate(temp.getJSONArray("Date").get(j).toString());
				gtd.addSearchCount((int) temp.getJSONArray("Count").get(j));
			}
			GTD.add(gtd);
		}
		System.out.println("趨勢資料庫讀取完畢");
	}

	private void tableView()
	{		
		//準備表格顯示
		if(GTD.size()!=0)
		{
			tableGData = new Object[GTD.size()][5];
			for(int i=0;i<GTD.size();i++)
			{
				tableGData[i][0] = Boolean.FALSE;
				tableGData[i][1] = GTD.get(i).getAddress();
				tableGData[i][2] = GTD.get(i).getLatestSearchCount();
				tableGData[i][3] = GTD.get(i).getLatestDate();
				tableGData[i][4] = GTD.get(i).getFullDate().get(0);
				hGTmodel.addRow(tableGData[i]);
			}

		}
		else 
			tableGData =  null;


	}

	//負責將更新的內容寫入Config檔案
	//呼叫時機：完成刷新流程之後
	private void WriteConfig() //throws IOException
	{
		try
		{
			FileWriter ff = new FileWriter("./GTrendHistory.json");	
			JSONObject json = new JSONObject();				//JSON元件
			try
			{
				JSONArray jsonMember = new JSONArray();		//GTrend標籤
				for(GTrendData g: GTD)						//擷取每一個資料，構成JSONObject
				{
					JSONObject name = new JSONObject();
					name.put("Address", g.getAddress());
					JSONArray datelist = new JSONArray();
					for(String ss: g.getFullDate())
					{
						datelist.put(ss);
					}
					JSONArray searchcountlist = new JSONArray();
					for(Integer ss: g.getFullSearchCount())
					{
						searchcountlist.put(ss);
					}
					name.put("Date", datelist);
					name.put("Count", searchcountlist);
					jsonMember.put(name);
				}
				json.put("GTrend", jsonMember);
				String writeString = json.toString();
				ff.write(writeString);
				ff.close();
				System.out.println("成功寫入GTrendHistory.json");
			}catch(JSONException jse){
				jse.printStackTrace();
		}}catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}

}
