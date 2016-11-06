package com.thomazfbcortez.chargebackflow;

import com.google.gson.JsonSyntaxException;
import com.thomazfbcortez.chargebackflow.api.message.GETEntryEndpointMessage;
import com.thomazfbcortez.chargebackflow.exception.JSONParsingException;

import org.junit.Before;
import org.junit.Test;

import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.ALIKE_ENTRY_ENDPOINT_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.BROKEN_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.EMPTY_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.ENTRY_ENDPOINT_RESPONSE;

public class GETEntryEndpointMessageTests
{
    private GETEntryEndpointMessage getEntryEndpointMessage;

    @Before
    public void init()
    {
        getEntryEndpointMessage = new GETEntryEndpointMessage("");
    }

    @Test(expected = JSONParsingException.class)
    public void parse_EndpointMessageWithEmptyResponse_ShouldThrowParsingException()
    {
        getEntryEndpointMessage.setJsonResponse("");
        getEntryEndpointMessage.parse();
        getEntryEndpointMessage.checkReliability();
    }

    @Test(expected = JSONParsingException.class)
    public void parse_EndpointMessageWithNullResponse_ShouldThrowParsingException()
    {
        getEntryEndpointMessage.setJsonResponse(null);
        getEntryEndpointMessage.parse();
        getEntryEndpointMessage.checkReliability();
    }

    @Test(expected = JSONParsingException.class)
    public void parse_EndpointMessageWithAlikeJSON_ShouldThrowParsingException()
    {
        getEntryEndpointMessage.setJsonResponse(ALIKE_ENTRY_ENDPOINT_RESPONSE);
        getEntryEndpointMessage.parse();
        getEntryEndpointMessage.checkReliability();
    }

    @Test(expected = JSONParsingException.class)
    public void parse_EndpointMessageWithEmptyJSON_ShouldThrowParsingException()
    {
        getEntryEndpointMessage.setJsonResponse(EMPTY_RESPONSE);
        getEntryEndpointMessage.parse();
        getEntryEndpointMessage.checkReliability();
    }

    @Test(expected = JsonSyntaxException.class)
    public void parse_MessageWithWrongJSONSyntax_ShouldThrowSyntaxException()
    {
        getEntryEndpointMessage.setJsonResponse(BROKEN_RESPONSE);
        getEntryEndpointMessage.parse();
        getEntryEndpointMessage.checkReliability();
    }

    @Test
    public void parse_EndpointMessageWithCorrectJSON_ShouldSucceed()
    {
        getEntryEndpointMessage.setJsonResponse(ENTRY_ENDPOINT_RESPONSE);
        getEntryEndpointMessage.parse();
        getEntryEndpointMessage.checkReliability();
    }
}
