final class PlayerTile extends Tile{
	//Variables
	private boolean haveInfo;

	//Constructors
	PlayerTile(){
		super();
		haveInfo = true;
	}
	PlayerTile(int tileId, int x, int y, boolean up, boolean down, boolean left, boolean right){
		super(tileId, x, y, up, down, left, right);
		haveInfo = true;
	}
	PlayerTile(PlayerTile tile){
		super(tile);
		haveInfo = tile.haveInfo();
	}
	
	//Gettets-setters
	public boolean haveInfo() {
		return haveInfo;
	}
	public void setHasSupply(boolean haveInfo) {
		this.haveInfo = haveInfo;
	}
}