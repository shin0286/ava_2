package com.fit.config;

/**
 * Bound of variable
 *
 * @author ducanh
 */
public class Bound implements IBound {

    private int lower;

    private int upper;

    public Bound() {
    }

    public Bound(int lower, int upper) {
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public int getLower() {
        return lower;
    }

    @Override
    public void setLower(int lower) {
        this.lower = lower;
    }

    @Override
    public int getUpper() {
        return upper;
    }

    @Override
    public void setUpper(int upper) {
        this.upper = upper;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "[" + lower + ".." + upper + "]";
    }
}
