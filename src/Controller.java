import compressors.huffman.HFreqTable;
import compressors.huffman.HuffmanInputStream;
import compressors.huffman.HuffmanOutputStream;

import java.io.*;

public class Controller {
    Model model;

    public Controller() {
    }

    public Controller(Model model) {
        this.model = model;
    }

    public void getInfoFromFile() throws IOException {
        FileReader reader;
        StringBuilder resultStringBuilder = new StringBuilder();
        reader = new FileReader(model.getFile());

        BufferedReader br = new BufferedReader(reader);
        String line;
        while ((line = br.readLine()) != null) {
            resultStringBuilder.append(line).append("\n");
        }
        resultStringBuilder.replace(resultStringBuilder.lastIndexOf("\n"), resultStringBuilder.length(), "");
        model.getFile().setSourceBuilder(new StringBuilder());
        model.getFile().getSourceBuilder().append(resultStringBuilder.toString());
    }

    private void writeHeader(File outFile) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(outFile, "rw");
        model.getFile().setSourceFileLength((int) (outFile.length()));
        raf.writeBytes(model.getFile().getHeader());
        raf.close();
//        FileOutputStream out = new FileOutputStream(outFile);
//        out.write(headerBytes);
//        out.flush();
//        out.close();
    }

    private void preWriteHeader(File outFile) throws IOException {
        byte[] headerBytes = model.getFile().getHeader().getBytes();
        FileOutputStream out = new FileOutputStream(outFile);
        out.write(headerBytes);
        out.flush();
        out.close();
    }

    public void writeFileDef(FileWithHeader file, String absoluteFilePath) throws IOException {
        String fileSeparator = System.getProperty("file.separator");
        File finalFile = new File(absoluteFilePath + fileSeparator + file.getName());
        if (finalFile.createNewFile()) {
            System.out.println(absoluteFilePath + " File Created");
        }

        byte[] sourceBytes = file.getSourceBuilder().toString().getBytes();

        FileOutputStream out = new FileOutputStream(finalFile);
        out.write(sourceBytes);
        out.flush();
        out.close();
    }

    public void writeFileDefWithHeader(FileWithHeader file, String absoluteFilePath) throws IOException {
        String fileSeparator = System.getProperty("file.separator");
        File finalFile = new File(absoluteFilePath + fileSeparator + file.getName());
        if (finalFile.createNewFile()) {
            System.out.println(absoluteFilePath + " File Created");
        }

        byte[] headerBytes = file.getHeader().getBytes();
        byte[] sourceBytes = file.getSourceBuilder().toString().getBytes();

        FileOutputStream out = new FileOutputStream(finalFile);
        out.write(headerBytes);
        out.write(sourceBytes);
        out.flush();
        out.close();
    }

    public void encodeHuffman(FileWithHeader file, String absoluteFilePath) throws IOException {
        File inFile = file;
        String fileSeparator = System.getProperty("file.separator");
        File outFile = new File(absoluteFilePath + fileSeparator + file.getName());
        InputStream in = new FileInputStream(inFile);
//        in.skip(32);

        preWriteHeader(outFile);

        HuffmanOutputStream hout = new HuffmanOutputStream(
                new FileOutputStream(outFile, true));
        byte buf[] = new byte[4096];
        int len;

        while ((len = in.read(buf)) != -1)
            hout.write(buf, 0, len);

        in.close();
        hout.close();

        writeHeader(outFile);

        System.out.println("--------------------------------------------------------");
        System.out.println("Compression: done");
        System.out.println("Original file size:     " + inFile.length() * 8);
        System.out.println("Compressed file size:   " + outFile.length() * 8);
        System.out.print("Compression efficiency: ");
        if (inFile.length() > outFile.length()) {
            System.out.format("%.2f%%\n",
                    (100.0 - (((double) outFile.length() / (double) inFile.length()) * 100)));
        } else
            System.out.println("none");
        calcEntropy(outFile);
        System.out.println("--------------------------------------------------------");
    }

    public void decodeHuffman(FileWithHeader file, String absoluteFilePath) throws IOException {

        File inFile = file;
        String fileSeparator = System.getProperty("file.separator");
        File outFile = new File(absoluteFilePath + fileSeparator + file.getName());
        HuffmanInputStream hin = new HuffmanInputStream(new FileInputStream(
                inFile));
        hin.skip(32);
        preWriteHeader(outFile);
        OutputStream hout = new FileOutputStream(outFile, true);
        byte buf[] = new byte[4096];
        int len;


        while ((len = hin.read(buf)) != -1)
            hout.write(buf, 0, len);

        hin.close();
        hout.close();

//        writeHeader(outFile);
        System.out.println("--------------------------------------------------------");
        System.out.println("Decompression: done");
        System.out.println("Original file size:     " + inFile.length() * 8);
        System.out.println("Decompressed file size: " + outFile.length() * 8);
        System.out.println("--------------------------------------------------------");

//        calcEntropy(outFile);
    }

    public void calcEntropy(File file) throws IOException {
        InputStream in = new FileInputStream(file);
        HFreqTable ftbl = new HFreqTable();
        int sym;

        while ((sym = in.read()) != -1)
            ftbl.add(sym);

        in.close();
        System.out.format("Entropy: %.2f\n", ftbl.entropy() * file.length());
    }
}
