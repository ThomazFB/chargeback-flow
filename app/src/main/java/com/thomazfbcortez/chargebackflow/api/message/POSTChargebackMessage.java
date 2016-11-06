package com.thomazfbcortez.chargebackflow.api.message;

import com.thomazfbcortez.chargebackflow.model.Chargeback;

public class POSTChargebackMessage extends POSTMessage
{
    public POSTChargebackMessage(String url, Chargeback chargeback)
    {
        this.url = url;
        message = gson.toJson(chargeback);
    }
}
