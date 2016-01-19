package com.wallf.bukalib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author acton
 */
public class BukaExtractor {

    private BukaFile mBukaFile;
    private File mOutputDir;

    public static void main(String[] args) {
        BukaExtractor extractor = new BukaExtractor();
        File inputDir = new File("./input/217208");
        String[] bukaFiles = inputDir.list();
        for (int i = 0; i < bukaFiles.length; ++i) {
            if (bukaFiles[i].endsWith(".buka")) {
                //read buka file
                try {
                    extractor.readBukaFile(inputDir.getAbsolutePath() + "/" + bukaFiles[i]);
                    //create output dir
                    extractor.createOutputDir();
                    //extract *.buka to multi resource files
                    extractor.extractBukaFile();
                    //convert resource file to jpg
                    extractor.convertResources();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (BadBukaException e) {
                    e.printStackTrace();
                } finally {
                    //clean temp file
                    extractor.clean();
                }
            }
        }
    }

    private void readBukaFile(String bukafile) throws FileNotFoundException, BadBukaException {
        mBukaFile = new BukaFile(bukafile);
    }

    private void createOutputDir() {
        String comicName = mBukaFile.getComicName();
        int chapterId = mBukaFile.getChapterId();
        File output = new File("output");
        if (!output.exists() || !output.isDirectory()) {
            output.mkdir();
        }
        File targetDir = new File(output, comicName + "-" + chapterId);
        if (!targetDir.exists() || !targetDir.isDirectory()) {
            targetDir.mkdir();
        }
        mOutputDir = targetDir;
    }

    private void extractBukaFile() throws IOException {
        mBukaFile.extractTo(mOutputDir);
    }

    private void convertResources() {

    }

    private void clean() {
        try {
            if (mBukaFile != null)
                mBukaFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
