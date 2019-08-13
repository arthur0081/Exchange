package com.slabs.exchange.util;

import com.slabs.exchange.common.exception.ExchangeException;

import java.io.*;
import java.net.URLDecoder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtil {
    static final int BUFFER = 1024;

    public static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     *
     * 功能描述：将数据流写入指定文件
     *
     * @param inputStream
     *            数据流
     * @param fileName
     *            要写入的文件
     * @throws Exception
     */
    public static void writeFile(InputStream inputStream, String fileName) throws Exception {
        File file = new File(fileName);
        if (file.exists()) {
            if (!file.canWrite()) {
                throw new ExchangeException("该文件【" + fileName + "】暂时不能写入，请稍候再试！");
            }
        }
        String path = file.getParent();
        createDirectory(path, null);

        BufferedOutputStream b = null;
        // ----------将文件保存到服务器-------------------
        try {
            b = new BufferedOutputStream(new FileOutputStream(fileName), 1024 * 1024);
            byte[] buffer = new byte[1024 * 1024];
            int readLength = 0;
            while ((readLength = inputStream.read(buffer)) > 0) {
                b.write(buffer, 0, readLength);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {// 关闭文件流
                if (b != null) {
                    b.flush();
                    b.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void createDirectory(String directory, String subDirectory) {
        String dir[];
        File fl = new File(directory);
        try {
            if ((subDirectory == null || "".equals(subDirectory)) && fl.exists() != true) {
                fl.mkdirs();
            } else if (subDirectory != null && !"".equals(subDirectory)) {
                dir = subDirectory.replace('\\', '/').split("/");
                for (int i = 0; i < dir.length; i++) {
                    File subFile = new File(directory + File.separator + dir[i]);
                    if (subFile.exists() == false)
                        subFile.mkdir();
                    directory += File.separator + dir[i];
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 删除指定文件夹下所有文件path 文件夹完整绝对路径
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获取指定文件夹下的文件
     *
     */
    public static File[] getFiles(String folderPath) {
        File file = new File(folderPath);
        File[] fileList = file.listFiles();
        return fileList;
    }

    /**
     * 删除文件夹folderPath 文件夹完整绝对路径
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * 功能描述：获取文件的MD5值
     *
     * @param file
     * @return MD5后的hex字符串
     * @throws Exception
     */
    public static String getFileMD5(File file) throws Exception {
        InputStream fis = null;
        MessageDigest md5 = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024 * 512];
            md5 = MessageDigest.getInstance("MD5");
            int numRead = 0;
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        return toHexString(md5.digest());
    }

    /**
     * 功能描述：将二进制转换为hex字符串
     */
    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * 如果文件夹不存在，创建
     *
     * @param filePath
     * @throws IOException
     */
    public static boolean createDirectory(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            // 文件不存在，创建
            return file.mkdirs();
        }
        return true;
    }

    /**
     * @Description:拷贝文件
     */
    public static boolean copyFile(String sourceFile, String targtFile) {
        try {
            File sfile = new File(sourceFile);
            File tfile = new File(targtFile);
            if (sfile.exists()) {
                FileInputStream in = null;
                FileOutputStream out = null;
                try {
                    in = new FileInputStream(sfile);
                    out = new FileOutputStream(tfile);

                    byte[] buff = new byte[1024];
                    int length = 0;
                    while ((length = in.read(buff, 0, 1024)) > 0) {
                        out.write(buff, 0, length);
                    }

                } catch (Exception e) {
                    return false;
                } finally {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 文件初始化方法（创建文件夹或删除文件夹下的文件）
     *
     * @param filePath
     * @throws IOException
     */
    public static void fileInitialize(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            // 文件不存在，创建
            file.mkdirs();
        } else {
            // 文件存在删除
            deleteDirectory(file);
        }
    }

    /**
     * 提供删除目录和文件
     *
     * @param dir
     * @throws IOException
     */
    public static void deleteDirectory(File dir) throws IOException {
        if ((dir == null) || !dir.isDirectory()) {
            return;
        }
        File[] entries = dir.listFiles();
        int sz = entries.length;
        for (int i = 0; i < sz; i++) {
            if (entries[i].isDirectory()) {
                deleteDirectory(entries[i]);
            } else {
                try {
                    entries[i].delete();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     *
     * 功能描述：将byte数组转换为int
     *
     */
    public static int bytes2int(byte[] val) {
        int ret = 0;
        ret |= (int) (val[0] & 0xff) << 0;
        ret |= (int) (val[1] & 0xff) << 8;
        ret |= (int) (val[2] & 0xff) << 16;
        ret |= (int) (val[3] & 0xff) << 24;

        return ret;
    }

    public static byte[] int2bytes(int val) {
        byte[] b = new byte[4];
        b[0] = (byte) (val);
        b[1] = (byte) (val >> 8);
        b[2] = (byte) (val >> 16);
        b[3] = (byte) (val >> 24);
        return b;
    }

    /**
     *
     * 功能描述：对byte数组进行MD5计算
     *
     */
    public static byte[] md5Byte(byte[] b) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        digest.update(b);
        return digest.digest();
    }

    /**
     *
     * 功能描述：比较两个byte数组是否相等
     *
     */
    public static boolean equalBytes(byte[] arrayA, byte[] arrayB) {
        if (arrayA == null || arrayB == null) {
            return false;
        }
        if (arrayA.length != arrayB.length) {
            return false;
        }

        for (int i = 0; i < arrayA.length; i++) {
            if (arrayA[i] != arrayB[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取class所在绝对路径
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getRealPath() throws UnsupportedEncodingException {
        String realFilePath = FileUtil.class.getResource("/").getPath();
        realFilePath = URLDecoder.decode(realFilePath, "utf-8");
        return realFilePath;
    }

    /**
     * @Description: 获取文件MD5
     */
    public static byte[] getFileMD5Bytes(String filePath) throws Exception {
        InputStream fis = null;
        MessageDigest md5 = null;
        try {
            fis = new FileInputStream(filePath);
            byte[] buffer = new byte[1024 * 512];
            md5 = MessageDigest.getInstance("MD5");
            int numRead = 0;
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        return md5.digest();
    }

    /**
     *
     * @Description: 可以在处理大文件时，提升性能
     */
    public static byte[] toByteArray3(String filename) throws IOException {
        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(filename, "r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).load();
            System.out.println(byteBuffer.isLoaded());
            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
