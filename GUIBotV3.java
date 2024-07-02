import java.util.*;
import java.io.File;
import java.util.Random;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GUIBotV3 extends JFrame implements ActionListener {
    int helloCount;  // count of "Hello" to detect irritation

    // GUI declarations
    JButton b1, b2, b3;
    JTextField t1;
    JTextArea t2; // multi-line text box
    JLabel lb1, lb2;

    HashMap<String, String> knowledge;
    String lastBotQuestion;
    String userName;

    public GUIBotV3(String t) { // constructor accepts window title
        // GUI Window settings
        super(t); // send window title to super class constructor
        helloCount = 0;

        // Set the window's background color to a custom color
        getContentPane().setBackground(new Color(0xDDEEFF));
        setLayout(new BorderLayout(10, 10));

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        headerPanel.setBackground(new Color(0xDDEEFF));

        lb2 = new JLabel("Virtual Assistant for Bank of Lanka");
        lb2.setFont(new Font("Arial", Font.BOLD, 20));
        lb2.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(lb2);

        face(headerPanel);
        add(headerPanel, BorderLayout.NORTH);

        // Center panel for chat area
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerPanel.setBackground(new Color(0xDDEEFF));

        t2 = new JTextArea(15, 40); // create multiline text box
        t2.setLineWrap(true);
        t2.setWrapStyleWord(true);
        t2.setEditable(false);
        t2.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(t2);
        centerPanel.add(scrollPane);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel for input area
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        bottomPanel.setBackground(new Color(0xDDEEFF));

        lb1 = new JLabel("User ");
        lb1.setFont(new Font("Arial", Font.PLAIN, 14));
        bottomPanel.add(lb1); // show label box

        t1 = new JTextField(25);
        t1.setFont(new Font("Arial", Font.PLAIN, 14));
        bottomPanel.add(t1);

        ImageIcon submitIcon = new ImageIcon("send.png"); // Change the path to your image file
        b1 = new JButton(submitIcon); // create button with image
        b1.addActionListener(this);
        b1.setToolTipText("Send message");
        bottomPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Add space between buttons
        bottomPanel.add(b1); // show button

        ImageIcon clearIcon = new ImageIcon("clear.png"); // Change the path to your image file
        b2 = new JButton(clearIcon); // create button with image
        b2.addActionListener(this);
        b2.setToolTipText("Clear chat");
        bottomPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Add space between buttons
        bottomPanel.add(b2); // show button

        ImageIcon newChatIcon = new ImageIcon("newchat.png"); // Change the path to your image file
        b3 = new JButton(newChatIcon); // create button with image
        b3.addActionListener(this);
        b3.setToolTipText("Start new chat");
        bottomPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Add space between buttons
        bottomPanel.add(b3); // show button

        add(bottomPanel, BorderLayout.SOUTH);

        // Hash map settings
        knowledge = new HashMap<>();
        knowledge.put("hi", "Hello... Pleased to meet you!");
        knowledge.put("hello", "Hello! Nice to meet you!");
        knowledge.put("good morning", "Good Morning!");
        knowledge.put("i need advice", "Please visit WWW.BOL.LK for advices or contact 011 526 8899");
        knowledge.put("your name", "My name is Tina. What is your name?");
        knowledge.put("what is your name", "My name is Tina. What is your name?");
        knowledge.put("good evening", "Good evening!");
        knowledge.put("thank you", "You're welcome!");
        knowledge.put("bye", "Good bye! ");
        knowledge.put("i'm good", "Do you need any help? If yes, please ask.");
    }

    public void face(JPanel panel) {
        // image
        try {
            BufferedImage myPicture = ImageIO.read(new File("Botimg.jpg"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(picLabel);
        } catch (Exception e) {
            System.out.println("Image file not found");
        }
    }

    public void answer(String question) {
        String lowercaseQuestion = question.toLowerCase().trim();

        // If the bot previously asked for the user's name
        if (lastBotQuestion != null && lastBotQuestion.equals("What is your name?")) {
            userName = question; // Store the user's name
            appendMessage("Bot: Nice to meet you, " + userName + "! How are you today?");
            lastBotQuestion = null; // Reset the lastBotQuestion
            return;
        }

        String response = getClosestMatch(lowercaseQuestion);
        if (response != null) {
            if (lowercaseQuestion.equals("bye")) {
                response += userName != null ? " " + userName : "";
            } else if (lowercaseQuestion.equals("your name") || lowercaseQuestion.equals("what is your name")) {
                response += userName != null ? " It's nice to talk to you again, " + userName + "!" : "";
            }
            appendMessage("Bot: " + response);
            if (response.contains("What is your name?")) {
                lastBotQuestion = "What is your name?"; // Set the lastBotQuestion to track the context
            }
            return;
        }

        if (lowercaseQuestion.contains("hello")) {
            String[] A = {"Hi!", "Hello!", "Hello Dear", "Hey, nice to see you"};
            Random r1 = new Random(); // make random number object
            int n = r1.nextInt(4); // generate random number
            appendMessage("Bot: " + A[n]);

        } else if (lowercaseQuestion.contains("how are you")) {
            helloCount++;
            if (helloCount > 5) {
                appendMessage("Bot: You are bothering me.");
            } else {
                String[] B = {"I'm fine", "I am okay", "Not bad", "Good", "Alright"};
                Random r1 = new Random(); // make random number object
                int n = r1.nextInt(5); // generate random number
                appendMessage("Bot: " + B[n]);
            }
        } else if (lowercaseQuestion.contains("loans")) {
            showLoans();
        } else if (lowercaseQuestion.contains("insurances")) {
            showInsurances();
        } else if (lowercaseQuestion.contains("branches")) {
            showBranches();
        } else {
            appendMessage("Bot: I'm not sure how to respond to that.");
        }
    }

    public void showLoans() {
        try {
            File obj = new File("loans.txt");
            Scanner s = new Scanner(obj);
            while (s.hasNextLine()) {
                String data = s.nextLine();
                appendMessage("Bot: " + data);
            }
            s.close();
        } catch (Exception e) {
            appendMessage("Bot: File not found.");
        }
    }

    public void showInsurances() {
        try {
            File obj = new File("Insurances.txt");
            Scanner s = new Scanner(obj);
            while (s.hasNextLine()) {
                String data = s.nextLine();
                appendMessage("Bot: " + data);
            }
            s.close();
        } catch (Exception e) {
            appendMessage("Bot: File not found.");
        }
    }

    public void showBranches() {
        try {
            File obj = new File("branches.txt");
            Scanner s = new Scanner(obj);
            while (s.hasNextLine()) {
                String data = s.nextLine();
                appendMessage("Bot: " + data);
            }
            s.close();
        } catch (Exception e) {
            appendMessage("Bot: Branches file not found.");
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b1) {
            String userQuery = t1.getText();
            appendMessage("User: " + userQuery);
            answer(userQuery); // call answer method
            t1.setText(""); // clear the input field
        }
        if (e.getSource() == b2) {
            t1.setText("");
            t2.setText("");
            lastBotQuestion = null; // Reset lastBotQuestion on clear
        }
        if (e.getSource() == b3) {
            t1.setText("");
            t2.setText("");
            lastBotQuestion = null; // Reset lastBotQuestion on new chat
            userName = null;
            helloCount = 0;
            appendMessage("Bot: New chat started. How can I assist you today?");
        }
    }

    private void appendMessage(String message) {
        t2.append(message + "\n");
        t2.setCaretPosition(t2.getDocument().getLength()); // Auto-scroll to the bottom
    }

    private String getClosestMatch(String input) {
        int threshold = 3; // set the threshold for Levenshtein distance
        String closestMatch = null;
        int closestDistance = Integer.MAX_VALUE;

        for (String key : knowledge.keySet()) {
            int distance = levenshteinDistance(input, key);
            if (distance < closestDistance && distance <= threshold) {
                closestDistance = distance;
                closestMatch = knowledge.get(key);
            }
        }
        return closestMatch;
    }

    private int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1),
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                }
            }
        }
        return dp[a.length()][b.length()];
    }

    public static void main(String args[]) {
        GUIBotV3 d1 = new GUIBotV3("Virtual Assistant for Bank of Lanka");
        d1.setSize(600, 600); // window size
        d1.setVisible(true); // show window frame
        d1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit
    }
} // end GUI demo
