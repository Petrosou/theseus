import java.util.ArrayList;
class HeuristicPlayer extends Player{
    //Variables
    protected ArrayList<Integer[]> path;      //player moves' description [int die, int pickedSupply, int blocksToSupply, int blocksToOpponent, tileId, opponentTileId]
    protected int ability;
    protected int wallAbility;
    protected double revisitPenalty = 0.01;
    protected double a = 0.01;
    protected PlayerBoard playerMap;

    //Constuctors
    HeuristicPlayer(){
        super();
        path = new ArrayList<>(0);
        ability = 3;
        wallAbility = 0;
        playerMap = new PlayerBoard();
    }
    HeuristicPlayer(int playerId, String name, int x, int y, int N, int S, ArrayList<Integer[]> path, int ability, int wallAbility){
        super(playerId, name, x, y);
        this.path = path;
        this.ability = ability;
        this.wallAbility = wallAbility;

        playerMap = new PlayerBoard(N, S);
        for(int i = 0; i<playerMap.getS(); ++i)
        	playerMap.getSupplies()[i] = new Supply();
    }

    //Getters setters
    public void setAbility(int ability){
        this.ability = ability;
    }

    public int getAbility(){
        return ability;
    }

    public void setWallAbility(int wallAbility){
        this.wallAbility = wallAbility;
    }

    public int getWallAbility(){
        return wallAbility;
    }

    public void erasePath(){
        path.clear();
    }
    
    public void setA(double a){
        this.a = a;
    }
    
    public double getA(){
        return a;
    }

    public Board getPlayerMap(){
        return playerMap;
    }

    //Special functions
    protected int[] seeAround(Board board, int opponentPos, int die){
        int blocksToOpponent = Integer.MAX_VALUE, blocksToSupply = Integer.MAX_VALUE, blocksToWall = -1;
        int nId = x*board.getN() + y;
        for(int i = 0; i<ability; ++i){
            //Interfering wall
            if(board.getTiles()[nId].getWallInDirection(die)) {
                playerMap.tiles[nId].setWallInDirection(die, true);
                break;
            }
            nId = board.getTiles()[nId].neighborTileId(die, board.getN());
            //Opponent
            if(opponentPos == nId){
                blocksToOpponent = i + 1;
            }

            //Supply
            boolean foundSupply = false;
            for(int j = 0;  j<board.getS(); ++j) {
            	if(nId == board.getSupplies()[j].getSupplyTileId()) {
            		foundSupply = true;
            		playerMap.getSupplies()[j].setSupplyId(j+1);
            		playerMap.getSupplies()[j].setX(board.getTiles()[nId].getX());
            		playerMap.getSupplies()[j].setY(board.getTiles()[nId].getY());
            		playerMap.getSupplies()[j].setSupplyTileId(nId);
            		if(board.getSupplies()[j].isObtainable() ) {
            			playerMap.getSupplies()[j].setObtainable(true);
		                if(blocksToSupply == Integer.MAX_VALUE){
		                    blocksToSupply = i + 1;
		                }
            		}
            	}
            }
            if(!foundSupply) {
        		((PlayerTile)playerMap.getTiles()[nId]).setHasSupply(false);
        	}
             
            //Enough data collected
            if(blocksToOpponent != Integer.MAX_VALUE && blocksToSupply != Integer.MAX_VALUE)
                break;
        }
      //BlocksToWall
        nId = x*board.getN() + y;
        for(int i = 0; i<=wallAbility; ++i){
            //Interfering wall
            if(board.getTiles()[nId].getWallInDirection(die)) {
                blocksToWall = i;
                break;
            }
            nId = board.getTiles()[nId].neighborTileId(die, board.getN());
        }
        
        //Find the tileId of the tile with wall
        nId = x*board.getN() + y;
        for(int i = 0; i<blocksToWall; ++i) {
        	nId = board.getTiles()[nId].neighborTileId(die, board.getN());
        }
        
        if(blocksToWall != -1){
            playerMap.getTiles()[nId].setWallInDirection(die, true);
            int nnId = playerMap.getTiles()[nId].neighborTileId(die, playerMap.getN());
            if(0<nnId && nnId<playerMap.getN()*playerMap.getN()-2) {
            	int oppositeDie = (die == 1)?(5):(die == 5)?(1):(die == 3)?(7):(3);
            	playerMap.getTiles()[nnId].setWallInDirection(oppositeDie, true);
            }
        }


        int[] tempArray = {blocksToSupply, blocksToOpponent, blocksToWall};
        return tempArray;
    }
    
    public double evaluate(Board board, int opponentPos, int die){
        int[] observation = seeAround(board, opponentPos, die);
        int blocksToClosestSupply = Integer.MAX_VALUE;
        int blocksToOpponent = observation[1];
        int blocksToWall = observation[2];
        double penalty = 0;

        //Approach supplies
        if(!board.getTiles()[x*board.getN() + y].getWallInDirection(die)){
            int neighborTileId = board.getTiles()[x*board.getN() + y].neighborTileId(die, board.getN());
            Tile neighbor = board.getTiles()[neighborTileId];
            for(int i = 0; i<playerMap.getS(); ++i){
            	if(playerMap.getSupplies()[i].getSupplyTileId() == 0)
            		continue;
            	Tile supplyTile = playerMap.getTiles()[playerMap.getSupplies()[i].getSupplyTileId()];
            	if(board.getSupplies()[i].isObtainable()) {
            		if(blocksToClosestSupply>neighbor.distance(supplyTile) + 1)
            			blocksToClosestSupply = neighbor.distance(supplyTile) + 1;
            	}
            }
        }
        
         
/*        if(!board.getTiles()[x*board.getN() + y].getWallInDirection(die)){
            int neighborTileId = board.getTiles()[x*board.getN() + y].neighborTileId(die, board.getN());
            Tile neighbor = board.getTiles()[neighborTileId];
            for(int i = 0; i<playerMap.getTiles().length; ++i){
            	if(((PlayerTile)playerMap.getTiles()[i]).haveInfo())
            		continue;       
            penalty+=0.001/(neighbor.distance(playerMap.getTiles()[i])+1);
            }
        }*/

        if(name.equals("Theseus")){
            //Avoid revisiting a tile
            for(int i = 0; i<path.size(); ++i){
                if(path.get(i)[4] == board.getTiles()[x*board.getN() + y].neighborTileId(die, board.getN())){
                    penalty+=revisitPenalty;
                }
            }
            ////Special case MS
            if(blocksToOpponent == 1 && blocksToClosestSupply == 1){
                if(score == board.getS() - 1){
                    return Double.POSITIVE_INFINITY;
                }
                return Double.NEGATIVE_INFINITY;
            }
            //Minotaur is one block away
            if(blocksToOpponent == 1 && wallAbility != -1)
                if(blocksToWall == 0)
                    return Double.NEGATIVE_INFINITY;
            //Case losing turn
            if(wallAbility != -1 && blocksToWall == 0)
                    return -10;
            //Minotaur is two blocks away
            if(blocksToOpponent == 2){
                if(score == board.getS() - 1 && blocksToClosestSupply == 1)
                    return Double.POSITIVE_INFINITY;
                else
                    return Double.NEGATIVE_INFINITY;
            }
            //General case
            return 0.5/(blocksToClosestSupply - 1) - 1.0/(blocksToOpponent - 1)-penalty;
        }
        //This is only for Minotaur

        //avoid back and forth movements
        if(!path.isEmpty()){
            if((path.get(path.size()-1)[0]%4 == die%4) && (path.get(path.size()-1)[0] != die)){
                penalty+=a;
            }
        }
        //Case losing turn
        if(wallAbility != -1 && blocksToWall == 0)
            return -10;
        //General case
        return 0.5/(blocksToClosestSupply) + 1.0/(blocksToOpponent - 1)-penalty;  //there's not -1 so bloscksToOpponent is more important
    }

    //returns the move that has the greatest value
    public int getNextMove(RestrictedGameBoard board, int opponentPos){
        double[] movesValues = new double[4];
        int randomDirection = 1 + 2 * ((int) (Math.random() * 10) % 4);
        double maxValue = movesValues[randomDirection/2] = evaluate(board, opponentPos, randomDirection);
        int maxValueDie = randomDirection;
        for(int i = 0; i<4; ++i){
            if(2*i+1 == randomDirection)
                continue;
            if(maxValue < (movesValues[i] = evaluate(board, opponentPos, 2*i + 1))){
                maxValue = movesValues[i];
                maxValueDie = 2*i + 1;
            }
        }

        int[] observation = seeAround(board, opponentPos, maxValueDie);
        Integer[] tempArray = {maxValueDie, 0, observation[0] - 1, observation[1] - 1, board.getN()*x+y, opponentPos};
        if(name.equals("Theseus") && maxValue == Double.POSITIVE_INFINITY){
                tempArray[1] = 1;
                //Set obtainable false
                for(int i = 0; i<playerMap.getS(); ++i){
                    if(playerMap.getSupplies()[i].getSupplyTileId() == board.getTiles()[board.getN()*x+y].neighborTileId(maxValueDie, board.getN())){
                    	playerMap.getSupplies()[i].setObtainable(false);;
                        break;
                    }
                }
        }
        path.add(tempArray);
        return maxValueDie;
    }

    public void statistics(){
        System.out.println("\nStatistics of " + name + ":");
        int ups, rights, downs, lefts, currentRound;
        ups = rights = downs = lefts = 0;
        for(int i = 0; i<path.size(); ++i){
            currentRound = i + 1;
            switch(path.get(i)[0]) {
                case 1://case UP
                    System.out.println(name + " moved up in round " + currentRound + ".");
                    ++ups;
                    break;
                case 3://case RIGHT
                    System.out.println(name + " moved right in round " + currentRound + ".");
                ++rights;
                    break;
                case 5://Case DOWN
                    System.out.println(name + " moved down in round " + currentRound + ".");
                ++downs;
                    break;
                case 7://Case LEFT
                    System.out.println(name + " moved left in round " + currentRound + ".");
                ++lefts;
                    break;
                default:
                    System.out.println("Some unexpected error happened in HeuristicPlayer-> void statistics()-> switch(path.get(i)[0])");
                    java.lang.System.exit(1);
            }

            int blocksToSupply = path.get(i)[2];
            if(blocksToSupply == 0){
                if(name.equals("Theseus"))
                    System.out.println("Theseus picked up a supply");
                else
                    System.out.println("Minotaur guards a supply");
            }
            else if(blocksToSupply == 1)
                System.out.println(name + " was " + blocksToSupply + " block away from a supply.");
            else if(blocksToSupply <= ability)
                System.out.println(name + " was " + blocksToSupply + " blocks away from a supply.");
            else
                System.out.println("Supplies were not visible.");
            
            System.out.println();
        }
        System.out.println(name + " tried to moved up a total of " + ups + " times.");
        System.out.println(name + " tried to moved right a total of " + rights + " times.");
        System.out.println(name + " tried to moved down a total of " + downs + " times.");
        System.out.println(name + " tried to moved left a total of " + lefts + " times.");

    }

    public int[] move(RestrictedGameBoard board, int opponentPos) {
		int[] details = new int[4];
        details[3] = -1;
        int die = getNextMove(board, opponentPos);
		switch(die) {
		case 1://case UP
			System.out.println(name + " rolled UP.");		
			break;
		case 3://case RIGHT
			System.out.println(name + " rolled RIGHT.");
			break;
		case 5://Case DOWN
			System.out.println(name + " rolled DOWN.");
			break;
		case 7://Case LEFT
			System.out.println(name + " rolled LEFT.");
			break;
		}
		
		//Valid move check
		//Invalid
		if(board.getTiles()[board.getN()*x+y].getWallInDirection(die)) {
			System.out.println(name + " cannot move that way.");
			details[0] = board.getN()*x+y;
			details[1] = board.getTiles()[board.getN()*x+y].getX();
			details[2] = board.getTiles()[board.getN()*x+y].getY();
		}
		//Valid
		else {
			details[0] = board.getTiles()[board.getN()*x+y].neighborTileId(die, board.getN());
			details[1] = details[0]/board.getN();
			details[2] = details[0]%board.getN();
			setX(details[1]);
			setY(details[2]);
		}
        
		//Theseus collected supply check
		if(name.equals("Theseus")) {
			for(int i = 0 ; i < board.getS() ; i++) {
				if((details[0] == board.getSupplies()[i].getSupplyTileId())&&(board.getSupplies()[i].isObtainable())) {
                    System.out.println(name + " picked up supply " + board.getSupplies()[i].getSupplyId() + ".");
					details[3] = i;
					board.getSupplies()[i].setObtainable(false);
					break;
				}
			}
		}
		return details;
	}
}