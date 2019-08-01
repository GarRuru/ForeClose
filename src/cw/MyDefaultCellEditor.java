package cw;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

public class MyDefaultCellEditor extends DefaultCellEditor{

	private DetailButton button;
	private ArrayList<ArrayList<CrawData>> totalCrawData;
	private CRAWUX saveUX = null;
    public MyDefaultCellEditor(ArrayList<ArrayList<CrawData>> tmp, CRAWUX myUX) {
		super(new JTextField());
		totalCrawData = tmp;
		saveUX = myUX;
        button = new DetailButton("");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//System.out.printf("%d %d\n",button.getRow(),button.getColum());
            	//詳細資料
            	if(button.getColumn()==4) {
	            	if(!totalCrawData.get(button.getRow()).get(0).getFindableBOOL())
	            		return;
	            	DetailData myDD = new DetailData(totalCrawData.get(button.getRow()));
            	}
            	//備註
            	else if(button.getColumn()==5) {

            		NoteUX myNote = new NoteUX(totalCrawData.get(button.getRow()).get(0).getNote(),0);
            		myNote.Run(saveUX, totalCrawData.get(button.getRow()));
            	}
            }
        });
    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
    	if(column==4) {
    		button.setText(totalCrawData.get(row).get(0).getFindable());
    	}
    	else if(column==5) {
    		String btnText = totalCrawData.get(row).get(0).getNote();
    		if(btnText.length() >=5)
    			button.setText(btnText.substring(0, 5));
    		else
    			button.setText(btnText);
    	}
    	button.setRow(row);
		button.setColumn(column);
        return button;
    }
}

