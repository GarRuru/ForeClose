package cw;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

//法拍屋網站：http://aomp.judicial.gov.tw/abbs/wkw/WHD2A00.jsp

public class Crawler {
    private static  String[] district = {"TPD", "PCD", "SLD", "TYD", "SCD", "MLD", "TCD", "NTD", "CHD", "ULD", "CYD", "TND", "CTD", "KSD", "PTD", "TTD", "HLD", "ILD", "KLD", "PHD", "KMD", "LCD"};
    private static final String[] districtCourt = {"臺灣台北地方法院", "臺灣新北地方法院", "臺灣士林地方法院", "臺灣桃園地方法院", "臺灣新竹地方法院", "臺灣苗栗地方法院",
    		"臺灣臺中地方法院", "臺灣南投地方法院", "臺灣彰化地方法院", "臺灣雲林地方法院", "臺灣嘉義地方法院", "臺灣臺南地方法院", "臺灣橋頭地方法院", "臺灣高雄地方法院",
    		"臺灣屏東地方法院", "臺灣臺東地方法院", "臺灣花蓮地方法院", "臺灣宜蘭地方法院", "臺灣基隆地方法院", "臺灣澎湖地方法院", "福建金門地方法院", "福建連江地方法院"};

	//依照地址搜尋: 地址與法院名稱
	public static ArrayList<ArrayList<String>> searchCrno(String address, String Court)
	{
		ArrayList<ArrayList<String>> totalData = new ArrayList<ArrayList<String>>();
		ArrayList<String> returnVal = new ArrayList<String>();
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
                    return null;
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"Big5"));
                String current;
                String resultDoc = "";
                while((current = br.readLine()) != null)
                    resultDoc += current + "\n";
                //System.out.println(resultDoc);
                if(resultDoc.contains("查無資料"))
                {
                	returnVal.add("Not Found");
                	returnVal.add(Court);
                	totalData.add(returnVal);
                	return totalData;
                }
                //找到的情況
                else
                {
                    Document doc = Jsoup.connect(urlString).userAgent("Mozilla/5.0").get();
                    Elements results = doc.select("td[width=20%]");		//地址
    				Elements results2 = doc.select("td[width=15%]");	//案號
    				Elements results3 = doc.select("td[width=10%]");	//法拍日
    				Elements results4 = doc.select("td[width=6%]");		//點交
    				Elements results5 = doc.select("table[width=100%] td:not([width])td:not([align])td:not([class])");		//價錢
    				for(int i = 1; i < results.size(); i++) {
    					ArrayList<String> crawdata = new ArrayList<String>();
    					//地址那欄，顯示在表格上的是第一個找到的結果，剩餘的收入另一個List
    	                String tempTable[] = results.get(i).toString().split("<|>");
    	                String findAddr = tempTable[4].substring(1);
    	                findAddr = findAddr.replaceAll("\\s*", ""); //去掉頭尾多餘空白
    	                //案號那一欄
    	                tempTable = results2.get(i).toString().split("<|>");
    	                String findcrawNo = URLReplace(tempTable[2]);
    	                //下次法拍日
    	                tempTable = results3.get(i).toString().split("<|>");
    	                String nextsellDate = tempTable[4].substring(3,12);
                        System.out.println("案號："+ findcrawNo + " 地址：[" + findAddr + "] 下次拍賣時間:" + nextsellDate);
                        //拍賣價錢
                        tempTable = results5.get((i-1)*3).toString().split("<|>");
    	                String price = URLReplace(tempTable[2]);
    	                price = price.replaceAll("\\s*", ""); //去掉頭尾多餘空白
                        //點交
    	                tempTable = results4.get(i).toString().split("<|>");
    	                String handover = URLReplace(tempTable[4]);
    	                handover = handover.replaceAll("\\s*", ""); //去掉頭尾多餘空白
                        //網站
                        tempTable = results.get(i).toString().split("<|>");
                        String tt[] =  tempTable[3].split("\"");
    	                String addressURL = tt[1];
    	                System.out.println("拍賣價錢：" + price + " " + handover + " " +addressURL);
                        //準備回傳值
    	                crawdata.add(findcrawNo);
    	                crawdata.add(findAddr);
    	                crawdata.add(Court);//.substring(2));
    	                crawdata.add(nextsellDate);
    	                crawdata.add(price);
    	                crawdata.add(handover);
    	                crawdata.add(addressURL);
    	                totalData.add(crawdata);
    	                System.out.printf("totalDatasize:%d %d\nvalue:%s\n",totalData.size(), totalData.get(0).size(),totalData.get(0).get(0).toString());
    				}

    				for(ArrayList<String> s: totalData)
    				{
    					for(String z: s)
    					{
    						System.out.print("[" + z + "]");
    					}
    					System.out.println("");
    				}
    				System.out.println("===========");
                	return totalData;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
			e.printStackTrace();
		}

		//其他不可預期的錯誤
        return null;
	}

	//依照字號搜尋: 字號與法院名稱
	public static ArrayList<ArrayList<String>> searchCrno(int crmno,String Court)
	{
		ArrayList<ArrayList<String>> totalData = new ArrayList<ArrayList<String>>();
		ArrayList<String> returnVal = new ArrayList<String>();
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
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"Big5"));
            String current = "";
            String resultDoc = "";
            while((current = br.readLine()) != null)
                resultDoc += current + "\n";
            System.out.println(resultDoc);
            if(resultDoc.contains("查無資料"))
            {
            	returnVal.add("Not Found");
            	returnVal.add(Court);
            	totalData.add(returnVal);
            	return totalData;
            }
			else
			{
				Document doc = Jsoup.connect(urlString).userAgent("Mozilla/5.0").get();
				Elements results = doc.select("td[width=20%]");		//地址
				Elements results2 = doc.select("td[width=15%]");	//案號
				Elements results3 = doc.select("td[width=10%]");	//法拍日
				Elements results4 = doc.select("td[width=6%]");		//點交
				Elements results5 = doc.select("table[width=100%] td:not([width])td:not([align])td:not([class])");		//價錢
				for(int i = 1; i < results.size(); i++) {
					ArrayList<String> crawdata = new ArrayList<String>();
					//地址那欄，固定只抓第一個找到的結果
	                String tempTable[] = results.get(i).toString().split("<|>");
	                String findAddr = tempTable[4].substring(1);
	                findAddr = findAddr.replaceAll("\\s*", ""); //去掉頭尾多餘空白
	                //案號那一欄
	                tempTable = results2.get(i).toString().split("<|>");
	                String findcrawNo = URLReplace(tempTable[2]);
	                //下次法拍日
	                tempTable = results3.get(i).toString().split("<|>");
	                String nextsellDate = tempTable[4].substring(3,12);
	                System.out.println("案號："+ findcrawNo + " 地址：[" + findAddr + "] 下次拍賣時間:" + nextsellDate);
	                //拍賣價錢
                    tempTable = results5.get((i-1)*3).toString().split("<|>");
	                String price = URLReplace(tempTable[2]);
	                price = price.replaceAll("\\s*", ""); //去掉頭尾多餘空白
	                System.out.println("yeeee"+price);
                    //點交
	                tempTable = results4.get(i).toString().split("<|>");
	                String handover = URLReplace(tempTable[4]);
	                handover = handover.replaceAll("\\s*", ""); //去掉頭尾多餘空白
	                System.out.println(handover);
                    //網站
                    tempTable = results.get(i).toString().split("<|>");
                    String tt[] =  tempTable[3].split("\"");
	                String addressURL = tt[1];
	                System.out.println(addressURL);
	                //準備回傳值
	                crawdata.add(findcrawNo);
	                crawdata.add(findAddr);
	                crawdata.add(Court);//.substring(2));
	                crawdata.add(nextsellDate);
	                crawdata.add(price);
	                crawdata.add(handover);
	                crawdata.add(addressURL);
	                totalData.add(crawdata);
	                System.out.printf("totalDatasize:%d %d\nvalue:%s\n",totalData.size(), totalData.get(0).size(),totalData.get(0).get(0).toString());
				}

            	return totalData;
			}
        }
        catch (IOException e)
        {
			e.printStackTrace();
		}
		//其他不可預期的錯誤
        return null;

	}

	//下載成Excel 格式的檔案
	//By Common.io
    @SuppressWarnings("deprecation")
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

    //第2功能:查google搜尋法拍(jsoup)
    public static String[] searchGoogleDataResults(String address) throws InterruptedException{
        String returnVal[] = new String[2];
        System.out.println("查詢目標:" + address);
    	try
    	{
            final String googleHead = "https://www.google.com/search?q=";
            String search = "\"" + address + "\" \"" + "法拍\"";
            search = googleHead + search;
            //因為Google偷偷改掉網頁格式，div死去A_A
            //替代品: HttpUnit(HtmlUnit)
            com.gargoylesoftware.htmlunit.WebClient wbc = new com.gargoylesoftware.htmlunit.WebClient(BrowserVersion.CHROME);
            HtmlPage rootPage = wbc.getPage(search);
            wbc.waitForBackgroundJavaScript(750);
            Document doc = Jsoup.parse(rootPage.asXml());
            //System.out.println(doc.toString());
            Elements results = doc.select("div#resultStats");
            String temp = results.text();
            String[] temp1 = temp.split(" ");
            String[] temp2 = temp1[1].split(",");
            String recordStr = "";
            for(String i : temp2){
                recordStr += i;
            }

			int record = Integer.parseInt(recordStr);
            returnVal[0] = address;
            returnVal[1] = recordStr;
            wbc.close();
            return returnVal;
        }
    	catch (IOException e)
    	{
            e.printStackTrace();
        }
        return null;	//搜尋出錯
    }

    //更新Google趨勢：只需回傳數字
    public static int UpdateGoogleDataResults(String address)
    {
        System.out.println("更新目標:" + address);
    	try
    	{
            final String googleHead = "https://www.google.com/search?q=";
            String search = "\"" + address + "\" \"" + "法拍\"";
            search = googleHead + search;
            com.gargoylesoftware.htmlunit.WebClient wbc = new com.gargoylesoftware.htmlunit.WebClient(BrowserVersion.CHROME);
            HtmlPage rootPage = wbc.getPage(search);
            wbc.waitForBackgroundJavaScript(750);
            Document doc = Jsoup.parse(rootPage.asXml());
            //System.out.println(doc.toString());
            Elements results = doc.select("div#resultStats");
            String temp = results.text();
            String[] temp1 = temp.split(" ");
            String[] temp2 = temp1[1].split(",");
            String recordStr = "";
            for(String i : temp2){
                recordStr += i;
            }
            int record = Integer.parseInt(recordStr);
            wbc.close();
            return record;
        }
    	catch (IOException e)
    	{
            e.printStackTrace();
        }
        return -1;	//搜尋出錯

    }

    //去除網頁內容中的特定字元集
    public static String URLReplace(String s)
    {
    	String E;
    	E = s.replaceAll("&amp;","&");
    	E = E.replaceAll("&lt;","<");
    	E = E.replaceAll("&gt;",">");
    	E = E.replaceAll("&nbsp;"," ");
    	E = E.replaceAll("&#39;","\'");
    	E = E.replaceAll("&quot;","\"");
    	//System.out.println("Convert: " + E);
    	return E;
    }

    //由輸入的地址自動判定歸屬的法院: 回傳法院的英文縮寫
    //Source: https://www.judicial.gov.tw/jw9706/pdf/20150814-1760-d6.pdf?sid=C0OV36
    public static String addressCheck(String address)
    {
    	//台北地方法院
    	if((address.indexOf("臺北") != -1 || address.indexOf("台北") != -1) ||
    	   (address.indexOf("中正區") != -1 || address.indexOf("松山區") != -1 || address.indexOf("信義區") != -1 ||
    	    address.indexOf("文山區") != -1 || address.indexOf("大安區") != -1 || address.indexOf("萬華區") != -1 ||
    	    address.indexOf("中山區") != -1))
    			return districtCourt[0];
    	if((address.indexOf("新北") != -1) ||
    	   (address.indexOf("新店區") != -1 || address.indexOf("烏來區") != -1 || address.indexOf("區")!= -1 ||
    	    address.indexOf("石碇區") != -1 || address.indexOf("坪林區") != -1))
    			return districtCourt[0];
    	//士林地方法院
    	if((address.indexOf("臺北") != -1 || address.indexOf("台北") != -1) ||
    	   (address.indexOf("士林區") != -1 || address.indexOf("北投區") != -1 || address.indexOf("大同區") != -1 ||
    	    address.indexOf("內湖區") != -1 || address.indexOf("南港區") != -1 ))
    			return districtCourt[2];
    	if((address.indexOf("新北") != -1) ||
    	   (address.indexOf("汐止區") != -1 || address.indexOf("淡水區") != -1 || address.indexOf("八里區") != -1 ||
    	    address.indexOf("三芝區") != -1 || address.indexOf("石門區") != -1 ))
    			return districtCourt[2];
    	//新北地方法院
    	if(address.indexOf("土城區") != -1 || address.indexOf("板橋區") != -1 || address.indexOf("三重區") != -1 ||
    	   address.indexOf("永和區") != -1 || address.indexOf("中和區") != -1 || address.indexOf("新莊區") != -1 ||
    	   address.indexOf("蘆洲區") != -1 || address.indexOf("三峽區") != -1 || address.indexOf("樹林區") != -1 ||
    	   address.indexOf("鶯歌區") != -1 || address.indexOf("泰山區") != -1 || address.indexOf("五股區") != -1 ||
    	   address.indexOf("林口區") != -1)
    		return districtCourt[1];
    	//桃園地方法院
    	if(address.indexOf("桃園") != -1)
    		return districtCourt[3];
    	//新竹地方法院
    	if(address.indexOf("新竹") != -1)
    		return districtCourt[4];
    	//宜蘭地方法院
    	if(address.indexOf("宜蘭") != -1)
    		return districtCourt[17];
    	//基隆地方法院
    	if(address.indexOf("基隆") != -1)
    		return districtCourt[18];
    	if(address.indexOf("瑞芳區") != -1 || address.indexOf("貢寮區") != -1 || address.indexOf("雙溪區") != -1 ||
    	   address.indexOf("平溪區") != -1 || address.indexOf("金山區") != -1 || address.indexOf("萬里區") != -1)
    	    return districtCourt[18];
    	//花蓮地方法院
    	if(address.indexOf("花蓮") != -1)
    		return districtCourt[16];
    	//台東地方法院
    	if(address.indexOf("臺東") != -1 || address.indexOf("台東") != -1)
    		return districtCourt[15];
    	//台中地方法院
    	if(address.indexOf("台中") != -1 || address.indexOf("臺中") != -1)
    		return districtCourt[6];
    	//苗栗地方法院
    	if(address.indexOf("苗栗") != -1)
    		return districtCourt[5];
    	//南投地方法院
    	if(address.indexOf("南投") != -1)
    		return districtCourt[7];
    	//彰化地方法院
    	if(address.indexOf("彰化") != -1)
    		return districtCourt[8];
    	//雲林地方法院
    	if(address.indexOf("雲林") != -1)
    		return districtCourt[9];
    	//嘉義地方法院
    	if(address.indexOf("嘉義") != -1)
    		return districtCourt[10];
    	//台南地方法院
    	if(address.indexOf("台南") != -1 || address.indexOf("臺南") != -1)
    		return districtCourt[11];
    	//高雄地方法院
    	if(address.indexOf("小港") != -1 || address.indexOf("旗津") != -1 || address.indexOf("前鎮") != -1 ||
    	   address.indexOf("苓雅") != -1 || address.indexOf("新興") != -1 || address.indexOf("前金") != -1 ||
    	   address.indexOf("三民") != -1 || address.indexOf("鼓山") != -1 || address.indexOf("鹽埕") != -1 ||
    	   address.indexOf("鳳山") != -1 || address.indexOf("大寮") != -1 || address.indexOf("林園") != -1 ||
    	   address.indexOf("太平") != -1 || address.indexOf("東沙") != -1)	//太平島&東沙群島
    		return districtCourt[13];
    	//橋頭地方法院
    	if(address.indexOf("楠梓") != -1 || address.indexOf("左營") != -1 || address.indexOf("大樹") != -1 ||
		   address.indexOf("大社") != -1 || address.indexOf("仁武") != -1 || address.indexOf("烏松	") != -1 ||
		   address.indexOf("岡山") != -1 || address.indexOf("橋頭") != -1 || address.indexOf("燕巢") != -1 ||
		   address.indexOf("田寮") != -1 || address.indexOf("阿蓮") != -1 || address.indexOf("路竹") != -1 ||
		   address.indexOf("湖內") != -1 || address.indexOf("茄萣") != -1 || address.indexOf("永安") != -1 ||
		   address.indexOf("彌陀") != -1 || address.indexOf("梓官") != -1 || address.indexOf("旗山") != -1 ||
		   address.indexOf("美濃") != -1 || address.indexOf("六龜") != -1 || address.indexOf("甲仙") != -1 ||
		   address.indexOf("杉林") != -1 || address.indexOf("內門") != -1 || address.indexOf("茂林") != -1 ||
		   address.indexOf("桃源") != -1 || address.indexOf("那瑪夏") != -1)	//太平島&東沙群島
			return districtCourt[12];
    	//屏東地方法院
    	if(address.indexOf("屏東") != -1)
    		return districtCourt[14];
    	//澎湖地方法院
    	if(address.indexOf("澎湖") != -1)
    		return districtCourt[19];
    	//金門地方法院
    	if(address.indexOf("金門") != -1)
    		return districtCourt[20];
    	//連江地方法院
    	if(address.indexOf("連江") != -1)
    		return districtCourt[21];

    	return "None";
    }
}
