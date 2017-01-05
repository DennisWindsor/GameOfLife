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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Class to create a window to get an input value.
 *
 * @author Dennis Windsor
 */
public class GetInputWindow {
     static Object returnValue;
    
     /**
      * Displays a window to get an integer value with a given title and prompt.
      * 
      * @param title    The desired title of the window
      * @param message  The input prompt to be displayed
      * @return Either the integer value entered, or null if the cancel button
      *         is pressed
      */
    public static Object getInt(String title, String message){
        
        // Basic setup - refactor if more functions added
        Stage window = new Stage();
        window.setTitle(title);
        window.initModality(Modality.APPLICATION_MODAL);
        
        // ArrayList to simplify addind elements - refactor when improving
        // formatting
        ArrayList<Node> elements = new ArrayList<>();
        
        GridPane grid = new GridPane();
        int height = 60;
        int width = 450;
        Label label = new Label(message);
        GridPane.setConstraints(label, 0, 0);
        elements.add(label);
        TextField value = new TextField();
        GridPane.setConstraints(value, 1, 0);
        elements.add(value);
        Button confirm = new Button("Confirm");
                confirm.setOnAction(e -> {
            if (isInt(value.getText())){
                window.close();
            }else{
                ErrorBox.display("Error", "That is not a valid number.");
            }
        });
        confirm.setPrefWidth(width/2);
        elements.add(confirm);
        GridPane.setConstraints(confirm, 0, 1);
        Button cancel = new Button("Cancel"); 
        cancel.setPrefWidth(width/2);
        cancel.setOnAction(e -> {
            returnValue = null;
            window.close();
        });
        elements.add(cancel);
        GridPane.setConstraints(cancel, 1, 1);
        
        //VBox layout = new VBox();
        
        elements.forEach((element) -> {
            grid.getChildren().add(element);
         });
        
        Scene scene = new Scene(grid, width, height);
        window.setScene(scene);
        window.showAndWait();
        
        return returnValue;
    }
    
    // Check value given is an integer.
    private static boolean isInt(String value){
        try{
            int number = Integer.parseInt(value);
            returnValue = number;
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }  
}
