/**
 * Soulidis Petros 9971 petrosis@ece.auth.gr
 * Terzidis Alexandros 10072 terzidisa@ece.auth.gr
 */

import java.util.ArrayList;

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
	
	//Special functions
	static boolean canSeeOpponent(Player player, Player opponent, int N){
		if(!(player instanceof MinMaxPlayer))
			return false;
		
		int playerPos = player.getX()*N + player.getY();
		int opponentPos = opponent.getX()*N + opponent.getY();
		for(int i = 1; i<=((MinMaxPlayer)player).getAbility(); ++i) {
				if(playerPos == opponentPos+i || playerPos == opponentPos-i || playerPos == opponentPos+N || playerPos == opponentPos-N)
					return true;
		}
		
		return false;
	}

	public static void main(String[] args) {
		Game game = new Game();
		final int N = 15, S = 4, W = 100 + (int)(Math.random()*6), n = 100;
		GameBoard board = new GameBoard(N, S, W);
		board.createBoard();
		MinMaxPlayer[] gamers = new MinMaxPlayer[2];
		gamers[0] = new MinMaxPlayer(1, "Theseus", 0, 0, N, S, new ArrayList<>(n), 3, 3);
		gamers[1] = new MinMaxPlayer(2, "Minotaur", N/2, N/2, N, S, new ArrayList<>(n), 3, 0);
		int winnerIdx = -1;
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
			if(canSeeOpponent(gamers[0], gamers[1], N)) {
				if(gamers[0].move(new RestrictedGameBoard(board, gamers[0]), gamers[1].getX()*N + gamers[1].getY())[3] != -1){
					gamers[0].setScore(gamers[0].getScore() + 1);
				}
			}
			else {
				if(gamers[0].move(new RestrictedGameBoard(board, gamers[0]), -1)[3] != -1){
					gamers[0].setScore(gamers[0].getScore() + 1);
				}
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
			if(canSeeOpponent(gamers[1], gamers[0], N))
				gamers[1].move(new RestrictedGameBoard(board, gamers[1]), gamers[0].getX()*N + gamers[0].getY());
			else
				gamers[1].move(new RestrictedGameBoard(board, gamers[1]), -1);

			//Check if Minotaur ran into Theseus
			if(gamers[0].getX() == gamers[1].getX() && gamers[0].getY() == gamers[1].getY()) {
				winnerIdx = 1;
				break;
			}	
		}while(game.getRound() < n);		//n rounds -> 2n plays


		gamers[0].statistics();

		//Tie
		if(winnerIdx == -1) {
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