import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;

public class Frame extends JFrame {
    private JComboBox<String> modelDropdown;
    private JTextArea chatArea;
    private JTextArea promptArea;
    private JButton sendButton;

    public Frame() {
        setTitle("Local LLM Client");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        System.out.println("Initializing frame components...");

        String[] models = {"codellama:7b", "llama2:7b", "mistral:7b", "gemma:7b"};
        modelDropdown = new JComboBox<>(models);
        add(modelDropdown, BorderLayout.NORTH);
        System.out.println("Model dropdown created with options: " + String.join(", ", models));

        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));
        add(centerContainer, BorderLayout.CENTER);

        JPanel chatPanel = new JPanel(new BorderLayout());
        chatArea = new JTextArea();
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setEditable(false);
        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatPanel.add(chatScroll, BorderLayout.CENTER);
        centerContainer.add(chatPanel);
        System.out.println("Chat area created");

        JPanel inputPanel = new JPanel(new BorderLayout());
        promptArea = new JTextArea();
        promptArea.setLineWrap(true);
        promptArea.setWrapStyleWord(true);
        promptArea.setEditable(true);
        JScrollPane promptScroll = new JScrollPane(promptArea);
        inputPanel.add(promptScroll, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        inputPanel.add(sendButton, BorderLayout.EAST);
        centerContainer.add(inputPanel);
        System.out.println("Input panel with prompt area and send button created");

        sendButton.addActionListener(e -> {
            System.out.println("Send button clicked");
            sendPrompt();
        });

        promptArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isShiftDown()) {
                    e.consume();
                    System.out.println("Enter key pressed in prompt area");
                    sendPrompt();
                }
            }
        });

        setVisible(true);
        System.out.println("Frame made visible");
    }

    private void sendPrompt() {
        String prompt = promptArea.getText().trim();
        if (prompt.isEmpty()) return;

        chatArea.append("User: " + prompt + "\nLLM: "); // Start LLM response line
        promptArea.setText("");
        
        String selectedModel = (String) modelDropdown.getSelectedItem();
        String ollamaUrl = "http://localhost:11434/api/generate";

        String requestBody = String.format("{\"model\": \"%s\", \"prompt\": \"%s\"}", selectedModel, prompt);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ollamaUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        new Thread(() -> {
            try {
                HttpResponse<Stream<String>> response = client.send(
                    request, 
                    HttpResponse.BodyHandlers.ofLines()
                );
                
                response.body().forEach(line -> {
                    if (line.contains("\"response\":\"")) {
                        String[] parts = line.split("\"response\":\"");
                        if (parts.length > 1) {
                            String responseToken = parts[1].split("\"")[0];  // New local variable
                            final String token = responseToken.replace("\\n", "\n");  // Now effectively final
                            SwingUtilities.invokeLater(() -> {
                                chatArea.append(token);
                                chatArea.setCaretPosition(chatArea.getDocument().getLength());
                            });
                        }
                    }
                });
                SwingUtilities.invokeLater(() -> chatArea.append("\n\n"));
                chatArea.append(" END");
                
            } catch (IOException | InterruptedException ex) {
                SwingUtilities.invokeLater(() -> chatArea.append("\nError: " + ex.getMessage() + "\n\n"));
            }
        }).start();
    }

    public static void main(String[] args) {
        System.out.println("Starting application...");
        new Frame();
    }
}