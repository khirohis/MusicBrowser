package net.hogelab.musicbrowser.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

/**
 * Created by kobayasi on 2017/03/06.
 */

public class GenericDialogFragment extends DialogFragment {

    private static final String TAG = GenericDialogFragment.class.getSimpleName();

    private static final String EXTRA_KEY_REQUEST_CODE = "requestCode";
    private static final String EXTRA_KEY_TITLE = "title";
    private static final String EXTRA_KEY_MESSAGE = "message";
    private static final String EXTRA_KEY_POSITIVE_BUTTON = "positiveButton";
    private static final String EXTRA_KEY_NEGATIVE_BUTTON = "negativeButton";
    private static final String EXTRA_KEY_NEUTRAL_BUTTON = "neutralButton";


    public static class Builder {
        private final int requestCode;
        private Fragment targetFragment;
        private String tag;

        private String title;
        private String message;

        private String positiveButton;
        private String negativeButton;
        private String neutralButton;

        public Builder(int requestCode) {
            this.requestCode = requestCode;
        }

        public Builder(Fragment targetFragment, int requestCode) {
            this.requestCode = requestCode;
            this.targetFragment = targetFragment;
        }


        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setPositiveButton(String positiveButton) {
            this.positiveButton = positiveButton;
            return this;
        }

        public Builder setNegativeButton(String negativeButton) {
            this.negativeButton = negativeButton;
            return this;
        }

        public Builder setNeutralButton(String neutralButton) {
            this.neutralButton = neutralButton;
            return this;
        }


        public void show(FragmentManager fragmentManager) {
            createDialog().show(fragmentManager, tag);
        }

        public void show(FragmentTransaction fragmentTransaction) {
            createDialog().show(fragmentTransaction, tag);
        }


        private GenericDialogFragment createDialog() {
            Bundle arguments = new Bundle();
            arguments.putInt(EXTRA_KEY_REQUEST_CODE, requestCode);
            arguments.putString(EXTRA_KEY_TITLE, title);
            arguments.putString(EXTRA_KEY_MESSAGE, message);
            arguments.putString(EXTRA_KEY_POSITIVE_BUTTON, positiveButton);
            arguments.putString(EXTRA_KEY_NEGATIVE_BUTTON, negativeButton);
            arguments.putString(EXTRA_KEY_NEUTRAL_BUTTON, neutralButton);

            GenericDialogFragment dialog = new GenericDialogFragment();
            dialog.setArguments(arguments);

            if (targetFragment != null) {
                dialog.setTargetFragment(targetFragment, requestCode);
            }

            return dialog;
        }
    }


    @Override
    public @NonNull Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        final int requestCode = arguments.getInt(EXTRA_KEY_REQUEST_CODE);
        final String title = arguments.getString(EXTRA_KEY_TITLE);
        final String message = arguments.getString(EXTRA_KEY_MESSAGE);
        final String positiveButton = arguments.getString(EXTRA_KEY_POSITIVE_BUTTON);
        final String negativeButton = arguments.getString(EXTRA_KEY_NEGATIVE_BUTTON);
        final String neutralButton = arguments.getString(EXTRA_KEY_NEUTRAL_BUTTON);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (title != null) {
            builder.setTitle(title);
        }
        if (message != null) {
            builder.setMessage(message);
        }

        DialogInterface.OnClickListener listener = (dialog, which) -> {
            Fragment targetFragment = getTargetFragment();
            if (targetFragment != null) {
                targetFragment.onActivityResult(getTargetRequestCode(), which, createResultData());
            } else {
                Activity activity = getActivity();
                if (activity != null) {
                    PendingIntent intent = activity.createPendingResult(requestCode,
                            createResultData(), PendingIntent.FLAG_ONE_SHOT);
                    try {
                        intent.send(which);
                    } catch (PendingIntent.CanceledException e) {
                    }
                }
            }
        };

        if (positiveButton != null) {
            builder.setPositiveButton(positiveButton, listener);
        }
        if (negativeButton != null) {
            builder.setNegativeButton(negativeButton, listener);
        }
        if (neutralButton != null) {
            builder.setNeutralButton(neutralButton, listener);
        }

        return builder.create();
    }


    protected Intent createResultData() {
        return new Intent();
    }
}
