package cw;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;

public class AutoCrawler extends TimerTask{
	public enum Time {DAY,MONTH,YEAR,WEEK,HOUR};
	private Date nowTime;
	private ArrayList<String> addressList = new ArrayList<String>();
	private ArrayList<String> courtList = new ArrayList<String>();

	public AutoCrawler() {

	}

	public void CrawlerRunOnce(String address, String court) {
		//搜尋結果
		int result = 1;//Crawler.searchCrno(address, court);
		if(result == 1)
			System.out.println("有");
		else
			System.out.println("沒");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run(){
		//System.out.println(nowTime.getHours());
		nowTime=new Date();
		System.out.println(nowTime.getMinutes());
		if((nowTime.getHours()==8 || nowTime.getHours()==12 || nowTime.getHours()==17)
				&& nowTime.getMinutes()==0)
		{
			System.out.println("有事做");
		}
		else
		{
			System.out.println("沒事幹");
		}
    }
	public void setInputData(String address, String court) {
		addressList.add(address);
		courtList.add(court);
	}
}
