package com.wallf.bukalib;

/**
 * @author acton
 */
public class MetaInfo {
    private String mName;
    private int mStartPos;
    private int mSize;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getStartPosition() {
        return mStartPos;
    }

    public void setStartPosition(int pos) {
        this.mStartPos = pos;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        this.mSize = size;
    }
}
