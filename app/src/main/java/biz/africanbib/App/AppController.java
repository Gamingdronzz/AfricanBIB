package biz.africanbib.App;
import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.hypertrack.hyperlog.HyperLog;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import biz.africanbib.Tools.BitmapCache;

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;

    public void onCreate() {
        super.onCreate();
        mInstance = this;


    }

    public void writeLogToFile(String businessName)
    {
        File outputFile = new File(getApplicationContext().getFilesDir(), businessName + ".txt");
        try
        {
            Runtime.getRuntime().exec("logcat -c");
            Runtime.getRuntime().exec("logcat -v time -f " + outputFile.getAbsolutePath());
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        Log.v("Application","File Created at : " + outputFile.getAbsolutePath());

    }

    public static synchronized AppController getInstance() {
        AppController appController;
        synchronized (AppController.class) {
            appController = mInstance;
        }
        return appController;
    }

    public RequestQueue getRequestQueue() {
        if (this.mRequestQueue == null) {
            this.mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return this.mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (this.mImageLoader == null) {
            this.mImageLoader = new ImageLoader(this.mRequestQueue, new BitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (this.mRequestQueue != null) {
            this.mRequestQueue.cancelAll(tag);
        }
    }

    /* Checks if external storage is available for read and write */



}
