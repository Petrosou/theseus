/**
 * Soulidis Petros 9971 petrosis@ece.auth.gr
 * Terzidis Alexandros 10072 terzidisa@ece.auth.gr
 */

public class Supply {
	
	private int supplyId;
	private int x;
	private int y;
	private int supplyTileId;
	
	private boolean obtainable;
	
	public Supply() {
		supplyId = 0;
		x = 0;
		y = 0;
		supplyTileId = 0;
		obtainable = false;
	}
	
	public Supply(int supplyId, int x, int y, int supplyTileId) {
		this.supplyId = supplyId;
		this.x = x;
		this.y = y;
		this.supplyTileId = supplyTileId;
		obtainable = true;
	}
	
	public Supply(Supply sup) {
		supplyId = sup.getSupplyId();
		x = sup.getX();
		y = sup.getY();
		supplyTileId = sup.getSupplyTileId();
		obtainable = sup.isObtainable();
	}
	
	public int getSupplyId() {
		return supplyId;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getSupplyTileId() {
		return supplyTileId;
	}
	
	public boolean isObtainable() {
		return obtainable;
	}
	
	public void setSupplyId(int supplyId) {
		this.supplyId = supplyId;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setObtainable(boolean obtainable) {
		this.obtainable = obtainable;
	}
	
	public void setSupplyTileId(int supplyTileId) {
		this.supplyTileId = supplyTileId;
	}

}
