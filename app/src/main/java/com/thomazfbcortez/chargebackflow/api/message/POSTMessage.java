package com.thomazfbcortez.chargebackflow.api.message;

import com.thomazfbcortez.chargebackflow.exception.OperationNotCompletedException;
import com.thomazfbcortez.chargebackflow.model.Operation;

/**
 * All POSTs messages need additional info (message and operation) to operate with the API,
 * so any POST message must be implemented as a class extending POSTMessage
 */

public abstract class POSTMessage extends Message
{
    protected String message;
    protected Operation operation;

    public String getMessage()
    {
        return message;
    }

    @Override
    public void setJsonResponse(String response)
    {
        operation = gson.fromJson(response, Operation.class);
    }

    @Override
    public Operation getResult()
    {
        return operation;
    }

    @Override
    public void checkReliability()
    {
        if(!operation.operationSucceeded())
            throw new OperationNotCompletedException("API didn't returned OK to the operation" + this.toString());
    }

    @Override
    public void parse()
    {

    }
}
