package com.thomazfbcortez.chargebackflow.model;

public class EntryEndpoint extends ReliableModel
{
    Links links;

    @Override
    public boolean isModelNullable()
    {
        return (links == null || (links.noticeEndpoint == null && links.chargebackEndpoint == null));
    }

    public Links getLinks()
    {
        return links;
    }

    public void setLinks(Links links)
    {
        this.links = links;
    }
}
