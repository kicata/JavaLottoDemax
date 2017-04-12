package eurobetKrystoslovicaCheck;

public class Position {
	private  int row;
	private int col;
	private String letter;
	private boolean isVisited;
	
	public Position(int row, int col, String letter, boolean isVisited) {
		super();
		this.row = row;
		this.col = col;
		this.letter = letter;
		this.isVisited = isVisited;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public boolean isVisited() {
		return isVisited;
	}

	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}
	

}
