package game;
import java.util.Random;

public class AIA {
	private String algorithm = "Random";
	private char colour = 'W';
	private char [][] position = new char[11][11];
	private Boolean complete = false;
	private int currentDepth;
	private int currentRank;
	private int currentFile;

	/* New random number generator shared between classes for efficiency */
	private Random random;
	
	/* Game position states */
	final static char UNOCCUPIED = ' ';
	final static char WHITE = 'W';
	final static char BLACK = 'B';

	public AIA(String myAlgorithm, char myColour, char[][] worldPosition, int depth, int fileHint, int rankHint) {
		algorithm = myAlgorithm;
		if(algorithm.equals("Random")) random = new Random();
		colour = myColour;
		currentDepth = depth;
		for(int i=1; i<11; i++) {
			for(int j=1; j<11; j++){
				position[i][j] = worldPosition[i][j];
			}
		}
	}

	/* Returns a move when the game requests it. Rank is shifted to the higher byte for speed.
	 */
	public int getMove() {
		/* Checks first if the supreme algorithm set is complete and returns that value
		 * Otherwise returns its best incomplete judgement
		 */
		if(complete && algorithm.equals("Supreme")) return 0;
		if(!complete && algorithm.equals("Sureme")) return 0;

		/* Otherwise Default to Random Algorithm
		 * Select a random file, then a random rank allowable in that file
		 */
		currentFile = random.nextInt(10)+1;
		switch (currentFile) {
			case 1:  case 2: case 3: case 4:
				currentRank = random.nextInt(4)+1;
				break;
			case 5:  case 6: case 7:
				currentRank = random.nextInt(3)+5;
				break;
			case 8:  case 9:
				currentRank = random.nextInt(2)+8;
     	                break;
			case 10:
				currentRank = 10;
				break;
			default: currentRank = 0;
				break;
		}

		/* Debugging */
		//System.out.println("File: "+currentFile);
		//System.out.println("Rank: "+currentRank);
		return currentFile + (currentRank<<4);
	}

	public void updateMove(int file, int rank, char currentPlayer) {
		position [file][rank] = currentPlayer;
	}

	public void gameOver() {
	}
}