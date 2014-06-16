package player;

public class Util {

	public static char[][] cloneCharGrid(char[][] array) {
	    int rows=array.length ;

	    //clone the 'shallow' structure of array
	    char[][] newArray =(char[][]) array.clone();
	    //clone the 'deep' structure of array
	    for(int row=0;row<rows;row++){
	        newArray[row]=(char[]) array[row].clone();
	    }

	    return newArray;
	}
	
	// sanity checker - check that i,j is position that exist somewhere in potential Pylos pyramid
	public static boolean isValidPosition(int i, int j){
		if(i < 4) return (j > -1 && j < 4);
		else if(i > 3 && i < 7) return (j > -1 && j < 3);
		else if(i > 6 && i < 9) return (j > -1 && j < 2);
		else if(i == 9) return (j == 0);
		else System.out.println("invalid Position dected. i="+i+", j="+j); return false;
	}
	
	public static int levelAt(int i){
		if(i < 4 && i>-1) return 0;
		else if(i > 3 && i < 7) return 1;
		else if(i > 6 && i < 9) return 2;
		else if(i == 9) return 3;
		else throw new RuntimeException("invalid pos passed to levelAt(int)");
		
	}
}
