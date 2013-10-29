package arevir.chris.s4root;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Toast;

import java.io.IOException;

// TODO: Make dialog popups black to match theme

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onRootClick(View v){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Are you sure?");

        alertDialogBuilder
                .setMessage("There is no way of unrooting available at the moment")
                .setCancelable(false)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close the dialog box and do nothing
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If this button is clicked, start rooting process
                        root();
                        // Currently does nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void onTestClick(View v){
        /* Will check the device for proper root capabilities */
        boolean root;
        String resultMessage = "";
        String resultTitle = "";
        Process p;

        try{
            p = Runtime.getRuntime().exec("su");
            root = true;
        } catch(IOException io){
            root = false;
        }

        if (root){
            resultMessage += "Your device appears to be rooted!";
            resultTitle += "Congrats!";
        }
        else{
            resultMessage += "Your device does not appear to be rooted, something must have gone wrong";
            resultTitle += "Uh oh...";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(resultTitle);
        alertDialogBuilder
                .setMessage(resultMessage)
                .setCancelable(false)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void root(){
        // TODO: Check that this is a Samsung Galaxy S4 on MF3 firmware
        //String firmware = "";
        String deviceName= getDeviceName();
        Toast.makeText(getApplicationContext(), deviceName, Toast.LENGTH_LONG).show();
        // TODO: Check for exFat formatted external SD card
        // TODO: Copy files over
        // TODO: Execute pwn
        // TODO: Execute script
        // TODO: Clean up by deleting copied files
    }

    // The following two methods were adapted from http://stackoverflow.com/questions/1995439/get-android-phone-model-programmatically

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
