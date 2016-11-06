package com.thomazfbcortez.chargebackflow.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.thomazfbcortez.chargebackflow.R;
import com.thomazfbcortez.chargebackflow.android.ChargebackApplication;
import com.thomazfbcortez.chargebackflow.android.fragment.NoticeFragment;
import com.thomazfbcortez.chargebackflow.api.API;
import com.thomazfbcortez.chargebackflow.api.message.ErrorMessage;
import com.thomazfbcortez.chargebackflow.api.message.GETNoticeMessage;
import com.thomazfbcortez.chargebackflow.api.message.GETEntryEndpointMessage;
import com.thomazfbcortez.chargebackflow.model.EntryEndpoint;
import com.thomazfbcortez.chargebackflow.utils.MaterialDialogWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChargeActivity extends Activity
{
    public static final String NOTICE_FRAGMENT_TAG = "noticeFragment";
    public static final String CHARGEBACK_FRAGMENT_TAG = "chargebackFragment";
    public static final String FINISH_FRAGMENT_TAG = "finishFragment";
    @Inject API api;
    @Inject EventBus eventBus;
    MaterialDialog progressDialog;
    EntryEndpoint entryEndpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        ChargebackApplication.getInstance().getAppGraph().inject(this);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        eventBus.register(this);
    }

    @Override
    protected void onStop()
    {
        eventBus.unregister(this);
        super.onStop();
    }

    public void onChargeBackButtonClick(View view)
    {
        createProgressDialog();
        if (entryEndpoint == null)
        {
            requestEndpoint();
        }
        else
        {
            requestNoticeInfo();
        }
    }

    public void createProgressDialog()
    {
        progressDialog = MaterialDialogWrapper.buildWithLoadingSettings(this);
        if(progressDialog != null) progressDialog.show();
    }

    private void requestEndpoint()
    {
        GETEntryEndpointMessage getEntryEndpointMessage = new GETEntryEndpointMessage(getString(R.string.api_endpoint));
        api.request(getEntryEndpointMessage);
    }

    private void requestNoticeInfo()
    {
        GETNoticeMessage getNoticeMessage = new GETNoticeMessage(entryEndpoint);
        api.request(getNoticeMessage);
    }

    @Subscribe
    public void onAPIEndpointReceivedEvent(GETEntryEndpointMessage message)
    {
        entryEndpoint = message.getResult();
        requestNoticeInfo();
    }

    @Subscribe
    public void onNoticeReceivedEvent(GETNoticeMessage message)
    {
        if (progressDialog != null) progressDialog.dismiss();
        NoticeFragment.newInstance(message.getResult()).show(getFragmentManager(), NOTICE_FRAGMENT_TAG);
    }

    @Subscribe
    public void onMessageErrorEvent(ErrorMessage errorEvent)
    {
        if (progressDialog != null) progressDialog.dismiss();
        MaterialDialogWrapper.buildWithErrorSettings(this, errorEvent.getId()).show();
    }

    public MaterialDialog getProgressDialog()
    {
        return progressDialog;
    }
}