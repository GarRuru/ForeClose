package cw;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.json.JSONObject;

public class UX {
	public String ConfigLoadString;

	//設定類參數: 定時爬蟲相關
	public static int currentDBVersion = 28;
	public static boolean autoRefresh;
	public static int updateHR;
	private FrameToFrame me;

	//UI元件
	private JFrame frmParser;
	private CRAWUX panel0;
	private ExcelReportUX panel1;
	private GTrendTrackUX panel2;

	private JTable Data;
	//Google趨勢表格

	public UX() {
		setUIFont(new javax.swing.plaf.FontUIResource("微軟正黑體",Font.PLAIN,13));
		ConfigLoadString = LoadData();
		JSONParse(ConfigLoadString);
		initialize();
	}

	private String LoadData()
	{
		String jsonBuffer = "";
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("./config.json"),"UTF-8"));
			String tempString = null;
			int line = 1;
			while((tempString = reader.readLine()) != null){
				//System.out.println("Line #" + line + ": " + tempString);
				jsonBuffer += tempString;
				line++;
			}
			reader.close();
			//jsonBuffer.substring(1);	//去除JSON檔案裡因編輯器塞入的header 字元(BOM)
		}catch (java.io.FileNotFoundException ffe){
			ffe.printStackTrace();
			System.out.println("找不到config.json，程式初始化...");
			autoRefresh = true;
			updateHR = 9;	//預設是9點
			return "";
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			if(reader != null){
				try {
					reader.close();
				}catch(IOException e1){
					e1.printStackTrace();
				}
			}
		}
		return jsonBuffer;
	}

	private void JSONParse(String input)
	{
		if(input.equals(""))
			return;
		JSONObject J;
		try{
			J = new JSONObject(input);
			//設定參數
			autoRefresh = (boolean)J.get("AutoRefresh");
			updateHR = J.getInt("UpdateHR");
			int FileVersion = Integer.parseInt(J.get("DBVersion").toString());
			if(updateHR>23 || updateHR<0)
				updateHR = 9;
			if(FileVersion != currentDBVersion)
				throw new org.json.JSONException("版本不一致呦!");

			//System.out.println("Load Fisle From Config Complete.");
		}catch(org.json.JSONException jsje) {
			int dialogBtn = JOptionPane.YES_NO_OPTION;
			dialogBtn = JOptionPane.showConfirmDialog(null, "資料庫需要升級以支援目前版本，要執行嗎？","資料庫版本不一致",dialogBtn);
			System.out.println("選擇："+dialogBtn);
			if(dialogBtn == JOptionPane.YES_OPTION)
			{
				System.out.println("升級資料庫...!!!");
				DBCheck db = new DBCheck();
				db.DBUpdate();
				JOptionPane.showMessageDialog(null, "資料庫升級完成!","完成",JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				JOptionPane.showMessageDialog(null, "<html>放棄升級，程式將結束。<br>建議先備份資料庫後再行升級!</html>","程式即將關閉...",JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

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
		//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

		//分頁標籤
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
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

		//計時器設定(Global Settings)
		JButton settingsBtn = new JButton("設定");
		settingsBtn.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
				TimerSetUX gtsu= new TimerSetUX();
				me=panel2;
        		gtsu.addListener(me);
        	}
        });

		settingsBtn.setBounds(620, 6, 66, 29);
		frmParser.getContentPane().add(settingsBtn);

		//關於我們
		JButton aboutBtn = new JButton("關於");
		aboutBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame aboutUS = new JFrame("關於");
				aboutUS.setBounds(150,150,400,200);
				aboutUS.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				aboutUS.getContentPane().setLayout(null);
				JLabel intro = new JLabel("法拍屋爬蟲系統 2.73");
				intro.setBounds(10,0,400,100);
				intro.setFont(new Font("微軟正黑體",Font.PLAIN,13));
				JLabel intro2 = new JLabel("作者：鄒承皓、陳育祥、陳育軒\n");
				intro2.setBounds(10,30,400,100);
				intro2.setFont(new Font("微軟正黑體",Font.PLAIN,13));
				JButton intro3 = new JButton("更新紀錄");
				intro3.setBounds(70,120,127,39);
				intro3.setFont(new Font("微軟正黑體",Font.PLAIN,13));

				aboutUS.getContentPane().add(intro);
				aboutUS.getContentPane().add(intro2);
				aboutUS.getContentPane().add(intro3);
				JButton closeBtn = new JButton("Close");
				closeBtn.setBounds(220,120,77,39);
				aboutUS.getContentPane().add(closeBtn);

				closeBtn.addActionListener(new ActionListener()
				{
				@Override
				public void actionPerformed(ActionEvent e) {
					aboutUS.dispose();
				}
				});
				aboutUS.setVisible(true);

				intro3.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("啟動瀏覽器：開啟更新紀錄");
						try {
		                    Desktop.getDesktop().browse(new URI("https://hackmd.io/@GarRuru/S1bEO-TJB"));
		                    System.out.println("成功開啟");
		                } catch (URISyntaxException | IOException ex) {
		                    JOptionPane.showMessageDialog(null, "Fail");
		                    System.out.println("Open url fail");
		                }
					}
				});
			}
		});

		aboutBtn.setBounds(686, 6, 66, 29);
		frmParser.getContentPane().add(aboutBtn);

		JButton helpBtn = new JButton("?");
		helpBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(tabbedPane.getSelectedIndex())
				{
				case 0:
					JOptionPane.showMessageDialog(frmParser,"<html>法拍屋爬蟲<br><br>"
							+ "輸入地址與選擇法院，可以查詢目前法拍資料庫上<br>是否存在相關紀錄</html>" ,"提示 - 法拍屋爬蟲",JOptionPane.PLAIN_MESSAGE);
					break;
				case 1:
					JOptionPane.showMessageDialog(frmParser,"<html>報表匯出<br><br>"
							+ "預設輸出檔名為「法院名稱+輸出日期」</html>" ,"提示 - 報表匯出",JOptionPane.PLAIN_MESSAGE);
					break;
				case 2:
					JOptionPane.showMessageDialog(frmParser,"<html>Google趨勢搜尋<br><br>"
							+ "由於Google改變了搜尋顯示的結果，導致需要較長的等待時間，大約3~5秒左右。<br>"
							+ "請注意「刷新」及「刪除」需要勾選核取方塊才有作用</html>" ,"提示 - Google趨勢搜尋",JOptionPane.PLAIN_MESSAGE);
					break;

				}
			}
		});
		helpBtn.setBounds(748, 6, 46, 29);
		frmParser.getContentPane().add(helpBtn);
	}

	public static void setUIFont(javax.swing.plaf.FontUIResource f)
	{
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while(keys.hasMoreElements())
		{
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if(value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UX window = new UX();
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					window.frmParser.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
