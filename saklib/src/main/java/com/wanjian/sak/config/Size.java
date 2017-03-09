package com.wanjian.sak.config;


import java.util.LinkedList;
import java.util.List;

/**
 * Created by wanjian on 2017/2/20.
 */

public class Size {

    private float length;
    private String unit;

    private Size() {
    }

    public Size(int length, String unit) {
        this.length = length;
        this.unit = unit;
    }

    public float getLength() {
        return length;
    }

    public Size setLength(float length) {
        this.length = length;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public Size setUnit(String unit) {
        this.unit = unit;
        return this;
    }


    private static List<Size> sList = new LinkedList<>();

    public static Size obtain() {
        if (sList.isEmpty()) {
            return new Size();
        }

        return sList.get(0);

    }

    public static void returnSize(Size size) {
        if (size == null) {
            throw new RuntimeException("size can not be null!");
        }
        if (sList.size() < 100) {
            sList.add(size);
        }
    }

    @Override
    public String toString() {
        return length + " " + unit;
    }
}
