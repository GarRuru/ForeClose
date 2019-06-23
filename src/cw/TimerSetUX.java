package cw;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.json.JSONException;
import org.json.JSONObject;

public class TimerSetUX extends JFrame implements ActionListener{
	//static boolean autoRefresh=false;
	//static int H = 9,M = 0;
	Vector<FrameToFrame> v = new Vector<FrameToFrame>();
	//UI元件
	JCheckBox autoRefreshCBox;
	JSpinner HRSpin;
	//JSpinner MMSpin;
	JButton acceptBtn;
	JButton cancelBtn;
	
	public TimerSetUX()
	{
		JLabel TitleLabel = new JLabel("自動更新");
		TitleLabel.setFont(new Font("微軟正黑體", Font.BOLD, 20));
		TitleLabel.setBounds(17,6,144,51);
		
		
		
		autoRefreshCBox = new JCheckBox("自動重新整理");
		autoRefreshCBox.setFont(new Font("微軟正黑體",Font.PLAIN,18));
		autoRefreshCBox.setSelected(UX.autoRefresh);
		autoRefreshCBox.setBounds(17,55,160,51);
		autoRefreshCBox.addActionListener(this);
		
		//Spinner參數：init val, min, max, step
		SpinnerModel HRModel = new SpinnerNumberModel(UX.updateHR, 0, 23, 1);
		//SpinnerModel MINModel = new SpinnerNumberModel(M, 0, 50, 10);
		HRSpin = new JSpinner(HRModel);
		HRSpin.setBounds(146, 118, 80, 45);
		HRSpin.setFont(new Font("微軟正黑體",Font.PLAIN,18));
		//MMSpin = new JSpinner(MINModel);
		//MMSpin.setBounds(202, 98, 80, 45);
		//MMSpin.setFont(new Font("微軟正黑體",Font.PLAIN,18));
		if(!UX.autoRefresh)
		{
			HRSpin.setEnabled(false);
			//MMSpin.setEnabled(false);
		}
		else
		{
			HRSpin.setEnabled(true);
			//MMSpin.setEnabled(true);
		}
		//JLabel timesetLabel = new JLabel("於每天的                ：                 自動更新");
		JLabel timesetLabel = new JLabel("於每天的                             時自動更新");
		timesetLabel.setFont(new Font("微軟正黑體",Font.PLAIN,16));
		timesetLabel.setBounds(52,118,368,51);
		
		JLabel hintLabel = new JLabel("<html>自動更新將會於每日指定時間刷新法拍屋追蹤狀態與Google趨勢</html>");
		hintLabel.setFont(new Font("微軟正黑體",Font.PLAIN,14));
		hintLabel.setBounds(17,168,368,51);
		
		
		acceptBtn = new JButton("確定");
		acceptBtn.setBounds(147,229,117,29);
		acceptBtn.addActionListener(this);
		cancelBtn = new JButton("取消");
		cancelBtn.setBounds(277,229,117,29);
		cancelBtn.addActionListener(this);
		
		add(TitleLabel);
		add(autoRefreshCBox);
		add(HRSpin);
		//add(MMSpin);
		add(timesetLabel);
		add(hintLabel);
		add(acceptBtn);
		add(cancelBtn);

		getContentPane().setLayout(null);
		setBounds(200,200,400,300);
		setTitle("計時器設定");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	public void addListener(FrameToFrame n){
		System.out.println("你好哇");
		v.add(n);
	}
	public void frameToFrameListener(){
		System.out.println(v.size());
		for(int i=0;i<v.size();i++) {
			v.get(i).frameToFrameEvent(autoRefreshCBox.isSelected(),(Integer)HRSpin.getValue());
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e);
		if(e.getSource() == autoRefreshCBox)
		{
			if(autoRefreshCBox.isSelected())
			{
				HRSpin.setEnabled(true);
				//MMSpin.setEnabled(true);
			}
			else
			{
				HRSpin.setEnabled(false);
				//MMSpin.setEnabled(false);
			}
		}
		else if(e.getSource() == acceptBtn)
		{
			UX.autoRefresh = autoRefreshCBox.isSelected();
			UX.updateHR = Integer.parseInt(HRSpin.getValue().toString());
			//M = Integer.parseInt(MMSpin.getValue().toString());
			
			//if(H>=24 || H<0 || M>=60 || M<0)
			//	JOptionPane.showMessageDialog(null, "請確認時間格式正確！","時間格式錯誤",JOptionPane.ERROR_MESSAGE);
			WriteConfig();
			frameToFrameListener();
			dispose();
		}
		
		else	//取消的按鈕
		{
			dispose();
		}
	
	}
	
	private void WriteConfig() //throws IOException
	{
		try{
			FileWriter ff = new FileWriter("./config.json");	
			JSONObject json = new JSONObject();				//JSON元件
			try{
				json.put("DBVersion", UX.currentDBVersion);
				json.put("AutoRefresh", UX.autoRefresh);
				json.put("UpdateHR", UX.updateHR);
				String writeString = json.toString();
				ff.write(writeString);
				ff.close();
				System.out.println("成功寫入config.json，定時器已經更新");	
			}catch(JSONException jse){
				jse.printStackTrace();
		}}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
	}

	
}
