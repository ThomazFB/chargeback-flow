package com.thomazfbcortez.chargebackflow;

import com.google.gson.JsonSyntaxException;
import com.thomazfbcortez.chargebackflow.api.message.GETNoticeMessage;
import com.thomazfbcortez.chargebackflow.exception.JSONParsingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.ALIKE_NOTICE_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.BROKEN_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.EMPTY_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.NOTICE_RESPONSE;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.buildGETEntryEndpointMessageMock;

@RunWith(JUnit4.class)
public class GETNoticeMessageTests
{
    private GETNoticeMessage getNoticeMessage;

    @Before
    public void init()
    {
        getNoticeMessage = new GETNoticeMessage(buildGETEntryEndpointMessageMock().getResult());
    }

    @Test(expected = JSONParsingException.class)
    public void parse_NoticeMessageWithEmptyResponse_ShouldThrowParsingException()
    {
        getNoticeMessage.setJsonResponse("");
        getNoticeMessage.parse();
        getNoticeMessage.checkReliability();
    }

    @Test(expected = JSONParsingException.class)
    public void parse_NoticeMessageWithNullResponse_ShouldThrowParsingException()
    {
        getNoticeMessage.setJsonResponse(null);
        getNoticeMessage.parse();
        getNoticeMessage.checkReliability();
    }

    @Test(expected = JSONParsingException.class)
    public void parse_NoticeMessageWithAlikeJSON_ShouldThrowParsingException()
    {
        getNoticeMessage.setJsonResponse(ALIKE_NOTICE_RESPONSE);
        getNoticeMessage.parse();
        getNoticeMessage.checkReliability();
    }

    @Test(expected = JSONParsingException.class)
    public void parse_NoticeMessageWithEmptyJSON_ShouldThrowParsingException()
    {
        getNoticeMessage.setJsonResponse(EMPTY_RESPONSE);
        getNoticeMessage.parse();
        getNoticeMessage.checkReliability();
    }

    @Test(expected = JsonSyntaxException.class)
    public void parse_MessageWithWrongJSONSyntax_ShouldThrowSyntaxException()
    {
        getNoticeMessage.setJsonResponse(BROKEN_RESPONSE);
        getNoticeMessage.parse();
        getNoticeMessage.checkReliability();
    }

    @Test
    public void parse_NoticeMessageWithCorrectJSON_ShouldSucceed()
    {
        getNoticeMessage.setJsonResponse(NOTICE_RESPONSE);
        getNoticeMessage.parse();
        getNoticeMessage.checkReliability();
    }
}
