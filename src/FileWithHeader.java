import java.io.*;

/**
 * header:
 * 0-3   -4b-sign
 * 4-5   -2b-version
 * 6     -1b-compression types
 * 7     -1b-lab number
 * 8     -1b-files count
 * 9-15  -7b-reserved
 * 16-19 -4b-source file length
 * 20-23 -4b-data length
 * 24-27 -4b-subheader length
 * 28-31 -4b-reserved
 *
 * all bytes: 32
 */

public class FileWithHeader {

    private String sign;
    private int version;
    private int compressionType;
    private int labNumber;
    private int filesCount;
    private int sourceFileLength;
    private int dataLength;
    private int subheaderLength;


    InputStream in;
    File file;

    public FileWithHeader(File file) {
        try {
            this.file = file;
            this.in = new FileInputStream(this.file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // считываем кусками заголовок и заполняем соответсвующие поля
    public void setHeader() {
        try {
            byte[] buff = new byte[4];
            in.read(buff);
            sign = new String(buff);

            version = readBytes(in, 2);
            compressionType = in.read();
            labNumber = in.read();
            filesCount = in.read();
            in.skip(7);
            sourceFileLength = readBytes(in, 2);
            dataLength = readBytes(in, 4);
            subheaderLength = readBytes(in, 4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSign() {
        return sign;
    }

    public int getVersion() {
        return version;
    }

    public int getCompressionType() {
        return compressionType;
    }

    public int getLabNumber() {
        return labNumber;
    }

    public int getFilesCount() {
        return filesCount;
    }

    public int getSourceFileLength() {
        return sourceFileLength;
    }

    public int getDataLength() {
        return dataLength;
    }

    public int getSubheaderLength() {
        return subheaderLength;
    }

    // чтение и преобразование байтов для полей
    public static int readBytes(InputStream in, int amount) throws IOException {
        byte[] buff = new byte[amount];
        in.read(buff);
        return parseBytesToInt(buff);
    }

    // преобразует byte[] в int
    public static int parseBytesToInt(byte[] arr) {
        int i = arr.length - 1;
        int result = 0;
        for (byte b : arr) {
            result += b * Math.pow(10, i);
            i--;
        }
        return result;
    }

    //TODO: возможно косяк с корректностью преобразования строки в последовательность байтов
    public String getHeader() {
        return sign +
                version +
                compressionType +
                labNumber +
                filesCount +
                sourceFileLength +
                dataLength +
                subheaderLength;
    }
}
