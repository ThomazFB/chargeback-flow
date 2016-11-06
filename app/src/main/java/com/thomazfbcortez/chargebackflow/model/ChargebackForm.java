package com.thomazfbcortez.chargebackflow.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class ChargebackForm extends ReliableModel
{
    @SerializedName("comment_hint")
    private String hint;
    private String id;
    private String title;
    private boolean autoblock;
    @SerializedName("reason_details")
    private List<HashMap<String, String>> reasonDetails;
    private Links links;

    @Override
    public boolean isModelNullable()
    {
        return (hint == null
                || id == null
                || title == null
                || reasonDetails == null
                || reasonDetails.get(0) == null
                || reasonDetails.get(1) == null
                || links == null
                || links.getSelfEndpoint() == null
                || links.getBlockCardEndpoint() == null
                || links.getUnblockCardEndpoint() == null);
    }

    public String getHint()
    {
        return hint;
    }

    public void setHint(String hint)
    {
        this.hint = hint;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public boolean isAutoblock()
    {
        return autoblock;
    }

    public void setAutoblock(boolean autoblock)
    {
        this.autoblock = autoblock;
    }

    public List<HashMap<String,String>> getReasonDetails()
    {
        return reasonDetails;
    }

    public void setReasonDetails(List<HashMap<String, String>> reasonDetails)
    {
        this.reasonDetails = reasonDetails;
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
