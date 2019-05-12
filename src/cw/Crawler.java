package cw;
import java.util.*;
import java.net.*;
import java.io.*;
import org.apache.commons.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Crawler {
    private static  String[] district = {"TPD", "PCD", "SLD", "TYD", "SCD", "MLD", "TCD", "NTD", "CHD", "ULD", "CYD", "TND", "CTD", "KSD", "PTD", "TTD", "HLD", "ILD", "KLD", "PHD", "KMD", "LCD"};
    private static final String[] districtCourt = {"臺灣台北地方法院", "臺灣新北地方法院", "臺灣士林地方法院", "臺灣桃園地方法院", "臺灣新竹地方法院", "臺灣苗栗地方法院",
    		"臺灣臺中地方法院", "臺灣南投地方法院", "臺灣彰化地方法院", "臺灣雲林地方法院", "臺灣嘉義地方法院", "臺灣臺南地方法院", "臺灣橋頭地方法院", "臺灣高雄地方法院", 
    		"臺灣屏東地方法院", "臺灣臺東地方法院", "臺灣花蓮地方法院", "臺灣宜蘭地方法院", "臺灣基隆地方法院", "臺灣澎湖地方法院", "福建金門地方法院", "福建連江地方法院"};
    private static ArrayList<Integer> googleSearchResults = new ArrayList<>(); //存google查的資料數量的array

    
	//依照地址搜尋: 地址與法院名稱
	public static int searchCrno(String address, String Court)
	{		
		System.out.println("Address:" + address + ",法院:" + Court);
        try {
			String addressVAR = URLEncoder.encode(address, "BIG5");
			String urlString = "http://aomp.judicial.gov.tw"
	                + "/abbs/wkw/WHD2A03.jsp?50B8DA8E0FA862F5A04E060849A1EF7D=C3743233306CA43032EAA3FF8116DC1A"
	                + "&hsimun=all&ctmd=all&sec=all&saledate1=&saledate2=&crmyy=&crmid="
	                + "&crmno="
	                + "&dpt=&minprice1=&minprice2=&saleno=&area1=&area2="
	                + "&registeno=" + addressVAR
	                + "&checkyn=all&emptyyn=all&rrange=%A4%A3%A4%C0&comm_yn=&owner1=&order=odcrm&courtX=" + district[Arrays.asList(districtCourt).indexOf(Court)]
	                + "&proptypeX=C52&saletypeX=1&query_typeX=db";
            try {
                URL url = new URL(urlString);
                URLConnection urlConnection = url.openConnection();
                HttpURLConnection connection = null;
                if(urlConnection instanceof HttpURLConnection)
                    connection = (HttpURLConnection) urlConnection;
                else{
                    System.out.println("連線失敗");
                    return -1;
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"Big5"));
                String current;
                String resultDoc = "";
                while((current = br.readLine()) != null)
                    resultDoc += current + "\n";
                System.out.println(resultDoc);
                if(resultDoc.contains("查無資料"))
                	return 0;
                else return 1;
                
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
			e.printStackTrace();
		}
        
		//其他不可預期的錯誤 
        return -1;
	}
    
	//依照字號搜尋: 字號與法院名稱
	public static int searchCrno(int crmno,String Court)	
	{
        try 
        {
			String urlString = "http://aomp.judicial.gov.tw"
                + "/abbs/wkw/WHD2A03.jsp?50B8DA8E0FA862F5A04E060849A1EF7D=C3743233306CA43032EAA3FF8116DC1A"
                + "&hsimun=all&ctmd=all&sec=all&saledate1=&saledate2=&crmyy=&crmid="
                + "&crmno=" + String.format("%06d", crmno)
                + "&dpt=&minprice1=&minprice2=&saleno=&area1=&area2="
                + "&registeno="
                + "&checkyn=all&emptyyn=all&rrange=%A4%A3%A4%C0&comm_yn=&owner1=&order=odcrm&courtX=" + district[Arrays.asList(districtCourt).indexOf(Court)]
                + "&proptypeX=C52&saletypeX=1&query_typeX=db";
            URL url = new URL(urlString);
			URLConnection urlConnection = url.openConnection();
            HttpURLConnection connection = null;
            if(urlConnection instanceof HttpURLConnection)
                connection = (HttpURLConnection) urlConnection;
            else
            {
                System.out.println("連線失敗");
                return -1;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"Big5"));
            String current = "";
            String resultDoc = "";
            while((current = br.readLine()) != null)
                resultDoc += current + "\n";
            System.out.println(resultDoc);
            if(resultDoc.contains("查無資料"))
                return 0;
			else
				return 1;
        } 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
		//其他不可預期的錯誤
        return -1;

	}

	//下載成Excel 格式的檔案
	//By Common.io
    public static int downloadDistrictCourtData(String Court, String DestFolder)
    {
    	try 
    	{
            String urlString = "http://aomp.judicial.gov.tw"
                + "/abbs/wkw/WHD2A03_DOWNLOAD.jsp?50B8DA8E0FA862F5A04E060849A1EF7D=C3743233306CA43032EAA3FF8116DC1A"
                + "&hsimun=all&ctmd=all&sec=all&saledate1=&saledate2=&crmyy=&crmid="
                + "&crmno="
                + "&dpt=&minprice1=&minprice2=&saleno=&area1=&area2="
                + "&registeno=&checkyn=all&emptyyn=all&rrange=%A4%A3%A4%C0&comm_yn=&owner1=&order=odcrm"
                + "&courtX=" + district[Arrays.asList(districtCourt).indexOf(Court)]
                + "&proptypeX=C52&saletypeX=1&query_typeX=db";
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection connection = null;
            if(urlConnection instanceof HttpURLConnection)
                connection = (HttpURLConnection) urlConnection;
            else{
                System.out.println("!!! 連線失敗 !!!");
                return -1;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String current;
            String resultDoc = "";
            while((current = br.readLine()) != null)
                resultDoc += current + "\n";
            if(!(resultDoc.contains("查無資料"))){
                Date today = new Date();
                URL source = new URL(urlString);
                //String theStrDestDir = "./";
                File theStockDest = new File(DestFolder);
                FileUtils.forceMkdir(theStockDest);
                File destination = new File(DestFolder + '/'+ Court + "-"
                                    + String.format("%04d", today.getYear() + 1900)
                                    + String.format("%02d", today.getMonth() + 1)
                                    + String.format("%02d", today.getDate())
                                    + ".xls");
                FileUtils.copyURLToFile(source, destination);
                System.out.println("> 檔案下載完成!");
                return 1;	//Success
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    	return -1;
    }
    
    
    public static int searchGoogleDataResults(String address){ //第2功能:查google搜尋法拍(jsoup)
        try {
            String googleHead = "https://www.google.com/search?q=";
            String search = address + " " + "法拍";
            search = googleHead + search;
            Document doc = Jsoup.connect(search).userAgent("Mozilla/5.0").get();
            Elements results = doc.select("div#resultStats");
            String temp = results.text();
            String[] temp1 = temp.split(" ");
            String[] temp2 = temp1[1].split(",");
            String recordStr = "";
            for(String i : temp2){
                recordStr += i;
            }
            int record = Integer.parseInt(recordStr);
            googleSearchResults.add(record);
            System.out.println(googleSearchResults);
            return record;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;	//搜尋出錯
    }

}
