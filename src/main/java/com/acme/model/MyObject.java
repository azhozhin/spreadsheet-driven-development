package com.acme.model;

public class MyObject {

    private boolean negate;

    public MyObject() {
        this.negate = false;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    public boolean isNegate() {
        return negate;
    }
}
