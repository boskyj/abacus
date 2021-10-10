package com.bos.abacus;

public interface AbacusHandler {
    public void preStart();
    public void setAnswer(long answer);
    public void setQuestion(long question);
    public void onCalculationEnd();
}
