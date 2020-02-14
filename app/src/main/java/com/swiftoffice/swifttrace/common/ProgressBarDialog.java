package com.swiftoffice.swifttrace.common;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.swiftoffice.swifttrace.R;


public class ProgressBarDialog {

    public static Dialog dialog;
    private static ImageView ivLoad;

    public static void showProgressBar(final Activity activity, String title) {

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog=null;
                return;
            }
        }

        try {
            title = "Alert";
            dialog = new Dialog(activity,
                    android.R.style.Theme_Translucent_NoTitleBar);
            dialog.getWindow().getAttributes().windowAnimations = R.style.AlertDialog_AppCompat;
            dialog.setContentView(R.layout.progressbar_dialog);

            FrameLayout frameLayout = (FrameLayout) dialog
                    .findViewById(R.id.rv);
            //new ASSL(activity, frameLayout, 1134, 720, true);

            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);


            ivLoad = (ImageView) dialog.findViewById(R.id.ivLoad);
            ivLoad.setVisibility(View.GONE);

            if(dialog!=null && !dialog.isShowing())
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void dismissProgressDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {

                dialog.dismiss();
                dialog=null;
            }
        }
    }

}
