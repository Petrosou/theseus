public class RestrictedGameBoard extends Board{
    RestrictedGameBoard(GameBoard board, Player player){
    	super(board.N, board.S, 0);
    	
    	int ability;
    	if(!(player instanceof HeuristicPlayer))
    		ability = 0;
    	else
    		ability = ((HeuristicPlayer)player).getAbility();
    	
    	int nId = player.getX()*board.getN() + player.getY();
    	tiles[nId] = new RestrictedGameTile(board.tiles[nId]);
    	for(int die = 1; die<8; die+=2) {
	    	for(int i = 0; i<ability; ++i) {
	    		//Interfering wall
	            if(board.getTiles()[nId].getWallInDirection(die)) {
	                break;
	            }
	            nId = board.getTiles()[nId].neighborTileId(die, board.getN());
	            tiles[nId] = new RestrictedGameTile(board.tiles[nId]);
	            //Supply
	            Supply supply;
	            for(int j = 0;  j<board.getS(); ++j) {
	            	supply = board.getSupplies()[j];
	            	if(nId == supply.getSupplyTileId() && supply.isObtainable()) {
		                supplies[supply.getSupplyId()-1] = supply;
	            	}
	            }
	
	    	}
	    	nId = player.getX()*board.getN() + player.getY();
	    }
    }
    	
}
