package com.wallf.bukalib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author acton
 */
public class BukaFile {

    public static final String BUKA_FILE_FLAG = "buka";
    private static final int BUFFER_SIZE = 10 * 1024;
    private int mMinorVersion;
    private int mMajorVersion;
    private ChapterInfo mChapterInfo;
    private String mComicName;
    private RandomAccessFile mBukaFile;
    private String mBukaFilePath;
    private int mComicId;
    private int mChapterId;
    private LinkedHashMap<String, MetaInfo> mMetas = new LinkedHashMap<>();

    public BukaFile(String path) throws FileNotFoundException, BadBukaException {
        mBukaFilePath = path;
        File bukaFile = new File(mBukaFilePath);
        if (bukaFile.exists()) {
            if (bukaFile.isFile()) {
                mBukaFile = new RandomAccessFile(bukaFile, "r");
                try {
                    readInfoFromFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        mBukaFile.close();
                        mBukaFile = null;
                    } catch (IOException e1) {
                    }
                    throw new BadBukaException("Read buka file fail!");
                }
            } else {
                throw new FileNotFoundException(mBukaFilePath + " is not a file.");
            }
        } else {
            throw new FileNotFoundException(mBukaFilePath + " is non-existent.");
        }
    }

    public void close() throws IOException {
        if (mBukaFile != null) {
            mBukaFile.close();
            mBukaFile = null;
        }
    }

    private void readInfoFromFile() throws IOException, BadBukaException {
        byte[] buffer = new byte[128];
        mBukaFile.read(buffer);
        int startPos = 0;
        //check file type header
        if (buffer[0] == 'b' && buffer[1] == 'u' && buffer[2] == 'k' && buffer[3] == 'a') {
            //get version info. startPos = 4;
            ByteBuffer bbuffer = ByteBuffer.allocate(8);
            bbuffer.put(buffer, 4, 8);
            bbuffer.order(ByteOrder.LITTLE_ENDIAN);
            mMajorVersion = bbuffer.getInt(0);
            mMinorVersion = bbuffer.getInt(4);
            //get comic id. startPos = 12
            bbuffer.position(0);
            bbuffer.put(buffer, 12, 4);
            mComicId = bbuffer.getInt(0);
            //get chapter id. startPos = 16
            bbuffer.position(0);
            bbuffer.put(buffer, 16, 4);
            mChapterId = bbuffer.getInt(0);
            //get comic name. startPos = 20
            int nameEndPos = 20;
            for (int i = 20; i < 128; ++i) {
                if (buffer[i] == 0x00) {
                    nameEndPos = i;
                    break;
                }
            }
            assert (nameEndPos > 20);
            String comicName = new String(buffer, 20, nameEndPos - 20, Charset.forName("utf-8"));
            startPos = nameEndPos + 1;
            //read meta data len. startPos = nameEndPos + 1;
            bbuffer.position(0);
            bbuffer.put(buffer, startPos, 4);
            int metaLen = bbuffer.getInt(0);
            //read meta data
            mBukaFile.seek(nameEndPos + 5);
            buffer = new byte[metaLen];
            mBukaFile.read(buffer);
            startPos = 0;
            int pointer, size, endPos = 0;
            while (startPos + 8 < metaLen) {
                bbuffer.position(0);
                bbuffer.put(buffer, startPos, 8);
                pointer = bbuffer.getInt(0);
                size = bbuffer.getInt(4);
                startPos += 8;
                endPos = startPos;
                for (int i = startPos; i < metaLen; ++i) {
                    if (buffer[i] == 0x00) {
                        endPos = i;
                        break;
                    }
                }
                if (endPos > startPos) {
                    String metaName = new String(buffer, startPos, endPos - startPos, Charset.forName("utf-8"));
                    MetaInfo info = new MetaInfo();
                    info.setName(metaName);
                    info.setSize(size);
                    info.setStartPosition(pointer);
                    mMetas.put(metaName, info);
                    startPos = endPos + 1;
                } else {
                    break;
                }
            }
            //Read chapter info
            readChapterInfo();
            mComicName = comicName;
        } else {
            throw new BadBukaException("not a buka file.");
        }
    }

    private void readChapterInfo() throws IOException {
        MetaInfo chapterMetaInfo = mMetas.get("chaporder.dat");
        if (chapterMetaInfo != null) {
            byte[] buffer = new byte[chapterMetaInfo.getSize()];
            mBukaFile.seek(chapterMetaInfo.getStartPosition());
            mBukaFile.read(buffer);
            String infoStr = new String(buffer, Charset.forName("utf-8"));
            mChapterInfo = new ChapterInfo(infoStr);
        }
    }

    public RandomAccessFile getBukaFile() {
        return mBukaFile;
    }

    public Map<String, MetaInfo> getMetas() {
        return mMetas;
    }

    public void extractTo(File outputDir) throws IOException {
        Map<String, MetaInfo> metas = mMetas;
        Set<String> keys = metas.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            MetaInfo info = metas.get(key);
            if (key.endsWith(".bup")) {
                mBukaFile.seek(info.getStartPosition() + 64);
                byte[] headByte = new byte[32];
                mBukaFile.read(headByte);
                String type = detectfileType(headByte);
                if (type != null) {
                    mBukaFile.seek(info.getStartPosition() + 64);
                    File outFile = new File(outputDir, key.substring(0, key.length() - 4) + "." + type);
                    FileOutputStream fout = null;
                    try {
                        fout = new FileOutputStream(outFile);
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int outLen = 0, readLen;
                        int size = info.getSize() - 64;
                        while (outLen < size) {
                            readLen = mBukaFile.read(buffer);
                            if (readLen > 0) {
                                if ((outLen + readLen) > size) {
                                    readLen = size - outLen;
                                }
                                fout.write(buffer, 0, readLen);
                                outLen += readLen;
                            } else {
                                break;
                            }
                        }
                    } finally {
                        if (fout != null) {
                            fout.close();
                        }
                    }
                }
            } else if (key.equals("logo")) {
                mBukaFile.seek(info.getStartPosition());
                byte[] headByte = new byte[32];
                mBukaFile.read(headByte);
                String type = detectfileType(headByte);
                if (type != null) {
                    File outFile = new File(outputDir, key + "." + type);
                    FileOutputStream fout = null;
                    try {
                        fout = new FileOutputStream(outFile);
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int outLen = 0, readLen;
                        int size = info.getSize();
                        while (outLen < size) {
                            readLen = mBukaFile.read(buffer);
                            if (readLen > 0) {
                                if ((outLen + readLen) > size) {
                                    readLen = size - outLen;
                                }
                                fout.write(buffer, 0, readLen);
                                outLen += readLen;
                            } else {
                                break;
                            }
                        }
                    } finally {
                        if (fout != null) {
                            fout.close();
                        }
                    }
                }
            } else {
                mBukaFile.seek(info.getStartPosition());
                File outFile = new File(outputDir, key);
                FileOutputStream fout = null;
                try {
                    fout = new FileOutputStream(outFile);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int outLen = 0, readLen;
                    int size = info.getSize();
                    while (outLen < size) {
                        readLen = mBukaFile.read(buffer);
                        if (readLen > 0) {
                            if ((outLen + readLen) > size) {
                                readLen = size - outLen;
                            }
                            fout.write(buffer, 0, readLen);
                            outLen += readLen;
                        } else {
                            break;
                        }
                    }
                } finally {
                    if (fout != null) {
                        fout.close();
                    }
                }
            }
        }
    }

    private String detectfileType(final byte[] byte32) {
        if (byte32[6] == 'J' && byte32[7] == 'F' && byte32[8] == 'I' && byte32[9] == 'F') {
            return "jpg";
        } else if (byte32[6] == 'E' && byte32[7] == 'x' && byte32[8] == 'i' && byte32[9] == 'f') {
            return "jpg";
        } else if (byte32[0] == 'R' && byte32[1] == 'I' && byte32[2] == 'F' && byte32[3] == 'F') {
            return "webp";
        } else if (byte32[8] == 'W' && byte32[9] == 'E' && byte32[10] == 'B' && byte32[11] == 'P'
                && byte32[12] == 'V' && byte32[13] == 'P' && byte32[14] == '8' && byte32[15] == ' ') {
            return "webp";
        } else if (byte32[0] == 0x211 && byte32[1] == 'P' && byte32[2] == 'N' && byte32[3] == 'G'
                && byte32[4] == '\r' && byte32[5] == '\n' && byte32[6] == 0x32 && byte32[7] == '\n') {
            return "png";
        } else if (byte32[0] == 'G' && byte32[1] == 'I' && byte32[2] == 'F' && byte32[3] == '8'
                && byte32[4] == '7' && byte32[5] == 'a') {
            return "gif";
        } else if (byte32[0] == 'G' && byte32[1] == 'I' && byte32[2] == 'F' && byte32[3] == '8'
                && byte32[4] == '9' && byte32[5] == 'a') {
            return "gif";
        } else if (byte32[0] == 'b' && byte32[1] == 'u' && byte32[2] == 'p' && byte32[3] == 0x00) {
            return "bup";
        }
        return null;
    }

    public String getComicName() {
        return mComicName;
    }

    public int getChapterId() {
        return mChapterId;
    }
}
