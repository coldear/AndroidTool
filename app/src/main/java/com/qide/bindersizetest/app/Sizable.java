package com.qide.bindersizetest.app;

/**
 * Created by qideren on 11/11/15.
 */
public class Sizable implements Comparable<Sizable> {
    String name;
    int size;

    @Override
    public int compareTo(Sizable sizable) {
        if (size < sizable.size) {
            return -1;
        } else if (size == sizable.size) {
            return 0;
        } else {
            return 1;
        }
    }

    public Sizable(String name, int size) {
        this.name = name;
        this.size = size;
    }


    @Override
    public String toString() {
        return name + ":\t" + size;
    }
}
