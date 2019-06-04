package cw;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//Panel 0: 法拍屋追蹤
public class CRAWUX extends JPanel {
	//UI 元件
	private JTextField searchTextField;

	
	//一般搜尋表格
	private String[] tableCol = {"","房屋地址","拍賣案號","拍賣法院","公告狀態","詳細資料"};	//第一個欄位是CheckBox
	private Object[][] tableData;// = {{Boolean.FALSE,"0001", "基隆市北寧路199號","北院", "108/5/12"}};
	private JTable historyTable;
	private DefaultTableModel hTmodel;
	private ArrayList<ArrayList<CrawData>> ACD = new ArrayList<ArrayList<CrawData>>();		//升級資料型態，因應詳細資料需求
	//private ArrayList<ArrayList<CrawData>> totalCrawData = new ArrayList<ArrayList<CrawData>>();

	
	public CRAWUX() 
	{
		super();
		setBounds(35, 76, 726, 485);
		setLayout(null);
		setVisible(true);

		JComboBox<String> crawPicker = new JComboBox<String>();
		crawPicker.setBounds(206, 77, 172, 27);
		add(crawPicker);

		JLabel p0TitleLabel = new JLabel("法拍屋追蹤");
		p0TitleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
		p0TitleLabel.setBounds(23, 20, 172, 53);
		add(p0TitleLabel);

		JRadioButton crnoRadioBtn = new JRadioButton("依字號");
		crnoRadioBtn.setBounds(106, 77, 78, 23);
		add(crnoRadioBtn);

		JRadioButton addrRadioBtn = new JRadioButton("依地址");
		addrRadioBtn.setBounds(23, 77, 78, 23);
		addrRadioBtn.setSelected(true);
		add(addrRadioBtn);

		ButtonGroup group = new ButtonGroup();
		group.add(addrRadioBtn);
		group.add(crnoRadioBtn);

		searchTextField = new JTextField();
		searchTextField.setBounds(23, 112, 571, 26);
		add(searchTextField);
		searchTextField.setColumns(10);

		JButton button = new JButton("加入追蹤清單");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 地址查詢
				if (addrRadioBtn.isSelected()) {
					ArrayList<String> result;
					ArrayList<ArrayList<String>> totalData;
					try {	//防呆：選得是地址，但輸入的是字號
						int crmno = Integer.parseInt(searchTextField.getText());
						JOptionPane.showMessageDialog(null, "請輸入有效地址", "錯誤", JOptionPane.ERROR_MESSAGE);
						searchTextField.setText("");
						return;
					} catch (NumberFormatException e1) {
						System.out.println("請輸入正確的地址");
					}
					totalData = Crawler.searchCrno(searchTextField.getText(),
							String.valueOf(crawPicker.getSelectedItem()));
					result = totalData.get(0);
					if (!result.get(0).contains("Not")) {
						System.out.println("找到惹");
						for (String s : result)
							System.out.print("[" + s + "]"); 
						//把搜尋結果的第一筆加到表格裡面
						hTmodel.addRow(
								new Object[] { Boolean.FALSE, result.get(1), result.get(0), result.get(2), "已公告" });
						
						setToTotalCrawData(totalData, crawPicker.getSelectedItem().toString(), true,searchTextField.getText());
						searchTextField.setText("");	//清空搜尋框
					} else {
						// System.out.println("找不到");
						// JOptionPane.showMessageDialog(frmParser,"找不到地址","查詢結果",JOptionPane.WARNING_MESSAGE);
						hTmodel.addRow(new Object[] { Boolean.FALSE, searchTextField.getText().toString(), "",
								crawPicker.getSelectedItem(), "未公告" });
						setToTotalCrawData(totalData, crawPicker.getSelectedItem().toString(), true,searchTextField.getText());
						searchTextField.setText("");
					}

				}
				// 字號查詢
				else {
					try // 檢查是不是輸入數字，如果不是要跳錯誤視窗
					{
						ArrayList<String> result;
						ArrayList<ArrayList<String>> totalData;
						totalData = Crawler.searchCrno(Integer.parseInt(searchTextField.getText()),
								String.valueOf(crawPicker.getSelectedItem()));
						result = totalData.get(0);
						if (!result.get(0).contains("Not")) {
							System.out.println("字號" + searchTextField.getText() + "有找到");
							for (String s : result)
								System.out.print("[" + s + "]");
							hTmodel.addRow(
									new Object[] { Boolean.FALSE, result.get(1), result.get(0), result.get(2), "已找到" });

							setToTotalCrawData(totalData, crawPicker.getSelectedItem().toString(), false,searchTextField.getText());
							searchTextField.setText("");
						} else {
							// System.out.println("字號" + searchTextField.getText() + "找不到");
							// JOptionPane.showMessageDialog(frmParser,"案號"+Integer.parseInt(searchTextField.getText())+
							// "找不到","查詢結果",JOptionPane.WARNING_MESSAGE);
							hTmodel.addRow(new Object[] { Boolean.FALSE, "", searchTextField.getText().toString(),
									crawPicker.getSelectedItem(), "未公告" });
							setToTotalCrawData(totalData, crawPicker.getSelectedItem().toString(), false,searchTextField.getText());
							searchTextField.setText("");

						}
					} catch (NumberFormatException nfe) {
						System.out.println(nfe);
						JOptionPane.showMessageDialog(null, "請輸入有效的數字", "錯誤", JOptionPane.ERROR_MESSAGE);
					} catch (NullPointerException npe) {
						// 不明錯誤，無正確回傳值
						System.out.println(npe);
						JOptionPane.showMessageDialog(null, "發生不明錯誤，請稍後再試!", "錯誤", JOptionPane.ERROR_MESSAGE);
					}
				}
				if (tableData == null) {
					tableData = new Object[1][6];
					tableData[0][0] = Boolean.FALSE;
					tableData[0][1] = "";
					tableData[0][2] = "";
					tableData[0][3] = "";
					tableData[0][4] = "";
					tableData[0][5] = "";
				}
			}
		
		});

		
		button.setBounds(606, 112, 117, 29);
		add(button);

		// 表格部分
		hTmodel = new DefaultTableModel(tableData, tableCol) {
			private boolean ImInLoop = false;

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if (tableData == null)
					return String.class;
				return tableData[0][columnIndex].getClass();
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				if (columnIndex == 0 || columnIndex == 5)
					return true;
				return false;
			}
		};
		historyTable = new JTable(hTmodel);
		historyTable.setBounds(23, 187, 684, 236);
		historyTable.getTableHeader().setFont(new Font("Lucida Grande", Font.PLAIN, 15)); // Header Style
		historyTable.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		
		// 詳細資料button設定
		TableCellRenderer buttinRe = new TableCellRenderer() {
			private DetailButton button = new DetailButton("詳細資料");

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				return button;
			}
		};
		//Detail按鈕
		MyDefaultCellEditor editor = new MyDefaultCellEditor(ACD);
		historyTable.getColumnModel().getColumn(5).setCellEditor(editor);
		historyTable.getColumnModel().getColumn(5).setCellRenderer(buttinRe);

		// 不能選取表格
		historyTable.setRowSelectionAllowed(false);
		historyTable.setRowHeight(30);
		///////////
		JScrollPane scrollPane = new JScrollPane(historyTable);
		scrollPane.setBounds(23, 187, 702, 236);
		add(scrollPane);

		//表格寬度設定
		TableColumn column = null;
		for (int i = 0; i < 6; i++) {
			column = historyTable.getColumnModel().getColumn(i);
			if (i == 0)
				column.setPreferredWidth(15);
			else if (i == 1)
				column.setPreferredWidth(245);
			else if (i == 2)
				column.setPreferredWidth(160);
			else if (i == 3)
				column.setPreferredWidth(100);
			else if (i == 4)
				column.setPreferredWidth(60);
			else
				column.setPreferredWidth(60);
		}

		// 下拉式選單的選項
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

		JButton deleteSelItemBtn = new JButton("刪除已選的項目");
		deleteSelItemBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: config未更動
				/*
				 * System.out.println("顯示圖表：選到第 "+historyTable.getSelectedRow() +"個row");
				 * if(historyTable.getSelectedRow() != -1) {
				 * ACD.remove(historyTable.getSelectedRow());
				 * hTmodel.removeRow(historyTable.getSelectedRow());
				 * 
				 * }
				 */
				for (int i = 0; i < historyTable.getRowCount(); i++) {
					if (historyTable.getValueAt(i, 0) == Boolean.TRUE) {
						ACD.remove(i);// 全部測好再用
						hTmodel.removeRow(i);
						i--;
					}
					System.out.println(ACD.size());
				}
				WriteConfig();

			}
		});
		deleteSelItemBtn.setBounds(450, 450, 150, 30);
		add(deleteSelItemBtn);

		JButton allRefreshButton = new JButton("全部更新");
		allRefreshButton.setBounds(150, 450, 150, 30);
		add(allRefreshButton);
		allRefreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				int DBsize = ACD.size();
				//int tempcount = 0;
				ArrayList<ArrayList<CrawData>> ACDCOPY = new ArrayList<ArrayList<CrawData>>(ACD);
				for(ArrayList<CrawData> alcd : ACDCOPY)
				{
					try {
						int NO = Integer.parseInt(alcd.get(0).getKeyword());
						ArrayList<ArrayList<String>> tempResult;
						tempResult = Crawler.searchCrno(NO, alcd.get(0).getFULLCourt());
						if(!tempResult.get(0).get(0).contains("Not"))
						{
							hTmodel.addRow(new Object[] { Boolean.FALSE, tempResult.get(0).get(1), tempResult.get(0).get(0), tempResult.get(0).get(2).substring(2), "已找到" });
							setToTotalCrawData(tempResult, alcd.get(0).getFULLCourt(), false, alcd.get(0).getKeyword());
						}
						else
						{
							hTmodel.addRow(new Object[] { Boolean.FALSE, tempResult.get(0).get(1), tempResult.get(0).get(0), tempResult.get(0).get(2).substring(2), "未公告" });
							setToTotalCrawData(tempResult, alcd.get(0).getFULLCourt(), false, alcd.get(0).getKeyword());
						}
					}catch(NumberFormatException nfe) {
						//無法解析數字，就是地址查詢
						ArrayList<ArrayList<String>> tempResult;
						tempResult = Crawler.searchCrno(alcd.get(0).getKeyword(), alcd.get(0).getFULLCourt());
						System.out.println(tempResult);
						if(!tempResult.get(0).get(0).contains("Not"))
						{
							hTmodel.addRow(new Object[] { Boolean.FALSE, tempResult.get(0).get(1), tempResult.get(0).get(0), tempResult.get(0).get(2).substring(2), "已找到" });
							setToTotalCrawData(tempResult, alcd.get(0).getFULLCourt(), true, alcd.get(0).getKeyword());
						}
						else
						{
							hTmodel.addRow(new Object[] { Boolean.FALSE, tempResult.get(0).get(1), tempResult.get(0).get(0), tempResult.get(0).get(2).substring(2), "未公告" });
							setToTotalCrawData(tempResult, alcd.get(0).getFULLCourt(), true, alcd.get(0).getKeyword());
						}
						
					}
				}
				
				for(int i=0;i<DBsize;i++)
				{
					ACD.remove(0);
					hTmodel.removeRow(0);
				}
				WriteConfig();
			}
			
		});

		LoadConfig();	//一切就緒再讀config
        tableView();	//準備表格呈現
	}
	
	public void setToTotalCrawData(ArrayList<ArrayList<String>> totalData, String court,Boolean ifAddress,String keyword) {
		//傳入參數：搜尋全部結果、法院名稱、地址(true)/字號(false)查詢
		//把當筆的搜尋記錄加入ACD
		ArrayList<CrawData> tmp = new ArrayList<CrawData>();
		for(ArrayList<String> result:totalData) {
			if(ifAddress) {
				if(!result.get(0).contains("Not"))
				{
					CrawData cwt = new CrawData(result.get(0),result.get(1),result.get(2),true,result.get(3),result.get(4),result.get(5),result.get(6));
					tmp.add(cwt);
				}
				else
				{
					CrawData cwt = new CrawData("",searchTextField.getText().toString(),court,false,"","","","");
					tmp.add(cwt);
					break;
				}
			}
			else {
				if(!result.get(0).contains("Not"))
				{
					CrawData cwt = new CrawData(result.get(0),result.get(1),result.get(2),true,result.get(3),result.get(4),result.get(5),result.get(6));
					tmp.add(cwt);
				}
				else
				{
					CrawData cwt = new CrawData(searchTextField.getText().toString(),"",court,false,"","","","");
					tmp.add(cwt);
					break;
				}
			}
		}
		tmp.get(0).setKeyword(keyword);		//加上關鍵字
		ACD.add(tmp);
		WriteConfig();						//寫檔
	}

	//從檔案讀取
	private void LoadConfig()
	{
		String jsonBuffer = "";
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("./CrawHistory.json"),"UTF-8"));
			String tempString = null;
			while((tempString = reader.readLine()) != null)
			{
				jsonBuffer += tempString;
			}
			reader.close();
		}catch (java.io.FileNotFoundException ffe) {
			ffe.printStackTrace();
			System.out.println("找不到法拍屋資料庫，程式初始化...");
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
	
	//字串解析、生成對應物件
	private void JSONParse(String inputStream)
	{
		
		JSONObject J = new JSONObject(inputStream);
		JSONArray t = J.getJSONArray("CRAW");
		for(int i=0;i<t.length();i++)	//每一個地址
		{
			ArrayList<CrawData> acd = new ArrayList<CrawData>();
			System.out.println("Load: " + t.get(i));
			JSONObject temp = (JSONObject) t.get(i);
			CrawData cwd = new CrawData(temp.getString("CrawNo"),temp.getString("Address"),temp.getString("Court"),
										temp.getBoolean("isFindable"),temp.getString("SellDate"),temp.getString("Price"),
										temp.getString("Handover"),temp.getString("website"));
			cwd.setKeyword(temp.getString("SearchKeyword"));		//當初搜尋使用的關鍵字（供刷新搜尋用）
			acd.add(cwd);
			JSONArray another = temp.getJSONArray("Another");
			for(int j=0;j<another.length();j++)
			{
				JSONObject temp2 = (JSONObject) another.get(j);
				CrawData ano = new CrawData(temp2.getString("CrawNo"),temp2.getString("Address"),temp2.getString("Court"),
						temp2.getBoolean("isFindable"),temp2.getString("SellDate"),temp2.getString("Price"),
						temp2.getString("Handover"),temp2.getString("website"));
				acd.add(ano);
			}
			ACD.add(acd);
		}
		System.out.println("法拍屋歷史讀取完畢");
	}
	
	private void tableView()
	{
		//準備表格顯示
		if(ACD.size()!=0)
		{
			tableData = new Object[ACD.size()][6];
			for(int i=0;i< ACD.size();i++)
			{
				tableData[i][0] = Boolean.FALSE;
				tableData[i][1] = ACD.get(i).get(0).getAddress();
				tableData[i][2] = ACD.get(i).get(0).getNo();
				tableData[i][3] = ACD.get(i).get(0).getCourt();
				tableData[i][4] = ACD.get(i).get(0).getFindable();
				tableData[i][5] = "Button";
				hTmodel.addRow(tableData[i]);

			}
		}
		else
			tableData =  null;
	}
	
	//負責將更新的內容寫入Config檔案
	//呼叫時機：完成刷新流程之後
	private void WriteConfig() //throws IOException
	{
		try
		{
		FileWriter ff = new FileWriter("./CrawHistory2.json");	
		JSONObject json = new JSONObject();				//JSON元件
		try
		{
			JSONArray jsonMember = new JSONArray();		//CRAW標籤
			for(ArrayList<CrawData> alcd : ACD)
			{
				JSONObject name = new JSONObject();
				name.put("SearchKeyword", alcd.get(0).getKeyword());
				name.put("Address", alcd.get(0).getAddress());
				name.put("CrawNo", alcd.get(0).getNo());
				name.put("Court", alcd.get(0).getCourt());
				name.put("isFindable", alcd.get(0).getFindableBOOL());
				name.put("SellDate", alcd.get(0).getsellDate());
				name.put("Price", Integer.toString(alcd.get(0).getPriceINT()));
				name.put("Handover", alcd.get(0).getHandover());
				name.put("website", alcd.get(0).getWebsite());
				JSONArray another = new JSONArray();	//another標籤
				for(int i=1;i<alcd.size();i++)
				{
					JSONObject nme = new JSONObject();
					nme.put("Address", alcd.get(i).getAddress());
					nme.put("CrawNo", alcd.get(i).getNo());
					nme.put("Court", alcd.get(i).getCourt());
					nme.put("isFindable", alcd.get(i).getFindableBOOL());
					nme.put("SellDate", alcd.get(i).getsellDate());
					nme.put("Price", Integer.toString(alcd.get(i).getPriceINT()));
					nme.put("Handover", alcd.get(i).getHandover());
					nme.put("website", alcd.get(i).getWebsite());
					another.put(nme);
				}
				name.put("Another",another);
				jsonMember.put(name);
				
			}
			
			json.put("CRAW", jsonMember);
			String writeString = json.toString();
			ff.write(writeString);
			ff.close();
			System.out.println("成功寫入CrawHistory.json");
	
			
		}catch(JSONException jse){
			jse.printStackTrace();
		}}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
	}

}


//allRefreshButton.addActionListener(new ActionListener() {
//
//@Override
//public void actionPerformed(ActionEvent arg0) {
//	// TODO Auto-generated method stub
//	ACD.clear();
//
//	for (CrawData i : ACD) {
//		if (i.getAddress().equals("")) {
//			/*
//			 * ArrayList<String> result; ArrayList<ArrayList<String>> totalData; totalData =
//			 * Crawler.searchCrno(searchTextField.getText(),String.valueOf(crawPicker.
//			 * getSelectedItem())); result = totalData.get(0);
//			 * if(!result.get(0).contains("Not")) { hTmodel.addRow(new Object[]
//			 * {Boolean.FALSE,result.get(0),result.get(1),result.get(2),"已公告"}); CrawData
//			 * cwt = new
//			 * CrawData(result.get(0),result.get(1),result.get(2),true,result.get(3),result.
//			 * get(4),result.get(5),result.get(6)); ACD.add(cwt);
//			 * setToTotalCrawData(totalData, crawPicker.getSelectedItem().toString(), true);
//			 * } else { hTmodel.addRow(new Object[]
//			 * {Boolean.FALSE,searchTextField.getText().toString(),"",crawPicker.
//			 * getSelectedItem(),"未公告"}); CrawData cwt = new
//			 * CrawData("",searchTextField.getText().toString(),crawPicker.getSelectedItem()
//			 * .toString(),false,"","","",""); ACD.add(cwt); setToTotalCrawData(totalData,
//			 * crawPicker.getSelectedItem().toString(), true); }
//			 */
//			// Crawler.searchCrno(Integer.parseInt(i.getNo()), i.getCourt());
//
//		} else if (i.getNo().equals("")) {
//			/*
//			 * ArrayList<String> result; ArrayList<ArrayList<String>> totalData; totalData =
//			 * Crawler.searchCrno(Integer.parseInt(searchTextField.getText()),String.valueOf
//			 * (crawPicker.getSelectedItem())); result = totalData.get(0);
//			 * if(!result.get(0).contains("Not")) { hTmodel.addRow(new Object[]
//			 * {Boolean.FALSE,result.get(0),result.get(1),result.get(2),"已找到"}); CrawData
//			 * cwt = new
//			 * CrawData(result.get(0),result.get(1),result.get(2),true,result.get(3),result.
//			 * get(4),result.get(5),result.get(6)); ACD.add(cwt);
//			 * setToTotalCrawData(totalData, crawPicker.getSelectedItem().toString(),
//			 * false); } else { hTmodel.addRow(new Object[]
//			 * {Boolean.FALSE,"",searchTextField.getText().toString(),crawPicker.
//			 * getSelectedItem(),"未公告"}); CrawData cwt = new
//			 * CrawData(searchTextField.getText().toString(),"",crawPicker.getSelectedItem()
//			 * .toString(),false,"","","",""); ACD.add(cwt); setToTotalCrawData(totalData,
//			 * crawPicker.getSelectedItem().toString(), false); }
//			 */
//			// Crawler.searchCrno(i.getAddress(), i.getCourt());
//		}
//	}
//}
// 
//});

