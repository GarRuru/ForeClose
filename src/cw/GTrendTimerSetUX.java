package cw;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class GTrendTimerSetUX extends JFrame implements ActionListener{
	static boolean autoRefresh=false;
	static int H = 9,M = 0;
	
	//UI元件
	JCheckBox autoRefreshCBox;
	JSpinner HRSpin;
	JSpinner MMSpin;
	JButton acceptBtn;
	JButton cancelBtn;
	
	public GTrendTimerSetUX()
	{
		JLabel TitleLabel = new JLabel("自動更新");
		TitleLabel.setFont(new Font("微軟正黑體", Font.BOLD, 20));
		TitleLabel.setBounds(17,6,144,51);
		
		
		
		autoRefreshCBox = new JCheckBox("自動重新整理");
		autoRefreshCBox.setFont(new Font("微軟正黑體",Font.PLAIN,18));
		autoRefreshCBox.setSelected(autoRefresh);
		autoRefreshCBox.setBounds(17,55,160,51);
		autoRefreshCBox.addActionListener(this);
		
		//Spinner參數：init val, min, max, step
		SpinnerModel HRModel = new SpinnerNumberModel(H, 0, 23, 1);
		SpinnerModel MINModel = new SpinnerNumberModel(M, 0, 50, 10);
		HRSpin = new JSpinner(HRModel);
		HRSpin.setBounds(111, 118, 80, 45);
		HRSpin.setFont(new Font("微軟正黑體",Font.PLAIN,18));
		MMSpin = new JSpinner(MINModel);
		MMSpin.setBounds(202, 118, 80, 45);
		MMSpin.setFont(new Font("微軟正黑體",Font.PLAIN,18));
		if(!autoRefresh)
		{
			HRSpin.setEnabled(false);
			MMSpin.setEnabled(false);
		}
		else
		{
			HRSpin.setEnabled(true);
			MMSpin.setEnabled(true);
		}
		JLabel timesetLabel = new JLabel("於每天的                ：                 自動更新");
		timesetLabel.setFont(new Font("微軟正黑體",Font.PLAIN,18));
		timesetLabel.setBounds(17,118,368,51);
		
		acceptBtn = new JButton("保存配置");
		acceptBtn.setBounds(147,229,117,29);
		acceptBtn.addActionListener(this);
		cancelBtn = new JButton("取消");
		cancelBtn.setBounds(277,229,117,29);
		cancelBtn.addActionListener(this);
		
		add(TitleLabel);
		add(autoRefreshCBox);
		add(HRSpin);
		add(MMSpin);
		add(timesetLabel);
		add(acceptBtn);
		add(cancelBtn);

		getContentPane().setLayout(null);
		setBounds(200,200,400,300);
		setTitle("計時器設定");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e);
		if(e.getSource() == autoRefreshCBox)
		{
			if(autoRefreshCBox.isSelected())
			{
				HRSpin.setEnabled(true);
				MMSpin.setEnabled(true);
			}
			else
			{
				HRSpin.setEnabled(false);
				MMSpin.setEnabled(false);
			}
		}
		else if(e.getSource() == acceptBtn)
		{
			autoRefresh = autoRefreshCBox.isSelected();
			H = Integer.parseInt(HRSpin.getValue().toString());
			M = Integer.parseInt(MMSpin.getValue().toString());
			//if(H>=24 || H<0 || M>=60 || M<0)
			//	JOptionPane.showMessageDialog(null, "請確認時間格式正確！","時間格式錯誤",JOptionPane.ERROR_MESSAGE);
			dispose();
		}
		
		else	//取消的按鈕
		{
			dispose();
		}
	
	}
	
	
}
