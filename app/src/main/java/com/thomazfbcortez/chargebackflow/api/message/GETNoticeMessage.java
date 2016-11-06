package com.thomazfbcortez.chargebackflow.api.message;

import com.thomazfbcortez.chargebackflow.model.EntryEndpoint;
import com.thomazfbcortez.chargebackflow.model.Notice;

public class GETNoticeMessage extends Message
{
    private Notice noticeResult;

    public GETNoticeMessage(EntryEndpoint EntryEndpoint)
    {
        url = EntryEndpoint.getLinks().getNoticeEndpoint().getHref();
    }

    @Override
    public Notice getResult()
    {
        return noticeResult;
    }

    public void setResult(Notice result)
    {
        this.noticeResult = result;
    }

    @Override
    public void parse()
    {
        noticeResult = gson.fromJson(jsonResponse, Notice.class);
    }
}