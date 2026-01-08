import javax.swing.*;
import java.awt.*;

public class TicTacToeEasyAI {

    JFrame frame;
    JButton[] buttons = new JButton[9];
    boolean humanTurn = true;

    int humanScore = 0;
    int aiScore = 0;

    JLabel scoreLabel;

    public static void main(String[] args) {
        new TicTacToeEasyAI();
    }

    public TicTacToeEasyAI() {

        frame = new JFrame("Tic Tac Toe (Easy AI)");
        frame.setLayout(new BorderLayout());

        scoreLabel = new JLabel("Human (X): 0   AI (O): 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel board = new JPanel(new GridLayout(3, 3));
        Font font = new Font("Arial", Font.BOLD, 50);

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(font);

            int index = i;
            buttons[i].addActionListener(e -> {

                if (!humanTurn) return;
                if (!buttons[index].getText().equals("")) return;

                buttons[index].setText("X");
                humanTurn = false;

                if (checkGameEnd("X")) return;

                aiMove();

                checkGameEnd("O");
                humanTurn = true;
            });

            board.add(buttons[i]);
        }

        JButton restart = new JButton("Play Again");
        restart.addActionListener(e -> resetGame());

        frame.add(scoreLabel, BorderLayout.NORTH);
        frame.add(board, BorderLayout.CENTER);
        frame.add(restart, BorderLayout.SOUTH);

        frame.setSize(400, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    void aiMove() {

        int move;

        move = findWinningMove("O");
        if (move != -1) {
            buttons[move].setText("O");
            return;
        }

        move = findWinningMove("X");
        if (move != -1) {
            buttons[move].setText("O");
            return;
        }

        if (buttons[4].getText().equals("")) {
            buttons[4].setText("O");
            return;
        }

        int[] corners = {0, 2, 6, 8};
        for (int c : corners) {
            if (buttons[c].getText().equals("")) {
                buttons[c].setText("O");
                return;
            }
        }

        int[] empty = getEmptyCells();
        int random = empty[(int)(Math.random() * empty.length)];
        buttons[random].setText("O");
    }

    int findWinningMove(String symbol) {

        int[][] wins = {
            {0,1,2},{3,4,5},{6,7,8},
            {0,3,6},{1,4,7},{2,5,8},
            {0,4,8},{2,4,6}
        };

        for (int[] w : wins) {
            int count = 0;
            int emptyIndex = -1;

            for (int i : w) {
                if (buttons[i].getText().equals(symbol))
                    count++;
                else if (buttons[i].getText().equals(""))
                    emptyIndex = i;
            }

            if (count == 2 && emptyIndex != -1)
                return emptyIndex;
        }
        return -1;
    }

    int[] getEmptyCells() {

        int[] temp = new int[9];
        int count = 0;

        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().equals("")) {
                temp[count++] = i;
            }
        }

        int[] empty = new int[count];
        System.arraycopy(temp, 0, empty, 0, count);
        return empty;
    }

    boolean checkGameEnd(String player) {

        if (checkWin(player)) {

            if (player.equals("X")) humanScore++;
            else aiScore++;

            updateScoreLabel();

            JOptionPane.showMessageDialog(frame, player + " wins!");
            disableBoard();
            return true;
        }

        if (isDraw()) {
            JOptionPane.showMessageDialog(frame, "Draw!");
            disableBoard();
            return true;
        }
        return false;
    }

    void updateScoreLabel() {
        scoreLabel.setText("Human (X): " + humanScore + "   AI (O): " + aiScore);
    }

    boolean checkWin(String s) {

        int[][] wins = {
            {0,1,2},{3,4,5},{6,7,8},
            {0,3,6},{1,4,7},{2,5,8},
            {0,4,8},{2,4,6}
        };

        for (int[] w : wins) {
            if (buttons[w[0]].getText().equals(s) &&
                buttons[w[1]].getText().equals(s) &&
                buttons[w[2]].getText().equals(s)) {
                return true;
            }
        }
        return false;
    }

    boolean isDraw() {
        for (JButton b : buttons)
            if (b.getText().equals("")) return false;
        return true;
    }

    void disableBoard() {
        for (JButton b : buttons)
            b.setEnabled(false);
    }

    void resetGame() {
        for (JButton b : buttons) {
            b.setText("");
            b.setEnabled(true);
        }
        humanTurn = true;
    }
}
