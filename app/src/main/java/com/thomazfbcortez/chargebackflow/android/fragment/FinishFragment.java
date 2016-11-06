package com.thomazfbcortez.chargebackflow.android.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.thomazfbcortez.chargebackflow.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FinishFragment extends DialogFragment
{
    @Bind(R.id.finish_close_button) TextView closeButton;

    private static final double DIALOG_HEIGHT_PROPORTION = 1.6;

    public static FinishFragment newInstance()
    {
        return new FinishFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_finish, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        closeButton.setOnClickListener(onCloseButtonClick());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        resizeDialog();
    }

    private View.OnClickListener onCloseButtonClick()
    {
        return new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        };
    }

    private void resizeDialog()
    {
        if (getDialog() != null)
        {
            Window window = getDialog().getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, getDialogHeight());
        }
    }

    private int getDialogHeight()
    {
        return (int) (getResources().getDisplayMetrics().heightPixels / DIALOG_HEIGHT_PROPORTION);
    }
}
