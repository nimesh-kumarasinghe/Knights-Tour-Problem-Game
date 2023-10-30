/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.KnightsTourProblemController;
import Model.KnightsTourProblemModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import CustomFonts.CustomFonts;

/**
 *
 * @author ndila
 */
public class KnightsTourProblemUI extends javax.swing.JFrame {

    /**
     * Creates new form KnightsTourProblemUI
     */
    
    private KnightsTourProblemController controller;
    private KnightsTourProblemModel model;
    private JPanel chessboardPanel;
    private JButton[][] squareButtons;
    private BufferedImage knightImage;
    private BufferedImage frameImage,backgroundImage;
    private ImageIcon knightIcon;
    private ImageIcon frameIcon, backgroundIcon;
    public  String playerName;
    private BufferedImage boardFrame;
    private ImageIcon boardFrameImage;
    public CustomFonts cus_font = new CustomFonts();
    
    public KnightsTourProblemUI(KnightsTourProblemController controller) {
        initComponents();
        this.controller = controller;
        this.model = new KnightsTourProblemModel(controller, this);
        this.chessboardPanel = new JPanel(new GridLayout(controller.getSize(), controller.getSize()));
        this.squareButtons = new JButton[controller.getSize()][controller.getSize()];  
 
        try {
            knightImage = ImageIO.read(getClass().getResource("Knight3.png"));
            int imageSize = 68;
            knightIcon = new ImageIcon(knightImage.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH));
            
            frameImage = ImageIO.read(getClass().getResource("Frame-4.png"));
            int imgSize = 960;
            frameIcon = new ImageIcon(frameImage.getScaledInstance(imgSize, imgSize, Image.SCALE_SMOOTH));
            jLabel1.setIcon(frameIcon);
            
            backgroundImage = ImageIO.read(getClass().getResource("background3.png"));
            backgroundIcon = new ImageIcon(backgroundImage.getScaledInstance(1920, 1080, Image.SCALE_SMOOTH));
            lbl_background.setIcon(backgroundIcon);
            
            BufferedImage closeImage = ImageIO.read(getClass().getResource("closeIcon.png"));
            ImageIcon closedIcon = new ImageIcon(closeImage.getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            lbl_close.setIcon(closedIcon);
            
            BufferedImage homeImage = ImageIO.read(getClass().getResource("homeIcon.png"));
            ImageIcon homeIcon = new ImageIcon(homeImage.getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            lbl_mainMenu.setIcon(homeIcon);
            
            BufferedImage backHorseImage = ImageIO.read(getClass().getResource("horses.png"));
            ImageIcon backHorseIcon = new ImageIcon(backHorseImage.getScaledInstance(600, 600, Image.SCALE_SMOOTH));
            lbl_horses.setIcon(backHorseIcon);
            
            BufferedImage resetImage = ImageIO.read(getClass().getResource("ResetButton.png"));
            ImageIcon resetIcon = new ImageIcon(resetImage.getScaledInstance(190, 45, Image.SCALE_SMOOTH));
            lbl_reset.setIcon(resetIcon);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        setTitle("Knight's Tour Problem");
        initializeView();
        
        
        for (int i = 0; i < controller.getSize(); i++) {
            for (int j = 0; j < controller.getSize(); j++) {
                addSquareButtonListener(new SquareButtonListener(i, j), i, j);
            }
        }
    }
    public void initializeView() {
        boolean isWhite = true; // Flag to determine the square color (start with white)

        for (int i = 0; i < controller.getSize(); i++) {
            for (int j = 0; j < controller.getSize(); j++) {
                Font customFont = new Font("Arial", Font.BOLD, 27);
                squareButtons[i][j] = new JButton();
                //squareButtons[i][j].setPreferredSize(new Dimension(75, 75));
                //squareButtons[i][j].setBorder(BorderFactory.createEmptyBorder());
                squareButtons[i][j].setFont(customFont);
                squareButtons[i][j].setBorder(BorderFactory.createRaisedSoftBevelBorder());

                // Set the background color to black or white
                if (isWhite) {
                    squareButtons[i][j].setBackground(new Color(111,155,83)); //Green
                } else {
                    squareButtons[i][j].setBackground(new Color(238,238,210)); //Beige
                }
                isWhite = !isWhite; // Toggle the square color

                chessboardPanel.add(squareButtons[i][j]);
            }
            isWhite = !isWhite; // Toggle the square color for the next row
        }

        panel_chessBoard.setLayout(new BorderLayout());
        panel_chessBoard.add(chessboardPanel, BorderLayout.CENTER);
    }
    
    public void updateView() {    
        int[][] board = controller.getBoard();
        for (int i = 0; i < controller.getSize(); i++) {
            for (int j = 0; j < controller.getSize(); j++) {
                squareButtons[i][j].setIcon(null); // Clear any previous icon
            }
        }
        System.out.println("Size: "+controller.getSize());

        // Set the knight image icon on the selected square
        int moveCount = controller.getMoveCount();
        if (moveCount > 0) {
            Point currentMove = controller.getSolution().get(moveCount - 1);
            int x = (int) currentMove.getX();
            int y = (int) currentMove.getY();
            squareButtons[x][y].setIcon(knightIcon);
            squareButtons[x][y].setFocusPainted(false);
            squareButtons[x][y].setFocusable(false);
          

            // Set the number on the selected square
            squareButtons[x][y].setText(Integer.toString(moveCount));
            lbl_moveCount.setText(Integer.toString(moveCount));
        } 
        System.out.println("Move count: "+moveCount);
    }

    public void addSquareButtonListener(ActionListener listener, int x, int y) {
        squareButtons[x][y].addActionListener(listener);
    }

    private class SquareButtonListener implements ActionListener {
        private int x;
        private int y;
        
        public SquareButtonListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //controller.printMoveCounts();
            if (controller.makeMove(x, y)) {
                updateView();
                clearHighlighting();
                // Calculate possible movement squares
                List<Point> possibleMoves = controller.calculatePossibleMoves(x, y);

                // Highlight the possible movement squares
                highlightPossibleMoves(possibleMoves);

                if (controller.getMoveCount() == controller.getSize() * controller.getSize()) {
                    JOptionPane.showMessageDialog(null, "Congratulations! Knight's Tour Completed.", "You Win", JOptionPane.INFORMATION_MESSAGE);
                    model.saveUserData(playerName,controller.getMetrixAnswer());
                    controller.reset();
                    resetChessboard();
                    lbl_moveCount.setText("0");
                }
                    
            } else {
                JOptionPane.showMessageDialog(null, "Invalid move. Try again!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
            if(controller.hasNoEmptyMoveSquares(x, y) && controller.getMoveCount() != 64){
                    JOptionPane.showMessageDialog(null, "Game Over!", "Game Over", JOptionPane.ERROR_MESSAGE);
                    controller.reset();
                    resetChessboard();
                    lbl_moveCount.setText("0");
            }
        }
    }
    
    private void highlightPossibleMoves(List<Point> possibleMoves) {
        for (Point move : possibleMoves) {
            int moveX = (int) move.getX();
            int moveY = (int) move.getY();
            squareButtons[moveX][moveY].setBackground(new Color(251, 216, 93)); // Yellow
        }
    }
    private Color getSquareColor(int x, int y) {
        // Check if the square is at an even or odd row/column
        if ((x + y) % 2 == 0) {
            return new Color(111,155,83); //Beige
        } else {
            return new Color(238,238,210); //Beige
        }
    }
    
    public void clearHighlighting() {
        for (int i = 0; i < controller.getSize(); i++) {
            for (int j = 0; j < controller.getSize(); j++) {
                squareButtons[i][j].setBackground(getSquareColor(i, j));
            }
        }
    }
    
     public void clearChessboard() {
        for (int i = 0; i < controller.getSize(); i++) {
            for (int j = 0; j < controller.getSize(); j++) {
                squareButtons[i][j].setIcon(null);
                squareButtons[i][j].setText("");
            }
        }
    }
     
    public void resetChessboard() {
        clearChessboard(); 
        inputName();
    }
    
    public void inputName(){
        while (true) {
            JTextField nameField = new JTextField();
            JComponent[] inputs = new JComponent[]{
                new JLabel("Enter your name:"),
                nameField
            };

            int result = JOptionPane.showOptionDialog(
                null,
                inputs,
                "Player Name Input",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Start", "Exit"}, // Button labels
                "Start" // Default button
            );

            if (result == JOptionPane.OK_OPTION) {
                playerName = nameField.getText();
                if (playerName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Name should be entered to start!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if (!playerName.matches("^[a-zA-Z ]+$")) {
                    JOptionPane.showMessageDialog(null, "Name should contain only alphabet characters!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    lbl_playerName.setText(playerName);
                    System.out.println("Entered Name: " + playerName);
                    break; 
                }
            } else {
                System.out.println("User canceled the input.");
                System.exit(0); 
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_background = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        panel_chessBoard = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lbl_playerName = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lbl_moveCount = new javax.swing.JLabel();
        lbl_mainMenu = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lbl_close = new javax.swing.JLabel();
        lbl_horses = new javax.swing.JLabel();
        lbl_reset = new javax.swing.JLabel();
        lbl_background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 255, 204));
        setMinimumSize(new java.awt.Dimension(1920, 1080));
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel_background.setBackground(new java.awt.Color(207, 214, 228));
        panel_background.setMinimumSize(new java.awt.Dimension(1920, 1080));
        panel_background.setPreferredSize(new java.awt.Dimension(1920, 1080));
        panel_background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 51, 102));
        jPanel2.setMinimumSize(new java.awt.Dimension(960, 960));
        jPanel2.setPreferredSize(new java.awt.Dimension(960, 960));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel_chessBoard.setBackground(new java.awt.Color(204, 255, 255));
        panel_chessBoard.setMinimumSize(new java.awt.Dimension(900, 900));
        panel_chessBoard.setPreferredSize(new java.awt.Dimension(900, 900));
        panel_chessBoard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(panel_chessBoard, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 960, 960));

        panel_background.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, -1, -1));

        jLabel3.setBackground(new java.awt.Color(245, 231, 182));
        jLabel3.setFont(cus_font.komikz(90));
        jLabel3.setForeground(new java.awt.Color(238, 238, 210));
        jLabel3.setText("Problem");
        panel_background.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 250, -1, -1));

        jPanel3.setOpaque(false);
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Gadugi", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(238, 238, 210));
        jLabel2.setText("Player Name:");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 20, -1, -1));

        lbl_playerName.setFont(new java.awt.Font("Gadugi", 1, 36)); // NOI18N
        lbl_playerName.setForeground(new java.awt.Color(255, 204, 51));
        lbl_playerName.setAlignmentX(0.5F);
        jPanel3.add(lbl_playerName, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, -1, -1));

        jLabel4.setFont(new java.awt.Font("Gadugi", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(238, 238, 210));
        jLabel4.setText("Moves:");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        jLabel5.setFont(new java.awt.Font("Gadugi", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 204, 51));
        jLabel5.setText("/64");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 70, -1));

        lbl_moveCount.setFont(new java.awt.Font("Gadugi", 1, 36)); // NOI18N
        lbl_moveCount.setForeground(new java.awt.Color(255, 51, 51));
        lbl_moveCount.setText("0");
        jPanel3.add(lbl_moveCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, -1, -1));

        panel_background.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 390, -1, -1));

        lbl_mainMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_mainMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_mainMenuMouseClicked(evt);
            }
        });
        panel_background.add(lbl_mainMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(1727, 21, -1, -1));

        jLabel6.setBackground(new java.awt.Color(245, 231, 182));
        jLabel6.setFont(cus_font.komikz(110));
        jLabel6.setForeground(new java.awt.Color(238, 238, 210));
        jLabel6.setText("Knight's Tour");
        panel_background.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 130, -1, -1));

        lbl_close.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_closeMouseClicked(evt);
            }
        });
        panel_background.add(lbl_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(1820, 21, -1, -1));

        lbl_horses.setMaximumSize(new java.awt.Dimension(600, 600));
        lbl_horses.setMinimumSize(new java.awt.Dimension(600, 600));
        lbl_horses.setPreferredSize(new java.awt.Dimension(600, 600));
        panel_background.add(lbl_horses, new org.netbeans.lib.awtextra.AbsoluteConstraints(1290, 450, -1, -1));

        lbl_reset.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_reset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_resetMouseClicked(evt);
            }
        });
        panel_background.add(lbl_reset, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 600, -1, -1));

        lbl_background.setMaximumSize(new java.awt.Dimension(1920, 1080));
        lbl_background.setMinimumSize(new java.awt.Dimension(1920, 1080));
        lbl_background.setPreferredSize(new java.awt.Dimension(1920, 1080));
        panel_background.add(lbl_background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panel_background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lbl_mainMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_mainMenuMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_mainMenuMouseClicked

    private void lbl_closeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_closeMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lbl_closeMouseClicked

    private void lbl_resetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_resetMouseClicked
        clearHighlighting();
        controller.reset();
        resetChessboard();
        lbl_moveCount.setText("0");
    }//GEN-LAST:event_lbl_resetMouseClicked

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(KnightsTourProblemUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(KnightsTourProblemUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(KnightsTourProblemUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(KnightsTourProblemUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new KnightsTourProblemUI().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lbl_background;
    private javax.swing.JLabel lbl_close;
    private javax.swing.JLabel lbl_horses;
    private javax.swing.JLabel lbl_mainMenu;
    private javax.swing.JLabel lbl_moveCount;
    private javax.swing.JLabel lbl_playerName;
    private javax.swing.JLabel lbl_reset;
    private javax.swing.JPanel panel_background;
    private javax.swing.JPanel panel_chessBoard;
    // End of variables declaration//GEN-END:variables
}
