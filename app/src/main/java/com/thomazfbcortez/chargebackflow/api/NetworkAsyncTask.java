package com.thomazfbcortez.chargebackflow.api;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.thomazfbcortez.chargebackflow.R;
import com.thomazfbcortez.chargebackflow.android.ChargebackApplication;
import com.thomazfbcortez.chargebackflow.api.message.ErrorMessage;
import com.thomazfbcortez.chargebackflow.api.message.Message;
import com.thomazfbcortez.chargebackflow.api.message.POSTMessage;
import com.thomazfbcortez.chargebackflow.exception.JSONParsingException;
import com.thomazfbcortez.chargebackflow.exception.OperationNotCompletedException;

import org.greenrobot.eventbus.EventBus;

import java.io.FileNotFoundException;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

public class NetworkAsyncTask extends AsyncTask<Message, Void, Message>
{
    @Inject API api;
    @Inject EventBus eventBus;
    private Message message;

    public NetworkAsyncTask()
    {
        ChargebackApplication.getInstance().getAppGraph().inject(this);
    }
    
    @Override
    protected Message doInBackground(Message... params)
    {
        message = params[0];
        try
        {
            if (message instanceof POSTMessage)
                doPost();
            else
                doGet();
        }
        catch (JSONParsingException | JsonSyntaxException
                | OperationNotCompletedException | FileNotFoundException exception)
        {
            message.setErrorID(R.string.api_data_error);
            cancel(true);
        }
        catch (Exception exception)
        {
            message.setErrorID(R.string.no_connection_error);
            cancel(true);
        }
        return message;
    }

    private void doPost() throws Exception
    {
        POSTMessage postMessage = (POSTMessage) message;
        String json = api.sendJSONFrom(postMessage);
        postMessage.setJsonResponse(json);
        postMessage.checkReliability();
    }

    private void doGet() throws Exception
    {
        message.setJsonResponse(api.fetchJSONTo(message));
        message.parse();
        message.checkReliability();
    }

    @Override
    protected void onPostExecute(Message result)
    {
        eventBus.post(result);
    }

    @Override
    protected void onCancelled(Message error)
    {
        eventBus.post(new ErrorMessage(error.getErrorID()));
    }
}