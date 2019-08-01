package cw;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class DetailData extends JFrame{
	private ArrayList<CrawData> detailCrawData = new ArrayList<CrawData>();

	public DetailData(ArrayList<CrawData> ACD) {
		detailCrawData = ACD;
		getContentPane().setLayout(null);
		int index = 0;
		//建立標題
		JLabel addressTitleLabel = new JLabel("地址");
		addressTitleLabel.setBounds(20, 30 * index, 400, 30);
		JLabel crmnoTitleLabel = new JLabel("字號");
		crmnoTitleLabel.setBounds(420, 30 * index, 100, 30);
		JLabel sellDateTitleLabel = new JLabel("下次拍賣日期");
		sellDateTitleLabel.setBounds(520, 30 * index, 100, 30);
		JLabel priceTitleLabel = new JLabel("拍賣價格");
		priceTitleLabel.setBounds(620, 30 * index, 100, 30);
		JLabel handoverTitleLabel = new JLabel("點交狀態");
		handoverTitleLabel.setBounds(720, 30 * index, 100, 30);
		JLabel websiteTitleLabel = new JLabel("公文網址");
		websiteTitleLabel.setBounds(820, 30 * index, 60, 30);
		//加進pane
		getContentPane().add(addressTitleLabel);
		getContentPane().add(crmnoTitleLabel);
		getContentPane().add(sellDateTitleLabel);
		getContentPane().add(priceTitleLabel);
		getContentPane().add(handoverTitleLabel);
		getContentPane().add(websiteTitleLabel);
		index++;
		//生出資料
		for(CrawData i : detailCrawData) {
			JLabel addressLabel = new JLabel(i.getAddress());
			addressLabel.setBounds(20, 30 * index, 400, 30);
			JLabel crmnoLabel;
			if(i.getNo().isEmpty())
				crmnoLabel = new JLabel(i.getNo());//.substring(7));
			else
				crmnoLabel = new JLabel(i.getNo().substring(7));
			crmnoLabel.setBounds(420, 30 * index, 100, 30);
			JLabel sellDateLabel = new JLabel(i.getsellDate());
			sellDateLabel.setBounds(520, 30 * index, 100, 30);
			JLabel priceLabel = new JLabel("" + i.getPrice());
			priceLabel.setBounds(620, 30 * index, 100, 30);
			JLabel handoverLabel = new JLabel(i.getHandover());
			handoverLabel.setBounds(720, 30 * index, 100, 30);
			JButton websiteButton = new JButton("連結");
			websiteButton.setBounds(820, 30 * index, 60, 30);
			if(i.getWebsite().equals(""))
				websiteButton.setEnabled(false);

			websiteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					System.out.println("啟動瀏覽器：開啟公文PDF連結");
					try {
	                    Desktop.getDesktop().browse(new URI(i.getWebsite()));
	                    System.out.println("Open url success");
	                } catch (URISyntaxException | IOException ex) {
	                    JOptionPane.showMessageDialog(null, "Fail");
	                    System.out.println("Open url fail");
	                }
				}

			});

			getContentPane().add(addressLabel);
			getContentPane().add(crmnoLabel);
			getContentPane().add(sellDateLabel);
			getContentPane().add(priceLabel);
			getContentPane().add(handoverLabel);
			getContentPane().add(websiteButton);

			index++;
		}
		//frame設定
		setBounds(200, 200, 900, 600);
		setTitle("詳細資料");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
