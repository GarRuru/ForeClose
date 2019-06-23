package cw;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;

public class NoteUX extends JFrame{
	private String note;
	private int mode;	//0=CRAW, 1=GTREND
	private JTextPane textPane;
	
	public NoteUX(String note, int mode) {
		this.note = note;
		this.mode = mode;
	}
	public void Run(CRAWUX cwUX, ArrayList<CrawData> ACD)
	{
		ArrayList<CrawData> detailCrawData = new ArrayList<CrawData>();
		getContentPane().setLayout(null);
		JLabel titleLabel = new JLabel("備註：");
		titleLabel.setBounds(17, 6, 144, 29);
		titleLabel.setFont(new Font("微軟正黑體",Font.PLAIN,16));
		add(titleLabel);
		
		//文字區域
		textPane = new JTextPane();
		textPane.setText(note);
		textPane.setBounds(17, 49, 367, 282);
		add(textPane);
		
		//按鈕
		JButton saveBtn = new JButton("保存");
		saveBtn.setBounds(46, 343, 117, 29);
		add(saveBtn);
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				saveAndExit(ACD);
				cwUX.WriteConfig();
				dispose();
			}
		});
		
		JButton cancelBtn = new JButton("取消");
		cancelBtn.setBounds(234, 343, 117, 29);
		add(cancelBtn);
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});

		//關閉：強制存檔
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				saveAndExit(ACD);
				cwUX.WriteConfig();
				e.getWindow().dispose();
			}
		});
		
		
		
		setBounds(200,200,400,400);
		setTitle("備註");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	
	public void Run(GTrendTrackUX gUX, ArrayList<GTrendData> GTD,int index)
	{
		getContentPane().setLayout(null);
		JLabel titleLabel = new JLabel("備註：");
		titleLabel.setBounds(17, 6, 144, 29);
		titleLabel.setFont(new Font("微軟正黑體",Font.PLAIN,16));
		add(titleLabel);
		
		//文字區域
		textPane = new JTextPane();
		textPane.setText(note);
		textPane.setBounds(17, 49, 367, 282);
		add(textPane);
		
		//按鈕
		JButton saveBtn = new JButton("保存");
		saveBtn.setBounds(46, 343, 117, 29);
		add(saveBtn);
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				saveAndExit(GTD, index);
				gUX.WriteConfig();
				dispose();
			}
		});
		
		JButton cancelBtn = new JButton("取消");
		cancelBtn.setBounds(234, 343, 117, 29);
		add(cancelBtn);
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});

		//關閉：強制存檔
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				saveAndExit(GTD, index);
				gUX.WriteConfig();
				e.getWindow().dispose();
			}
		});
		
		
		
		setBounds(200,200,400,400);
		setTitle("備註");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	//法拍屋寫入
	private void saveAndExit(ArrayList<CrawData> ACD)
	{
		ACD.get(0).setNote(textPane.getText());
	}
	private void saveAndExit(ArrayList<GTrendData> GTD, int index)
	{
		GTD.get(index).setNote(textPane.getText());
	}
	
}
