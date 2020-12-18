final class PlayerTile extends Tile{
	//Variables
	private boolean haveInfo;

	//Constructors
	PlayerTile(){
		super();
		haveInfo = false;
	}
	PlayerTile(int tileId, int x, int y, boolean up, boolean down, boolean left, boolean right){
		super(tileId, x, y, up, down, left, right);
		haveInfo = false;
	}
	PlayerTile(PlayerTile tile){
		super(tile);
		haveInfo = tile.haveInfo();
	}
	
	//Gettets-setters
	public boolean haveInfo() {
		return haveInfo;
	}
	public void setHaveInfo(boolean haveInfo) {
		this.haveInfo = haveInfo;
	}
}