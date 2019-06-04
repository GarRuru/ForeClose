package cw;

import java.util.ArrayList;

public class GTrendData {
	private String address;					//地址
	private ArrayList<Integer> searchCount;	//搜尋數歷史
	private ArrayList<String> recordDate;	//搜尋時間歷史
	
	public GTrendData()
	{
		address = "";
		searchCount = new ArrayList<Integer>();
		recordDate = new ArrayList<String>();
	}

	public void setAddress(String addr)
	{
		address = addr;
	}
	public String getAddress()
	{
		return address;
	}
	
	//增加一次搜尋的結果
	public void addSearchCount(int sc)
	{
		searchCount.add(new Integer(sc));
	}
	
	//把整個搜尋的歷史記錄回傳
	public ArrayList<Integer> getFullSearchCount()
	{
		return searchCount;
	}
	//只回傳最近一次的搜尋數
	public int getLatestSearchCount()
	{
		return searchCount.get(searchCount.size()-1);
	}
	
	//增加一次搜尋的結果（日期）
	public void addDate(String dt)
	{
		recordDate.add(dt);
	}
	//回傳全部搜尋的日期
	public ArrayList<String> getFullDate()
	{
		return recordDate;
	}
	//只回傳最近一次的搜尋日期
	public String getLatestDate()
	{
		return recordDate.get(recordDate.size()-1);
	}
	
	
	
}
