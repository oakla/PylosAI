package game;


//import java.util.Scanner;
import java.io.*;

class Game {
	/* Clear screen attempt boolean that indicates if the first native call to clear the screen
	 * was successful. If unsuccessful all subsequent calls to an instance of Game.clearScreen() will
	 * be emulated by dumping clearScreenHeight number of newLines. Only try one native call as it is
	 * expensive in time if it doesn't work.
	 */
	private Boolean clearScreenShouldWork = true;
	private int clearScreenHeight = 60;
	
	/* An array of characters that contains a move consisting of up to three actions in the form of:
	 * [A][Add Action][Remove Action][Remove Action]
	 * [A][Add Action][Remove Action]
	 * [A][Add Action]
	 * [E][Remove Action][Add Action]
	 */
	private char [] move = new char [7];
	private int moveLength;

	/* Game position states */
	final static char UNOCCUPIED = ' ';
	final static char WHITE = 'W';
	final static char BLACK = 'B';

	public static void main(String[] args) throws IOException {
		Game game = new Game();

		/* Game Model position array starts at indices [1,1] representing [a,1]
		   Thus row 0 and column 0 of the array are not used. So in effect is a 10x10 array,
		   implemented in an 11x11 array, for ease of indexing in the code.
		 */
		char[][] position = new char[11][11];

		/* Initialization of of all game positions to unoccupied. */
		for(int i=1; i<11; i++) {
			for(int j=1; j<11; j++){
				position[i][j] = UNOCCUPIED;
			}
		}
		
		/* Sets the currentPlayer to be the starting player in the game as white*/
		int currentPlayer = WHITE;
		
		/* Request a colour from the human that they would like to play as */
		game.clearScreen();
		System.out.println("Please select either Black or White as your colour.\nPlease enter the " +
				"character 'B' for black or 'W' for white to make your selection.");
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in)); 
	    int readResult = 0;
	    int readLength = 1;
	    int readPosition = 0;
	    char requestedColour = ' ';
	    Boolean validColour = false;

	    /* Give the human player three attempts to select a valid colour */
	    for (int i=0; i<3 && !validColour; i++) {
	    	while(((readResult = buffer.read()) != -1) && readPosition <= readLength) { 
	    		requestedColour = (char) readResult;
                System.out.println(requestedColour);
                readPosition++;
	    	}
	    	if (requestedColour == 'w' || requestedColour == 'W' || requestedColour == 'b' || 
	    		requestedColour == 'B') validColour = true;
	    	if (requestedColour == 'w' || requestedColour == 'W') requestedColour = WHITE;
	    	if (requestedColour == 'b' || requestedColour == 'B') requestedColour = BLACK;
	    	else System.out.println("Try again and enter a valid colour.");
	    }
	    
	    /* If a valid colour has not been requested, assign the default of WHITE */
	    if(!validColour) requestedColour = WHITE;
	    
	    /* Advise the player of the colour they'll be playing as. */
	    System.out.println("You have been assigned to play as " + ((requestedColour == WHITE) ? "White." : "Black."));


		
		/* Creates a Artificial Intelligence Agent utilising a random algorithm,
		 * and sets the agent's playing colour to white, gives it a reference to the
		 * array of board positions, sets search depth to zero, and opponent file and
		 * rank hints to zero. These may be set to a rank or a file value, when the human
		 * player presses a valid keystroke. Effectively we're giving the AI agent a hint
		 * as to what the human's next mone is likely to be.
		 */
		AIA aia = new AIA("Random", ((requestedColour == WHITE) ? BLACK : WHITE), position, 0, 0, 0);

		int ply = 1;
		Boolean quit = false;
		while(position[10][10]==' ' && !quit) {
			game.clearScreen();
			int moveFile = 0;
			int moveRank = 0;
			Boolean validMove = false;

			while(validMove==false && !quit){
				System.out.println("Ply = "+ply);
				int temp = 0;
				if(currentPlayer==WHITE) {
					// Check to see if the human player is white. If so, request a move from them first.
					if (requestedColour == WHITE) {
						validMove = game.getConsoleMove();
						
						/* If the user input was not successful, prompt the user to give up, generate an
						 * AIA agent to generate a random move containing an add, or alternatively allow
						 * the user to try again
						 */
						if (!validMove) {
							System.out.println("Would you like to give up chump or get us to generate " +
									"a random");
							System.out.println("add for you for this move?\n");
							System.out.println("Q = Quit\nR = Generate Random Action\nT = Try Again");
						}
						/* Request a colour from the human that they would like to play as */
						game.clearScreen();
						System.out.println("Please select either Black or White as your colour. Please enter the " +
								"character 'B' for black or 'W' for white to make your selection.");
						buffer = new BufferedReader(new InputStreamReader(System.in)); 
					    readResult = 0;
					    readLength = 1;
					    readPosition = 0;
					    int requestedResponse = ' ';
					    Boolean validResponse = false;
					    /* Give the human player three attempts to select a valid colour */
					    for (int i=0; i<3 && !validResponse; i++) {
					    	while(((readResult = buffer.read()) != -1) && readPosition <= readLength) { 
					    		requestedResponse = (char) readResult;
				                System.out.println(requestedColour);
				                readPosition++;
					    	}
					    	if (requestedResponse == 'q' || requestedResponse == 'Q' ||
					    		requestedResponse == 'r' || requestedResponse == 'R' || 
					    		requestedResponse == 't' || requestedResponse == 'T') validResponse = true;
					    	if (requestedResponse == 'q' || requestedColour == 'Q') quit = true;
					    	if (requestedResponse == 'b' || requestedColour == 'B') requestedColour = BLACK;
					    	else System.out.println("Please try again and enter a valid response.");
					    }
					    
					    /* If a valid colour has not been requested, assign the default of WHITE */
					    if(!validResponse) quit = true;
					    System.out.println("Forcing a quit and default loss.");
					}
					else {
						/* Request a move from the Artificial Intelligence Agent first, and decode the 
						 * file and rank if AIA. Rank has been bitshifted into the higher byte by AI 
						 * Agent for speed
						 */
						System.out.println("Game Requesting Move from White AI Agent");
						temp= aia.getMove();
					}
				}

				if(currentPlayer==BLACK) {
					// Check to see if the human player is white. If so, request a move from them first.
					if (requestedColour == WHITE) {
						
					}
					else {
						/* Request a move from the Artificial Intelligence Agent first, and decode the 
						 * file and rank if AIA. Rank has been bitshifted into the higher byte by AI 
						 * Agent for speed
						 */
						System.out.println("Game Requesting Move from Black AI Agent");
						temp= aia.getMove();
					}
				}

				int file = temp&0x0F;
				int rank = (temp>>4)&0x0F;
				if(currentPlayer==WHITE) {
					System.out.println("White Requested File: "+(char)(('A'-1+file)));
					System.out.println("White Requested Rank: "+rank);
				}
				if(currentPlayer==BLACK) {
					System.out.println("Black Requested File: "+(char)(('A'-1+file)));
					System.out.println("Black Requested Rank: "+rank);
				}

				/* Game Rule Checking Section */

				/* Position must be in the valid file range */
				if(0<file&&file<11) validMove = true;

				/* Position Must also be unoccupied */
				validMove &= (position[file][rank]==UNOCCUPIED);

				/*  Other Checks to be implemented*/

				/* If it's still a valid position then game will accept this move and
				 * not request another move from the agent, and this while loop will
				 * finish.
				 */
				if(validMove) {
					moveFile = file;
					moveRank = rank;
				}
				else if (!quit) System.out.println("The game rejected the proposed move. Requesting another.");
			}
			if (!quit) {
			System.out.println("The Game accepted move as: "+((char)(('A'-1+moveFile)))+moveRank);
			/* Update the board position, switch the current player, display the board. Increase
			 * the ply count. On the next entrance of this while loop the game will check if the
			 * goal state has been reached. If so, will drop out of it.
			 */
			position[moveFile][moveRank] = (char) currentPlayer;
			aia.updateMove(moveFile, moveRank, (char) currentPlayer);
			if (currentPlayer==WHITE) currentPlayer=BLACK;
			else if (currentPlayer==BLACK) currentPlayer=WHITE;
			game.displayBoard(position);
			ply++;
			}
		}
		
		/* At this point outside of the while loop, we know that a winner has been determined after
		 * the goal state has been reached.
		 */
		if(position[10][10]==WHITE) System.out.println("White Wins");
		if(position[10][10]==BLACK) System.out.println("Black Wins");
		if(quit) System.out.println((currentPlayer == WHITE)?"White Loses by Default":"Black Loses by Default");

		/* Advise both agents that the game is over */
		aia.gameOver();
	}

	public void clearScreen() {
		if (clearScreenShouldWork) {
			try {
				Runtime.getRuntime().exec("cls.exe"); 
			} catch (Exception e) {
				clearScreenShouldWork = false;
				for(int i=0;i<clearScreenHeight;i++) System.out.println();
			}
		}
		else for(int i=0;i<clearScreenHeight;i++) System.out.println();
	}

	/* Returns true if the requested user input initially appears to be a valid formed move */
	public Boolean getConsoleMove() throws IOException {
		/* Request a colour from the human that they would like to play as */
		System.out.println("Please enter your move consisting of up to three actions. Please enter in " +
				"the form of:");
		System.out.println("AA2B3C4 that represents an add action A2 followed by two removes B3 and C4 or");
		System.out.println("EA1H8 that represents escalating a ball from a position from A1 to H8"+ "\n");
		System.out.println("Note: An add action does not have to have one or two remove actions following,");
		System.out.println("ie, AA2B3 or AA2 is also acceptable");

		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in)); 
	    int readResult = 0;
	    int readLength = 7;
	    int readPosition = 0;
	    move[0] = 'A';
	    move[1] = 'A';
	    move[3] = 'A';
	    move[5] = 'A';
	    Boolean validMove = false;

	    /* Give the human player three attempts to select a valid move, will check simply that it
	     * is received in the required from. Will not check to see if valid on the board. Done by 
	     * the method that calls getConsoleMove() instead
	     */
	    for (int i=0; i<3 && !validMove; i++) {
	    	while(((readResult = buffer.read()) != -1) && readPosition <readLength) { 
	    		move[readPosition] = (char) readResult;
                readPosition++;
	    	}
	    	moveLength = readPosition;
	    	
	    	// Check to see that the string entered is at least three characters in length
	    	if (!(validMove = moveLength >=3))
	    	System.out.println("The mimimum string length of three to create a move was not received.");
	    	
	    	// Convert valid lower case letter to upper case, should be in positions 0, 1, 3 and 5.
	    	if (moveLength<=3) {
	    		move[0] = Character.toUpperCase(move[0]);
	    		move[1] = Character.toUpperCase(move[1]);
	    	}
	    	if (moveLength<=5) move[3] = Character.toUpperCase(move[3]);
	    	if (moveLength<=5) move[5] = Character.toUpperCase(move[5]);
	    	
	    	// Check to see that the first action is an add or an escalate
	    	if (!(validMove &= (move[0]=='A'|| move[0] == 'E')))
	    	System.out.println("The initial action was not an Add ('A') or an Escalate ('E')");
	    	
	    	/*
	    	if (requestedColour == 'w' || requestedColour == 'W' || requestedColour == 'b' || 
	    		requestedColour == 'B') validMove = true;
	    	if (requestedColour == 'w' || requestedColour == 'W') requestedColour = WHITE;
	    	if (requestedColour == 'b' || requestedColour == 'B') requestedColour = BLACK;
	    	if(!validMove) System.out.println("Please try again to enter a possibly valid move.");
	    	*/
	    }
	    return validMove;
	}
	
	public void displayBoard(char[][] position) {
		/* Displaying the board positions
		 */
		System.out.println("   -------             -----               ---                 -                ");
		System.out.println("4| "+position[1][4]+" "+position[2][4]+" "+position[3][4]+" "+position[4][4]+" |        7| "+position[5][7]+" "+position[6][7]+" "+position[7][7]+" |          9| "+position[8][9]+" "+position[9][9]+" |           10| "+position[10][10]+" |              ");
		System.out.println("3| "+position[1][3]+" "+position[2][3]+" "+position[3][3]+" "+position[4][3]+" |        6| "+position[5][6]+" "+position[6][6]+" "+position[7][6]+" |          8| "+position[8][8]+" "+position[9][8]+" |               -                ");
		System.out.println("2| "+position[1][2]+" "+position[2][2]+" "+position[3][2]+" "+position[4][2]+" |        5| "+position[5][5]+" "+position[6][5]+" "+position[7][5]+" |             ---                 J                ");
		System.out.println("1| "+position[1][1]+" "+position[2][1]+" "+position[3][1]+" "+position[4][1]+" |           -----               H I                                  ");
		System.out.println("   -------             E F G                                                    ");
		System.out.println("   A B C D                                                                      ");

		/* Development Code of Ruler and Game Environment
		System.out.print  ("0        1         2         3         4         5         6         7         8");
		System.out.println("12345678901234567890123456789012345678901234567890123456789012345678901234567890");
		System.out.println("   -------             -----               ---                 -                ");
		System.out.println("4|         |        7|       |          9|     |           10|   |              ");
		System.out.println("3|         |        6|       |          8|     |               -                ");
		System.out.println("2|         |        5|       |             ---                 J                ");
		System.out.println("1|         |           -----               H I                                  ");
		System.out.println("   -------             E F G                                                    ");
		System.out.println("   A B C D                                                                      ");
		 */
	}

	void miscCode() {
		/* Me messing around with reading from the input... Sigh
		String string= "  ";
		char action;
		char file;
		int rank;
		Scanner scanner = new Scanner( System.in );
		System.out.println(scanner.hasNext() == true ? (action = (string = scanner.next()).charAt(0)) : "NoCharacter");
		System.out.println((file = string.charAt(1)));
		Scanner stringScanner = new Scanner (string);
		System.out.println(stringScanner.hasNextInt() == true ? scanner.nextInt() : "No Int");
		scanner.close();
		stringScanner.close();
		 */

		/* Development Code for Reading from a Line
		Scanner scanner = new Scanner( System.in );
		System.out.println(scanner.hasNext() == true ? scanner.next() : "");
		System.out.println(scanner.hasNextInt() == true ? scanner.nextInt() : "Int");
		System.out.println(scanner.hasNextDouble() == true ? scanner.nextDouble() : "Double");
		System.out.println(scanner.hasNextLong() == true ? scanner.nextLong() : "Long");
		System.out.println(scanner.hasNextBoolean() == true ? scanner.nextBoolean() : "Boolean");
		scanner.close();
		 */
	}
}