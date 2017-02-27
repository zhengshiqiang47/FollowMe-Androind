package com.example.coderqiang.followme.Model;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by CoderQiang on 2017/2/17.
 */

public class MySearchResult {
    public static final int TYPE_LOCATION=1;
    public static final int TYPE_USER=2;
    public static final int TYPE_SCENICSPOT=3;
    public static final int TYPE_DYNAMIC=4;
    public static final int TYPE_ADDRESS=5;

    public MySearchResult(int type, String content, String note, String typeStr) {
        this.type = type;
        this.content = content;
        this.note = note;
        this.typeStr = typeStr;
    }

    private int type;
    private String content;
    private String note;
    private String typeStr;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }
}
