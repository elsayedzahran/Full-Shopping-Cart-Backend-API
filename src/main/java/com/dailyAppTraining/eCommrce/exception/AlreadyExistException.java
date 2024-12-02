package com.dailyAppTraining.eCommrce.exception;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String type){
        super("This " + type + " already exists");
    }
}
