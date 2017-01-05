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

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Generic error message class.
 * 
 * @author Dennis Windsor
 */
public class ErrorBox {
    
    /**
     * Creates a generic error message given a title and message for the error
     * box and message to be displayed.
     * 
     * @param title     Title for error box
     * @param message   Message to be displayed
     */
    public static void display(String title, String message) {
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);

        Label label = new Label(message);
        Button close = new Button("Close");
        close.setOnAction(e -> stage.close());

        VBox layout = new VBox();
        layout.getChildren().addAll(label, close);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.showAndWait();
    }

}
