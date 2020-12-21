package ru.eviilcass.compressors.RLEImage;

import ru.eviilcass.compressors.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Arrays;

public class FileChooser extends JPanel
                        implements ActionListener {

    static private final String newline = "\n";
    private JButton addButton, compressionButton, decompressionButton;
    private JTextArea log;
    private JFileChooser fc;
    private File fileIn, fileRle, fileOut;
    private ImageToCompression imgTC;
    private Desktop d = Desktop.getDesktop();
    private Controller controller;

    private FileChooser(Controller controller) {
        super(new BorderLayout());
        this.controller = controller;


        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        fc = new JFileChooser();


        addButton = new JButton("Add image...");
        fc.setCurrentDirectory(new java.io.File("D:\\Nick\\University\\4_Kurs_1_Sem\\Otik\\Programs\\Otik_Lab\\files"));

        addButton.addActionListener(this);


        compressionButton = new JButton("Compress to File");
        compressionButton.addActionListener(this);

        decompressionButton = new JButton("Decompress to File");
        decompressionButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(compressionButton);
        buttonPanel.add(decompressionButton);


        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }



    public void actionPerformed(ActionEvent e) {


        if (e.getSource() == addButton) {

            int returnVal = fc.showOpenDialog(FileChooser.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fileIn = fc.getSelectedFile();
                log.append("Opening: " + fileIn.getName() + "." + newline);
                log.append("Size: " + fileIn.length() + " byte." + newline);

                try
                {
                    d.open(fileIn);
                    imgTC = new ImageToCompression(fileIn);
                }
                catch (IOException ex){
                    ex.printStackTrace();
                }

            } else {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        }


        else if (e.getSource() == compressionButton) {



            MyColorReader colorReader = new MyColorReader(imgTC);
           fileRle = new File( imgTC.title.substring(0 ,imgTC.title.length() -4)+ ".otic");

           try (FileOutputStream str = new FileOutputStream(fileRle)) {
               //
               controller.setFile(fileIn);
               controller.getInfoFromFile();
               controller.preWriteHeader(fileRle);
               //

               str.write(colorReader.toFile);

               UserDefinedFileAttributeView view = Files.getFileAttributeView(fileRle.toPath(), UserDefinedFileAttributeView.class);
               view.write("user.width", Charset.defaultCharset().encode(imgTC.w + ""));
               view.write("user.height", Charset.defaultCharset().encode(imgTC.h + ""));
               view.write("user.RedSize", Charset.defaultCharset().encode(colorReader.cRed.size() + ""));
               view.write("user.GreenSize", Charset.defaultCharset().encode(colorReader.cGreen.size() + ""));
               view.write("user.BlueSize", Charset.defaultCharset().encode(colorReader.cBlue.size() + ""));

               //
               controller.writeHeader(fileRle);
               //

           }
           catch (IOException ex){
               ex.printStackTrace();
           }

           float cr = (float)fileIn.length()/(float)fileRle.length();

            log.append("Saving: " + fileRle.getName() + "." + newline);
            log.append("Size: " + (int)fileRle.length() + " byte." + newline);
            log.append("Compression rate: "+ round(cr, 2) +  newline);


            log.setCaretPosition(log.getDocument().getLength());
        }


        else if (e.getSource() == decompressionButton)  {

            try{
                //
                byte[] fileContent_1 = Files.readAllBytes(fileRle.toPath());
                byte[] fileContent = Arrays.copyOfRange(fileContent_1, 32, fileContent_1.length);
                //

                int width = Integer.valueOf(readData(fileRle, "user.width"));
                int height = Integer.valueOf(readData(fileRle, "user.height"));
                int redLength = Integer.valueOf(readData(fileRle, "user.RedSize"));
                int greenLength = Integer.valueOf(readData(fileRle, "user.GreenSize"));

                byte[] redArr = Arrays.copyOfRange(fileContent, 0, redLength);
                byte[] greenArr = Arrays.copyOfRange(fileContent, redLength, redLength + greenLength);
                byte[] blueArr = Arrays.copyOfRange(fileContent, redLength + greenLength, fileContent.length);


                MyColorWriter colorWriter = new MyColorWriter(width, height, redArr, greenArr, blueArr);

                fileOut = new File("FromRle_"+fileRle.getName().substring(0,fileRle.getName().length()-4)+fileIn.getName().substring(fileIn.getName().lastIndexOf(".")+1));
                ImageIO.write(colorWriter.bimg, fileIn.getName().substring(fileIn.getName().lastIndexOf(".")+1), fileOut);

                d.open(fileOut);

            }catch (IOException ex){
                ex.printStackTrace();
            }
            log.append("Saving: " + fileOut.getName() + "." + newline);

            log.setCaretPosition(log.getDocument().getLength());
        }
    }

    public static void createAndShowGUI(Controller controller) {

        JFrame frame = new JFrame("RleCompressionImage");
        frame.setDefaultCloseOperation(2);


        frame.add(new FileChooser(controller));

        frame.pack();
        frame.setVisible(true);
    }


    private static float round(double f, int places) {
        float temp = (float)(f*(Math.pow(10, places)));
        temp = (Math.round(temp));
        temp = temp/(int)(Math.pow(10, places));
        return temp;
    }


    private static String readData(File file, String s) {
        UserDefinedFileAttributeView view = Files.getFileAttributeView(file.toPath(), UserDefinedFileAttributeView.class);
        ByteBuffer buf = null;
        try {
            buf = ByteBuffer.allocate(view.size(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            view.read(s, buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        buf.flip();

        return Charset.defaultCharset().decode(buf).toString();
    }

}
