import java.util.ArrayList;
class HeuristicPlayer extends Player{
    private ArrayList<Integer[]> path;      //player moves [int die, int pickedSupply, int supplyId, int blocksToSupply int blocksToOpponent]

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
        
        Integer[] tempArray = {maxValueDie, -1, -1, -1, -1};
        path.add(tempArray);
        return maxValueDie;
    }

    void statistics(){
        int ups, rights, downs, lefts, currentRound;
        ups = rights = downs = lefts = 0;
        for(int i = 0; i<path.size(); ++i){
            currentRound = i/2 + 1;
            if(name.equals("Theseus")){
                if(score == 0)
                    System.out.println("Theseus hadn't collected any supplies in round " + currentRound + ".");
                else if(score == 1)
                    System.out.println("Theseus had collected one supply in round " + currentRound + ".");
                else if(score > 1 && score < board.getS())
                    System.out.println("Theseus had collected " + score + " supply in round " + currentRound + ".");
                else if(score == board.getS())
                    System.out.println("Theseus collected all supplies in round " + currentRound + ".");
                else{
                    System.out.println("Some unexpected error happened while printing the \"statistics\" of Theseus");
                    java.lang.System.exit(1);
                }
            }
            int blocksToSupply = path.get(i)[3];
            switch(blocksToSupply){
                case(0):
                    System.out.println(name + "cannot see any supplies.");
                    break;
                case(1):
                    System.out.println(name + "is next to a supply.");
                    break;
                case(2):
                    System.out.println(name + "is two blocks away from a supply.");
                    break;
                default:
                    System.out.println("Some unexpected error happened in HeuristicPlayer-> void statistics()-> switch(blocksToSupply)");
                    java.lang.System.exit(1);
            }
            switch(path.get(i)[0]) {
                case 1://case UP
                    ++ups;
                    break;
                case 3://case RIGHT
                    ++rights;
                    break;
                case 5://Case DOWN
                    ++downs;
                    break;
                case 7://Case LEFT
                    ++lefts;
                    break;
                default:
                    System.out.println("Some unexpected error happened in HeuristicPlayer-> void statistics()-> switch(path.get(i)[0])");
                    java.lang.System.exit(1);
            }
        }
        System.out.println(name + " moved up a total of " + ups + " times.");
        System.out.println(name + " moved right a total of " + rights + " times.");
        System.out.println(name + " moved down a total of " + downs + " times.");
        System.out.println(name + " moved left a total of " + lefts + " times.");

    }


}