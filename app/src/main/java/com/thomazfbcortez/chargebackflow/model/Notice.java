package com.thomazfbcortez.chargebackflow.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Notice extends ReliableModel
{
    String title;
    String description;
    @SerializedName("primary_action") Action primaryAction;
    @SerializedName("secondary_action") Action secondaryAction;
    Links links;

    @Override
    public boolean isModelNullable()
    {
        return (title == null
                || description == null
                || primaryAction == null
                || secondaryAction == null
                || links == null
                || links.getChargebackEndpoint() == null);
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Action getPrimaryAction()
    {
        return primaryAction;
    }

    public void setPrimaryAction(Action primaryAction)
    {
        this.primaryAction = primaryAction;
    }

    public Action getSecondaryAction()
    {
        return secondaryAction;
    }

    public void setSecondaryAction(Action secondaryAction)
    {
        this.secondaryAction = secondaryAction;
    }

    public Links getLinks()
    {
        return links;
    }

    public void setLinks(Links links)
    {
        this.links = links;
    }

    public static class Action implements Serializable
    {
        String title;
        String action;

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getAction()
        {
            return action;
        }

        public void setAction(String action)
        {
            this.action = action;
        }
    }
}
