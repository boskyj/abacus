package com.bos.abacus;

import android.util.Log;

import java.util.concurrent.ThreadLocalRandom;

public class Abacus {

    private static Abacus instance;

    private int start;
    private int end;
    private int count;
    private int repeatCount;
    private int questionDelay;
    private int answerDelay;
    private AbacusHandler handler;
    private boolean stop;
    private final Object lock = new Object();
    private volatile boolean paused = false;

    public Abacus() {
    }


    public static Abacus getInstance() {
        if (instance == null) {
            instance = new Abacus();
        }
        return instance;
    }

    public void setHandler(AbacusHandler handler) {
        this.handler = handler;
    }

    public void start(int start, int end, int rows, int questionCount, int questionDelay, int answerDelay, ProblemType type) {
        try {
            this.start = start;
            this.end = end;
            this.count = rows;
            this.repeatCount = questionCount;
            this.questionDelay = questionDelay;
            this.answerDelay = answerDelay;
            handler.preStart();
            switch (type) {
                case SUMWITHSUBSTRACT:
                    for (int i = 0; i < repeatCount; i++) {
                        if (stop) {
                            Log.d("Abacus", "SUMWITHSUBSTRACT stopped");
                            break;
                        }
                        doSumWithLess();
                    }
                    break;
                case MULTIPLY:
                    for (int i = 0; i < repeatCount; i++) {
                        if (stop) {
                            Log.d("Abacus", "MULTIPLY stopped");
                            break;
                        }
                        doMultiplication();
                    }
                    break;
                default:
                    for (int i = 0; i < repeatCount; i++) {
                        synchronized (lock) {
                            if(paused) {
                                wait();
                            }
                            if (stop) {
                                Log.d("Abacus", "Sum stopped");
                                break;
                            }
                            doSum();
                        }
                    }
            }
            handler.onCalculationEnd();
        }catch (AbacusException|InterruptedException e){
            Log.e("Abacus",e.getMessage());
        }

    }
    public void pause() {
          paused = true;
    }
    public void resume() {
        synchronized (lock) {
            paused = false;
            lock.notifyAll();
        }
    }

    public void setStop(boolean stop){
        this.stop=stop;
    }

    public void doSum() {

        long sum = ThreadLocalRandom.current().longs(start, end).limit(count).map(x -> {

            try {
                Thread.sleep((long) questionDelay * 1000);
            } catch (InterruptedException e) {
                throw  new AbacusException("Thread interrupted",e);
            }

            handler.setQuestion(x);
            return x;
        }).sum();
        try {
            Thread.sleep((long) answerDelay * 1000);
        } catch (InterruptedException e) {
            throw  new AbacusException("Thread interrupted",e);
        }

        handler.setAnswer(sum);

    }

    public void doSumWithLess() {
        long values[] = ThreadLocalRandom.current().longs(start, end).limit(count).toArray();
        long sum = 0;

        for (long val : values) {
            if (sum > val) {
                val = val * -1;
            }
            try {
                Thread.sleep((long) questionDelay * 1000);
            } catch (InterruptedException e) {
                throw  new AbacusException("Thread interrupted",e);
            }
            handler.setQuestion(val);
            sum = sum + val;

        }
        try {
            Thread.sleep((long) answerDelay * 1000);
        } catch (InterruptedException e) {
            throw  new AbacusException("Thread interrupted",e);
        }
        handler.setAnswer(sum);
    }

    public void doMultiplication() {

        long mul = ThreadLocalRandom.current().longs(start, end).limit(count).map(x -> {

            try {
                Thread.sleep((long) questionDelay * 1000);
            } catch (InterruptedException e) {
                throw  new AbacusException("Thread interrupted",e);
            }
            handler.setQuestion(x);
            return x;
        }).reduce(1, (a, b) -> a * b);
        try {
            Thread.sleep((long) answerDelay * 1000);
        } catch (InterruptedException e) {
            throw  new AbacusException("Thread interrupted",e);
        }
        handler.setAnswer(mul);
    }


}


