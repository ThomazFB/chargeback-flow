package com.thomazfbcortez.chargebackflow;

import com.thomazfbcortez.chargebackflow.api.API;
import com.thomazfbcortez.chargebackflow.api.message.GETEntryEndpointMessage;
import com.thomazfbcortez.chargebackflow.api.message.GETNoticeMessage;
import com.thomazfbcortez.chargebackflow.api.message.Message;
import com.thomazfbcortez.chargebackflow.api.message.POSTChargebackMessage;
import com.thomazfbcortez.chargebackflow.api.message.POSTMessage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import javax.inject.Inject;

public class APITests
{
    private API api;

    @Before
    public void init()
    {
        api = new API();
    }

    @Test(expected = NullPointerException.class)
    public void fetchJSONTo_NullMessage_ShouldThrowException() throws Throwable
    {
        api.fetchJSONTo(null);
    }

    @Test(expected = MalformedURLException.class)
    public void fetchJSONFTo_MessageWithNullURL_ShouldThrowException() throws Throwable
    {
        GETEntryEndpointMessage getEntryEndpointMessage = new GETEntryEndpointMessage(null);
        api.fetchJSONTo(getEntryEndpointMessage);
    }

    @Test(expected = MalformedURLException.class)
    public void fetchJSONTo_MessageWithInvalidURL_ShouldThrowException() throws Throwable
    {
        GETEntryEndpointMessage getEntryEndpointMessage = new GETEntryEndpointMessage("39-57810-39671036927306y9239th");
        api.fetchJSONTo(getEntryEndpointMessage);
    }

    @Test(expected = NullPointerException.class)
    public void sendJSONFrom_NullMessage_ShouldThrowException() throws Throwable
    {
        api.sendJSONFrom(null);
    }

    @Test(expected = ProtocolException.class)
    public void sendJSONFrom_MessageWithNullURL_ShouldThrowException() throws Throwable
    {
        POSTChargebackMessage chargebackMessage = new POSTChargebackMessage("http://www.google.com", null);
        api.sendJSONFrom(chargebackMessage);
    }
}