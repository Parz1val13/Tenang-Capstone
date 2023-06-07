package com.example.tenang_capstone.ui.panic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.example.tenang_capstone.R;

public class SeekHelpDialog {

    Activity myActivity;

    public SeekHelpDialog(Activity myActivity) {
        this.myActivity = myActivity;
    }

    public void openSeekHelpDialog() {
        AlertDialog.Builder bottomSheetDialog = new AlertDialog.Builder(myActivity);


        bottomSheetDialog.setView(R.layout.seek_help_dialog)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform action when the Cancel button is clicked
            }
        });
        bottomSheetDialog.show();
    }
}
