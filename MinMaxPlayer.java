import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MinMaxPlayer extends HeuristicPlayer {

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
            root.children.get(i).nodeMove[0] = root.nodeMove[0];
            root.children.get(i).nodeMove[1] = root.nodeMove[1];

            //player movement simulation
            MinMaxPlayer player = new MinMaxPlayer(playerId, name, x, y, root.nodeBoard.getN(), root.nodeBoard.getS(), path, ability, wallAbility);
            root.children.get(i).nodeEvaluation = player.evaluate(player.getX()*root.nodeBoard.getN()+player.getY(), opponentPos, 2*i+1, root.children.get(i).nodeBoard);
            boolean gameEnded = player.endGameMove(player, opponentPos, 2*i+1, root);
            root.children.get(i).nodeMove[0] = player.getX();
            root.children.get(i).nodeMove[1] = player.getY();
            
            if(!gameEnded && opponentPos!=-1){
			    createOpponentSubtree(player.getX()*root.nodeBoard.getN()+player.getY(), opponentPos, root.children.get(i), root.children.get(i).nodeDepth, root.children.get(i).nodeEvaluation);
            }
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
            parent.children.get(i).nodeMove[0] = opponentPos/parent.nodeBoard.getN();
            parent.children.get(i).nodeMove[1] = opponentPos%parent.nodeBoard.getN();


            //opponent movement simulation
            MinMaxPlayer player = new MinMaxPlayer();
            player.setX(opponentPos/parent.nodeBoard.getN());
            player.setY(opponentPos%parent.nodeBoard.getN());
            parent.children.get(i).nodeEvaluation = parentEval - player.evaluate(opponentPos, currentPos, 2*i+1, parent.children.get(i).nodeBoard);

            player.endGameMove(player, opponentPos, 2*i+1, parent.children.get(i));
            parent.children.get(i).nodeMove[0] = player.getX();
            parent.children.get(i).nodeMove[1] = player.getY();
        }
    }
    
    boolean endGameMove(MinMaxPlayer player, int opponentPos, int die, Node root){
        PlayerBoard board = root.nodeBoard;
        if(!board.getTiles()[board.getN()*root.nodeMove[0]+root.nodeMove[1]].getWallInDirection(die)) {
            player.setX(board.getTiles()[root.nodeMove[0]*board.getN()+root.nodeMove[1]].neighborTileId(die, board.getN()));
            player.setY(board.getTiles()[root.nodeMove[0]*board.getN()+root.nodeMove[1]].neighborTileId(die, board.getN()));        
        }
        if(name.equals("Theseus")) {
            for(int i = 0 ; i < board.getS() ; i++) {
                if((board.getTiles()[root.nodeMove[0]+root.nodeMove[1]].neighborTileId(die, board.getN()) == board.getSupplies()[i].getSupplyTileId())&&(board.getSupplies()[i].isObtainable())) {
                    board.getSupplies()[i].setObtainable(false);
                    if(score == root.nodeBoard.S-1)
                        return true;
                    break;
                }
            }
        }
        if(root.nodeMove[0]*root.nodeBoard.N + root.nodeMove[1] == opponentPos)
            return true;
        return false;        
    }

    int chooseMinMaxMove(Node root){
        double maxDiff = Double.NEGATIVE_INFINITY;
        int move = -1;
        double[] minOfLeaves = new double[4];
        for(int i = 0 ;  i < 4 ; i++){
            for(int j = 0; j<4; j++){
                if(root.children.get(i).children != null){
                    minOfLeaves[i] = root.children.get(i).children.get(j).nodeEvaluation;
                }
                else{
                    minOfLeaves[i] = root.children.get(i).nodeEvaluation;
                }
            }
            if(maxDiff < minOfLeaves[i]){
                maxDiff = minOfLeaves[i];
                move = 2*i+1;
            }
            
        }
        return move;
    }
    @Override
    public int[] move(RestrictedGameBoard board, int opponentPos){
        Node root = new Node();
        root.nodeBoard = playerMap;
        root.nodeMove[0] = getX();
        root.nodeMove[1] = getY();
        createMySubtree(board, opponentPos, root, 1);
        int selectedMove = chooseMinMaxMove(root);

        //update path
        int[] observation = seeAround(board, opponentPos, selectedMove);
        Integer[] tempArray = {selectedMove, 0, observation[0] - 1, observation[1] - 1, board.getN()*x+y, opponentPos};
        if(name.equals("Theseus") && root.children.get(selectedMove/2).nodeEvaluation == Double.POSITIVE_INFINITY){
                tempArray[1] = 1;
                //Set obtainable false
                for(int i = 0; i<playerMap.getS(); ++i){
                    if(playerMap.getSupplies()[i].getSupplyTileId() == board.getTiles()[board.getN()*x+y].neighborTileId(selectedMove, board.getN())){
                    	playerMap.getSupplies()[i].setObtainable(false);
                        break;
                    }
                }
        }
        path.add(tempArray);
        return super.move(board, selectedMove);
    }
}
