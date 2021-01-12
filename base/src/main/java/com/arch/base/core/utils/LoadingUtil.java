package com.arch.base.core.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.core.widget.ContentLoadingProgressBar;

import com.arch.base.core.R;


public class LoadingUtil {
    public static Dialog loadingDialog;

    public static Dialog createLoadingDialog(Context context) {
        closeDialog();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_load_dialog, null);
        ContentLoadingProgressBar progressBar = v.findViewById(R.id.progress);
        progressBar.show();
        loadingDialog = new Dialog(context, R.style.loadDialogStyle);
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
              LinearLayout.LayoutParams.MATCH_PARENT,
              LinearLayout.LayoutParams.MATCH_PARENT));

        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        loadingDialog.show();

        return loadingDialog;
    }
    public static Dialog createLoadingDialog(Context context,boolean outSide) {
        closeDialog();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_load_dialog, null);
        ContentLoadingProgressBar progressBar = v.findViewById(R.id.progress);
        progressBar.show();
        loadingDialog = new Dialog(context, R.style.loadDialogStyle);
        loadingDialog.setCancelable(outSide);
        loadingDialog.setCanceledOnTouchOutside(outSide);
        loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
              LinearLayout.LayoutParams.MATCH_PARENT,
              LinearLayout.LayoutParams.MATCH_PARENT));

        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        loadingDialog.show();

        return loadingDialog;
    }
    /**
     * 关闭dialog
     */
    public static void closeDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }

    }
}
