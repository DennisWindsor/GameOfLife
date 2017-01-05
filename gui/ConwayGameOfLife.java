/*
 * Copyright (C) 2017 Dennis
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
package gui;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.Duration;
import Board.Board;

/**
 *
 * @author Dennis Windsor
 */
public class ConwayGameOfLife extends Application {
    static Board board;
    static double genSpeed = 0.1;
    static int cSize = 10;
    static Stage window;

    /**
     * Starts game with default 100 by 100 size board.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        board = new Board(100);
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Conway's Game of Life: Menu");
        int width = 400;
        int height = 235;
        ArrayList<Button> buttons = new ArrayList<>();
        
        // Setup all buttons
        createNewBoard(buttons);
        setSize(buttons);
        setSpeed(buttons);
        addToBoard(buttons);
        flipCell(buttons);
        showState(buttons);
        startGame(buttons);

        VBox layout = new VBox();
        layout.setSpacing(10);
        layout.setAlignment(Pos.CENTER);
        for (Button button : buttons){
            button.setPrefWidth(200);
            layout.getChildren().add(button);
        }
        Scene menu = new Scene(layout, width, height);
        window.setScene(menu);
        window.show();
    }
    
    // Setup up button to create new board
    private static void createNewBoard(ArrayList<Button> buttons){
        Button newBoard = new Button("Create New Board");
        buttons.add(newBoard);
        newBoard.setOnAction(e -> {
            Object size = GetInputWindow.getInt("Set Size of New Board",
                    "Enter new board size:");
            if (size != null){
                board = new Board((int) size);
            }
        });
    }
    
    //Setup button to change cell size
    private static void setSize(ArrayList<Button> buttons){
        Button setCellSize = new Button("Set Size of Cells");
        buttons.add(setCellSize);
        setCellSize.setOnAction(e-> {
            Object size = GetInputWindow.getInt("Set Size of Cells",
                    "Enter new cell size in pixels:");
            if (size != null){
                cSize = (int) size;
            }
        });
    }
    
    // Setup button to change speed of game
    private static void setSpeed(ArrayList<Button> buttons){
        Button setSpeed = new Button("Set Speed");
        buttons.add(setSpeed);
        setSpeed.setOnAction(e->{
           Object speed = GetInputWindow.getInt("Change Speed of Game", 
                   "Enter length of gerenation in milliseconds:");
           if (speed != null){
               genSpeed = (double)(int) speed / 1000;
           }
        });
    }
    
    // Setup button to add shape to board
    private static void addToBoard(ArrayList<Button> buttons){
        Button addShape = new Button("Add Shape");
        buttons.add(addShape);
        addShape.setOnAction(e->addShapes());
    }
    
    // Setup button to change state of cell
    private static void flipCell(ArrayList<Button> buttons){
        Button setCell = new Button("Change Cell Value");
        buttons.add(setCell);
        setCell.setOnAction(e->flipCells());
    }
    
    // Setup button to show current game state
    private static void showState(ArrayList<Button> buttons){
        Button checkState = new Button("Check Current State");
        buttons.add(checkState);
        checkState.setOnAction(e->drawBoard());
    }
        
    // Setup button to start game animation
    private static void startGame(ArrayList<Button> buttons){
        Button startAnimation = new Button("Start Game of Life");
        buttons.add(startAnimation);
        startAnimation.setOnAction(e->animateBoard());
    }
    
    // Draw game board
    private static void drawBoard(){
        Stage stage = new Stage();
        stage.setTitle("Game of Life - Current State");
        Group root = new Group();
        
        int size = board.getSize();

        GraphicsContext gc = boardSetup(stage, root, size);
        
        fillCells(gc, size);
        
        stage.show();
    }
    
    // Fill in live cells on the board
    private static void fillCells(GraphicsContext gc, int size){
        boolean[][] currBoard = board.getBoardState();
        gc.clearRect(0, 0, size*cSize, size*cSize);
        int i;
        int j;
        for (i = 0; i < size; i++)
            for (j=0; j < size; j++)
                if (currBoard[i][j])
                    gc.fillRect(i*cSize, j*cSize, cSize, cSize);
    }
    
    // Create animation for Game of Life
    private static void animateBoard(){
        Stage stage = new Stage();
        Group root = new Group();
        stage.setTitle("Game of Life");
        int size = board.getSize();
        
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount( Timeline.INDEFINITE );
        
        final long timeStart = System.currentTimeMillis();
        
        GraphicsContext gc = boardSetup(stage, root, size);

        KeyFrame kf = new KeyFrame(
            Duration.seconds(genSpeed), (ActionEvent ae) -> {
                fillCells(gc, size);
                board.update();
            });
        gameLoop.getKeyFrames().add( kf );
        gameLoop.play();
        
        stage.setOnCloseRequest(e->{
            gameLoop.stop();
        });
        stage.show();
    }
    
    // Board drawing setup
    private static GraphicsContext boardSetup(Stage stage, Group root, int size){
        Scene scene = new Scene(root);
        
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        
        Canvas canvas = new Canvas(size*cSize, size*cSize);
        root.getChildren().add(canvas);
        
        GraphicsContext gc = canvas.getGraphicsContext2D();
        return gc;
    }
    
    // Helper to add shape
    private static void addShapes(){
        Stage stage = new Stage();
        stage.setTitle("Choose position to add shape.");
        Group root = new Group();
        
        int size = board.getSize();

        GraphicsContext gc = boardSetup(stage, root, size);
        
        fillCells(gc, size);
        
        stage.show();
        
        root.setOnMouseClicked(e->{
            int x = (int) (e.getSceneX()/cSize);
            int y = (int) (e.getSceneY()/cSize);
            placeShape(x, y);
            stage.close();
        });
   }
    
    // Helper to add shape
    private static void placeShape(int x, int y){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Conway's Game of Life - Shape Selection");
        int height = 60;
        int width = 300;
        
        GridPane grid = new GridPane();
        
        Label label1 = new Label("Choose shape to add: ");
        GridPane.setConstraints(label1, 0, 0);
        ChoiceBox<String> choices = new ChoiceBox<>();
        GridPane.setConstraints(choices, 1, 0);
        choices.setPrefWidth(width/2);
        
        String[] shapes = board.getShapes();
        choices.getItems().addAll(Arrays.asList(shapes));
        
        Button confirm = new Button("Confirm");
        confirm.setOnAction(e->{
           board.addShape(choices.getValue(), x, y);
           stage.close();
        });
        GridPane.setConstraints(confirm, 0, 1);
        confirm.setPrefWidth(width/2);
        Button cancel = new Button("Cancel");
        cancel.setOnAction(e-> {
                stage.close();
        });
        cancel.setPrefWidth(width/2);
        GridPane.setConstraints(cancel, 1, 1);
        grid.getChildren().addAll(label1, choices, confirm, cancel);
        
        Scene scene = new Scene(grid, width, height);
        stage.setScene(scene);
        stage.show();
    }
    
    // Helper to flip value of single cell
    private static void flipCells(){
        Stage stage = new Stage();
        stage.setTitle("Click cells to change value.");
        Group root = new Group();
        
        int size = board.getSize();

        GraphicsContext gc = boardSetup(stage, root, size);
        
        fillCells(gc, size);
        
        root.setOnMouseClicked(e->{
            int x = (int) (e.getSceneX()/cSize);
            int y = (int) (e.getSceneY()/cSize);
            board.flipCell(x, y);
            gc.fillRect(x*cSize, y*cSize, cSize, cSize);
        });
        
        stage.show();
    }
}
