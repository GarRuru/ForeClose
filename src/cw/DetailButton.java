package cw;

import javax.swing.JButton;

public class DetailButton extends JButton{
	private int row;
	private int column;
	
	public DetailButton() {
		super();
	}
	
	public DetailButton(String name) {
		super(name);
	}
	
	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
	
	
}
