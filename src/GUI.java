import javax.swing.*;
import java.awt.*;
import java.io.*;

public class GUI {

    private JTextArea textArea;
    private JPanel mainPanel;
    private JButton chooseFileButton;
    private JButton archiveButton;
    private JButton unarchiveButton;
    private JLabel fileChoosedLabel;
    private File file;
    private FileWithHeader header;

    public static void main(String[] args) {
        new GUI().buildGui();
    }

    public void buildGui(){
        JFrame frame = new JFrame("Chat");
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel upperPanel1 = new JPanel();
        JPanel upperPanel2 = new JPanel();
        JPanel upperPanel3 = new JPanel();


        JPanel upperPanels = new JPanel();
        upperPanels.setLayout(new GridLayout(4,1));
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        upperPanel1.setLayout(new GridLayout(1,2));
        upperPanel2.setLayout(new GridLayout(1,2));

        upperPanels.add(upperPanel1);
        upperPanels.add(upperPanel2);

        chooseFileButton = new JButton("Выбрать файл");
        archiveButton = new JButton("Архивировать");
        unarchiveButton = new JButton("Разархивировать");

        fileChoosedLabel = new JLabel("файл не выбран");
        fileChoosedLabel.setForeground(new Color(255, 20, 57));

        upperPanel1.add(chooseFileButton);
        upperPanel1.add(fileChoosedLabel);
        upperPanel2.add(archiveButton);
        upperPanel2.add(unarchiveButton);
        upperPanels.add(scrollPane);

        archiveButton.addActionListener( l -> {
            FileReader reader;
            try {
                StringBuilder resultStringBuilder = getStringBuilder();

                String archived = Archiver.compression(resultStringBuilder.toString());

                writeFile(archived, "archived");


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        unarchiveButton.addActionListener( l -> {
            FileReader reader;
            try {
                StringBuilder resultStringBuilder = getStringBuilder();

                String unarchived = Archiver.decompression(resultStringBuilder.toString());

                writeFile(unarchived, "unarchived");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        unarchiveButton.addActionListener( l -> {

        });

        chooseFileButton.addActionListener(l -> {
            JFileChooser chooser = new JFileChooser();
            chooser.showDialog(frame,"Выбрать файл");
            file = chooser.getSelectedFile();

            // хэдер
            header = new FileWithHeader(file);

            fileChoosedLabel.setText(file.getAbsolutePath());
            fileChoosedLabel.setForeground(new Color(29, 191,10));
        });



        frame.getContentPane().add(BorderLayout.CENTER,upperPanels);

        frame.setSize(500,300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void writeFile(String unarchived, String name) throws IOException {
        String fileSeparator = System.getProperty("file.separator");
        String absoluteFilePath = file.getParentFile() + fileSeparator + name+(int)(Math.random()*1000)+".otik";
        File newFile = new File(absoluteFilePath);
        if (file.createNewFile()) {
            System.out.println(absoluteFilePath + " File Created");
        }

        byte[] byteArray = unarchived.getBytes();

        FileOutputStream out = new FileOutputStream(absoluteFilePath);
        out.write(byteArray);
        out.close();
    }

    private StringBuilder getStringBuilder() throws IOException {
        FileReader reader;
        StringBuilder resultStringBuilder = new StringBuilder();
        reader = new FileReader(header.file);

        BufferedReader br = new BufferedReader(reader);
        String line;
        while ((line = br.readLine()) != null) {
            resultStringBuilder.append(line).append("\n");
        }
        return resultStringBuilder;
    }
}
