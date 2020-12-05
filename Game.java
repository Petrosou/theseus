import java.util.ArrayList;

/**
 * Σουλίδης Πέτρος 9971 petrosis@ece.auth.gr Τερζίδης Αλέξανδρος 10072
 * terzidisa@ece.auth.gr
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
		final int N = 15, S = 4, W = 100 + (int)(Math.random()*6), n = 100;
		Board board = new Board(N, S, W);
		board.createBoard();
		HeuristicPlayer[] gamers = new HeuristicPlayer[2];
		gamers[0] = new HeuristicPlayer(1, "Theseus", board, 0, 0, 0, new ArrayList<>(n), 3, 1);
		gamers[1] = new HeuristicPlayer(2, "Minotaur", board, 0, N/2, N/2,  new ArrayList<>(n), 0, 0);
		int winnerIdx = -1;
		int theseus, minotaur, ties;
		theseus = minotaur = ties = 0;
		//Times ran declared here
		double tries = 100000;
		for(int k = 0 ; k<((int)tries); k++){
			do{
				game.setRound(game.getRound() + 1);

				//Movements
				
				//Theseus' turn

				int die = gamers[0].getNextMove(N * gamers[0].getX() + gamers[0].getY(), N * gamers[1].getX() + gamers[1].getY());

				//Check if a supply was collected
				if(gamers[0].move(die)[3] != -1){
					gamers[0].setScore(gamers[0].getScore() + 1);
				}

				//Check if all supplies were collected
				if(gamers[0].getScore() == board.getS()){
					winnerIdx = 0;
					break;
				}
				
				//Check if Theseus ran into Minotaur
				if(gamers[0].getX() == gamers[1].getX() && gamers[0].getY()==gamers[1].getY()) {
					winnerIdx = 1;
					break;
				}
				
				//Minotaur's turn
				die = gamers[1].getNextMove(N * gamers[1].getX() + gamers[1].getY(), N * gamers[0].getX() + gamers[0].getY());
				gamers[1].move(die);

				//Check if Minotaur ran into Theseus
				if(gamers[0].getX() == gamers[1].getX() && gamers[0].getY() == gamers[1].getY()) {
					winnerIdx = 1;
					break;
				}	
			}while(game.getRound() < n);		//n rounds -> 2n plays
			
			//println that reassures program didn't crash
			if(k%1000 == 0)
				System.out.println("Game " + k + " ended in " + game.getRound() + " rounds.");

			//switch that handles counters
			switch(winnerIdx){
				case -1:
					ties++;
					break;
				case 0:
					theseus++;
					break;
				case 1:
					minotaur++;
					break;
			}

			//game reset
			board.empty();
			board.createBoard();
			gamers[0].setX(0);
			gamers[0].setY(0);
			gamers[1].setX(N/2);
			gamers[1].setY(N/2);
			gamers[0].setScore(0);
			gamers[0].erasePath();
			gamers[1].erasePath();
			game.setRound(0);
			winnerIdx = -1;
		}
		//final printing
		
		if((theseus+minotaur+ties)==((int)tries)){
			System.out.println("\n");
			System.out.println("Successfully ran " + (int)tries + " times");
			System.out.println();
			System.out.println("Percentages for S = " + S + ", n = " + n + ", Theseus ability: "+gamers[0].getAbility()+", Minotaur ability: "+gamers[1].getAbility());
			System.out.println("Theseus: " + 100*theseus/tries + "%");
			System.out.println("Minotaur: " + 100*minotaur/tries + "%");
			System.out.println("Ties: " + 100*ties/tries + "%");
		}
		else{
			System.out.println("Error... Ran " + (theseus+minotaur+ties) + " times.");
		}
		

	}

}