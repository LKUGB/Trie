import java.io.IOException;
import java.io.BufferedReader;

// class to represent a Boggle grid of letters
// size will be ( gridSize x gridSize )
class Grid {

    private char[][] grid;
    private int gridSize;

    // constructor - reads size and characters through an already open BufferedReader
    //               returns immediately if gridSize == 0
    public Grid( BufferedReader br ){
        try {
            // read the size
            this.gridSize = Integer.parseInt(br.readLine());
            if (gridSize == 0) {
                br.close();
                return;
            }

            // allocate space for the character grid
            grid = new char[gridSize][gridSize];
            // read the characters
            for (int row = 0; row < gridSize; row++) {
                String line = br.readLine();
                String[] parts = line.split(" ");
                for (int col = 0; col < gridSize; col++) {
                    grid[row][col] = parts[col].charAt(0);
                }
            }
        } catch (IOException e) {
            System.out.println("Exception while processing grid file.");
        }
    }

    int getGridSize(){
        return gridSize;
    }

    char getLetter(int row, int col){
        return grid[row][col];
    }

    // return a String representation of a Grid
    public String toString(){
        StringBuilder result = new StringBuilder();
        for(int row=0; row<gridSize; row++){
            for(int col=0; col<gridSize; col++){
                result.append(grid[row][col] + " ");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
