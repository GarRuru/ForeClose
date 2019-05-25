package cw;

import java.util.*;

public class AutoCrawler extends TimerTask{
	public enum Time {DAY,MONTH,YEAR,WEEK,HOUR};
	private Date nowTime;
	private Boolean alreadyStart=false;
	private ArrayList<String> addressList = new ArrayList<String>();
	private ArrayList<String> courtList = new ArrayList<String>();
	//8. 12. 5.
	/*
	 ArrayList中要塞設定要定時爬的資料
	 其他地方就補上原本單次操作會呈現的東西就好
	*/
	public AutoCrawler() {
		
	}
	
	public void CrawlerRunOnce(String address, String court) {
		//搜尋結果
		int result = 1;//Crawler.searchCrno(address, court);
		if(result == 1)
			System.out.println("有");
		else System.out.println("沒");
		//出現提示視窗
		
		//excel表呈現
		
	}
	@Override
	public void run(){
		//System.out.println(nowTime.getHours());
		nowTime=new Date();
		System.out.println(nowTime.getMinutes());
		if((nowTime.getHours()==8 || nowTime.getHours()==12 || nowTime.getHours()==17)
				&& nowTime.getMinutes()==0)
		{
			/*  根據陣列中的資料開始定時爬蟲
			for()
				CrawlerRunOnce(address,court);
			*/
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
	
	//清空資料、修改資料
	
	public static void main(String args[])
	{
		Timer timer = new Timer();
		AutoCrawler ac = new AutoCrawler();
		timer.schedule(ac, 0, 60000);
	}
	
}




/*
1.找現在時間
2.計算相差時間然後再跑一次code
3.設定下次跑的時間
*/