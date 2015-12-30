package com.wallf.bukalib;

import java.io.FileNotFoundException;

/**
 * @author acton
 */
public class BukaExtractor {

    private BukaFile mBukaFile;

    public static void main(String[] args) {
        BukaExtractor extractor = new BukaExtractor();
        //read buka file
        try {
            extractor.readBukaFile();
            //create output dir
            extractor.createOutputDir();
            //extract *.buka to multi resource files
            extractor.extractBukaFile();
            //convert resource file to jpg
            extractor.convertResources();
            //clean temp file
            extractor.clean();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BadBukaException e) {
            e.printStackTrace();
        }
    }

    private void readBukaFile() throws FileNotFoundException, BadBukaException {
        mBukaFile = new BukaFile("65537.buka");
    }

    private void createOutputDir() {

    }

    private void extractBukaFile() {

    }

    private void convertResources() {

    }

    private void clean() {

    }
}
