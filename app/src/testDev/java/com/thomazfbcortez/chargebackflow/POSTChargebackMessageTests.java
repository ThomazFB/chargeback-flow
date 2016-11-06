package com.thomazfbcortez.chargebackflow;

import com.google.gson.JsonSyntaxException;
import com.thomazfbcortez.chargebackflow.api.message.POSTChargebackMessage;
import com.thomazfbcortez.chargebackflow.exception.OperationNotCompletedException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.BROKEN_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.CHARGEBACK_SUBMISSION_JSON;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.EMPTY_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.POST_OK_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.buildChargebackMock;

public class POSTChargebackMessageTests
{
    private POSTChargebackMessage postChargebackMessage;

    @Before
    public void init()
    {
        postChargebackMessage = new POSTChargebackMessage("", buildChargebackMock());
    }

    @Test(expected = OperationNotCompletedException.class)
    public void parse_POSTChargebackMessageWithEmptyEmptyResponse_ShouldThrowOperationNotCompletedException()
    {
        postChargebackMessage.setJsonResponse(EMPTY_RESPONSE);
        postChargebackMessage.parse();
        postChargebackMessage.checkReliability();
    }

    @Test(expected = OperationNotCompletedException.class)
    public void parse_POSTChargebackMessageWithNotOkResponse_ShouldThrowOperationNotCompletedException()
    {
        postChargebackMessage.setJsonResponse("{\"status\":\"tooBad\"}");
        postChargebackMessage.parse();
        postChargebackMessage.checkReliability();
    }

    @Test(expected = JsonSyntaxException.class)
    public void parse_POSTChargebackMessageWithWrongJSONSyntaxResponse_ShouldThrowSyntaxException()
    {
        postChargebackMessage.setJsonResponse(BROKEN_RESPONSE);
        postChargebackMessage.parse();
        postChargebackMessage.checkReliability();
    }

    @Test
    public void parse_POSTChargebackMessageWithOkResponse_ShouldSucceed()
    {
        postChargebackMessage.setJsonResponse(POST_OK_RESPONSE);
        postChargebackMessage.parse();
        postChargebackMessage.checkReliability();
    }

    @Test
    public void parse_POSTChargebackMessage_ShouldBuildJSONCorrectly()
    {
        Assert.assertTrue(postChargebackMessage.getMessage().equals(CHARGEBACK_SUBMISSION_JSON));
    }
}
