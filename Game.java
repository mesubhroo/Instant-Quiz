package Instant_Quiz.com;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;


public class Game {
    private static String userName = "";
    private static int score = 0;
    private static JLabel scoreLabel;
    private static JLabel questionLabel;
    private static JButton[] optionButtons = new JButton[4];
    private static JButton nextButton;
    private static JButton lifelineButton;
    private static Timer timer;
    private static JLabel timerLabel;
    private static int timerCount = 30;
    private static int currentQuestionIndex = 0;
    private static List<Question> questions = new ArrayList<>();
    private static boolean lifelineUsed = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::startGame);
    }

    private static void startGame() {
        userName = JOptionPane.showInputDialog(null, "Enter your name:", "User Name", JOptionPane.PLAIN_MESSAGE);
        if (userName == null || userName.trim().isEmpty()) userName = "Guest";

        prepareQuestions();

        JFrame myframe = new JFrame("Kaun Banega Crorepati");
        myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myframe.setSize(800, 500);
        myframe.setLayout(new BorderLayout());

        // --- TOP PANEL ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(19, 31, 217));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Kaun Banega Crorepati", SwingConstants.CENTER);
        titleLabel.setFont(new Font("MV Boli", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        scoreLabel = new JLabel("Welcome, " + userName + " | Score: " + score + " ");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        topPanel.add(scoreLabel, BorderLayout.EAST);

        // --- QUIZ PANEL ---
        JPanel quizPanel = new JPanel();
        quizPanel.setLayout(new BoxLayout(quizPanel, BoxLayout.Y_AXIS));
        quizPanel.setBackground(new Color(19, 31, 217));
        quizPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        questionLabel = new JLabel("");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        quizPanel.add(questionLabel);
        quizPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JButton();
            optionButtons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            optionButtons[i].setMaximumSize(new Dimension(300, 40));
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 16));
            int finalI = i;
            optionButtons[i].addActionListener(e -> checkAnswer(optionButtons[finalI]));
            quizPanel.add(optionButtons[i]);
            quizPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Lifeline Button
        lifelineButton = new JButton("Use 50-50 Lifeline");
        lifelineButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        lifelineButton.setFont(new Font("Arial", Font.BOLD, 14));
        lifelineButton.addActionListener(e -> useFiftyFifty());
        quizPanel.add(lifelineButton);
        quizPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Timer Label
        timerLabel = new JLabel("Time left: " + timerCount + "s");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        quizPanel.add(timerLabel);
        quizPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        nextButton = new JButton("Next Question");
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.setFont(new Font("Arial", Font.BOLD, 16));
        nextButton.setVisible(false);
        nextButton.addActionListener(e -> {
            currentQuestionIndex++;
            loadQuestion();
        });
        quizPanel.add(nextButton);

        myframe.add(topPanel, BorderLayout.NORTH);
        myframe.add(quizPanel, BorderLayout.CENTER);
        myframe.setVisible(true);

        loadQuestion();
    }

    private static void prepareQuestions() {
        questions.add(new Question("Who is the king of the jungle?", "Lion", "Tiger", "Elephant", "Leopard", "Lion"));
        questions.add(new Question("Which planet is known as the Red Planet?", "Earth", "Mars", "Venus", "Jupiter", "Mars"));
        questions.add(new Question("What is the capital of India?", "Mumbai", "Delhi", "Kolkata", "Chennai", "Delhi"));
        questions.add(new Question("Which animal is known as the ship of the desert?", "Camel", "Horse", "Elephant", "Donkey", "Camel"));
        questions.add(new Question("How many continents are there?", "5", "6", "7", "8", "7"));
    }

    private static void loadQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            JOptionPane.showMessageDialog(null, "Quiz Finished!\nYour final score: " + score);
            System.exit(0);
        }

        Question q = questions.get(currentQuestionIndex);
        questionLabel.setText("Q" + (currentQuestionIndex + 1) + ": " + q.getQuestion());

        optionButtons[0].setText(q.getOptionA());
        optionButtons[1].setText(q.getOptionB());
        optionButtons[2].setText(q.getOptionC());
        optionButtons[3].setText(q.getOptionD());

        for (JButton btn : optionButtons) {
            btn.setEnabled(true);
            btn.setBackground(Color.LIGHT_GRAY);
        }

        lifelineButton.setEnabled(!lifelineUsed);
        nextButton.setVisible(false);

        startTimer();
    }

    private static void startTimer() {
        timerCount = 30; // Reset timer to 30 seconds
        timerLabel.setText("Time left: " + timerCount + "s");

        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(1000, e -> {
            timerCount--;
            timerLabel.setText("Time left: " + timerCount + "s");

            if (timerCount <= 0) {
                timer.stop();
                checkAnswer(optionButtons[0]); // Automatically select the first option as wrong
            }
        });
        timer.start();
    }

    private static void checkAnswer(JButton selectedButton) {
        String selectedOption = selectedButton.getText();
        Question currentQuestion = questions.get(currentQuestionIndex);

        for (JButton btn : optionButtons) {
            btn.setEnabled(false);
            if (btn.getText().equals(currentQuestion.getAnswer())) {
                btn.setBackground(Color.GREEN);
            }
        }

        if (selectedOption.equals(currentQuestion.getAnswer())) {
            score += 10;
            playSound("correct.wav");
        } else {
            selectedButton.setBackground(Color.RED);
            playSound("wrong.wav");
        }

        scoreLabel.setText("Welcome, " + userName + " | Score: " + score + " ");
        nextButton.setVisible(true);

//        showAudiencePoll();
    }

    private static void useFiftyFifty() {
        lifelineUsed = true;
        lifelineButton.setEnabled(false);

        Question currentQuestion = questions.get(currentQuestionIndex);
        List<JButton> wrongButtons = new ArrayList<>();

        for (JButton btn : optionButtons) {
            if (!btn.getText().equals(currentQuestion.getAnswer())) {
                wrongButtons.add(btn);
            }
        }

        Collections.shuffle(wrongButtons);
        wrongButtons.get(0).setText("");
        wrongButtons.get(1).setText("");
        wrongButtons.get(0).setEnabled(false);
        wrongButtons.get(1).setEnabled(false);
    }

    private static void showAudiencePoll() {
        Random rand = new Random();
        int correctPercentage = rand.nextInt(40) + 40;
        int wrongPercentage = 100 - correctPercentage;

        JOptionPane.showMessageDialog(null, "Audience Poll: \nCorrect: " + correctPercentage + "% \nWrong: " + wrongPercentage + "%");
    }









    private static void playSound(String filename) {
        try {
            File file = new File("src/sounds/" + filename);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            System.err.println("Sound error: " + e.getMessage());
        }
    }
}


