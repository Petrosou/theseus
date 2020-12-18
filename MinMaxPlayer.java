import java.util.ArrayList;

public class MinMaxPlayer extends HeuristicPlayer {
    int height = 2;

    //Constuctors
    public MinMaxPlayer() {
		super();
	}
	
	public MinMaxPlayer(int playerId, String name, int x, int y, int N, int S, ArrayList<Integer[]> path, int ability, int wallAbility) {
        super(playerId, name, x, y, N, S, path, ability, wallAbility);
    }


    //Special functions
    double evaluate(int currentPos, int opponentPos, int die, PlayerBoard nodeBoard){
        int N = nodeBoard.getN();
        int S = nodeBoard.getS();
        HeuristicPlayer fakePlayer = new HeuristicPlayer(playerId, name, currentPos/N, currentPos%N, N, S, new ArrayList<>(0), N, N);
        fakePlayer.playerMap = nodeBoard;
        fakePlayer.path = path;
        return fakePlayer.evaluate(nodeBoard, opponentPos, die);
    }

    void createMySubtree(RestrictedGameBoard board, int opponentPos, Node root, int depth){
        root.children = new ArrayList<Node>(4);

        //Update playerMap
        if(root.nodeDepth == 0){
            for(int i = 0; i<4; ++i)
                seeAround(board, opponentPos, 2*i+1);
        }
        
        //Form child
        for(int i = 0 ; i < 4; i++) {
            root.children.add(new Node());
            root.children.get(i).nodeDepth = root.nodeDepth+1;
            root.children.get(i).parent = root;
            root.children.get(i).nodeMove[2] = 2*i+1;
            root.children.get(i).children = null;
            root.children.get(i).nodeBoard = root.nodeBoard;

            //player movement simulation
            MinMaxPlayer player = new MinMaxPlayer(playerId, name, x, y, root.nodeBoard.getN(), root.nodeBoard.getS(), path, ability, wallAbility);
            root.children.get(i).nodeEvaluation = player.evaluate(player.getX()*root.nodeBoard.getN()+player.getY(), opponentPos, 2*i+1, root.children.get(i).nodeBoard);
            
			player.makeMove(root.children.get(i).nodeBoard, 2*i+1, root);
            root.children.get(i).nodeMove[0] = player.getX();
            root.children.get(i).nodeMove[1] = player.getY();
            
            //check if game ends
            //opponent child nodes
            if(opponentPos != -1)
			    createOpponentSubtree(player.getX()*root.nodeBoard.getN()+player.getY(), opponentPos, root.children.get(i), root.children.get(i).nodeDepth, root.children.get(i).nodeEvaluation);
        }
        
    }
    void createOpponentSubtree(int currentPos, int opponentPos, Node parent, int depth, double parentEval){
        parent.children = new ArrayList<Node>(4);
        for(int i = 0 ; i < 4 ; i++){
            parent.children.add(new Node());
            parent.children.get(i).nodeDepth = parent.nodeDepth+1;
            parent.children.get(i).parent = parent;
            parent.children.get(i).nodeMove[2] = 2*i+1;
            parent.children.get(i).children = null;
            parent.children.get(i).nodeBoard = parent.nodeBoard;
            
            //opponent movement simulation
            MinMaxPlayer player = new MinMaxPlayer();
            player.setX(opponentPos/parent.nodeBoard.getN());
            player.setY(opponentPos%parent.nodeBoard.getN());
            parent.children.get(i).nodeEvaluation = player.evaluate(opponentPos, currentPos, 2*i+1, parent.children.get(i).nodeBoard);

            player.makeMove(parent.children.get(i).nodeBoard, 2*i+1, parent.children.get(i));
            parent.children.get(i).nodeMove[0] = player.getX();
            parent.children.get(i).nodeMove[1] = player.getY();
        }
    }
    
    void makeMove(PlayerBoard board, int die, Node root){
        if(!board.getTiles()[board.getN()*root.nodeMove[0]+root.nodeMove[1]].getWallInDirection(die)) {
            setX(board.getTiles()[root.nodeMove[0]+root.nodeMove[1]].neighborTileId(die, board.getN())/board.getN());
            setY(board.getTiles()[root.nodeMove[0]+root.nodeMove[1]].neighborTileId(die, board.getN())%board.getN());        
        }
        if(name.equals("Theseus")) {
            for(int i = 0 ; i < board.getS() ; i++) {
                if((board.getTiles()[root.nodeMove[0]+root.nodeMove[1]].neighborTileId(die, board.getN()) == board.getSupplies()[i].getSupplyTileId())&&(board.getSupplies()[i].isObtainable())) {
                    board.getSupplies()[i].setObtainable(false);
                    break;
                }
            }
        }        
    }

    int chooseMinMaxMove(Node root){
        double opponentEval = 0;
        double myEval = 0;
        double maxDiff = Double.NEGATIVE_INFINITY;
        int move = -1;
        for(int i = 0 ;  i < 4 ; i++){
            myEval = root.children.get(i).nodeEvaluation;
            if(root.children.get(i).children == null){
                if(maxDiff < myEval){
                    maxDiff = myEval;
                    move = 2*i+1;
                }
                System.out.println(maxDiff);
                continue;
            }
            for(int j = 0 ; j < 4 ; j++){//Υπάρχει περίπτωση το παιχνίδι να τελειώνει στο 1ο επίπεδο
                opponentEval = root.children.get(i).children.get(j).nodeEvaluation;
                if(maxDiff < myEval - opponentEval){
                    maxDiff = myEval - opponentEval;
                    move = 2*i+1;
                }
            }
        }
        return move;
    }
    @Override
    public int[] move(RestrictedGameBoard board, int opponentPos){
        Node root = new Node();
        root.nodeBoard = playerMap;
        createMySubtree(board, opponentPos, root, 1);
        int selectedMove = chooseMinMaxMove(root);

        //update path
        int[] observation = seeAround(board, opponentPos, selectedMove);
        Integer[] tempArray = {selectedMove, 0, observation[0] - 1, observation[1] - 1, board.getN()*x+y, opponentPos};
        if(name.equals("Theseus") && selectedMove == Double.POSITIVE_INFINITY){
                tempArray[1] = 1;
                //Set obtainable false
                for(int i = 0; i<playerMap.getS(); ++i){
                    if(playerMap.getSupplies()[i].getSupplyTileId() == board.getTiles()[board.getN()*x+y].neighborTileId(selectedMove, board.getN())){
                    	playerMap.getSupplies()[i].setObtainable(false);;
                        break;
                    }
                }
        }
        path.add(tempArray);
        return super.move(board, selectedMove);
    }
}
