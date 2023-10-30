
import Model.KnightsTourProblemModel;
import Controller.KnightsTourProblemController;
import View.KnightsTourProblemUI;
import java.util.Arrays;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ndila
 */
public class Main {
    public static void main(String[] args) {
        KnightsTourProblemController controller = new KnightsTourProblemController(8); // Initialize with the desired chessboard size
        KnightsTourProblemUI view = new KnightsTourProblemUI(controller);
        KnightsTourProblemModel model = new KnightsTourProblemModel(controller, view);

        view.setVisible(true);
        view.inputName();
        controller.getMetrixAnswer();
    }
}


