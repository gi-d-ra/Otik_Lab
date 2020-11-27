import java.io.*;
import java.net.URI;

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
 * <p>
 * all bytes: 32
 */

/**
 * otik
 * 01
 * 0
 * 3
 * 1
 * .......
 * 0039
 * 0071
 * 0032
 * ....
*/

public class FileWithHeader extends File {

    private String sign = "otik";
    private int version;
    private int compressionType;
    private int labNumber;
    private int filesCount;
    private int sourceFileLength;
    private int dataLength;
    private int subheaderLength;

    private StringBuilder source = new StringBuilder();

    public void setSourceFileLength(int sourceFileLength) {
        this.sourceFileLength = sourceFileLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    public StringBuilder getSourceBuilder() {
        return source;
    }

    public void setSourceBuilder(StringBuilder source) {
        this.source = source;
    }

    public FileWithHeader(File file) {
        super(file.getPath());
    }

    public FileWithHeader(String pathname) {
        super(pathname);
    }

    public FileWithHeader(String parent, String child) {
        super(parent, child);
    }

    public FileWithHeader(File parent, String child) {
        super(parent, child);
    }

    public FileWithHeader(URI uri) {
        super(uri);
    }

    public void setCompressionType(int compressionType) {
        this.compressionType = compressionType;
    }

    // считываем кусками заголовок и заполняем соответсвующие поля
    public void setHeader(InputStream in) {
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

    public void setHeader(String header) {
        sign = header.substring(0, 4);
        version = Integer.parseInt(header.substring(4, 6));
        compressionType = Integer.parseInt(header.substring(6, 7));
        labNumber = Integer.parseInt(header.substring(7, 8));
        filesCount = Integer.parseInt(header.substring(8, 9));
        sourceFileLength = Integer.parseInt(header.substring(16, 20));
        dataLength = Integer.parseInt(header.substring(20, 24));
        subheaderLength = Integer.parseInt(header.substring(24, 28));
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

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setLabNumber(int labNumber) {
        this.labNumber = labNumber;
    }

    public void setFilesCount(int filesCount) {
        this.filesCount = filesCount;
    }

    public void setSubheaderLength(int subheaderLength) {
        this.subheaderLength = subheaderLength;
    }

    public void setSource(StringBuilder source) {
        this.source = source;
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

    public String getHeader() {
        return sign +
                getStringVersion() +
                compressionType +
                labNumber +
                filesCount +
                "......." +
                getStringSourceFileLength() +
                getStringDataLength() +
                getStringSubheaderLength() +
                "....";
    }

    private String getStringVersion() {
//        return version < 10 ? "0" + version : version + "";
        return "01";
    }

    private String getStringSourceFileLength() {
        sourceFileLength = source.length();
        if (sourceFileLength < 10)
            return "000" + sourceFileLength;
        else if (sourceFileLength < 100)
            return "00" + sourceFileLength;
        else if (sourceFileLength < 1000)
            return "0" + sourceFileLength;
        else return sourceFileLength + "";
    }

    private String getStringDataLength() {
        dataLength = source.length() + 32;
        if (dataLength < 10)
            return "000" + dataLength;
        else if (dataLength < 100)
            return "00" + dataLength;
        else if (dataLength < 1000)
            return "0" + dataLength;
        else return dataLength + "";
    }

    private String getStringSubheaderLength(){
        return "0032";
    }
}
