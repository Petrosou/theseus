/**
 * Soulidis Petros 9971 petrosis@ece.auth.gr
 * Terzidis Alexandros 10072 terzidisa@ece.auth.gr
 */

public class Tile {
	private int tileId;
	private int x;
	private int y;
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	private boolean haveInfo;
	public Tile() {
		tileId = 0;
		x = 0;
		y = 0;
		up = false;
		down = false;
		left = false;
		right = false;
		haveInfo = true;
	}
	
	public Tile(int tileId, int x, int y, boolean up, boolean down, boolean left, boolean right) {
		this.tileId = tileId;
		this.x = x;
		this.y = y;
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
		haveInfo = true;
	}
	
	public Tile(Tile theTile) {
		tileId = theTile.getTileId();
		x = theTile.getX();
		y = theTile.getY();
		up = theTile.getUp();
		down = theTile.getDown();
		left = theTile.getLeft();
		right = theTile.getRight();
	}
	
	public int getTileId() {
		return tileId;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean getUp() {
		return up;
	}	
	
	public boolean getDown() {
		return down;
	}
	
	public boolean getLeft() {
		return left;
	}
	
	
	public boolean getRight() {
		return right;
	}
	
	public void setTileId(int tileId) {
		this.tileId = tileId;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setUp(boolean up) {
		this.up = up;
	}
	
	public void setDown(boolean down) {
		this.down = down;
	}
	
	public void setLeft(boolean left) {
		this.left = left;
	}
	
	public void setRight(boolean right) {
		this.right = right;
	}
	
	public boolean haveInfo() {
		return haveInfo;
	}

	public void setHaveInfo(boolean haveInfo) {
		this.haveInfo = haveInfo;
	}

	//Special functions
    public int neighborTileId(int die, int N) {
    	switch(die) {
    		case 1:
    			return tileId + N;
    		case 3:
    			return tileId + 1;
    		case 5:
    			return tileId - N;
    		case 7:
    			return tileId - 1;
    		default:
    			System.out.println("Invalid argument for die: " + die);
    			return -1;
    	}
    }
    
    public boolean getWallInDirection(int die){
    	switch(die) {
		case 1:
			return up;
		case 3:
			return right;
		case 5:
			return down;
		case 7:
			return left;
		default:
			System.out.println("Invalid argument for die: " + die);
			return false;
		}
	}

public void setWallInDirection(int die, boolean set){
	switch(die){
		case 1:
			setUp(set);
			break;
		case 3:
			setRight(set);
			break;
		case 5:
			setDown(set);
			break;
		case 7:
			setLeft(set);
	}
}

	public int distance(Tile tile){
		return Math.abs(x-tile.x) + Math.abs(y-tile.y);
	}
}