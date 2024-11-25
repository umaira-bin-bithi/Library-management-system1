import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class EnhancedLibraryHomePageWithIcons {
    private JFrame frame;

    // Custom panel for background image
    static class CustomPanel extends JPanel {
        private Image backgroundImage;

        public CustomPanel(String imagePath) {
            try {
                backgroundImage = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                System.err.println("Error loading background image: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public EnhancedLibraryHomePageWithIcons() {
        createHomePage();
    }

    private void createHomePage() {
        // Main frame
        frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        // Left panel with background image and centered text
        CustomPanel leftPanel = new CustomPanel("images/library.jpg");
        leftPanel.setPreferredSize(new Dimension(600, frame.getHeight()));  // Set the width of the left panel (larger size)
        leftPanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'>Welcome to<br>Library Management System</div></html>", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        leftPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Right panel for forms
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        rightPanel.setBackground(new Color(245, 245, 245));
        rightPanel.setPreferredSize(new Dimension(300, frame.getHeight()));  // Set the width of the right panel (smaller size)

        // Create cards for Login and Sign-Up forms
        JPanel loginCard = createFormCardWithIcons("Login", "Username:", "Password:", "Login", "images/user.png");
        JPanel signUpCard = createSignUpFormWithIcons("Sign Up", "New Username:", "Password:", "Confirm Password:", "images/signup.png");

        // Add cards to the right panel
        rightPanel.add(loginCard);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        rightPanel.add(signUpCard);

        // Add panels to the main frame
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JPanel createFormCardWithIcons(String title, String field1, String field2, String buttonText, String iconPath) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Title
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(50, 50, 50));

        // Fields
        JLabel field1Label = new JLabel(field1);
        JTextField field1Input = new JTextField();
        JLabel field2Label = new JLabel(field2);
        JPasswordField field2Input = new JPasswordField();

        // Button with icon
        JButton actionButton = createStyledButtonWithIcon(buttonText, iconPath);

        // Add components to the card
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(field1Label);
        card.add(field1Input);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(field2Label);
        card.add(field2Input);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(actionButton);

        // Action listener for the buttons
        actionButton.addActionListener(e -> {
            if (title.equals("Login")) {
                String username = field1Input.getText();
                String password = new String(field2Input.getPassword());

                // Dummy credentials for validation
                String validUsername = "admin";
                String validPassword = "password123";

                if (username.equals(validUsername) && password.equals(validPassword)) {
                    JOptionPane.showMessageDialog(frame, "Login Successful!");
                } else {
                    if (!username.equals(validUsername)) {
                        JOptionPane.showMessageDialog(frame, "Invalid Username!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid Password!");
                    }
                }
            } else if (title.equals("Sign Up")) {
                String username = field1Input.getText();
                String password = new String(field2Input.getPassword());
                JOptionPane.showMessageDialog(frame, "Sign-Up Attempt: " + username);
            }
        });

        return card;
    }

    private JPanel createSignUpFormWithIcons(String title, String field1, String field2, String field3, String iconPath) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Title
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(50, 50, 50));

        // Fields
        JLabel field1Label = new JLabel(field1);
        JTextField field1Input = new JTextField();
        JLabel field2Label = new JLabel(field2);
        JPasswordField field2Input = new JPasswordField();
        JLabel field3Label = new JLabel(field3);
        JPasswordField field3Input = new JPasswordField();

        // Button with icon
        JButton actionButton = createStyledButtonWithIcon("Sign Up", iconPath);

        // Add components to the card
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(field1Label);
        card.add(field1Input);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(field2Label);
        card.add(field2Input);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(field3Label);
        card.add(field3Input);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(actionButton);

        // Action listener for the buttons
        actionButton.addActionListener(e -> {
            String username = field1Input.getText();
            String password = new String(field2Input.getPassword());
            String confirmPassword = new String(field3Input.getPassword());

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match!");
            } else {
                JOptionPane.showMessageDialog(frame, "Sign-Up Attempt: " + username);
            }
        });

        return card;
    }

    private JButton createStyledButtonWithIcon(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(100, 150, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Load icon and resize
        try {
            ImageIcon originalIcon = new ImageIcon(iconPath);
            Image iconImage = originalIcon.getImage(); // get the image from icon
            Image resizedIconImage = iconImage.getScaledInstance(24, 24, Image.SCALE_SMOOTH); // resize to desired size (24x24)
            ImageIcon resizedIcon = new ImageIcon(resizedIconImage);
            button.setIcon(resizedIcon);
        } catch (Exception e) {
            System.err.println("Error loading icon: " + e.getMessage());
        }

        // Add hover effect for button
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 230));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 150, 255));
            }
        });

        return button;
    }

    public static void main(String[] args) {
        new EnhancedLibraryHomePageWithIcons();
    }
}