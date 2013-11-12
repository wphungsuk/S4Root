package arevir.chris.s4root;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.os.Build;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

// TODO: Make dialog popups black to match theme

public class MainActivity extends Activity{

    String TARGET_BASE_PATH = "/storage/extSdCard/";
    ProgressDialog dialog;

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
        String device = Build.MODEL;
        Log.d("rooting", "getting baseband version");
        String baseband = Build.getRadioVersion();

        // Checking that this is a Samsung Galaxy S4 running the MF3 firmware
        if(device.equals("SAMSUNG-SGH-I337") & baseband.equals("I337UCUAMF3")){
            Log.d("rooting", "correct phone");
            Log.d("rooting", "attempting to copy files from assets to root of external SD");

            // Check for SD card availability
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
                Log.d("rooting", "SD card is available");
                copyFileOrDir("");
            }
            else{
                Log.d("rooting", "No SD card present");
                simpleAlertMessage("Hrm...", "There is no SD card present, or it is currently in use. Please insert one, or try again");
                return;
            }

            // TODO: Execute pwn
            Log.d("rooting", "executing exploit");
            Process p;
            try{
                p = Runtime.getRuntime().exec("sh ./storage/extSdCard/pwn");
                toasty("PWNing the phone, one moment...");
            } catch(IOException io){
                toasty("could not run exploit");
                return;
            }

            // TODO: Execute install script
            Log.d("rooting", "executing script");
            Process s;
            try {
                s = Runtime.getRuntime().exec("sh /storage/extSdCard/script.sh");
                toasty("Installing busybox and su...");
            }catch(Exception e){
                toasty("Could not run install script");
                return;
            }

            // Clean up by deleting copied files
            Log.d("rooting", "executing cleanup procedure");
            cleanUp();

            simpleAlertMessage("Congrats!", "Rooting seems to have gone successful! Check if it truly went successful with the root checker button!");
        }
        else{
            Log.d("rooting", "incorrect phone");
            simpleAlertMessage("Error", "This is not a Galaxy S4 running the MF3 Baseband, please look for a different way to root your phone!");
        }
    }

    //The next two methods modified from Yoram Cohen at http://stackoverflow.com/questions/4447477/android-how-to-copy-files-in-assets-to-sdcard
    // Used for moving all of the files included in assets to root of external SD card
    private void copyFileOrDir(String path) {
        AssetManager assetManager = this.getAssets();
        String assets[] = null;
        try {
            Log.d("copying", "copyFileOrDir() "+path);
            assets = assetManager.list(path);
            if (assets.length == 0) {
                copyFile(path);
            } else {
                String fullPath =  TARGET_BASE_PATH + path;
                Log.d("copying", "path="+fullPath);
                File dir = new File(fullPath);
                if (!dir.exists() && !path.startsWith("images") && !path.startsWith("sounds") && !path.startsWith("webkit") && !path.startsWith("kioskmode") && !path.startsWith("fonts"))
                    if (!dir.mkdirs()){
                        Log.i("copying", "could not create dir "+fullPath);
                    }
                for (int i = 0; i < assets.length; ++i) {
                    String p;
                    if (path.equals(""))
                        p = "";
                    else
                        p = path + "/";

                    if (!path.startsWith("images") && !path.startsWith("sounds") && !path.startsWith("webkit") && !path.startsWith("kioskmode") && !path.startsWith("fonts"))
                        copyFileOrDir( p + assets[i]);
                }
            }
        } catch (IOException ex) {
            Log.d("copying", "I/O Exception", ex);
        }
    }

    private void copyFile(String filename) {
        AssetManager assetManager = this.getAssets();

        InputStream in = null;
        OutputStream out = null;
        String newFileName = null;
        try {
            Log.d("copying", "copyFile() "+filename);
            in = assetManager.open(filename);
            if (filename.endsWith(".jpg")) // extension was added to avoid compression on APK file
                newFileName = TARGET_BASE_PATH + filename.substring(0, filename.length()-4);
            else
                newFileName = TARGET_BASE_PATH + filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.d("copying", "Exception in copyFile() of "+newFileName);
            Log.d("copying", "Exception in copyFile() "+e.toString());
        }

    }

    void cleanUp(){
        File script = new File(TARGET_BASE_PATH + "script.sh");
        File pwn = new File(TARGET_BASE_PATH + "pwn");
        File gs4ex = new File(TARGET_BASE_PATH + "gs4ex");

        script.delete();
        pwn.delete();
        recursiveDelete(gs4ex);
    }

    void recursiveDelete(File file) {
        if (file.isDirectory())
            for (File child : file.listFiles())
                recursiveDelete(child);

        file.delete();
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
