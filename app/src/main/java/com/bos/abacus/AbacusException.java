package com.bos.abacus;

public class AbacusException extends RuntimeException{
    public AbacusException(String message,Exception e){
        super(message,e);
    }
    public AbacusException(String message){
        super(message);
    }
}
