package com.swiftoffice.swifttrace.database;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.swiftoffice.swifttrace.R;
import com.swiftoffice.swifttrace.activities.HomeActivity;
import com.swiftoffice.swifttrace.common.ProgressBarDialog;

public class MyFirebaseRealTimeDatabase {


    private static FirebaseAuth mFirebaseAuthInstance;
    private static FirebaseFirestore mDatabaseReference;

    static {
    }

    /**
     * used to get the firebase database instance
     */
    public static FirebaseFirestore getmDatabaseReference() {
        if (mDatabaseReference == null) {
            mDatabaseReference = FirebaseFirestore.getInstance();
        }
        return mDatabaseReference;
    }

    /**
     * used to get firebase auth instance
     *
     * @return
     */
    public static FirebaseAuth getFirebaseAuthInstance() {
        if (mFirebaseAuthInstance == null)
            mFirebaseAuthInstance = FirebaseAuth.getInstance();

        return mFirebaseAuthInstance;
    }

    public static void clearInstance() {
        mFirebaseAuthInstance = null;
        mDatabaseReference = null;
    }


    /**
     * To sign in
     *
     * @param credential
     */
    public static void signInWithPhoneAuthCredential(PhoneAuthCredential credential, final Activity activity) {
        MyFirebaseRealTimeDatabase.getFirebaseAuthInstance().signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        ProgressBarDialog.dismissProgressDialog();

                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(activity, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            activity.finish();

                        } else {
                            //verification unsuccessful.. display an error message
                            String message = activity.getResources().getString(R.string.something_went_wrong);

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = activity.getResources().getString(R.string.entered_code_is_not_valid);
                            }

                            Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction(activity.getResources().getString(R.string.dismiss), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }


}
