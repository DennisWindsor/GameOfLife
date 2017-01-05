/*
 * Copyright (C) 2017 Dennis Windsor
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package Board;

import java.util.HashMap;

/**
 * This class consists of the representation of the board in Conway's Game of 
 * Life along with methods necessary to manipulate the board.
 * 
 * @author Dennis Windsor
 */
public class Board {
    // Basic board state
    private final int size;
    private boolean[][] board;
    // Collection of shapes that can be added to board
    private final HashMap<String, int[]> shapes;
    
    /**
     * Create a new board which is size by size large. All cells are initially
     * empty.
     * 
     * @param size length of one row of the grid
     * @exception IllegalArgumentException if size is negative
     */
    public Board(int size) throws IllegalArgumentException{
        if (size < 0)
            throw new IllegalArgumentException("Size must be positive.");
        this.size = size;
        int i, j;
        board = new boolean[size][size];
        for (i = 0; i < size; i++)
            for (j = 0; j < size; j ++)
                board[i][j] = false;
        shapes = new HashMap<>();
        addShapes();
    }

    // Populate shapes hashtable.
    private void addShapes() {
        // Still lifes
        shapes.put("block", new int[] {0,0,0,1,1,0,1,1});
        shapes.put("beehive", new int[] {0,1,0,2,1,0,1,3,2,1,2,2});
        shapes.put("loaf", new int[] {0,1,0,2,1,0,1,3,2,1,2,3,3,2});
        shapes.put("boat", new int[] {0,0,0,1,1,0,1,2,2,1});
        shapes.put("tub", new int[] {0,1,1,0,1,2,2,1});
        
        // Oscillators
        shapes.put("blinker", new int[] {0,1,1,1,2,1});
        shapes.put("toad", new int[] {1,1,1,2,1,3,2,0,2,1,2,2});
        shapes.put("beacon", new int[] {0,0,0,1,1,0,2,3,3,2,3,3});
        
        // Spaceships
        shapes.put("glider", new int[] {0,1,1,2,2,0,2,1,2,2});
        shapes.put("LWS", new int[] {0,0,0,3,1,4,2,0,2,4,3,1,3,2,3,3,3,4});

        // Methusalahs
        shapes.put("r-pentomino", new int[] {0,1,0,2,1,0,1,1,2,1});
        shapes.put("diehard", new int[] {0,6,1,0,1,1,2,1,2,5,2,6,2,7});
        shapes.put("acorn", new int[] {0,1,1,3,2,0,2,1,2,4,2,5,2,6});
    }
    
    /**
     * Updates the game board by advancing it one generation according to the 
     * following rules:
     * - A live cell with fewer than two live neighbours dies.
     * - A live cell with two or three live neighbours lives on.
     * - A live cell with more than three live neighbours dies.
     * - A dead cell with exactly three live neighbours becomes a live cell.
     */
    public void update(){
        boolean[][] new_board = new boolean[size][size];
        int i, j;
        for (i = 0; i < size; i++){
            for (j = 0; j < size; j++){
                int neighbours = getNeighbours(i, j);
                if (this.board[i][j]){
                  if (neighbours < 2)
                      new_board[i][j] = false;
                  else if (neighbours < 4)
                      new_board[i][j] = true;
                  else
                      new_board[i][j] = false;
                }
                else
                    if (neighbours == 3)
                        new_board[i][j] = true;
            }
        }
        this.board = new_board;
    }
    
    // Calculate number of live neighbours. 
    private int getNeighbours(int vert, int hor){
        int count = 0;
        int i, j;
        for (i = vert - 1; i < vert + 2; i++){
            for (j = hor - 1; j < hor + 2; j++){
                if (i > -1 && j > -1 && i < size && j < size && (i != vert 
                        || j != hor) && board[i][j]){
                    count++;
                }
            } 
        }
        return count;
    }
    
    /**
     * Returns an array of  Strings of the the names of all the shapes that can
     * be added to the board. This can be used for reference when using
     * addShape().
     * 
     * @return  An array of Strings containing the name of shapes that can be
     *          added to the board.
     */
    public String[] getShapes(){
        return (String[]) shapes.keySet().toArray(new String[shapes.size()]);
    }
    
    /**
     * Returns the current state of the board.
     * 
     * @return a 2-d boolean array representing the current board state
     */
    public boolean[][] getBoardState(){
        boolean[][] boardCopy = new boolean[size][size];
        int i, j;
        for (i = 0; i < size; i++)
            for (j = 0; j < size; j++)
                boardCopy[i][j] = board[i][j];
        return boardCopy;
    }
    
    /**
     * Changes the state of a single cell at row x, column y on the board.
     * 
     * @param x Row of cell to be changed
     * @param y Column of cell to be changed
     */
    public void flipCell(int x, int y){
        board[x][y] = !board[x][y];
    }
    
    /**
     * Add a given shape to the board.
     * 
     * @param shape String representation of shape to be added
     * @param x     row of upper right corner where shape should be added
     * @param y     column of upper right corner where shape should be added
     */
    public void addShape(String shape, int x, int y){
        int[] currShape = (int[]) shapes.get(shape);
        int i;
        for (i=0; i<currShape.length/2; i++){
            this.board[x+currShape[2*i]][y+currShape[2*i+1]] = true;
        }
    }
    
    public int getSize(){
        return this.size;
    }
}