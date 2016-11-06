package com.thomazfbcortez.chargebackflow.api;

import com.thomazfbcortez.chargebackflow.api.message.Message;
import com.thomazfbcortez.chargebackflow.api.message.POSTMessage;
import com.thomazfbcortez.chargebackflow.exception.ConnectionInputStreamException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class API
{
    /**
     * The request(Message message) method work on the fact that each specific action
     * the application needs from the API will be sent as message, and will be handled based
     * on the message polymorphic type sent.
     * <p/>
     * Expect the return of this method through a event call depending on the type
     * of the message requested.
     * <p/>
     * Example:
     * <p/>
     * Call -
     * API.request(myCustomMessage);
     * <p/>
     * Return -
     * Subscribe
     * public void onMyCustomMessageReceivedEvent(MyCustomMessage myCustomMessage);
     * <p/>
     * Just ensure that the class subscribing the event is registered on the EventBus
     * eventBus.register(this);
     *
     * @param message The message carrying all data needed to operate with the API
     */

    public void request(Message message)
    {
        NetworkAsyncTask asyncTask = new NetworkAsyncTask();
        asyncTask.execute(message);
    }

    public String fetchJSONTo(Message message) throws Exception
    {
        HttpURLConnection apiConnection = createConnection(message.getUrl(), "GET");
        return buildStringFrom(apiConnection.getInputStream());
    }

    public String sendJSONFrom(POSTMessage message) throws Exception
    {
        HttpURLConnection apiConnection = createConnection(message.getUrl(), "POST");
        PrintWriter printWriter = new PrintWriter(apiConnection.getOutputStream());
        printWriter.print(message.getMessage());
        printWriter.close();
        return buildStringFrom(apiConnection.getInputStream());
    }

    private HttpURLConnection createConnection(String urlText,
                                                      String requestMethod) throws Exception
    {
        URL url = new URL(urlText);
        HttpURLConnection apiConnection = (HttpURLConnection) url.openConnection();
        apiConnection.setRequestMethod(requestMethod);
        apiConnection.setConnectTimeout(10000);
        apiConnection.setReadTimeout(10000);
        return apiConnection;
    }

    private String buildStringFrom(InputStream apiInputStream)
    {
        try
        {
            String stringLine;
            InputStreamReader apiInputStreamReader = new InputStreamReader(apiInputStream);
            BufferedReader bufferedReader = new BufferedReader(apiInputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while ((stringLine = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(stringLine);
            }
            bufferedReader.close();
            return stringBuilder.toString();
        }
        catch (Exception ex)
        {
            throw new ConnectionInputStreamException("Error while building the JSON String");
        }
    }
}