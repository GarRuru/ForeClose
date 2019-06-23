package cw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//TODO: 下一版重要的工作，要把Note的欄位從空字串換掉！！！！！！！
public class DBCheck {
	public void DBUpdate()
	{
		String jsonBuffer = "";
		BufferedReader reader = null;
		//Section 1:CONFIG檔案部分
		try {
			//reader = new BufferedReader(new InputStreamReader(new FileInputStream("./config.json")));
			FileWriter ff = new FileWriter("./config.json");
			JSONObject json = new JSONObject();
			try
			{
				System.out.print(UX.autoRefresh);System.out.println(UX.updateHR);
				json.put("DBVersion",UX.currentDBVersion);
				json.put("AutoRefresh",UX.autoRefresh);
				json.put("UpdateHR",UX.updateHR);
				String ws = json.toString();
				ff.write(ws);
				ff.close();
			}catch(IOException ioe) {
				ioe.printStackTrace();
			}
		}catch (IOException ioe) {
			ioe.printStackTrace();
		}
		//Section 2-1: 法拍屋讀檔
		String tempString = "";
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("./CrawHistory.json"),"UTF-8"));
			while((tempString = reader.readLine()) != null)
				jsonBuffer += tempString;
			reader.close();
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			try {
				reader.close();
			}catch(IOException e1){
				e1.printStackTrace();
			}			
		}
		System.out.println(jsonBuffer);
			//Section 2-2:法拍屋建立ACD
		try {
			JSONObject J = new JSONObject(jsonBuffer);
			JSONArray t = J.getJSONArray("CRAW");
			ArrayList<ArrayList<CrawData>> ACD = new ArrayList<ArrayList<CrawData>>();
			for(int i=0;i<t.length();i++)	//每一個地址
			{
				ArrayList<CrawData> acd = new ArrayList<CrawData>();
				System.out.println("Load: " + t.get(i));
				JSONObject temp = (JSONObject) t.get(i);
				CrawData cwd = new CrawData(temp.getString("CrawNo"),temp.getString("Address"),temp.getString("Court"),
											temp.getBoolean("isFindable"),temp.getString("SellDate"),temp.getString("Price"),
											temp.getString("Handover"),temp.getString("website"),"");
				cwd.setKeyword(temp.getString("SearchKeyword"));		//當初搜尋使用的關鍵字（供刷新搜尋用）
				acd.add(cwd);
				JSONArray another = temp.getJSONArray("Another");
				for(int j=0;j<another.length();j++)
				{
					JSONObject temp2 = (JSONObject) another.get(j);
					CrawData ano = new CrawData(temp2.getString("CrawNo"),temp2.getString("Address"),temp2.getString("Court"),
							temp2.getBoolean("isFindable"),temp2.getString("SellDate"),temp2.getString("Price"),
							temp2.getString("Handover"),temp2.getString("website"),"");
					acd.add(ano);
				}
				ACD.add(acd);
			}
			//Section 2-3:法拍屋資料重新寫回Json	
			BufferedWriter ffx = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./CrawHistory.json"),"UTF-8"));	
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
					name.put("Court", alcd.get(0).getFULLCourt());
					name.put("isFindable", alcd.get(0).getFindableBOOL());
					name.put("SellDate", alcd.get(0).getsellDate());
					name.put("Price", Integer.toString(alcd.get(0).getPriceINT()));
					name.put("Handover", alcd.get(0).getHandover());
					name.put("website", alcd.get(0).getWebsite());
					name.put("Note", alcd.get(0).getNote());
					JSONArray another = new JSONArray();	//another標籤
					for(int i=1;i<alcd.size();i++)
					{
						JSONObject nme = new JSONObject();
						nme.put("Address", alcd.get(i).getAddress());
						nme.put("CrawNo", alcd.get(i).getNo());
						nme.put("Court", alcd.get(i).getFULLCourt());
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
				ffx.write(writeString);
				ffx.close();
				System.out.println("成功寫入CrawHistory.json");
		
				
			}catch(JSONException jse){
				jse.printStackTrace();
			}}catch(IOException ioe) {
				ioe.printStackTrace();
			}
			
		
		//Section 3-1:Google趨勢資料庫讀取
		jsonBuffer = "";
		tempString = "";
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("./GTrendHistory.json"),"UTF-8"));
			while((tempString = reader.readLine()) != null)
				jsonBuffer += tempString;
			reader.close();
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			try {
				reader.close();
			}catch(IOException e1){
				e1.printStackTrace();
			}			
		}
		System.out.println(jsonBuffer);
		
		//Section 3-2:趨勢資料庫讀取
		ArrayList<GTrendData> GTD = new ArrayList<GTrendData>();
		JSONObject J = new JSONObject(jsonBuffer);
		JSONArray t = J.getJSONArray("GTrend");
		for(int i=0;i<t.length();i++)
		{
			JSONObject temp = (JSONObject) t.get(i);
			//System.out.println(temp);
			GTrendData gtd = new GTrendData();
			gtd.setAddress(temp.getString("Address"));
			for(int j=0;j<temp.getJSONArray("Date").length();j++)
			{
				System.out.println("Load: " + temp.getJSONArray("Date").get(j).toString() + " " +temp.getJSONArray("Count").get(j));
				gtd.addDate(temp.getJSONArray("Date").get(j).toString());
				gtd.addSearchCount((int) temp.getJSONArray("Count").get(j));
			}
			GTD.add(gtd);
		}
		
		//Section 3-3:加入備註欄位
		try
		{
			Writer ff = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./GTrendHistory.json"),"UTF-8"));	
			JSONObject json = new JSONObject();				//JSON元件
			try
			{
				JSONArray jsonMember = new JSONArray();		//GTrend標籤
				for(GTrendData g: GTD)						//擷取每一個資料，構成JSONObject
				{
					JSONObject name = new JSONObject();
					name.put("Address", g.getAddress());
					JSONArray datelist = new JSONArray();
					for(String ss: g.getFullDate())
					{
						datelist.put(ss);
					}
					JSONArray searchcountlist = new JSONArray();
					for(Integer ss: g.getFullSearchCount())
					{
						searchcountlist.put(ss);
					}
					name.put("Date", datelist);
					name.put("Count", searchcountlist);
					name.put("Note", "");
					jsonMember.put(name);
				}
				json.put("GTrend", jsonMember);
				String writeString = json.toString();
				ff.write(writeString);
				ff.close();
			}catch(JSONException jse){
				jse.printStackTrace();
		}}catch (IOException ioe) {
			ioe.printStackTrace();
		}

		
	}
}
