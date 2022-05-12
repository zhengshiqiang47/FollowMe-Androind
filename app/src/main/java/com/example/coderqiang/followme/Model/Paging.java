package com.example.coderqiang.followme.Model;

import java.io.Serializable;
import java.util.List;

/**
 * @author: chenyi.zsq
 * @Date: 2022/5/11
 */
public class Paging<T> implements Serializable {

    private int page;

    private int size;

    private long count;

    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getTotalPage() {
        int pgSize = this.size;
        long total = this.count;
        long result = total / pgSize;
        if (total == 0 || total % pgSize != 0) {
            ++result;
        }
        return (int)result;
    }
}
