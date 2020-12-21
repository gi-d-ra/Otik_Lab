package ru.eviilcass.compressors;

import ru.eviilcass.compressors.RLEImage.FileChooser;

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
    private JButton RLEImageButton;
    private JLabel fileChosenLabel;
    private JLabel dirChosenLabel;
    private ButtonGroup compressionTypes;

    private JRadioButton r1 = new JRadioButton("Default");
    private JRadioButton r2 = new JRadioButton("Huffman");
    private JRadioButton r3 = new JRadioButton("ArithCode");

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
        JPanel upperPanel5 = new JPanel();

        Box panelsBox = new Box(BoxLayout.Y_AXIS);
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        upperPanel1.setLayout(new GridLayout(1, 2));
        upperPanel2.setLayout(new GridLayout(1, 2));
        upperPanel3.setLayout(new GridLayout(1, 2));
        upperPanel4.setLayout(new GridLayout(1, 2));
        upperPanel5.setLayout(new GridLayout(1, 2));

        panelsBox.add(upperPanel1);
        panelsBox.add(upperPanel2);
        panelsBox.add(upperPanel3);
        panelsBox.add(upperPanel4);
        panelsBox.add(upperPanel5);

        RLEImageButton = new JButton("RLE Image");
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
        compressionTypes.add(r3);
        r1.doClick();

        r1.setBounds(75, 50, 100, 30);
        r2.setBounds(75, 100, 100, 30);
        r3.setBounds(75, 100, 100, 30);

        upperPanel1.add(RLEImageButton);
        upperPanel2.add(chooseFileButton);
        upperPanel2.add(fileChosenLabel);
        upperPanel3.add(chooseDirToWriteButton);
        upperPanel3.add(dirChosenLabel);
        upperPanel4.add(archiveButton);
        upperPanel4.add(unarchiveButton);
        upperPanel5.add(r1);
        upperPanel5.add(r2);
        upperPanel5.add(r3);
        panelsBox.add(scrollPane);


        RLEImageButton.addActionListener(l ->{
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            FileChooser.createAndShowGUI(controller);
        });

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
                if (r3.isSelected()) {
                    model.getFile().setCompressionType(3);
                    model.getFile().setLabNumber(5);
                    model.getFile().setFilesCount(1);
                    controller.encodeArithm(model.getFile(), absoluteFilePathToWrite);
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
                    case 3 -> {
                        model.getFile().setCompressionType(0);
                        controller.decodeArithm(model.getFile(), absoluteFilePathToWrite);
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
            chooser.setCurrentDirectory(new java.io.File("D:\\Nick\\University\\4_Kurs_1_Sem\\Otik\\Programs\\Otik_Lab\\files"));
            chooser.showDialog(frame, "Выбрать файл");
            model.setFile(new FileWithHeader(chooser.getSelectedFile()));
            fileChosenLabel.setText(model.getFile().getAbsolutePath());
            fileChosenLabel.setForeground(new Color(29, 191, 10));
        });


        chooseDirToWriteButton.addActionListener(l -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("D:\\Nick\\University\\4_Kurs_1_Sem\\Otik\\Programs\\Otik_Lab\\files"));
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
