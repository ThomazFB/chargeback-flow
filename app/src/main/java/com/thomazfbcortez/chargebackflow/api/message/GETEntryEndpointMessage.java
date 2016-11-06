package com.thomazfbcortez.chargebackflow.api.message;

import com.thomazfbcortez.chargebackflow.model.EntryEndpoint;

public class GETEntryEndpointMessage extends Message
{
    private EntryEndpoint EntryEndpointResult;

    public GETEntryEndpointMessage(String defaultEndpointURL)
    {
        this.url = defaultEndpointURL;
    }

    @Override
    public EntryEndpoint getResult()
    {
        return EntryEndpointResult;
    }

    @Override
    public void parse()
    {
        EntryEndpointResult = gson.fromJson(jsonResponse, EntryEndpoint.class);
    }
}
