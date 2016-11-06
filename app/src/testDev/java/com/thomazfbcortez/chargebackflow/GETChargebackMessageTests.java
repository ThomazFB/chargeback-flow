package com.thomazfbcortez.chargebackflow;

import com.google.gson.JsonSyntaxException;
import com.thomazfbcortez.chargebackflow.api.message.GETChargebackMessage;
import com.thomazfbcortez.chargebackflow.exception.JSONParsingException;

import org.junit.Before;
import org.junit.Test;

import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.ALIKE_CHARGEBACK_FORM_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.BROKEN_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.CHARGEBACK_FORM_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.EMPTY_RESPONSE;

public class GETChargebackMessageTests
{
    private GETChargebackMessage getChargebackMessage;

    @Before
    public void init()
    {
        getChargebackMessage = new GETChargebackMessage("");
    }

    @Test(expected = JSONParsingException.class)
    public void parse_ChargebackMessageWithEmptyResponse_ShouldThrowParsingException()
    {
        getChargebackMessage.setJsonResponse("");
        getChargebackMessage.parse();
        getChargebackMessage.checkReliability();
    }

    @Test(expected = JSONParsingException.class)
    public void parse_ChargebackMessageWithNullResponse_ShouldThrowParsingException()
    {
        getChargebackMessage.setJsonResponse(null);
        getChargebackMessage.parse();
        getChargebackMessage.checkReliability();
    }

    @Test(expected = JSONParsingException.class)
    public void parse_ChargebackMessageWithAlikeJSON_ShouldThrowParsingException()
    {
        getChargebackMessage.setJsonResponse(ALIKE_CHARGEBACK_FORM_RESPONSE);
        getChargebackMessage.parse();
        getChargebackMessage.checkReliability();
    }

    @Test(expected = JSONParsingException.class)
    public void parse_ChargebackMessageWithEmptyJSON_ShouldThrowParsingException()
    {
        getChargebackMessage.setJsonResponse(EMPTY_RESPONSE);
        getChargebackMessage.parse();
        getChargebackMessage.checkReliability();
    }


    @Test(expected = JsonSyntaxException.class)
    public void parse_MessageWithWrongJSONSyntax_ShouldThrowSyntaxException()
    {
        getChargebackMessage.setJsonResponse(BROKEN_RESPONSE);
        getChargebackMessage.parse();
        getChargebackMessage.checkReliability();
    }

    @Test
    public void parse_ChargebackMessageWithCorrectJSON_ShouldSucceed()
    {
        getChargebackMessage.setJsonResponse(CHARGEBACK_FORM_RESPONSE);
        getChargebackMessage.parse();
        getChargebackMessage.checkReliability();
    }
}
