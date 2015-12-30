package com.wallf.bukalib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * @author acton
 */
public class BukaFile {

    public static final String BUKA_FILE_FLAG = "buka";
    private int mMinorVersion;
    private int mMajorVersion;
    private ChapterInfo mChapterInfo;
    private String mComicName;
    private RandomAccessFile mBukaFile;

    public BukaFile(String path) throws FileNotFoundException, BadBukaException {
        File bukaFile = new File(path);
        if (bukaFile.exists()) {
            if (bukaFile.isFile()) {
                mBukaFile = new RandomAccessFile(bukaFile, "r");
                try {
                    readInfoFromFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new BadBukaException("Read buka file fail!");
                } finally {
                    if (mBukaFile != null) {
                        try {
                            mBukaFile.close();
                        } catch (IOException e) {
                        }
                    }
                }
            } else {
                throw new FileNotFoundException(path + " is not a file.");
            }
        } else {
            throw new FileNotFoundException(path + " is non-existent.");
        }
    }

    private void readInfoFromFile() throws IOException, BadBukaException {
        byte[] buffer = new byte[128];
        mBukaFile.read(buffer);
        //check file type header
        if (buffer[0] == 'b' && buffer[1] == 'u' && buffer[2] == 'k' && buffer[3] == 'a') {
            //get version info
            ByteBuffer bbuffer = ByteBuffer.allocate(8);
            bbuffer.put(buffer, 4, 8);
            bbuffer.order(ByteOrder.LITTLE_ENDIAN);
            bbuffer.position(0);
            mMajorVersion = bbuffer.getInt(0);
            mMinorVersion = bbuffer.getInt(4);
            //
        } else {
            throw new BadBukaException("not a buka file.");
        }
    }
}
