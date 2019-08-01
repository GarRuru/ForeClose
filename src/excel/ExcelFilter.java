package excel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExcelFilter {
	private String excelPath = "";
	private int rowCount = 0;

	public ExcelFilter(String path){
		excelPath = path;
	}

	public void filter(){
		FileInputStream fis = null;
		POIFSFileSystem fs = null;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;

		try
		{
			fis = new FileInputStream(excelPath);
	        fs = new POIFSFileSystem(fis);
	        wb = new HSSFWorkbook(fs);
	        sheet = wb.getSheetAt(wb.getNumberOfSheets() - 1);
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "處理Excel發生錯誤", "Error", JOptionPane.ERROR_MESSAGE);
			ioe.printStackTrace();
		}

        while(true) {
        	HSSFRow row = sheet.getRow(rowCount);
        	if(row == null)
        		break;
        	rowCount++;
        }

        int i = 1;
        while(true) {
        	HSSFRow row = sheet.getRow(i);
        	if(row == null)
        		break;
        	if(row.getCell(5) == null)
        		break;
        	if(row.getCell(5).getStringCellValue().contains("全部") ||
        	   row.getCell(5).getStringCellValue().contains(" 1分之1")||
        	   row.getCell(5).getStringCellValue().contains("公有1分之1")||
        	   row.getCell(5).getStringCellValue().contains("公有全部")) {
        		//保留
        		i++;
        	} else {
        		//部分的房地產，去掉
        		//System.out.println("remove line" + i);
        		sheet.removeRow(row);
        		sheet.shiftRows(i + 1, rowCount, -1);
        		rowCount--;
        	}
        }
        i = 1;
        while(true) {
        	HSSFRow row = sheet.getRow(i);
        	if(row == null)
        		break;
        	if(row.getCell(0) == null)
        		break;
        	row.getCell(0).setCellValue("" + i);
        	i++;
        }
        try {
	        FileOutputStream fos = new FileOutputStream(excelPath);
	        wb.write(fos);
	        fos.flush();
	        fos.close();
	        wb.close();
	        System.out.println("Done: 移除Excel中不必要的資料");
        } catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "處理Excel發生錯誤", "Error", JOptionPane.ERROR_MESSAGE);
			ioe.printStackTrace();
        }
	}
}
