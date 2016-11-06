package com.thomazfbcortez.chargebackflow.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.afollestad.materialdialogs.MaterialDialog;
import com.thomazfbcortez.chargebackflow.R;
import com.thomazfbcortez.chargebackflow.api.message.ErrorMessage;

import org.greenrobot.eventbus.EventBus;

public class MaterialDialogWrapper
{
    public static MaterialDialog buildWithLoadingSettings(Context context)
    {
        return new MaterialDialog.Builder(context)
                .content(R.string.charge_activity_loading_dialog_content)
                .backgroundColor(ContextCompat.getColor(context, android.R.color.white))
                .contentColor(ContextCompat.getColor(context, android.R.color.black))
                .widgetColor(ContextCompat.getColor(context, R.color.enabled_purple))
                .progress(true, 0)
                .canceledOnTouchOutside(false)
                .keyListener(keyListener)
                .autoDismiss(false)
                .build();
    }

    public static MaterialDialog buildWithErrorSettings(Context context, int errorID)
    {
        return new MaterialDialog.Builder(context)
                .content(errorID)
                .positiveText("OK")
                .backgroundColor(ContextCompat.getColor(context, android.R.color.white))
                .contentColor(ContextCompat.getColor(context, android.R.color.black))
                .positiveColor(ContextCompat.getColor(context, R.color.enabled_purple))
                .build();
    }

    private static DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener()
    {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
        {
            return true;
        }
    };
}