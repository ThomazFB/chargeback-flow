package com.thomazfbcortez.chargebackflow.api.message;

public class ErrorMessage
{
    private final int errorID;

    public ErrorMessage(int errorID)
    {
        this.errorID = errorID;
    }

    public int getId()
    {
        return errorID;
    }
}
