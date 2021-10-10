package com.bos.abacus;

public enum ProblemType {
    SUM("Sum"), SUMWITHSUBSTRACT("Sum & Subtract"), MULTIPLY("Multiplication");

    private String displayVal;

    private ProblemType(String displayVal) {
        this.displayVal = displayVal;
    }

    public String getDisplayVal() {
        return displayVal;
    }

}
