package cw;

import java.awt.EventQueue;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TEsts {

	private JFrame frame;



	/**
	 * Create the application.
	 */
	public TEsts() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(200	, 200, 400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JCheckBox autoRefreshCBox = new JCheckBox("啟用自動更新");
		autoRefreshCBox.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		autoRefreshCBox.setBounds(17,55,160,51);
		SpinnerModel HRModel = new SpinnerNumberModel(9, 0, 23, 1);
		SpinnerModel MINModel = new SpinnerNumberModel(0, 0, 59, 1);
		JSpinner HRSpin = new JSpinner(HRModel);
		HRSpin.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		HRSpin.setBounds(111, 118, 79, 43);
		JSpinner MMSpin = new JSpinner(MINModel);
		MMSpin.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		MMSpin.setBounds(202, 118,79, 43);
		
		
		frame.getContentPane().add(autoRefreshCBox);
		frame.getContentPane().add(HRSpin);
		frame.getContentPane().add(MMSpin);
		
		JLabel lblNewLabel = new JLabel("自動更新");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblNewLabel.setBounds(17, 6, 144, 51);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("於每天的                ：                 自動更新");
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(17, 118, 368, 51);
		frame.getContentPane().add(lblNewLabel_1);
		
		JButton button = new JButton("保存配置");
		button.setBounds(147, 229, 117, 29);
		frame.getContentPane().add(button);
		
		JButton button_1 = new JButton("取消");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button_1.setBounds(277, 229, 117, 29);
		frame.getContentPane().add(button_1);

	}
}
