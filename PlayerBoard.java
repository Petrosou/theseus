final class PlayerBoard extends Board{
	//Constructors
	PlayerBoard(){
		super();
	}
	PlayerBoard(int N, int S){
		super(N, S, 0);
		for(int i = 0; i <= N * N - 1; i++)
			tiles[i] = new PlayerTile(i, i/N, i%N, false, false, false, false);
	}
	
	//Special functions
	@Override
	public void empty(){
		for(int i = 0 ; i < (N*N); i++){
			for(int j = 0; j<4; ++j){
				tiles[i].setWallInDirection(2*j+1, false);
			}
			((PlayerTile)tiles[i]).setHaveInfo(false);
		}
		for(int i = 0; i<S; ++i){
			supplies[i].setSupplyId(0);
			supplies[i].setSupplyTileId(0);
			supplies[i].setX(0);
			supplies[i].setY(0);
			supplies[i].setObtainable(false);
		}
		createOutline();
	}
}