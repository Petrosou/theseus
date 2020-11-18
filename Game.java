/**
Σουλίδης Πέτρος 9971 petrosis@ece.auth.gr
Τερζίδης Αλέξανδρος 10072 terzidisa@ece.auth.gr
*/

public class Game {
	private int round;
	
	public Game() {
		round = 0;
	}
	
	public Game(int round) {
		this.round = round;
	}
	
	//getters
	public int getRound() {
		return round;
	}
	
	//setters
	public void setRound(int round) {
		this.round = round;
	}
	
	
	public static void main(String[] args) {
		Game game = new Game();
		int N = 15, S = 4, W = 100 + (int)(Math.random()*61), n = 100;
		Board board = new Board(N, S, W);
		board.createBoard();
		Player[] gamers = new Player[2];
		gamers[0] = new Player(1, "Theseus", board, 0, 0, 0);
		gamers[1] = new Player(2, "Minotaur", board, 0, N/2, N/2);
		int winnerIdx = 0;
		do{
			System.out.println("\n\n");
			game.setRound(game.getRound() + 1);
			System.out.println("\t\t" + gamers[0].getName() + "  " + gamers[0].getScore() + " - " + gamers[1].getScore() + "  " + gamers[1].getName() + "\n");
			System.out.println("\t\t\tRound " + game.getRound());

			//Board representation
			String[][] repBoard = board.getStringRepresentation(N * gamers[0].getX() + gamers[0].getY(), N * gamers[1].getX() + gamers[1].getY());
			for(int i = 0 ; i <= 2 * N ; i++) {
				for(int j = 0;j <= N - 1 ; j++) {
					System.out.print(repBoard[i][j]);
				}
			}
			System.out.println("\n");
			
			//Movements
			
			//Theseus' turn
			
			//Check if a supply was collected
			if(gamers[0].move(N * gamers[0].getX() + gamers[0].getY())[3] != -1){
				gamers[0].setScore(gamers[0].getScore() + 1);
			}

			//Check if all supplies were collected
			if(gamers[0].getScore() == board.getS()){
				break;
			}
			
			//Check if Theseus ran into Minotaur
			if(gamers[0].getX() == gamers[1].getX() && gamers[0].getY()==gamers[1].getY()) {
				gamers[1].setScore(gamers[1].getScore() + 1);
				winnerIdx = 1;
				break;
			}
			
			//Minotaur's turn
			gamers[1].move(N * gamers[1].getX() + gamers[1].getY());

			//Check if Minotaur ran into Theseus
			if(gamers[0].getX() == gamers[1].getX() && gamers[0].getY() == gamers[1].getY()) {
				gamers[1].setScore(gamers[1].getScore() + 1);
				winnerIdx = 1;
				break;
			}	
		}while(game.getRound() < n);		//n rounds -> 2n plays
		
		//Tie
		if(game.getRound() == n) {
			System.out.println("Out of moves! (Tie)");
		}

		//Someone won
		else{
			System.out.println(gamers[winnerIdx].getName() + " won the round.");
			if(gamers[winnerIdx].getScore() == 5) {
				System.out.println("And the winner is " + gamers[winnerIdx].getName() + ", they won " + gamers[1 - winnerIdx].getName() + " " + gamers[winnerIdx].getScore() + " - " + gamers[1-winnerIdx].getScore()+"!");
			}
		}
	}

}