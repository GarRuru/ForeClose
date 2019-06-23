package cw;

public class CrawData {
	private String searchKWord;		//搜尋階段採用的關鍵字
	//欄位資料：法拍案號、拍賣地址、執行法院
	private String No,Address,Court;
	private boolean isFindable;		//已經公告了嗎?
	private String sellDate;		//預定法拍日，沒有公告就是空字串
	private String price;			//法拍起標價格
	private int priceINT;			
	private String Handover;		//點交
	private String website;			//公文連結
	private String Note;			//備註欄位
	
	public CrawData()
	{
		super();
	}
	
	public CrawData(String no,String address,String court,boolean find,String sellDate,
					String price,String Handover,String website,String Note)
	{
		super();
		searchKWord = "";
		this.No = no;
		this.Address = address;
		this.Court = court;
		this.isFindable = find;
		this.sellDate=sellDate;
		this.price=price;
		this.Handover=Handover;
		this.website=website;
		this.Note = Note;
		if(!price.equals(""))
			priceINT = Integer.parseInt(price.replace(",",""));
		else priceINT = 0;
			
	}
	public void setNo(String n)
	{
		No = n;
	}
	public String getNo()
	{
		return No;
	}
	
	
	public void setKeyword(String kw)
	{
		searchKWord = kw;
	}
	public String getKeyword()
	{
		return searchKWord;
	}
	
	public void setAddress(String addr)
	{
		Address = addr;
	}
	public String getAddress()
	{
		return Address;
	}
	
	public void setCourt(String court)
	{
		Court = court;
	}
	public String getCourt()
	{
		return Court.substring(2);
	}
	
	public String getFULLCourt()
	{
		return Court;
	}

	
	public void setFindable(boolean b)
	{
		isFindable = b;
	}
	public String getFindable()
	{
		if(isFindable)
			return "已公告";
		else return "未公告";
	}
	public boolean getFindableBOOL()
	{
		return isFindable;
	}
	
	
	public void setsellDate(String date)
	{
		sellDate = date;
	}
	public String getsellDate() 
	{
		return sellDate;
	}
	
	public void setPrice(String pr)
	{
		price = pr;
		priceINT = Integer.parseInt(price);
	}
	public String getPrice()
	{
		return price;
	}
	public int getPriceINT()
	{
		return priceINT;
	}
	
	public void setHandover(String b)
	{
		Handover = b;
	}
	public String getHandover()
	{
		return Handover;
	}

	public void setWebsite(String url)
	{
		website = url;
	}
	public String getWebsite()
	{
		return website;
	}
	
	public void setNote(String note)
	{
		Note = note;
	}
	public String getNote()
	{
		return Note;
	}
}
