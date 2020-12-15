import java.util.ArrayList;

final class GameBoard extends Board {
	//Variables
	private ArrayList<Integer> visitedNodes;
	private int holdsOtherNode;
	private boolean connection;
	private int maxWallsOnTile = 2; //Only the values 2 or 3 are accepted

	//Constructors
	GameBoard(){
		super();
		visitedNodes = new ArrayList<>(0);
	}
	GameBoard(int N, int S, int W){
		super(N, S, W);

		tiles = new Tile[N*N];
		for(int i = 0; i <= N * N - 1; i++)
			tiles[i] = new GameTile(i, i/N, i%N, false, false, false, false);
		visitedNodes = new ArrayList<>(0);
	}

	// Special functions
	@Override
	protected void createOutline() {
		super.createOutline();
		for (int i = 0; i < N; ++i) {
			((GameTile)tiles[i]).setBuildDown(false);
			((GameTile)tiles[i + N * (N - 1)]).setBuildUp(false);
			((GameTile)tiles[i * N]).setBuildLeft(false);
			((GameTile)tiles[i * N + N - 1]).setBuildRight(false);
		}

		if(maxWallsOnTile == 2){			//Can't build in corner tiles
			//Down left
			((GameTile)tiles[0]).setBuildRight(false);
			((GameTile)tiles[0]).setBuildUp(false);
			((GameTile)tiles[1]).setBuildLeft(false);
			((GameTile)tiles[N]).setBuildDown(false);
			//Down right
			((GameTile)tiles[N-1]).setBuildLeft(false);
			((GameTile)tiles[N-1]).setBuildUp(false);
			((GameTile)tiles[N-2]).setBuildRight(false);
			((GameTile)tiles[2*N-1]).setBuildDown(false);
			//Up left
			((GameTile)tiles[N*(N-1)]).setBuildRight(false);
			((GameTile)tiles[N*(N-1)]).setBuildDown(false);
			((GameTile)tiles[(N-1)*(N-1)]).setBuildUp(false);
			((GameTile)tiles[N*(N-1)+1]).setBuildLeft(false);
			//Up right
			((GameTile)tiles[N*N-1]).setBuildLeft(false);
			((GameTile)tiles[N*N-1]).setBuildDown(false);
			((GameTile)tiles[N*(N-1)-1]).setBuildUp(false);
			((GameTile)tiles[N*N-2]).setBuildRight(false);
		}
	}
	public void empty(){
		for(int i = 0 ; i < (N*N); i++){
			for(int j = 0; j<4; ++j){
				tiles[i].setWallInDirection(2*j+1, false);
				((GameTile)tiles[i]).setBuildInDirection(2*j+1, true);
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
	private int nodeNumber(Tile tile, int nodeOfTile){//Down left 0, down right 1...
		switch(nodeOfTile){
			case 0:
				return (N+1)*tile.getX() + tile.getY();
			case 1:
				return (N+1)*tile.getX() + tile.getY() + 1;
			case 2:
				return (N+1)*(tile.getX()+1) + tile.getY() + 1;
			case 3:
				return (N+1)*(tile.getX()+1) + tile.getY();
			default:
				System.out.println("Error in Board->nodeNumber, invalid argument for nodeOfTile: " + nodeOfTile);
				java.lang.System.exit(1);
				return -1;
		}
}
	private boolean wallBetweenNodes(int node1, int node2){
		if(Math.abs(node1-node2) == 1){
			if(node1/(N+1) != node2/(N+1))
				return false;
			if(node1/(N+1) == N){
				return (node1>node2)?tiles[N*(N-1) + node2%(N+1)].getUp():tiles[N*(N-1) + node1%(N+1)].getUp();
			}
			return (node1>node2)?tiles[(node2/(N+1))*N + node2%(N+1)].getDown():tiles[(node1/(N+1))*N + node1%(N+1)].getDown();
		}
		if(Math.abs(node1-node2) == N+1){
			if(node1%(N+1) == N){
				return (node1>node2)?tiles[(node2/(N+1))*N + N-1].getRight():tiles[(node1/(N+1))*N + N-1].getRight();
			}
			return (node1>node2)?tiles[(node2/(N+1))*N + node2%(N+1)].getLeft():tiles[(node1/(N+1))*N + node1%(N+1)].getLeft();
		}
		return false;
	}
	private int[] neighborNodes(int node){
		//Down
		if(node/(N+1) == 0){
			//Down left corner
			if(node%(N+1) == 0){
				return new int[] {1, N+1};
			}
			//Down right corner
			else if(node%(N+1) == N){
				return new int[]{N-1, N+N+1};
			}
			return new int[] {node-1, node+1, node+N+1};
		}
		//Up
		else if(node/(N+1) == N){
			//Up left corner
			if(node%(N+1) == 0){
				return new int[] {N*N-1, N*(N+1)+1};
			}
			//Up right corner
			else if(node%(N+1) == N){
				return new int[] {N*(N+1)-1, (N+1)*(N+1)-2};
			}
			return new int[] {node-(N+1), node-1, node+1};
		}
		//Left
		else if(node%(N+1) == 0){
			return new int[] {node-(N+1), node+1, node+N+1};
		}
		//Right
		else if(node%(N+1) == N){
			return new int[] {node-(N+1), node-1, node+N+1};
		}
		return new int[] {node-(N+1), node-1, node+1, node+N+1};
	}
	private int otherNode(Tile tile, int wall, int nodeOfTile){
		switch(wall){
			case 1:
				return (nodeOfTile == 3)?nodeNumber(tile, 2):nodeNumber(tile, 3);
			case 3:
				return (nodeOfTile == 1)?nodeNumber(tile, 2):nodeNumber(tile, 1);
			case 5:
				return (nodeOfTile == 0)?nodeNumber(tile, 1):nodeNumber(tile, 0);
			case 7:
				return (nodeOfTile == 0)?nodeNumber(tile, 3):nodeNumber(tile, 0);
			default:
				System.out.println("Error in Board->otherNode, invalid argument(s): ("+tile+", "+wall+", "+nodeOfTile+")");
				java.lang.System.exit(1);	
				return -1;	
		}
	}
	ArrayList<Integer> connectionNodes(int[] neighborNodes, int otherNode){
		ArrayList<Integer> cNodes = new ArrayList<>(3);
		for(int i = 0; i<neighborNodes.length; ++i){
			if(otherNode!=neighborNodes[i])
				cNodes.add(neighborNodes[i]);
		}
		return cNodes;
	}
	private void createsClosedLoop(int node, int otherNode){;		
		//All connection nodes
		ArrayList<Integer> cNodes = connectionNodes(neighborNodes(node), otherNode);
		//Non checked connection nodes
		for(int i = 0; i<visitedNodes.size(); ++i){
			for(int j = 0; j<cNodes.size(); ++j){
				if(visitedNodes.get(i).equals(cNodes.get(j)))
					cNodes.remove(j);
			}
		}
		visitedNodes.add(node);
		for(int i = 0; i<cNodes.size(); ++i){
			if(wallBetweenNodes(node, cNodes.get(i))){
				if(cNodes.get(i).equals(holdsOtherNode)){
					//Found otherNode
					connection = true;
					return;
				}
				//Continue searching
				createsClosedLoop(cNodes.get(i), node);
				if(connection)
					return;
			}
		}
		//Did not find otherNode from this node
		visitedNodes.remove(visitedNodes.size()-1);
	}
	private boolean createsLoop(int node, int otherNode){
		//check all possible paths
		//if one becomes closed return true
		holdsOtherNode = otherNode;
		connection = false;
		visitedNodes.clear();
		createsClosedLoop(node, otherNode);
		return connection;
	}
	public void createTile(){
		int counter1, counter2;			//number of walls on a tile
		int check = 0;			//number of inner walls currently built
		int randomIndex, randomDirection;

		createOutline();
		
		//The inner walls
		while (check<W) {
			randomIndex = (int)(Math.random()*tiles.length);
			randomDirection = (int)(Math.random()*2)*2 + 1;
			
			//Check if a wall can be built in this direction
			if(!((GameTile)tiles[randomIndex]).canBuildInDirection(randomDirection)) {
				continue;
			}
			//Creates loop
			if(createsLoop(nodeNumber(tiles[(randomIndex)], 2), otherNode(tiles[randomIndex], randomDirection, 2))){
				((GameTile)tiles[randomIndex]).setBuildInDirection(randomDirection, false);
				continue;
			}
			//Building wall
			int nId = tiles[randomIndex].neighborTileId(randomDirection, N);
			Tile neighbor = tiles[nId];
			tiles[randomIndex].setWallInDirection(randomDirection, true);
			((GameTile)tiles[randomIndex]).setBuildInDirection(randomDirection, false);
			int oppositeDirection = (randomDirection == 1)?(5):(7);
			neighbor.setWallInDirection(oppositeDirection, true);
			((GameTile)neighbor).setBuildInDirection(oppositeDirection, false);
			check++;
			//Check if there is the maximum amount of walls allowed on tiles
			//Count walls on current tile
			counter2 = counter1 = 0;
			for(int i = 1; i<8; i+=2){
				if(i == randomDirection) //We already know that there is a wall there
					continue;
				counter1 = (tiles[randomIndex].getWallInDirection(i))?(counter1 + 1):counter1;
			}

			//Count walls on neighbor tile
			int adjacentTileId;
			for(int i = 1; i<8; i+=2){
				if(i == randomDirection + 4) //We already know that there is a wall there
					continue;
				counter2 = (neighbor.getWallInDirection(i))?(counter2 + 1):counter2;
			}
			if(counter1 == maxWallsOnTile - 1){
				for(int i = 1; i<8; i+=2){
					oppositeDirection = (i == 1)?(5):(i == 5)?(1):(i == 3)?(7):(3);
					((GameTile)tiles[randomIndex]).setBuildInDirection(i, false);
					adjacentTileId = tiles[randomIndex].neighborTileId(i, N);
					if(adjacentTileId>-1 && adjacentTileId<tiles.length)
						((GameTile)tiles[adjacentTileId]).setBuildInDirection(oppositeDirection, false);
				}
			}
			if(counter2 == maxWallsOnTile - 1){
				for(int i = 1; i<8; i+=2){
					oppositeDirection = (i == 1)?(5):(i == 5)?(1):(i == 3)?(7):(3);
					((GameTile)tiles[nId]).setBuildInDirection(i, false);
					adjacentTileId = tiles[nId].neighborTileId(i, N);
					if(adjacentTileId>-1 && adjacentTileId<tiles.length)
						((GameTile)tiles[adjacentTileId]).setBuildInDirection(oppositeDirection, false);
				}
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
}