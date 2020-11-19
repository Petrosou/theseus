/**
Σουλίδης Πέτρος 9971 petrosis@ece.auth.gr
Τερζίδης Αλέξανδρος 10072 terzidisa@ece.auth.gr
*/

public class Player {
	protected int playerId;
	protected String name;
	protected Board board;
	protected int score;
	protected int x;
	protected int y;
	
	
	public Player() {
		playerId = 0;
		name = "";
		board = new Board();
		score = 0;
		x = 0;
		y = 0;
	}
	
	public Player(int playerId, String name, Board board, int score, int x, int y) {
		this.playerId = playerId;
		this.name = name;
		this.board = board;
		this.score = score;
		this.x = x;
		this.y = y;
	}
	
	//getters
	public int getPlayerId() {
		return playerId;
	}
	
	public String getName() {
		return name;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	//setters
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setBoard(Board board) {
		this.board = board;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	
	public int[] move(int id) {
		int[] details = new int[4];
		details[3] = -1;
		int randomDirection = 1 + 2 * ((int) (Math.random() * 10) % 4);
		switch(randomDirection) {
		case 1://case UP
			System.out.println(name + " rolled UP.");
			if(board.getTiles()[id].getUp()) {
				System.out.println(name + " cannot move up.");
				details[0] = id;
				details[1] = board.getTiles()[id].getX();
				details[2] = board.getTiles()[id].getY();
				break;
			}
			details[0] = id + board.getN();
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
			if(board.getTiles()[id].getRight()) {
				System.out.println(name + " cannot move right.");
				details[0] = id;
				details[1] = board.getTiles()[id].getX();
				details[2] = board.getTiles()[id].getY();
				break;
			}
			
			details[0] = id + 1;
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
			if(board.getTiles()[id].getDown()) {
				System.out.println(name + " cannot move down.");
				details[0] = id;
				details[1] = board.getTiles()[id].getX();
				details[2] = board.getTiles()[id].getY();
				break;
			}
			
			details[0] = id - board.getN();
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
			if(board.getTiles()[id].getLeft()) {
				System.out.println(name + " cannot move left.");
				details[0] = id;
				details[1] = board.getTiles()[id].getX();
				details[2] = board.getTiles()[id].getY();
				break;
			}
			
			details[0] = id - 1;
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