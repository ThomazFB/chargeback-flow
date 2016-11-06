package com.thomazfbcortez.chargebackflow.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Links implements Serializable
{
    @SerializedName("notice") Link noticeEndpoint;
    @SerializedName("chargeback") Link chargebackEndpoint;
    @SerializedName("self") Link selfEndpoint;
    @SerializedName("block_card") Link blockCardEndpoint;
    @SerializedName("unblock_card") Link unblockCardEndpoint;

    public Link getNoticeEndpoint()
    {
        return noticeEndpoint;
    }

    public void setNoticeEndpoint(Link noticeEndpoint)
    {
        this.noticeEndpoint = noticeEndpoint;
    }

    public Link getChargebackEndpoint()
    {
        return chargebackEndpoint;
    }

    public void setChargebackEndpoint(Link chargebackEndpoint)
    {
        this.chargebackEndpoint = chargebackEndpoint;
    }

    public Link getSelfEndpoint()
    {
        return selfEndpoint;
    }

    public void setSelfEndpoint(Link selfEndpoint)
    {
        this.selfEndpoint = selfEndpoint;
    }

    public Link getBlockCardEndpoint()
    {
        return blockCardEndpoint;
    }

    public void setBlockCardEndpoint(Link blockCardEndpoint)
    {
        this.blockCardEndpoint = blockCardEndpoint;
    }

    public Link getUnblockCardEndpoint()
    {
        return unblockCardEndpoint;
    }

    public void setUnblockCardEndpoint(Link unblockCardEndpoint)
    {
        this.unblockCardEndpoint = unblockCardEndpoint;
    }

    public static class Link implements Serializable
    {
        String href;

        public String getHref()
        {
            return href;
        }

        public void setHref(String href)
        {
            this.href = href;
        }
    }
}
