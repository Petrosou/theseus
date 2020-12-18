/**
 * Σουλίδης Πέτρος 9971 petrosis@ece.auth.gr Τερζίδης Αλέξανδρος 10072
 * terzidisa@ece.auth.gr
 */
abstract class Board {
	//variables
	protected int N;		//number of lines-rows
	protected int S;		//number of supplies			
	protected int W;		//number of walls
	protected Tile[] tiles;
	protected Supply[] supplies;		//the supplies of the labyrinth

	//c-tors
	Board(){
		N = 0;
		S = 0;
		W = 0;
	}	
	Board(int N, int S, int W){
		this.N = N;
		this.S = S;
		this.W = W;

		tiles = new Tile[N*N];

		supplies = new Supply[S];
		for(int i = 0; i<S; ++i)
			supplies[i] = new Supply();
	}
	
	//getters	
	public int getN(){
		return N;
	}
	public int getS(){
		return S;
	}
	public int getW(){
		return W;
	}
	public Supply[] getSupplies(){
		return supplies;
	}
	public Tile[] getTiles(){
		return tiles;
	}
	//setters
	public void setN(int N){
		this.N = N;
	}
	public void setS(int S){
		this.S = S;
	}
	public void setW(int W){
		this.W = W;
	}
	public void setSupplies(Supply[] supplies){
		this.supplies = supplies;
	}
	public void setTiles(Tile[] tiles){
		this.tiles = tiles;
	}
	
	//Special functions
	protected void createOutline() {
		for (int i = 0; i < N; ++i) {
			tiles[i].setDown(true);
			tiles[i + N * (N - 1)].setUp(true);
			tiles[i * N].setLeft(true);
			tiles[i * N + N - 1].setRight(true);
		}
	}
	public void empty(){
		for(int i = 0 ; i < (N*N); i++){
			for(int j = 0; j<4; ++j){
				tiles[i].setWallInDirection(2*j+1, false);
			}
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
	public String[][] getStringRepresentation(int theseusTile, int minotaurTile){
		String[][] theBoard = new String[2 * N + 1][N];
		int line = 0;
		int tempSupplyId = 0;
		for(int x = N - 1 ; x >= 0 ; x--) {
			for(int y = 0 ; y <= N - 1 ; y++) {
				for(int i = 0;  i< S ; i++) {
					if(N * x + y == supplies[i].getSupplyTileId() && supplies[i].isObtainable()) {
						tempSupplyId = supplies[i].getSupplyId();
					}
				}
				
				if(tiles[N * x + y].getUp()) {
					theBoard[line][y] = "+---";
				}
				else {
					theBoard[line][y] = "+   ";
				}
				if(y == N-1) {
					theBoard[line][y] = theBoard[line][y] + "+\n";
				}
				line++;
				
				if(tiles[ N * x + y].getLeft()) {
					theBoard[line][y] = "|";
				}
				else {
					theBoard[line][y] = " ";
				}
				
				if(tiles[N * x + y].getTileId() == theseusTile) {
					theBoard[line][y] = theBoard[line][y] + " T ";
				}
				else if(tempSupplyId != 0 && tiles[N * x + y].getTileId() == minotaurTile) {
					theBoard[line][y] = theBoard[line][y] + "Ms" + tempSupplyId;
				}
				else if(tempSupplyId == 0 && tiles[N * x + y].getTileId() == minotaurTile) {
					theBoard[line][y] = theBoard[line][y] + " M ";
				}
				else if(tempSupplyId != 0 && tiles[N * x + y].getTileId() != theseusTile) {
					theBoard[line][y] = theBoard[line][y] + "s" + tempSupplyId + " ";
				}
				else if(tempSupplyId == 0){
					theBoard[line][y] = theBoard[line][y] + "   ";
				}
				
				tempSupplyId = 0;
				if(y == N - 1) {
					theBoard[line][y] = theBoard[line][y] + "|\n";
				}
				line--;
			}
			line = line + 2;
		}
		theBoard[2*N][0] = "+   ";
		for(int i = 1; i <= N - 1 ; i++) {
			theBoard[2 * N][i] = "+---";
		}
		theBoard[2 * N][N - 1] = theBoard[2 * N][N - 1] + "+";
		return theBoard;
	}
}