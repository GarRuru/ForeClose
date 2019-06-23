package cw;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
public class GTrendTrackUX extends JPanel implements FrameToFrame{
	public static String GnoteDeli = "";
	public Timer Gtimer = new Timer();
	public AutoCrawler AutoCW = new AutoCrawler();
	private FrameToFrame me;
	
	private String[] tableGTrendCol = {"","地址","搜尋熱度", "上次更新","初次搜尋", "持續日數"};
	private Object[][] tableGData;
	private JTable historyGTable;
	private DefaultTableModel hGTmodel;
	private ArrayList<GTrendData> GTD = new ArrayList<GTrendData>();
	private JTextField googlesearchTextField;
	
	
	public GTrendTrackUX()
	{
		super();
		me=this;
		setBounds(35, 76, 726, 485);
		setLayout(null);
		setVisible(false);
		
		JLabel p2TitleLabel = new JLabel("Google 趨勢分析");
		p2TitleLabel.setFont(new Font("微軟正黑體", Font.BOLD, 24));
		p2TitleLabel.setBounds(23, 20, 229, 53);
		add(p2TitleLabel);

		googlesearchTextField = new JTextField();
		googlesearchTextField.setBounds(129, 112, 465, 26);
		add(googlesearchTextField);
		googlesearchTextField.setColumns(10);
		
		JLabel titleLabel = new JLabel("輸入地址");
		titleLabel.setBounds(42, 117, 61, 16);
		add(titleLabel);

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
					hGTmodel.addRow(new Object[] {Boolean.FALSE,result[0],result[1],curDate,curDate,"1"});
					GTrendData gted = new GTrendData();
					gted.setAddress(googlesearchTextField.getText());
					gted.addSearchCount(Integer.parseInt(result[1]));
					gted.addDate(curDate);
					GTD.add(gted);
					WriteConfig();
				}
				if(tableGData==null) {
					tableGData = new Object[1][6];
					tableGData[0][0] = Boolean.FALSE; 
					tableGData[0][1] = ""; 
					tableGData[0][2] = ""; 
					tableGData[0][3] = ""; 
					tableGData[0][4] = "";
					tableGData[0][5] = "";
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
		historyGTable.getTableHeader().setFont(new Font("微軟正黑體",Font.BOLD,15));
		historyGTable.setFont(new Font("微軟正黑體",Font.PLAIN,16));
		historyGTable.setRowHeight(30);			//設定欄位高度
		JScrollPane scrollGPane = new JScrollPane(historyGTable);
		scrollGPane.setBounds(23, 187, 684, 236);
		add(scrollGPane);
				
		
		
		JButton RefreshNowBtn = new JButton("刷新");
		RefreshNowBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		LocalDate today = LocalDate.now();
        		Date dd = new Date();
        		String curDate = (dd.getYear()+1900) + "/" + (dd.getMonth()+1) + "/" + dd.getDate();

        		for(int i=0;i<historyGTable.getRowCount();i++)
        		{
        			if(historyGTable.getValueAt(i, 0)==Boolean.TRUE)
        			{
        				int tempResult = Crawler.UpdateGoogleDataResults(GTD.get(i).getAddress());
        				if(tempResult != -1 && !GTD.get(i).getLatestDate().equals(curDate))
        				{
        					String[] daySplit = GTD.get(i).getFullDate().get(0).split("/");
        					LocalDate oldDate = LocalDate.of(Integer.parseInt(daySplit[0]), Month.of(Integer.parseInt(daySplit[1]))	
        													,Integer.parseInt(daySplit[2]));
        					long daysDiff = ChronoUnit.DAYS.between(oldDate,today);
        					System.out.println("新的搜尋結果:" + tempResult);
	        				GTD.get(i).addSearchCount(tempResult);
	        				GTD.get(i).addDate(curDate);
	        				hGTmodel.setValueAt(tempResult, i , 2);
	        				hGTmodel.setValueAt(curDate, i, 3);
	        				hGTmodel.setValueAt(daysDiff+1, i, 5);
	        			}
        				hGTmodel.setValueAt(Boolean.FALSE, i, 0);	//完成後取消勾選
        			}
        		}
        		WriteConfig();
        	}
        });
		RefreshNowBtn.setBounds(400, 450, 84, 29);
        add(RefreshNowBtn);
		
		
		
		
		
        JButton deleteGSelItemBtn = new JButton("刪除");
        deleteGSelItemBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {

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
        deleteGSelItemBtn.setForeground(Color.RED);
        deleteGSelItemBtn.setBounds(495, 450, 84, 29);
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
        showChartBtn.setBounds(133, 450, 117, 29);
        add(showChartBtn);
        
        JButton noteBtn = new JButton("顯示註解");
        noteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//沒有選到任何一列的時候：會回傳-1
				System.out.println("顯示註解：選到第 "+historyGTable.getSelectedRow() +"個row");
				if(historyGTable.getSelectedRow() != -1)
				{
					NoteUX nux = new NoteUX(GTD.get(historyGTable.getSelectedRow()).getNote(),1);
					nux.Run(GTrendTrackUX.this,GTD, historyGTable.getSelectedRow());
				}
			}
		});
        noteBtn.setBounds(253, 450, 117, 29);
        add(noteBtn);

        
        
        
        //表格寬度設定
		TableColumn column = null;
		for (int i = 0; i < 6; i++) {
			column = historyGTable.getColumnModel().getColumn(i);
			if (i == 0)
				column.setPreferredWidth(15);
			else if (i == 1)
				column.setPreferredWidth(245);
			else if (i == 2)
				column.setPreferredWidth(80);
			else if (i == 3)
				column.setPreferredWidth(90);
			else if (i == 4)
				column.setPreferredWidth(90);
			else 
				column.setPreferredWidth(60);
		}
		
		LoadConfig();		//從檔案讀取
        tableView();		//顯示表格		
        forceRefresh();		//再刷資料

        
		//timer 		
//      if(UX.autoRefresh) {
	        Gtimer.schedule(new TimerTask() {
	        	int scheduleCrawTime = UX.updateHR;
				@Override
				public void run()
				{
	        		Date dd = new Date();
					System.out.println("[" + dd.getHours() + ":" + dd.getMinutes() + "]" + "[G]我又爬蟲了喔～");
	        		String curDate = (dd.getYear()+1900) + "/" + (dd.getMonth()+1) + "/" + dd.getDate();
	        		if(UX.autoRefresh)
	        		{
		        		if(dd.getHours()==scheduleCrawTime && dd.getMinutes()==0) {
		        			System.out.println("[G]爬起來爬起來～");
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
		        		else {
		        			System.out.println("[G]爬蟲時間還沒到～");
		        		}
	        		}
	        		//else System.out.println("沒事！！");
				}
			},0,60000);
		}
//	}
	
	
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
			return;
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
		if(inputStream.equals(""))
			return;
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
			gtd.setNote(temp.getString("Note"));
			GTD.add(gtd);
		}
		System.out.println("趨勢資料庫讀取完畢");
	}

	private void tableView()
	{		
		//準備表格顯示
		if(GTD.size()!=0)
		{
			LocalDate today = LocalDate.now();
			tableGData = new Object[GTD.size()][6];
			for(int i=0;i<GTD.size();i++)
			{
				String[] daySplit = GTD.get(i).getFullDate().get(0).split("/");
				
				LocalDate oldDate = LocalDate.of(Integer.parseInt(daySplit[0]),Month.of(Integer.parseInt(daySplit[1]))
												,Integer.parseInt(daySplit[2]));
				long daysDiff = ChronoUnit.DAYS.between(oldDate, today)+1;
				
				tableGData[i][0] = Boolean.FALSE;
				tableGData[i][1] = GTD.get(i).getAddress();
				tableGData[i][2] = GTD.get(i).getLatestSearchCount();
				tableGData[i][3] = GTD.get(i).getLatestDate();
				tableGData[i][4] = GTD.get(i).getFullDate().get(0);
				tableGData[i][5] = daysDiff;
				
				hGTmodel.addRow(tableGData[i]);
			}

		}
		else 
			tableGData =  null;


	}

	//負責將更新的內容寫入Config檔案
	//呼叫時機：完成刷新流程之後
	public void WriteConfig() //throws IOException
	{
		try
		{
			Writer ff = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./GTrendHistory.json"),"UTF-8"));	
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
					name.put("Note", g.getNote());
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
	
	//啟動強制刷新
	private void forceRefresh()
	{
		System.out.println("[G]程式啟動，自動更新階段...");
		LocalDate today = LocalDate.now();
		Date dd = new Date();
		String curDate = (dd.getYear()+1900) + "/" + (dd.getMonth()+1) + "/" + dd.getDate();
		for(int i=0;i<historyGTable.getRowCount();i++)
		{
			int tempResult = Crawler.UpdateGoogleDataResults(GTD.get(i).getAddress());
			if(tempResult != -1 && !GTD.get(i).getLatestDate().equals(curDate))
			{
				System.out.println("資料不是最新的（加載今日數據中）");
				String[] daySplit = GTD.get(i).getFullDate().get(0).split("/");
				LocalDate oldDate = LocalDate.of(Integer.parseInt(daySplit[0]), Month.of(Integer.parseInt(daySplit[1]))	
												,Integer.parseInt(daySplit[2]));
				long daysDiff = ChronoUnit.DAYS.between(oldDate,today)+1;
				GTD.get(i).addSearchCount(tempResult);
				GTD.get(i).addDate(curDate);
				hGTmodel.setValueAt(tempResult, i , 2);
				hGTmodel.setValueAt(curDate, i, 3);
				hGTmodel.setValueAt(daysDiff, i, 5);
			}


		}
		WriteConfig();
	}
	
	public void frameToFrameEvent(Boolean setautoopen, int setcrawtime) {	
		//TODO: 邏輯檢查！！
		/*

				if(!UX.autoRefresh && setautoopen) {
			        Gtimer.schedule(new TimerTask() {
			        	int scheduleCrawTime = setcrawtime;
						@Override
						public void run()
						{
							System.out.println("[G']我又爬蟲了喔～");
			        		Date dd = new Date();
			        		String curDate = (dd.getYear()+1900) + "/" + (dd.getMonth()+1) + "/" + dd.getDate();
			        		if(dd.getHours()==scheduleCrawTime && dd.getMinutes()==0) {
			        			System.out.println("[G']爬起來爬起來～");
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
			        		else {
			        			System.out.println("[G']爬蟲時間還沒到～");
			        		}
			
						}
					},0,60000);
				}
				else if(UX.autoRefresh && setautoopen && UX.updateHR!=setcrawtime)
				{
					Gtimer.cancel();
					Gtimer = new Timer();
					Gtimer.schedule(new TimerTask() {
			        	int scheduleCrawTime = setcrawtime;
						@Override
						public void run()
						{
							System.out.println("[G'']我又爬蟲了喔～");
			        		Date dd = new Date();
			        		String curDate = (dd.getYear()+1900) + "/" + (dd.getMonth()+1) + "/" + dd.getDate();
			        		if(dd.getHours()==scheduleCrawTime && dd.getMinutes()==0) {
			        			System.out.println("[G'']爬起來爬起來～");
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
			        		else {
			        			System.out.println("[G'']爬蟲時間還沒到～");
			        		}
			
						}
					},0,60000);
				}
				else {
					Gtimer.cancel();
					Gtimer = new Timer();
				}
		*/
	} 
}
