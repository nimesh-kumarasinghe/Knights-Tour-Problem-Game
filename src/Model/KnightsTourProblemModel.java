/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author ndila
 */

import Controller.KnightsTourProblemController;
import View.KnightsTourProblemUI;
import Model.DatabaseConnection;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class KnightsTourProblemModel {
    private KnightsTourProblemController controller;
    private KnightsTourProblemUI view;
    private DatabaseConnection dbConnection;

    public KnightsTourProblemModel(KnightsTourProblemController controller, KnightsTourProblemUI view) {
        this.controller = controller;
        this.view = view;
        this.dbConnection = dbConnection;
    }

    //public KnightsTourProblemModel() {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    //}
    
    public void saveUserData(String playerName, String answer){      
        try {
                Connection connection = dbConnection.getConnection(); // Get the database connection

                String sql = "INSERT INTO knight_tour (player_name, moves) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                //String playerName = controller.getPlayerName();
                preparedStatement.setString(1, playerName);

                // Serialize the answer matrix to a suitable format (JSON)
                //ObjectMapper objectMapper = new ObjectMapper();
                //String answerMatrixJson = objectMapper.writeValueAsString(controller.moveSquare);
                preparedStatement.setString(2, answer);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    // Data saved successfully
                    System.out.println("User data saved successfully.");
                    //return true;
                } else {
                    // Failed to save data
                    System.out.println("User data save failed.");
                    //return false;
                }
                //return true;
        } catch (SQLException e) {
            e.printStackTrace();
            //return false;
        }
//        } catch (IOException e) {
//            e.printStackTrace();
//            //return false;
//        }
    }
}


