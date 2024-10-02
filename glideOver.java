package org.blender.play;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.lang.CharSequence;
import android.app.Activity;
import android.view.inputmethod.InputMethodManager;
import android.app.Notification;
import android.os.Build;
import android.app.NotificationManager;
import android.app.Notification;
import android.content.Intent;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import android.widget.*;
import android.view.*;
import android.text.SpannableString;
import android.view.View.*;


public class ControlCenterActivity extends Activity {
        private Notification.Builder _nb;
	private Button b_downloadlib;
	private Button b_startgame;
	private Button b_selectgame;
	private TextView t_gamename;
	private TextView t_binstats;
	private String gamepath;
	private String appdirbase;
	private View.OnKeyListener mKeyListener;
       public char[] binstatsch;public String running=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //PathsHelper.installIfNeeded(this);
	//FrameLayout mviewLayout = (FrameLayout) findViewById (R.id.mainframe);
	//.addView (mView);
	    setContentView(R.layout.main);
	    appdirbase = "/data/data/org.blender.play/";//getBaseContext().getFilesDir().getPath();//PathsHelper.getBaseAppDir(this);
	    b_startgame = (Button)findViewById(R.id.startbutton);//b_startgame.set_enabled(true);
	    b_selectgame = (Button)findViewById(R.id.gameselecterbutton);
	    t_gamename = (TextView)findViewById(R.id.gamenametext);
	    t_binstats = (EditText)findViewById(R.id.binarystatustext);
    	b_startgame.setOnClickListener(new OnClickListener() { 
            @Override
            public void onClick(View v) {
        if(running==null){ try{ 
          Intent intent = new Intent(ControlCenterActivity.this, GhostActivity.class);
          intent.setType("*/*");
           if(gamepath!=null)
           	intent.setData(Uri.parse("file://" + gamepath));
           else if(appdirbase!=null)
           	intent.setData(Uri.parse("file://" + appdirbase+"/exit.blend"));
        int leng=t_binstats.getEditableText().length();
        binstatsch = new char[leng];
        t_binstats.getEditableText().getChars(0,leng,binstatsch,0);
         intent.putExtra("arg",new String(binstatsch));
         running="true";
         startActivity(intent); 
       	 running="false";//BlenderNativeAPI.exit(0);
        }
       	catch (ActivityNotFoundException e) { running="false";}
        catch(Exception e){ running="false";}
      //_rthread=new ReaderThread(R.id.binarystatustext);
      //_rthread.start();
       } }
      });
     
     t_binstats.requestFocus();
     if(_nb==null) {
/*try{//if(Build.VERSION.SDK_INT>=26)//Build.VERSION_CODES.O)
       Class nc = Class.forName("android.app.NotificationChannel");
       Object ncObj = nc.getConstructor(new Class[] {String.class, CharSequence.class, int.class}).newInstance(ame,name,importance);
       /nc.getMethod("setDescription", String.class).invoke(ncObj, description);
       nc.getMethod("setVibrationPattern", long[].class).invoke(ncObj, new long[]{ 0 }); // enableVibration is bugged, use this as workaround
       nc.getMethod("enableVibration", boolean.class).invoke(ncObj, true);
       nc.getMethod("enableLights", boolean.class).invoke(ncObj, false);
       NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
       manager.getClass().getMethod("createNotificationChannel", nc).invoke(manager, ncObj);
      }catch(Exception e) {Log.e("glideOver","systemNotifyChannel:"+e.getMessage()); } 
*/
      _nb=buildNotification(running!=null?running:"None");
      }//if(Build.VERSION.SDK_INT<26) startService(new Intent(this,glideService.class));
      //else
      //startForegroundService();//startForeground(0x010a10, buildNotification());
     //else
      globUpdateNotification();
    }
   @Override public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
	    outAttrs.imeOptions |=EditorInfo.IME_ACTION_DONE| EditorInfo.IME_FLAG_NO_EXTRACT_UI;
	super.onCreateInputConnection(outAttrs);
    }
   @Override public onUpdateExtractingVisibility(EditorInfo outAttrs) {
    EditorInfo.imeOptions|= EditorInfo.IME_FLAG_NO_EXTRACT_UI;
    super.onUpdateExtractingVisibility(outAttrs); 
   }
   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:      
            if (resultCode == RESULT_OK) {  
                Uri uri = data.getData();
                String tpath = uri.getPath();
               	t_gamename.setText(tpath);
              running="true";
            }           
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
     /*protected boolean onKey(View v, int keyCode, KeyEvent event){
      if (keyCode==KeyEvent.KEYCODE_BACK){ v.requestFocus();
       InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
       imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
       return true;
      }
      return false; 
     }*/
    /*@Override public IBinder onBind(Intent intent) { return mBinder; }*/

     private void globUpdateNotification() {
         NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
       if(_nb!=null)
        manager.notify(1, _nb.build());
       //Intent notifyIntent = new Intent(this, ControlCenterActivity.class);
       //((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(0x010a10, buildNotification());
    }

    private Notification.Builder buildNotification(String msg) {
         PendingIntent pendingIntent= PendingIntent.getActivity(this, 0, getIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder nb = new Notification.Builder(this);
        //  if (Build.VERSION.SDK_INT >= 26) {
         //   try{ nb.getClass().getMethod("setChannelId", String.class).invoke(nb, "X"+hostname);//NOTIFICATION_CHANNEL_DEFAULT);
          //  }catch(Exception e){}
        //notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, 0);
       if(nb!=null){
        nb.setContentTitle("glideOver");//GetText(R.string.application_name));
      nb.setSmallIcon(android.R.drawable.ic_secure);
      if (msg!=null && msg!="") nb.setContentText(msg);
            nb.setContentIntent(pendingIntent);
           nb.setOngoing(true);
       }
        return (Notification.Builder)nb;
    }
    public void InstallFile(String installpath, String basepath) { }
   /* public class ReaderThread extends Thread{
      InputStream inputf;EditText rv;
      public void ReaderThread(InputStream inputf,int rid){out=outf;
       	byte[] buff = new byte[16384];int il;rv=(EditText)findViewById(rid);
      }
      @Override public void run(){
       try {
	      BufferedInputStream o=new BufferedInputStream( new FilterInputStream(inputf));
	      while ((il = o.read(buff))>0) { rv.append(buff,0,il); }
           } catch (Exception e) {
        	Log.w("BlendrCp", "readerror:");
           } finally  rv=null;
         }
	}
	@Override
     public void onNewIntent() {
	super.onNewIntent();
     }
   */  
   @Override
   public void onStart() {
     super.onStart();
       if(appdirbase==null)
      appdirbase = getBaseContext().getFilesDir().getPath();
      Uri intentdata = getIntent().getData();int urn=-1;String datapath;
       if(intentdata!=null) { datapath=intentdata.toString();
     if((urn=datapath.indexOf("://"))==-1)urn=0;else urn+=3;
      if(datapath.length()>0)
       gamepath = datapath.substring(urn,datapath.length());
      else gamepath=appdirbase+"/start.blend";
      //t_binstats.value=gamepath;
     }
   /*       if(intentdata != null) {
       	String path =  intentdata.getEncodedPath();
       	if(path != null) {
       	    appdirbase = appdirbase.substring(0, appdirbase.lastIndexOf("/")+1);
       	    InstallFile(path, appdirbase);
       	}// else Log.i("Blender","No path");
        }//else Log.i("Blender","No data");
   */
    }
}

