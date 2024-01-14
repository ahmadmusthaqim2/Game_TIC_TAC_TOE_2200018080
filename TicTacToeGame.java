import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeGame extends JFrame implements ActionListener {

    private JButton[][] buttons;
    private boolean playerTurn;
    private boolean vsComputer;
    private String currentPlayer;
    private int player1Score;
    private int player2Score;
    private JLabel player1ScoreLabel;
    private JLabel player2ScoreLabel;
    private JComboBox<String> difficultyComboBox;
    private JButton vsComputerButton;
    private JButton vsPlayerButton;

    public TicTacToeGame() {
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(45, 52, 54)); // Background color

        buttons = new JButton[3][3];
        playerTurn = true;
        vsComputer = false;
        currentPlayer = "X"; // Default player is X
        player1Score = 0;
        player2Score = 0;

        initializeButtons();
        setupUI();
    }

    private void initializeButtons() {
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(3, 3));
        gamePanel.setBackground(new Color(45, 52, 54)); // Background color

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
                buttons[i][j].addActionListener(this);
                buttons[i][j].setBackground(new Color(52, 73, 94)); // Button color
                buttons[i][j].setForeground(Color.WHITE); // Text color
                gamePanel.add(buttons[i][j]);
            }
        }

        add(gamePanel, BorderLayout.CENTER);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
                resetScore();
                enableVsButtons();
            }
        });
        resetButton.setBackground(new Color(231, 76, 60)); // Reset button color
        resetButton.setForeground(Color.WHITE); // Text color

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(resetButton);
        controlPanel.setBackground(new Color(45, 52, 54)); // Background color

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayout(2, 2));
        player1ScoreLabel = new JLabel("Player 1: 0");
        player1ScoreLabel.setForeground(Color.WHITE);
        scorePanel.add(player1ScoreLabel);
        player2ScoreLabel = new JLabel("Player 2: 0");
        player2ScoreLabel.setForeground(Color.WHITE);
        scorePanel.add(player2ScoreLabel);
        scorePanel.setBackground(new Color(45, 52, 54)); // Background color

        // Add difficulty selection
        JPanel difficultyPanel = new JPanel();
        difficultyPanel.setLayout(new FlowLayout());
        difficultyPanel.setBackground(new Color(45, 52, 54)); // Background color
        String[] difficultyOptions = {"Easy", "Normal", "Hard"};
        difficultyComboBox = new JComboBox<>(difficultyOptions);
        difficultyPanel.add(new JLabel("Difficulty:"));
        difficultyPanel.add(difficultyComboBox);

        // Add player vs computer button
        vsComputerButton = new JButton("Vs Computer");
        vsComputerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vsComputer = true;
                vsComputerButton.setEnabled(false);
                vsPlayerButton.setEnabled(false);
                resetGame();
            }
        });
        vsComputerButton.setBackground(new Color(52, 152, 219)); // Button color
        vsComputerButton.setForeground(Color.WHITE); // Text color

        // Add player vs player button
        vsPlayerButton = new JButton("Vs Player");
        vsPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vsComputer = false;
                vsComputerButton.setEnabled(false);
                vsPlayerButton.setEnabled(false);
                resetGame();
            }
        });
        vsPlayerButton.setBackground(new Color(52, 152, 219)); // Button color
        vsPlayerButton.setForeground(Color.WHITE); // Text color

        // Add buttons to control panel
        controlPanel.add(vsComputerButton);
        controlPanel.add(vsPlayerButton);

        // Add panels to the frame
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(scorePanel, BorderLayout.WEST);
        topPanel.add(difficultyPanel, BorderLayout.EAST);
        topPanel.setBackground(new Color(45, 52, 54)); // Background color

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(topPanel, BorderLayout.NORTH);
        bottomPanel.add(controlPanel, BorderLayout.SOUTH);
        bottomPanel.setBackground(new Color(45, 52, 54)); // Background color

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupUI() {
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        if (vsComputer) {
            handleVsComputer(clickedButton);
        } else {
            handlePlayerVsPlayer(clickedButton);
        }
    }

    private void handleVsComputer(JButton clickedButton) {
        if (playerTurn) {
            if (clickedButton.getText() != null && clickedButton.getText().equals("")) {
                clickedButton.setText(currentPlayer);
                playerTurn = false;
                if (!checkWin(currentPlayer) && !checkDraw()) {
                    computerMove();
                }
            }
        }
    }

    private void handlePlayerVsPlayer(JButton clickedButton) {
        if (clickedButton.getText() != null && clickedButton.getText().equals("")) {
            clickedButton.setText(currentPlayer);
            if (checkWin(currentPlayer) || checkDraw()) {
                updateScoreLabels();
                return;
            }
            togglePlayer();
        }
    }

    private void togglePlayer() {
        if (currentPlayer != null) {
            currentPlayer = currentPlayer.equals("X") ? "O" : "X";
        }
    }

    private void computerMove() {
        int row, col;
        do {
            row = (int) (Math.random() * 3);
            col = (int) (Math.random() * 3);
        } while (!buttons[row][col].getText().equals(""));
        buttons[row][col].setText("O");
        playerTurn = true;
        checkWin("O");
    }

    private boolean checkWin(String player) {
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText() != null &&
                    buttons[i][1].getText() != null &&
                    buttons[i][2].getText() != null &&
                    buttons[i][0].getText().equals(player) &&
                    buttons[i][1].getText().equals(player) &&
                    buttons[i][2].getText().equals(player)) {
                announceWinner(player);
                return true;
            }
            if (buttons[0][i].getText() != null &&
                    buttons[1][i].getText() != null &&
                    buttons[2][i].getText() != null &&
                    buttons[0][i].getText().equals(player) &&
                    buttons[1][i].getText().equals(player) &&
                    buttons[2][i].getText().equals(player)) {
                announceWinner(player);
                return true;
            }
        }
        if (buttons[0][0].getText() != null &&
                buttons[1][1].getText() != null &&
                buttons[2][2].getText() != null &&
                buttons[0][0].getText().equals(player) &&
                buttons[1][1].getText().equals(player) &&
                buttons[2][2].getText().equals(player)) {
            announceWinner(player);
            return true;
        }
        if (buttons[0][2].getText() != null &&
                buttons[1][1].getText() != null &&
                buttons[2][0].getText() != null &&
                buttons[0][2].getText().equals(player) &&
                buttons[1][1].getText().equals(player) &&
                buttons[2][0].getText().equals(player)) {
            announceWinner(player);
            return true;
        }

        return false;
    }

    private boolean checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        JOptionPane.showMessageDialog(this, "The game is a draw!");
        resetGame();
        return true;
    }

    private void announceWinner(String player) {
        if (vsComputer) {
            if (player.equals("X")) {
                player1Score++;
            } else {
                player2Score++;
            }
        } else {
            if (currentPlayer.equals("X")) {
                player1Score++;
            } else {
                player2Score++;
            }
        }
        updateScoreLabels();
        JOptionPane.showMessageDialog(this, "Player " + player + " wins!");
        resetGame();
    }

    private void updateScoreLabels() {
        player1ScoreLabel.setText("Player 1: " + player1Score);
        player2ScoreLabel.setText("Player 2: " + player2Score);
    }

    private void resetGame() {
        currentPlayer = "X";
        playerTurn = true;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
    }

    private void resetScore() {
        player1Score = 0;
        player2Score = 0;
        updateScoreLabels();
    }

    private void enableVsButtons() {
        vsComputerButton.setEnabled(true);
        vsPlayerButton.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TicTacToeGame();
        });
    }
}
