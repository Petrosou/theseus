final class PlayerBoard extends Board{
	//Constructors
	PlayerBoard(){
		super();
	}
	PlayerBoard(int N, int S){
		super(N, S, 0);
		for(int i = 0; i <= N * N - 1; i++)
			tiles[i] = new PlayerTile(i, i/N, i%N, false, false, false, false);
		createOutline();
	}
	PlayerBoard(PlayerBoard board){
		super(board.N, board.S, board.W);
		this.N = board.N;
		this.W = board.W;
		this.S = board.S;

		for(int i = 0; i <= N * N - 1; i++){
			tiles[i] = new PlayerTile((PlayerTile)board.tiles[i]);
		}
		for(int i = 0; i<S; ++i){
			supplies[i] = new Supply(board.supplies[i]);
		}
	}

	//Special functions
	public void empty(){
		for(int i = 0 ; i < (N*N); i++){
			for(int j = 0; j<4; ++j){
				tiles[i].setWallInDirection(2*j+1, false);
			}
			((PlayerTile)tiles[i]).setHasSupply(true);
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