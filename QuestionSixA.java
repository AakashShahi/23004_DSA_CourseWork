// This scenario presents another sample Java GUI application using multithreading and an asynchronous framework (SwingWorker) to demonstrate asynchronous progress updates and batch processing.
// Features:
// File selection dialog: Choose multiple files for conversion.
// Conversion options: Select the desired output format (e.g., PDF to Docx, image resize).
// Start button: Initiates conversion of selected files concurrently.
// Progress bar: Shows overall conversion progress with individual file indicators.
// Status bar: Displays information about each file being processed (name, conversion type, progress). Cancel button: Allows stopping the entire conversion process or individual files.
// Completion notification: Provides a message when all conversions are finished.
// Challenges:
// Efficiently manage multiple file conversions using separate threads.
// Update the GUI asynchronously to show individual file progress without blocking the main thread. Handle potential errors during file access or conversion and provide informative feedback.
// Allow cancelling specific files or the entire process gracefully.
// Implementation:
// Swing GUI: Design a graphical interface using Swing components for file selection, buttons, progress bars, and status messages.
// Multithreading: Use a thread pool to manage multiple conversion threads efficiently.
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuestionSixA extends JFrame {

    private JFileChooser fileChooser;
    private JButton selectFilesButton;
    private JButton startButton;
    private JButton cancelButton;
    private JProgressBar overallProgressBar;
    private JTextArea statusTextArea;
    private JCheckBox pdfToDocxCheckBox;
    private JCheckBox resizeImageCheckBox;
    private List<File> selectedFiles = new ArrayList<>();
    private ExecutorService executorService;

    public QuestionSixA() {
        setTitle("File Converter");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize components
        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        selectFilesButton = new JButton("Select Files");
        startButton = new JButton("Start");
        cancelButton = new JButton("Cancel");
        overallProgressBar = new JProgressBar();
        statusTextArea = new JTextArea();
        pdfToDocxCheckBox = new JCheckBox("PDF to DOCX");
        resizeImageCheckBox = new JCheckBox("Resize Image");

        // Style components
        overallProgressBar.setStringPainted(true);
        statusTextArea.setEditable(false);
        statusTextArea.setLineWrap(true);
        statusTextArea.setWrapStyleWord(true);

        // Create and customize panels
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        topPanel.add(selectFilesButton);
        topPanel.add(startButton);
        topPanel.add(cancelButton);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(2, 1, 10, 10));
        optionsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        optionsPanel.add(pdfToDocxCheckBox);
        optionsPanel.add(resizeImageCheckBox);

        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BorderLayout());
        progressPanel.add(new JScrollPane(statusTextArea), BorderLayout.CENTER);
        progressPanel.add(overallProgressBar, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(optionsPanel, BorderLayout.WEST);
        add(progressPanel, BorderLayout.CENTER);

        // Add action listeners
        selectFilesButton.addActionListener(new SelectFilesAction());
        startButton.addActionListener(new StartConversionAction());
        cancelButton.addActionListener(new CancelAction());

        // Initialize ExecutorService
        executorService = Executors.newFixedThreadPool(4); // Adjust the number of threads as needed
    }

    private class SelectFilesAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int returnValue = fileChooser.showOpenDialog(QuestionSixA.this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File[] files = fileChooser.getSelectedFiles();
                selectedFiles.clear(); // Clear previous selections
                for (File file : files) {
                    selectedFiles.add(file);
                }
                statusTextArea.append("Selected files:\n");
                for (File file : selectedFiles) {
                    statusTextArea.append(" - " + file.getName() + "\n");
                }
            }
        }
    }

    private class StartConversionAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedFiles.isEmpty()) {
                JOptionPane.showMessageDialog(QuestionSixA.this, "No files selected.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            overallProgressBar.setMaximum(selectedFiles.size());
            overallProgressBar.setValue(0);

            for (File file : selectedFiles) {
                startConversion(file);
            }
        }

        private void startConversion(File file) {
            SwingWorker<Void, String> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    // Determine the type of conversion selected
                    String conversionType = pdfToDocxCheckBox.isSelected() ? "PDF to DOCX" : resizeImageCheckBox.isSelected() ? "Resize Image" : "Unknown";

                    // Specify the output directory on the Desktop
                    File outputDir = new File("/Users/aakashshahi/Desktop");
                    if (!outputDir.exists()) {
                        outputDir.mkdir(); // Create the directory if it doesn't exist
                    }

                    // Create the output file in the specified directory
                    File outputFile = new File(outputDir, "converted_" + file.getName());

                    // Simulate the conversion process
                    for (int i = 0; i < 100; i++) {
                        if (isCancelled()) {
                            break;
                        }
                        Thread.sleep(50); // Simulate time-consuming task
                        publish(file.getName() + ": " + conversionType + " - " + i + "% complete");
                        setProgress(i + 1);
                    }

                    // Copy the file to the output file as a simulation of conversion
                    try {
                        Files.copy(file.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        publish("Saved converted file: " + outputFile.getName());
                    } catch (IOException ex) {
                        publish("Failed to save file: " + outputFile.getName());
                    }

                    return null;
                }

                @Override
                protected void process(List<String> chunks) {
                    for (String chunk : chunks) {
                        statusTextArea.append(chunk + "\n");
                    }
                }

                @Override
                protected void done() {
                    overallProgressBar.setValue(overallProgressBar.getValue() + 1);
                    if (overallProgressBar.getValue() == overallProgressBar.getMaximum()) {
                        JOptionPane.showMessageDialog(QuestionSixA.this, "All conversions completed!", "Complete", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            };

            executorService.submit(worker);
        }
    }

    private class CancelAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            executorService.shutdownNow();
            statusTextArea.append("Conversion process cancelled.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            QuestionSixA app = new QuestionSixA();
            app.setVisible(true);
        });
    }
}




