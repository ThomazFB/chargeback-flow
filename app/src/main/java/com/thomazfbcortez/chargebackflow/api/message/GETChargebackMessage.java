package com.thomazfbcortez.chargebackflow.api.message;

import com.thomazfbcortez.chargebackflow.model.ChargebackForm;

public class GETChargebackMessage extends Message
{
    private ChargebackForm chargebackFormResult;

    public GETChargebackMessage(String endpointURL)
    {
        this.url = endpointURL;
    }

    @Override
    public ChargebackForm getResult()
    {
        return chargebackFormResult;
    }

    public void setResult(ChargebackForm chargebackForm)
    {
        chargebackFormResult = chargebackForm;
    }

    @Override
    public void parse()
    {
        chargebackFormResult = gson.fromJson(jsonResponse, ChargebackForm.class);
    }
}
