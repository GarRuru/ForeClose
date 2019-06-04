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

    public MyDefaultCellEditor(ArrayList<ArrayList<CrawData>> tmp) {
		super(new JTextField());
		totalCrawData = tmp;
		
        button = new DetailButton("詳細資料");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//System.out.printf("%d %d\n",button.getRow(),button.getColum());
            	DetailData myDD = new DetailData(totalCrawData.get(button.getRow()));
            }
        });
    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
    	button.setRow(row);
		button.setColumn(column);
        return button;
    }
}

