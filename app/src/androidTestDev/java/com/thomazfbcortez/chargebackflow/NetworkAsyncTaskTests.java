package com.thomazfbcortez.chargebackflow;

import com.thomazfbcortez.chargebackflow.api.NetworkAsyncTask;
import com.thomazfbcortez.chargebackflow.api.message.GETChargebackMessage;
import com.thomazfbcortez.chargebackflow.api.message.GETEntryEndpointMessage;
import com.thomazfbcortez.chargebackflow.api.message.GETNoticeMessage;
import com.thomazfbcortez.chargebackflow.api.message.Message;
import com.thomazfbcortez.chargebackflow.api.message.POSTCardBlockMessage;
import com.thomazfbcortez.chargebackflow.api.message.POSTChargebackMessage;
import com.thomazfbcortez.chargebackflow.api.message.POSTMessage;

import junit.framework.Assert;

import java.util.concurrent.CancellationException;

import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.buildChargebackMock;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.buildGETChargebackMessageMockWithAutoblock;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.buildGETEntryEndpointMessageMock;
import static com.thomazfbcortez.chargebackflow.TestEnvironmentProvider.buildGETNoticeMessageMock;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

public class NetworkAsyncTaskTests extends InjectedInstrumentationTestCase
{
    private NetworkAsyncTask networkAsyncTask;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        networkAsyncTask = new NetworkAsyncTask();
    }

    public void testExecute_GETEntryPointMessageSent_ShouldCallPostMethod() throws Exception
    {
        networkAsyncTask.execute(buildGETEntryEndpointMessageMock()).get();
        verify(eventBus).post(isA(GETEntryEndpointMessage.class));
    }

    public void testExecute_GETNoticeMessageSent_ShouldCallPostMethod() throws Exception
    {
        networkAsyncTask.execute(buildGETNoticeMessageMock()).get();
        verify(eventBus).post(isA(GETNoticeMessage.class));
    }

    public void testExecute_GETChargebackMessageSent_ShouldCallPostMethod() throws Exception
    {
        networkAsyncTask.execute(buildGETChargebackMessageMockWithAutoblock(false)).get();
        verify(eventBus).post(isA(GETChargebackMessage.class));
    }

    public void testExecute_POSTCardblockMessageSent_ShouldCallPostMethod() throws Exception
    {
        networkAsyncTask.execute(new POSTCardBlockMessage("")).get();
        verify(eventBus).post(isA(POSTCardBlockMessage.class));
    }

    public void testExecute_POSTChargebackMessageSent_ShouldCallPostMethod() throws Exception
    {
        networkAsyncTask.execute(new POSTChargebackMessage("", buildChargebackMock())).get();
        verify(eventBus).post(isA(POSTChargebackMessage.class));
    }

    public void testExecute_BrokenGETMessageSent_ShouldCallPostMethodWithErrorMessage() throws Exception
    {
        doThrow(Exception.class).when(api).fetchJSONTo(any(Message.class));
        try
        {
            networkAsyncTask.execute(new GETEntryEndpointMessage("")).get();
        }
        catch (CancellationException exception)
        {
            return;
        }
        Assert.fail();
    }

    public void testExecute_BrokenPOSTMessageSent_ShouldCallPostMethodWithErrorMessage() throws Exception
    {
        doThrow(Exception.class).when(api).sendJSONFrom(any(POSTMessage.class));
        try
        {
            networkAsyncTask.execute(new POSTCardBlockMessage("")).get();
        }
        catch (CancellationException exception)
        {
            return;
        }
        Assert.fail();
    }

    public void testExecute_GETEntryPointMessageSent_ShouldCallFetchJsonToMethod() throws Exception
    {
        networkAsyncTask.execute(buildGETEntryEndpointMessageMock()).get();
        verify(api).fetchJSONTo(isA(GETEntryEndpointMessage.class));
    }

    public void testExecute_GETNoticeMessageSent_ShouldCallFetchJsonToMethod() throws Exception
    {
        networkAsyncTask.execute(buildGETNoticeMessageMock()).get();
        verify(api).fetchJSONTo(isA(GETNoticeMessage.class));
    }

    public void testExecute_GETChargebackMessageSent_ShouldCallFetchJsonToMethod() throws Exception
    {
        networkAsyncTask.execute(buildGETChargebackMessageMockWithAutoblock(false)).get();
        verify(api).fetchJSONTo(isA(GETChargebackMessage.class));
    }

    public void testExecute_POSTCardBlockMessage_ShouldCallSendJsonFromMethod() throws Exception
    {
        networkAsyncTask.execute(new POSTCardBlockMessage("")).get();
        verify(api).sendJSONFrom(isA(POSTCardBlockMessage.class));
    }

    public void testExecute_POSTChargebackMessage_ShouldCallSendJsonFromMethod() throws Exception
    {
        networkAsyncTask.execute(new POSTChargebackMessage("", buildChargebackMock())).get();
        verify(api).sendJSONFrom(isA(POSTChargebackMessage.class));
    }
}