package com.thomazfbcortez.chargebackflow.model;

public class Operation extends ReliableModel
{
    private String status;

    @Override
    public boolean isModelNullable()
    {
        return false;
    }

    public boolean operationSucceeded()
    {
        return (status != null && status.equals("Ok"));
    }
}
