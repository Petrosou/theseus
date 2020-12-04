import java.util.ArrayList;
class HeuristicPlayer extends Player{
    private ArrayList<Integer[]> path;      //player moves' description [int die, int pickedSupply, int blocksToSupply, int blocksToOpponent]
    private int ability;

    HeuristicPlayer(){
        super();
        path = new ArrayList<>(0);
        ability = 3;
    }

    HeuristicPlayer(int playerId, String name, Board board, int score, int x, int y, ArrayList<Integer[]> path, int ability){
        super(playerId, name, board, score, x, y);
        this.path = path;
        this.ability = ability;
    }

    public void setAbility(int ability){
        this.ability = ability;
    }

    public int getAbility(){
        return ability;
    }

    private int[] seeAround(int currentPos, int opponentPos, int die){
        int blocksToOpponent = Integer.MAX_VALUE, blocksToSupply = Integer.MAX_VALUE;
        switch(die) {
            case 1: //case UP
                for(int i = 0; i<ability; ++i){
                    //Interfering wall
                    if(board.getTiles()[currentPos + i*board.getN()].getUp()) {
                        break;
                    }
                    
                    //Opponent
                    if(opponentPos == currentPos + (i+1)*board.getN()){
                        blocksToOpponent = i + 1;
                    }

                    //Supply
                    for(int j = 0;  j< board.getS(); ++j) 
                        if(currentPos + (i + 1)*board.getN() == board.getSupplies()[j].getSupplyTileId() && board.getSupplies()[j].isObtainable() && blocksToSupply == Integer.MAX_VALUE)
                            blocksToSupply = i + 1;

                    //Enough data collected
                    if(blocksToOpponent != Integer.MAX_VALUE && blocksToSupply != Integer.MAX_VALUE)
                        break;
                }
                break;
            case 3: //case RIGHT
                for(int i = 0; i<ability; ++i){
                    //Interfering wall
                    if(board.getTiles()[currentPos + i].getRight()) {
                        break;
                    }
                    
                    //Opponent
                    if(opponentPos == currentPos + i + 1){
                        blocksToOpponent = i + 1;
                    }

                    //Supply
                    for(int j = 0;  j< board.getS(); ++j) 
                        if(currentPos + i + 1 == board.getSupplies()[j].getSupplyTileId() && board.getSupplies()[j].isObtainable() && blocksToSupply == Integer.MAX_VALUE)
                            blocksToSupply = i + 1;

                    //Enough data collected
                    if(blocksToOpponent != Integer.MAX_VALUE && blocksToSupply != Integer.MAX_VALUE)
                        break;
                }
                break;
            case 5: //case DOWN
                for(int i = 0; i<ability; ++i){
                    //Interfering wall
                    if(board.getTiles()[currentPos - i*board.getN()].getDown()) {
                        break;
                    }
                    
                    //Opponent
                    if(opponentPos == currentPos - (i+1)*board.getN()){
                        blocksToOpponent = i+1;
                    }

                    //Supply
                    for(int j = 0;  j<board.getS(); ++j) 
                        if(currentPos - (i+1)*board.getN() == board.getSupplies()[j].getSupplyTileId() && board.getSupplies()[j].isObtainable() && blocksToSupply == Integer.MAX_VALUE)
                            blocksToSupply = i+1;

                    //Enough data collected
                    if(blocksToOpponent != Integer.MAX_VALUE && blocksToSupply != Integer.MAX_VALUE)
                        break;
                }
                break;
            case 7: //case LEFT
                for(int i = 0; i<ability; ++i){
                    //Interfering wall
                    if(board.getTiles()[currentPos - i].getLeft()) {
                        break;
                    }
                    
                    //Opponent
                    if(opponentPos == currentPos - i - 1){
                        blocksToOpponent = i + 1;
                    }

                    //Supply
                    for(int j = 0;  j<board.getS(); ++j) 
                        if(currentPos - i - 1== board.getSupplies()[j].getSupplyTileId() && board.getSupplies()[j].isObtainable() && blocksToSupply == Integer.MAX_VALUE)
                            blocksToSupply = i + 1;

                    //Enough data collected
                    if(blocksToOpponent != Integer.MAX_VALUE && blocksToSupply != Integer.MAX_VALUE)
                        break;
                }
                break;
            default:
                System.out.println(die + " Some unexpected error in seeAround().");
                java.lang.System.exit(1);
        }
        int[] tempArray = {blocksToSupply, blocksToOpponent};
        return tempArray;
    }
    
    public double evaluate(int currentPos, int opponentPos, int die){
        int[] observation = seeAround(currentPos, opponentPos, die);
        int blocksToSupply = observation[0];
        int blocksToOpponent = observation[1];
        //Special case MS
        if(name.equals("Theseus") && blocksToOpponent == 1 && blocksToSupply == 1){
            if(score == board.getS() - 1){
                return Double.POSITIVE_INFINITY;
            }
            return Double.NEGATIVE_INFINITY;
        }

        //Minotaur is two blocks away
        if(name.equals("Theseus") && blocksToOpponent == 2){
            if(score == board.getS() - 1 && blocksToSupply == 1){
                return Double.POSITIVE_INFINITY;
            }
            return Double.NEGATIVE_INFINITY;
        }

        if(name.equals("Theseus"))
            return 0.5/(blocksToSupply - 1) - 1.0/(blocksToOpponent - 1);

        return 0.5/(blocksToSupply) + 1.0/(blocksToOpponent - 1);       //there's not -1 so bloscksToOpponent is more important
    }

    //returns the move that has the greatest value
    public int getNextMove(int currentPos, int opponentPos){
        double[] movesValues = new double[4];
        int randomDirection = 1 + 2 * ((int) (Math.random() * 10) % 4);
        double maxValue = evaluate(currentPos, opponentPos, randomDirection);
        int maxValueDie = randomDirection;
        for(int i = 0; i<4; ++i){
            if(i == randomDirection)
                continue;
            if(maxValue < (movesValues[i] = evaluate(currentPos, opponentPos, 2*i + 1))){
                maxValue = movesValues[i];
                maxValueDie = 2*i + 1;
            }
        }

        int[] observation = seeAround(currentPos, opponentPos, maxValueDie);
        Integer[] tempArray = {maxValueDie, 0, observation[0] - 1, observation[1] - 1};
        if(name.equals("Theseus") && maxValue == Double.POSITIVE_INFINITY)
                tempArray[1] = 1;
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

    public int[] move(int die) {
		int[] details = new int[4];
        details[3] = -1;
		switch(die) {
		case 1://case UP
			System.out.println(name + " rolled UP.");
			if(board.getTiles()[board.getN()*x+y].getUp()) {
				System.out.println(name + " cannot move up.");
				details[0] = board.getN()*x+y;
				details[1] = board.getTiles()[board.getN()*x+y].getX();
				details[2] = board.getTiles()[board.getN()*x+y].getY();
				break;
			}
			details[0] = board.getN()*x+y + board.getN();
			details[1] = board.getTiles()[details[0]].getX();
			details[2] = board.getTiles()[details[0]].getY();
			setX(details[1]);
			setY(details[2]);
			
			if(getPlayerId() == 1) {
				for(int i = 0 ; i < board.getS() ; i++) {
					if((details[0] == board.getSupplies()[i].getSupplyTileId())&&(board.getSupplies()[i].isObtainable())) {
                        System.out.println(name + " picked up supply " + board.getSupplies()[i].getSupplyId() + ".");
						details[3] = i;
						board.getSupplies()[i].setObtainable(false);
						break;
					}
				}
			}		
			break;
		case 3://case RIGHT
			System.out.println(name + " rolled RIGHT.");
			if(board.getTiles()[board.getN()*x+y].getRight()) {
				System.out.println(name + " cannot move right.");
				details[0] = board.getN()*x+y;
				details[1] = board.getTiles()[board.getN()*x+y].getX();
				details[2] = board.getTiles()[board.getN()*x+y].getY();
				break;
			}
			
			details[0] = board.getN()*x+y + 1;
			details[1] = board.getTiles()[details[0]].getX();
			details[2] = board.getTiles()[details[0]].getY();
			setX(details[1]);
			setY(details[2]);
			
			if(getPlayerId() == 1) {
				for(int i = 0 ; i < board.getS() ; i++) {
					if((details[0] == board.getSupplies()[i].getSupplyTileId())&&(board.getSupplies()[i].isObtainable())) {
						System.out.println(name + " picked up supply " + board.getSupplies()[i].getSupplyId() + ".");
						details[3] = i;
						board.getSupplies()[i].setObtainable(false);
						break;
					}
				}
			}
			break;
		case 5://Case DOWN
			System.out.println(name + " rolled DOWN.");
			if(board.getTiles()[board.getN()*x+y].getDown()) {
				System.out.println(name + " cannot move down.");
				details[0] = board.getN()*x+y;
				details[1] = board.getTiles()[board.getN()*x+y].getX();
				details[2] = board.getTiles()[board.getN()*x+y].getY();
				break;
			}
			
			details[0] = board.getN()*x+y - board.getN();
			details[1] = board.getTiles()[details[0]].getX();
			details[2] = board.getTiles()[details[0]].getY();
			setX(details[1]);
			setY(details[2]);
			
			if(getPlayerId() == 1) {
				for(int i = 0 ; i < board.getS() ; i++) {
					if(details[0] == board.getSupplies()[i].getSupplyTileId() && board.getSupplies()[i].isObtainable()) {
						System.out.println(name + " picked up supply " + board.getSupplies()[i].getSupplyId() + ".");
						details[3] = i;
						board.getSupplies()[i].setObtainable(false);
						break;
					}
				}
			}
			break;
		case 7://Case LEFT
			System.out.println(name + " rolled LEFT.");
			if(board.getTiles()[board.getN()*x+y].getLeft()) {
				System.out.println(name + " cannot move left.");
				details[0] = board.getN()*x+y;
				details[1] = board.getTiles()[board.getN()*x+y].getX();
				details[2] = board.getTiles()[board.getN()*x+y].getY();
				break;
			}
			
			details[0] = board.getN()*x+y - 1;
			details[1] = board.getTiles()[details[0]].getX();
			details[2] = board.getTiles()[details[0]].getY();
			setX(details[1]);
			setY(details[2]);
			
			if(getPlayerId() == 1) {
				for(int i = 0 ; i < board.getS() ; i++) {
					if((details[0] == board.getSupplies()[i].getSupplyTileId())&&(board.getSupplies()[i].isObtainable())) {
						System.out.println(name + " picked up supply " + board.getSupplies()[i].getSupplyId() + ".");
						details[3] = i;
						board.getSupplies()[i].setObtainable(false);
						break;
					}
				}
			}
			break;
		}
		return details;
	}
}