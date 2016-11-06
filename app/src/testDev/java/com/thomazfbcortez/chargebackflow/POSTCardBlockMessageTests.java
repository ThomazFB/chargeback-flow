package com.thomazfbcortez.chargebackflow;

import com.google.gson.JsonSyntaxException;
import com.thomazfbcortez.chargebackflow.api.message.POSTCardBlockMessage;
import com.thomazfbcortez.chargebackflow.exception.OperationNotCompletedException;

import org.junit.Before;
import org.junit.Test;

import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.BROKEN_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.EMPTY_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.POST_ERROR_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.POST_OK_RESPONSE;

public class POSTCardBlockMessageTests
{
    private POSTCardBlockMessage postCardBlockMessage;

    @Before
    public void init()
    {
        postCardBlockMessage = new POSTCardBlockMessage("");
    }

    @Test(expected = OperationNotCompletedException.class)
    public void parse_CardBlockMessageWithEmptyEmptyResponse_ShouldThrowOperationNotCompletedException()
    {
        postCardBlockMessage.setJsonResponse(EMPTY_RESPONSE);
        postCardBlockMessage.parse();
        postCardBlockMessage.checkReliability();
    }

    @Test(expected = OperationNotCompletedException.class)
    public void parse_CardBlockMessageWithNotOkResponse_ShouldThrowOperationNotCompletedException()
    {
        postCardBlockMessage.setJsonResponse(POST_ERROR_RESPONSE);
        postCardBlockMessage.parse();
        postCardBlockMessage.checkReliability();
    }

    @Test(expected = JsonSyntaxException.class)
    public void parse_CardBlockMessageWithWrongJSONSyntaxResponse_ShouldThrowSyntaxException()
    {
        postCardBlockMessage.setJsonResponse(BROKEN_RESPONSE);
        postCardBlockMessage.parse();
        postCardBlockMessage.checkReliability();
    }

    @Test
    public void parse_CardBlockMessageWithOkResponse_ShouldSucceed()
    {
        postCardBlockMessage.setJsonResponse(POST_OK_RESPONSE);
        postCardBlockMessage.parse();
        postCardBlockMessage.checkReliability();
    }
}
