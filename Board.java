/**
�������� ������ 9971 petrosis@ece.auth.gr
�������� ���������� 10072 terzidisa@ece.auth.gr
*/

public class Board {
	//variables
	private int N;		//number of lines-rows
	private int S;		//number of supplies			
	private int W;		//number of walls
	private Tile[] tiles;		//the labyrinth in Tile form
	private Supply[] supplies;		//the supplies of the labyrinth
	
	//c-tors
	public Board(){
		N = 0;
		S = 0;
		W = 0;
		tiles = new Tile[0];
		supplies = new Supply[0];
	}	
	
	public Board(int N, int S, int W){
		this.N = N;
		this.S = S;
		this.W = W;
		
		tiles = new Tile[N*N];
		for(int i = 0; i <= N * N - 1; i++)
			tiles[i] = new Tile(i, i/N, i%N, false, false, false, false);
		
		supplies = new Supply[S];
	}
	
	public Board(Board theBoard){
		N = theBoard.getN();
		S = theBoard.getS();
		W = theBoard.getW();
		tiles = theBoard.getTiles();
		supplies = theBoard.getSupplies();
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
	
	public Tile[] getTiles(){
		return tiles;
	}
	
	public Supply[] getSupplies(){
		return supplies;
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
	
	public void setTiles(Tile[] tiles){
		this.tiles = tiles;
	}
	
	public void setSupplies(Supply[] supplies){
		this.supplies = supplies;
	}
	

	//special functions
	public void createTile(){
		int counter;			//number of walls on a tile
		int check = 0;			//number of inner walls currently built
		int randomTileId, randomDirection;
		//initializer
		
		//The outline
		for(int i = 0; i <= N * N - 1; i++) {
			if(i / N == 0) {
				tiles[i].setDown(true);
			}
			else if(i / N == N - 1) {
				tiles[i].setUp(true);
			}
			
			if(i % N == 0) {
				tiles[i].setLeft(true);
			}
			else if(i % N == N - 1) {
				tiles[i].setRight(true);
			}
		}
		
		//The inner walls
		while (check<W) {
			counter = 0;
			randomTileId = 1 + (int)(Math.random()*(N*N-2)); //random index between 1 and (N^2)-2
			randomDirection = (int)(Math.random()*4);
			switch(randomDirection) {
			case 0: //case UP
				if(tiles[randomTileId].getUp()) {
					break;
				}
				//Current tile
				counter = (tiles[randomTileId].getLeft())?(counter + 1):counter;
				counter = (tiles[randomTileId].getDown())?(counter + 1):counter;
				counter = (tiles[randomTileId].getRight())?(counter + 1):counter;
				if(counter >= 2) {
					break;
				}
				//Neighbor tile
				counter = (tiles[randomTileId + N].getLeft())?(counter + 1):counter;
				counter = (tiles[randomTileId + N].getUp())?(counter + 1):counter;
				counter = (tiles[randomTileId + N].getRight())?(counter +1 ):counter;
				if(counter >= 2) {
					break;
				}
				tiles[randomTileId].setUp(true);
				tiles[randomTileId + N].setDown(true);
				check++;
				break;
			case 1: //case RIGHT
				if((tiles[randomTileId].getRight())) {
					break;
				}
				
				//Current tile
				counter = (tiles[randomTileId].getUp())?(counter + 1):counter;
				counter = (tiles[randomTileId].getDown())?(counter + 1):counter;
				counter = (tiles[randomTileId].getLeft())?(counter + 1):counter;
				if(counter >= 2) {
					break;
				}
				
				//Neighbor tile
				counter = (tiles[randomTileId+1].getUp())?(counter + 1):counter;
				counter = (tiles[randomTileId+1].getDown())?(counter + 1):counter;
				counter = (tiles[randomTileId+1].getRight())?(counter + 1):counter;
				if(counter >= 2) {
					break;
				}
				tiles[randomTileId].setRight(true);
				tiles[randomTileId + 1].setLeft(true);
				check++;
				break;
			case 2: //case DOWN
				if(tiles[randomTileId].getDown()) {
					break;
				}
				
				//Current tile
				counter = (tiles[randomTileId].getUp())?(counter + 1):counter;
				counter = (tiles[randomTileId].getRight())?(counter + 1):counter;
				counter = (tiles[randomTileId].getLeft())?(counter + 1):counter;
				if(counter >= 2) {
					break;
				}
				
				//Neighbor tile
				counter = (tiles[randomTileId - N].getRight())?(counter + 1):counter;
				counter = (tiles[randomTileId - N].getDown())?(counter + 1):counter;
				counter = (tiles[randomTileId - N].getLeft())?(counter + 1):counter;
				if(counter >= 2) {
					break;
				}
				tiles[randomTileId].setDown(true);
				tiles[randomTileId - N].setUp(true);
				check++;
				break;
			case 3: //case LEFT
				if(tiles[randomTileId].getLeft()) {
					break;
				}
				
				//Current tile
				counter = (tiles[randomTileId].getUp())?(counter + 1):counter;
				counter = (tiles[randomTileId].getRight())?(counter + 1):counter;
				counter = (tiles[randomTileId].getDown())?(counter + 1):counter;
				if(counter >= 2) {
					break;
				}
				
				//Neighbor tile
				counter = (tiles[randomTileId - 1].getUp())?(counter + 1):counter;
				counter = (tiles[randomTileId - 1].getLeft())?(counter + 1):counter;
				counter = (tiles[randomTileId - 1].getDown())?(counter + 1):counter;
				if(counter >=2 ) {
					break;
				}
				tiles[randomTileId].setLeft(true);
				tiles[randomTileId - 1].setRight(true);
				check++;
				break;
			}
		}
	}
	
	public void createSupply() {
		int randomTileId;
		int[] tileIds = new int[S];
		for(int i = 0; i < S; ++i){
			tileIds[i] = -1;
		}
		
		for(int i = 0; i < S;) {
			randomTileId = 1 + (int)(Math.random() * (N * N - 1));
			for(int j = 0; j < i; j++) {
				if(randomTileId == tileIds[j]) {
					randomTileId = N * N / 2;
					break;
				}
			}

			if(randomTileId == N * N / 2){	//minotaurTile
				continue;
			}

			supplies[i] = new Supply(i + 1, tiles[randomTileId].getX(), tiles[randomTileId].getY(), randomTileId);
			supplies[i].setObtainable(true);
			tileIds[i] = randomTileId;
			i++;
		}
	}
	
	public void createBoard() {
		createTile();
		createSupply();
	}

	public void empty(){
		for(int i = 0 ; i < (N*N); i++){
			tiles[i].setRight(false);
			tiles[i].setUp(false);
			tiles[i].setLeft(false);
			tiles[i].setDown(false);
		}
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