import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.*;

public class LibraryManagementDashboard {

    private JFrame frame;
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private Map<String, Book> books = new HashMap<>();
    private String currentUser = "User1";  // Mocked for simplicity
    private Map<String, String> borrowedBooks = new HashMap<>();
    private Image backgroundImage;
    private JPanel bookTablePanel;
    private JTextField searchField;

    // Book Class
    static class Book {
        private String title;
        private String author;
        private String ISBN;
        private boolean isAvailable;
        private String genre;

        public Book(String title, String author, String ISBN, String genre) {
            this.title = title;
            this.author = author;
            this.ISBN = ISBN;
            this.isAvailable = true;
            this.genre = genre;
        }

        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getISBN() { return ISBN; }
        public boolean isAvailable() { return isAvailable; }
        public void setAvailable(boolean available) { isAvailable = available; }
        public String getGenre() { return genre; }
    }

    public LibraryManagementDashboard() {
        loadBackgroundImage();
        initializeLibrary();
        createLibraryDashboard();
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("images/library.jpg"));  // Replace with the path to your background image
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }
    }

    private void initializeLibrary() {
        // Sample books for the library
        books.put("12345", new Book("Java Programming", "James Gosling", "12345", "Programming"));
        books.put("67890", new Book("Data Structures", "Mark Weiss", "67890", "Computer Science"));
        books.put("11121", new Book("Machine Learning", "Andrew Ng", "11121", "AI"));
    }

    private void createLibraryDashboard() {
        frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        // Main Panel with custom background
        JPanel mainPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 10));  // Add space between components

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel headerLabel = new JLabel("Library Management Dashboard");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headerLabel.setForeground(Color.blue);
        headerPanel.add(headerLabel);

        // Book Table Panel with background logic
        bookTablePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getComponentCount() == 0 && backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        bookTablePanel.setLayout(new BorderLayout());

        // Book Table
        String[] columns = {"Title", "Author", "ISBN", "Genre", "Available"};
        bookTableModel = new DefaultTableModel(columns, 0);
        bookTable = new JTable(bookTableModel);

        JScrollPane bookScrollPane = new JScrollPane(bookTable);
        updateBookTable();

        // Left-side panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);  // Add space between buttons

        // Create Buttons with Larger Size
        JButton borrowButton = createStyledButton("Borrow Book", "images/borrow_icon.png");
        JButton returnButton = createStyledButton("Return Book", "images/return_icon.png");
        JButton issueButton = createStyledButton("Issue Book", "images/issue_icon.png");
        JButton manageButton = createStyledButton("Manage Books", "images/manage_icon.png");
        JButton addBookButton = createStyledButton("Add Book", "images/add_icon.png");
        JButton viewRecordButton = createStyledButton("View Records", "images/view_icon.png");
        JButton createBookListButton = createStyledButton("Show Book List", "images/list_icon.png");

        // Add buttons to the button panel
        buttonPanel.add(borrowButton, gbc);
        gbc.gridy++;
        buttonPanel.add(returnButton, gbc);
        gbc.gridy++;
        buttonPanel.add(issueButton, gbc);
        gbc.gridy++;
        buttonPanel.add(manageButton, gbc);
        gbc.gridy++;
        buttonPanel.add(viewRecordButton, gbc);
        gbc.gridy++;
        buttonPanel.add(addBookButton, gbc);
        gbc.gridy++;
        buttonPanel.add(createBookListButton, gbc);

        // Action listeners
        borrowButton.addActionListener(e -> borrowBook());
        returnButton.addActionListener(e -> returnBook());
        issueButton.addActionListener(e -> issueBook());
        manageButton.addActionListener(e -> manageBooks());
        addBookButton.addActionListener(e -> addBook());
        viewRecordButton.addActionListener(e -> viewRecords());

        createBookListButton.addActionListener(e -> {
            bookTablePanel.removeAll();
            bookTablePanel.add(bookScrollPane, BorderLayout.CENTER);
            bookTablePanel.revalidate();
            bookTablePanel.repaint();
        });

        // Logout Panel
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = createStyledButton("Logout", "images/logout_icon.png");
        logoutButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Logged out successfully!"));
        logoutPanel.add(logoutButton);

        // **Search Panel - Adjusted for Top-Right**
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));  // Aligning to the right
        searchField = new JTextField(40);  // Search text field
        JButton searchButton = createStyledButton("Search", "images/search_icon.png");
        JButton clearSearchButton = createStyledButton("Clear Search", "images/clear_icon.png");

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearSearchButton);

        // **Search Action Listeners**
        searchButton.addActionListener(e -> searchBooks());
        clearSearchButton.addActionListener(e -> clearSearch());

        // Adding components to the main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(bookTablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(searchPanel, BorderLayout.NORTH);  // Placing the search panel at the top-right
        mainPanel.add(logoutPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setIcon(resizeIcon(new ImageIcon(iconPath), 30, 30));  // Larger icon size
        button.setBackground(new Color(0,0,255)); // Light blue background
        button.setForeground(Color.white); // White text
        button.setFocusPainted(false); // Remove the focus border
        button.setBorder(BorderFactory.createLineBorder(Color.white)); // White border
        button.setFont(new Font("Arial", Font.PLAIN, 20));  // Larger font size
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setPreferredSize(new Dimension(250, 50));  // Larger button size
        
        // Add hover effect for button
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0,0,200));  // Slightly darker blue
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0,0,255));  // Light blue
            }
        });

        return button;
    }

    private Icon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    private void updateBookTable() {
        DefaultTableModel model = (DefaultTableModel) bookTable.getModel();
        model.setRowCount(0);
        for (Book book : books.values()) {
            model.addRow(new Object[]{book.getTitle(), book.getAuthor(), book.getISBN(), book.getGenre(), book.isAvailable()});
        }
    }

    private void borrowBook() {
        String bookISBN = JOptionPane.showInputDialog(frame, "Enter ISBN of the book to borrow:");
        Book book = books.get(bookISBN);
        if (book != null && book.isAvailable()) {
            book.setAvailable(false);
            borrowedBooks.put(currentUser, bookISBN);
            updateBookTable();
            JOptionPane.showMessageDialog(frame, "Book borrowed successfully.");
        } else {
            JOptionPane.showMessageDialog(frame, "Book is not available.");
        }
    }

    private void returnBook() {
        String bookISBN = JOptionPane.showInputDialog(frame, "Enter ISBN of the book to return:");
        Book book = books.get(bookISBN);
        if (book != null && !book.isAvailable()) {
            book.setAvailable(true);
            borrowedBooks.remove(currentUser);
            updateBookTable();
            JOptionPane.showMessageDialog(frame, "Book returned successfully.");
        } else {
            JOptionPane.showMessageDialog(frame, "Book is not borrowed.");
        }
    }

    private void issueBook() {
        String bookISBN = JOptionPane.showInputDialog(frame, "Enter ISBN of the book to issue:");
        Book book = books.get(bookISBN);
        if (book != null && book.isAvailable()) {
            book.setAvailable(false);
            updateBookTable();
            JOptionPane.showMessageDialog(frame, "Book issued successfully.");
        } else {
            JOptionPane.showMessageDialog(frame, "Book is not available.");
        }
    }

    private void manageBooks() {
        // Simple dialog to manage book details (for demonstration)
        String bookISBN = JOptionPane.showInputDialog(frame, "Enter ISBN of the book to edit:");
        Book book = books.get(bookISBN);
        if (book != null) {
            String newTitle = JOptionPane.showInputDialog(frame, "Enter new title:", book.getTitle());
            String newAuthor = JOptionPane.showInputDialog(frame, "Enter new author:", book.getAuthor());
            book = new Book(newTitle, newAuthor, bookISBN, book.getGenre());  // Update book
            books.put(bookISBN, book);
            updateBookTable();
            JOptionPane.showMessageDialog(frame, "Book updated successfully.");
        } else {
            JOptionPane.showMessageDialog(frame, "Book not found.");
        }
    }

    private void addBook() {
        // Add new book to the library
        String title = JOptionPane.showInputDialog(frame, "Enter book title:");
        String author = JOptionPane.showInputDialog(frame, "Enter book author:");
        String ISBN = JOptionPane.showInputDialog(frame, "Enter book ISBN:");
        String genre = JOptionPane.showInputDialog(frame, "Enter book genre:");

        if (title != null && author != null && ISBN != null && genre != null) {
            Book newBook = new Book(title, author, ISBN, genre);
            books.put(ISBN, newBook);
            updateBookTable();
            JOptionPane.showMessageDialog(frame, "New book added successfully.");
        } else {
            JOptionPane.showMessageDialog(frame, "All fields are required.");
        }
    }

    private void viewRecords() {
        // Create a table for borrowed books
        String[] columns = {"Title", "Author", "ISBN", "Genre"};
        DefaultTableModel borrowedTableModel = new DefaultTableModel(columns, 0);
        JTable borrowedTable = new JTable(borrowedTableModel);

        // Add the borrowed books to the table
        for (Map.Entry<String, String> entry : borrowedBooks.entrySet()) {
            String bookISBN = entry.getValue();
            Book book = books.get(bookISBN);
            borrowedTableModel.addRow(new Object[]{book.getTitle(), book.getAuthor(), book.getISBN(), book.getGenre()});
        }

        // Scroll pane for borrowed books table
        JScrollPane borrowedScrollPane = new JScrollPane(borrowedTable);

        // Clear previous components and show the borrowed books table
        bookTablePanel.removeAll();
        bookTablePanel.add(borrowedScrollPane, BorderLayout.CENTER);
        bookTablePanel.revalidate();
        bookTablePanel.repaint();
    }

    private void searchBooks() {
        String query = searchField.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) bookTable.getModel();
        model.setRowCount(0);
        for (Book book : books.values()) {
            if (book.getTitle().toLowerCase().contains(query) || book.getAuthor().toLowerCase().contains(query) ||
                book.getGenre().toLowerCase().contains(query)) {
                model.addRow(new Object[]{book.getTitle(), book.getAuthor(), book.getISBN(), book.getGenre(), book.isAvailable()});
            }
        }
    }

    private void clearSearch() {
        searchField.setText("");
        updateBookTable();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryManagementDashboard());
    }
}
