package arevir.chris.s4root;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.os.Build;
import android.widget.Toast;
import java.io.IOException;

// TODO: Make dialog popups black to match theme

public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onRootClick(View v){
        Log.d("interface", "clicked root button");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Are you sure?");

        alertDialogBuilder
                .setMessage("There is no way of unrooting available at the moment")
                .setCancelable(false)
                .setNegativeButton("No", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        // if this button is clicked, just close the dialog box and do nothing
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                        // If this button is clicked, start rooting process
                        root();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void onTestClick(View v){
        Log.d("interface", "clicked root test button");
        /* Will check the device for proper root capabilities */
        boolean root;
        Process p;

        try{
            p = Runtime.getRuntime().exec("su");
            root = true;
        } catch(IOException io){
            root = false;
        }

        if (root){
            Log.d("interface", "successful root check");
            simpleAlertMessage("Congrats!", "Your device appears to be rooted!");
        }
        else{
            Log.d("interface", "not a successful root check");
            simpleAlertMessage("Uh oh...", "Your device does not appear to be rooted, something must have gone wrong");
        }
    }

    public void root(){
        Log.d("rooting", "enter root process");
        Log.d("rooting", "getting device name");
        String device = getDeviceName();
        Log.d("rooting", "getting baseband version");
        String baseband = Build.getRadioVersion();

        // Checking that this is a Samsung Galaxy S4 running the MF3 firmware
        if(device.equals("SAMSUNG-SGH-I337") & baseband.equals("I337UCUAMF3")){
            Log.d("rooting", "correct phone");
            // TODO: Copy files over from assets to external SD
            // Log.d("rooting", "attempting to copy files from assets to root of external SD");
            // TODO: Execute pwn
            // Log.d("rooting", "executing exploit");
            // TODO: Execute script
            // Log.d("rooting", "executing script");
            // TODO: Clean up by deleting copied files
            // Log.d("rooting", "executing cleanup procedure");
        }
        else{
            Log.d("rooting", "incorrect phone");
            simpleAlertMessage("Error", "This is not a Galaxy S4 running the MF3 Baseband, please look for a different way to root your phone!");
        }
    }

    public String getDeviceName(){
        return Build.MODEL;
    }

    public void toasty(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void simpleAlertMessage(String title, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(message)
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
}
