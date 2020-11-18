import java.util.ArrayList;
class HeuristicPlayer extends Player{
    private ArrayList<Integer[]> path;      //player moves [int die, double value, int supplyId, int blocksToOpponent]

    HeuristicPlayer(){
        super();
        path = new ArrayList<>(0);
    }

    HeuristicPlayer(int playerId, String name, Board board, int score, int x, int y, ArrayList<Integer[]> path){
        super(playerId, name, board, score, x, y);
        this.path = path;
    }
    double evaluate(int currentPos, int die){return 0;}

    //returns the move that has the greatest value
    int getNextMove(int currentPos){
        double[] movesValues = new double[4];

        double maxValue = movesValues[0] = evaluate(currentPos, 1);
        int maxValueDie = 1;
        for(int i = 1; i<4; ++i)
            if(maxValue < (movesValues[i] = evaluate(currentPos, 2*i + 1))){
                maxValue = movesValues[i];
                maxValueDie = 2*i + 1;
            }
        
        Integer[] tempArray = {maxValueDie, 0, -1, -1};
        path.add(tempArray);
        return maxValueDie;
    }

    void statistics(){
        System.out.println(path)
    }


}