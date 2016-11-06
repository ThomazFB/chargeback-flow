package com.thomazfbcortez.chargebackflow.exception;

public class OperationNotCompletedException extends RuntimeException
{
    public OperationNotCompletedException(String message)
    {
        super(message);
    }
}
