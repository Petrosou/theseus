final class GameTile extends Tile{
	//Variables
	private boolean buildUp;
	private boolean buildDown;
	private boolean buildLeft;
	private boolean buildRight;
	
	//Constructors
	GameTile(){
		super();
		buildUp = true;
		buildRight = true;
		buildDown = true;
		buildLeft = true;
	}
	GameTile(int tileId, int x, int y, boolean up, boolean down, boolean left, boolean right){
		super(tileId, x, y, up, down, left, right);
		buildUp = true;
		buildRight = true;
		buildDown = true;
		buildLeft = true;
	}
	GameTile(GameTile tile){
		super(tile);
		buildUp = tile.buildUp;
		buildRight = tile.buildRight;
		buildDown = tile.buildDown;
		buildLeft = tile.buildLeft;
	}
	
	//Getters-setters
	public boolean canBuildUp() {
		return buildUp;
	}
	public void setBuildUp(boolean buildUp) {
		this.buildUp = buildUp;
	}
	public boolean canBuildDown() {
		return buildDown;
	}
	public void setBuildDown(boolean buildDown) {
		this.buildDown = buildDown;
	}
	public boolean canBuildLeft() {
		return buildLeft;
	}
	public void setBuildLeft(boolean buildLeft) {
		this.buildLeft = buildLeft;
	}
	public boolean canBuildRight() {
		return buildRight;
	}
	public void setBuildRight(boolean buildRight) {
		this.buildRight = buildRight;
	}
	public boolean canBuildInDirection(int die){
    	switch(die) {
		case 1:
			return buildUp;
		case 3:
			return buildRight;
		case 5:
			return buildDown;
		case 7:
			return buildLeft;
		default:
			System.out.println("Invalid argument for die: " + die);
			return false;
		}
	}
	public void setBuildInDirection(int die, boolean set){
	switch(die){
		case 1:
			setBuildUp(set);
			break;
		case 3:
			setBuildRight(set);
			break;
		case 5:
			setBuildDown(set);
			break;
		case 7:
			setBuildLeft(set);
	}
}	
}
