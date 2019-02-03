package com.example.stormy.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.stormy.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AlertDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context context =getActivity();
        AlertDialog.Builder builder=new AlertDialog.Builder(context)
                .setTitle(R.string.error_value )
                .setMessage(R.string.error_message)
                .setPositiveButton(R.string.error_ok_button,null);

                AlertDialog dialog=builder.create();
        return dialog;
    }
}
