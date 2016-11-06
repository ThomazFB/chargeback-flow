package com.thomazfbcortez.chargebackflow.api.message;

import com.google.gson.Gson;
import com.thomazfbcortez.chargebackflow.exception.JSONParsingException;
import com.thomazfbcortez.chargebackflow.model.ReliableModel;

/**
 * Any desired message to the API must be implemented as a class extending Message,
 * this class ensure that for any API operation, all needed info will be reachable
 *
 * All messages must offer a result for the API operation with the getResult() method,
 * and be able to construct the result with the parse() method
 */

public abstract class Message
{
    protected String url;
    protected String jsonResponse;
    protected int errorID;
    protected Gson gson = new Gson();

    public abstract ReliableModel getResult();

    public abstract void parse();

    public String getUrl()
    {
        return url;
    }

    public void setJsonResponse(String jsonResponse)
    {
        this.jsonResponse = jsonResponse;
    }

    public int getErrorID()
    {
        return errorID;
    }

    public void setErrorID(int errorID)
    {
        this.errorID = errorID;
    }

    public void checkReliability()
    {
        if (getResult() == null || getResult().isModelNullable())
            throw new JSONParsingException("Failed to parse JSON response from " + this.toString());
    }
}