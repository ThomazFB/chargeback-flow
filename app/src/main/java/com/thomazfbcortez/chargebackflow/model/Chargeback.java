package com.thomazfbcortez.chargebackflow.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class Chargeback extends ReliableModel
{
    private String comment;
    @SerializedName("reason_details")
    private List<HashMap<String, Object>> reasonDetails;

    @Override
    public boolean isModelNullable()
    {
        return false;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getComment()
    {
        return comment;
    }

    public void setReasonDetails(List<HashMap<String, Object>> reasonDetails)
    {
        this.reasonDetails = reasonDetails;
    }

    public List<HashMap<String, Object>> getReasonDetails()
    {
        return reasonDetails;
    }
}
