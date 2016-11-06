package com.thomazfbcortez.chargebackflow.android.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.afollestad.materialdialogs.MaterialDialog;
import com.thomazfbcortez.chargebackflow.R;
import com.thomazfbcortez.chargebackflow.android.ChargebackApplication;
import com.thomazfbcortez.chargebackflow.api.API;
import com.thomazfbcortez.chargebackflow.api.message.ErrorMessage;
import com.thomazfbcortez.chargebackflow.model.ReliableModel;
import com.thomazfbcortez.chargebackflow.utils.MaterialDialogWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;

public abstract class BaseDialogFragment extends DialogFragment
{
    protected static final String DIALOG_INFO_KEY = "dialog_info_key";
    @Inject API api;
    @Inject EventBus eventBus;
    protected ReliableModel dialogInfo;
    protected MaterialDialog progressDialog;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        ChargebackApplication.getInstance().getAppGraph().inject(this);
        eventBus.register(this);
        ButterKnife.bind(this, view);
        checkDialogInfoState();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putSerializable(DIALOG_INFO_KEY, dialogInfo);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        resizeDialog();
    }

    @Override
    public void onDestroy()
    {
        eventBus.unregister(this);
        super.onPause();
    }

    private void resizeDialog()
    {
        if (getDialog() != null)
        {
            Window window = getDialog().getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    private void checkDialogInfoState()
    {
        if (dialogInfo == null)
            eventBus.post(new ErrorMessage(R.string.api_data_error));
        else
            initializeDialogUserInterface();
    }

    protected abstract void initializeDialogUserInterface();

    public void createProgressDialog()
    {
        progressDialog = MaterialDialogWrapper.buildWithLoadingSettings(getActivity());
        if(progressDialog != null) progressDialog.show();
    }

    @Subscribe
    public void onMessageErrorEvent(ErrorMessage errorEvent)
    {
        if(progressDialog != null) progressDialog.dismiss();
        this.dismiss();
    }

    public MaterialDialog getProgressDialog()
    {
        return progressDialog;
    }

    public ReliableModel getDialogInfo()
    {
        return dialogInfo;
    }
}