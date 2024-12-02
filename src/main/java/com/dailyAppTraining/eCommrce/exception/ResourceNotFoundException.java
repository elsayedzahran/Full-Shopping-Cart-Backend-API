package com.dailyAppTraining.eCommrce.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String type){
        super("This " + type + " doesn't exist");
    }
    public ResourceNotFoundException(String type, Long id){
        super("No " + type + " found with id:" + id);
    }
}
