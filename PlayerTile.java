final class PlayerTile extends Tile{
	//Variables
	private boolean hasSupply;

	//Constructors
	PlayerTile(){
		super();
		hasSupply = true;
	}
	PlayerTile(int tileId, int x, int y, boolean up, boolean down, boolean left, boolean right){
		super(tileId, x, y, up, down, left, right);
		hasSupply = true;
	}
	PlayerTile(PlayerTile tile){
		super(tile);
		hasSupply = tile.hasSupply();
	}
	
	//Gettets-setters
	public boolean hasSupply() {
		return hasSupply;
	}
	public void setHasSupply(boolean hasSupply) {
		this.hasSupply = hasSupply;
	}
}