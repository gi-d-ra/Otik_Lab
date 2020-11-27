import javax.swing.*;
import java.awt.*;
import java.io.*;

public class GUI {

    Model model;
    Controller controller;

    String absoluteFilePathToWrite;

    private JTextArea textArea;
    private JPanel mainPanel;
    private JButton chooseFileButton;
    private JButton chooseDirToWriteButton;
    private JButton archiveButton;
    private JButton unarchiveButton;
    private JLabel fileChosenLabel;
    private JLabel dirChosenLabel;
    private ButtonGroup compressionTypes;

    private JRadioButton r1 = new JRadioButton("Default");
    private JRadioButton r2 = new JRadioButton("Huffman");

    public GUI() {
        buildGui();
    }

    public GUI(Controller controller, Model model) {
        this.controller = controller;
        this.model = model;
        buildGui();
    }

    public void buildGui() {
        JFrame frame = new JFrame("Otik");
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel upperPanel1 = new JPanel();
        JPanel upperPanel2 = new JPanel();
        JPanel upperPanel3 = new JPanel();
        JPanel upperPanel4 = new JPanel();

        Box panelsBox = new Box(BoxLayout.Y_AXIS);
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        upperPanel1.setLayout(new GridLayout(1, 2));
        upperPanel2.setLayout(new GridLayout(1, 2));
        upperPanel3.setLayout(new GridLayout(1, 2));
        upperPanel4.setLayout(new GridLayout(1, 2));

        panelsBox.add(upperPanel1);
        panelsBox.add(upperPanel2);
        panelsBox.add(upperPanel3);
        panelsBox.add(upperPanel4);

        chooseFileButton = new JButton("Выбрать файл");
        chooseDirToWriteButton = new JButton("Выбрать папку для записи");
        archiveButton = new JButton("Архивировать");
        unarchiveButton = new JButton("Разархивировать");

        fileChosenLabel = new JLabel("                     Файл не выбран");
        dirChosenLabel = new JLabel("                    Папка не выбрана");
        fileChosenLabel.setForeground(new Color(255, 20, 57));
        dirChosenLabel.setForeground(new Color(255, 20, 57));

        compressionTypes = new ButtonGroup();
        compressionTypes.add(r1);
        compressionTypes.add(r2);
        r1.doClick();

        r1.setBounds(75, 50, 100, 30);
        r2.setBounds(75, 100, 100, 30);

        upperPanel1.add(chooseFileButton);
        upperPanel1.add(fileChosenLabel);
        upperPanel2.add(chooseDirToWriteButton);
        upperPanel2.add(dirChosenLabel);
        upperPanel3.add(archiveButton);
        upperPanel3.add(unarchiveButton);
        upperPanel4.add(r1);
        upperPanel4.add(r2);
        panelsBox.add(scrollPane);


        archiveButton.addActionListener(l -> {
            try {
                if (r1.isSelected()) {
                    controller.getInfoFromFile();
                    model.getFile().setCompressionType(1);
                    model.getFile().setLabNumber(3);
                    model.getFile().setFilesCount(1);
                    StringBuilder archived = Archiver.defCompression(model.getFile().getSourceBuilder());
                    model.getFile().setSourceBuilder(archived);
                    controller.writeFileDefWithHeader(model.getFile(), absoluteFilePathToWrite);
                }
                if (r2.isSelected()) {
                    model.getFile().setCompressionType(2);
                    model.getFile().setLabNumber(4);
                    model.getFile().setFilesCount(1);
                    controller.encodeHuffman(model.getFile(), absoluteFilePathToWrite);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        unarchiveButton.addActionListener(l -> {
            StringBuilder unarchived = new StringBuilder();
            try {
                controller.getInfoFromFileWithHeader();
                switch (model.getFile().getCompressionType()) {
                    case 0 -> {
                        unarchived.append(model.getFile().getSourceBuilder());
                        controller.writeFileDef(model.getFile(), absoluteFilePathToWrite);
                    }
                    case 1 -> {
                        unarchived.append(Archiver.defDecompression(model.getFile().getSourceBuilder()));
                        model.getFile().setSourceBuilder(unarchived);
                        controller.writeFileDef(model.getFile(), absoluteFilePathToWrite);
                    }
                    case 2 -> {
                        model.getFile().setCompressionType(0);
                        controller.decodeHuffman(model.getFile(), absoluteFilePathToWrite);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        chooseFileButton.addActionListener(l -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("D:\\Nick\\University\\4_Kurs_1_Sem\\Otik\\Programs\\Otik_Lab"));
            chooser.showDialog(frame, "Выбрать файл");
            model.setFile(new FileWithHeader(chooser.getSelectedFile()));
            fileChosenLabel.setText(model.getFile().getAbsolutePath());
            fileChosenLabel.setForeground(new Color(29, 191, 10));
        });


        chooseDirToWriteButton.addActionListener(l -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("D:\\Nick\\University\\4_Kurs_1_Sem\\Otik\\Programs\\Otik_Lab"));
            chooser.showDialog(frame, "Выбрать папку для записи");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            absoluteFilePathToWrite = chooser.getCurrentDirectory().getPath();

            dirChosenLabel.setText(absoluteFilePathToWrite);
            dirChosenLabel.setForeground(new Color(29, 191, 10));
        });


        frame.getContentPane().add(BorderLayout.CENTER, panelsBox);

        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
