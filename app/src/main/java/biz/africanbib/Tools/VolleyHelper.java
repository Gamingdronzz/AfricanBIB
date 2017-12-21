package biz.africanbib.Tools;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache.Entry;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import biz.africanbib.App.AppController;

import java.io.UnsupportedEncodingException;

import java.util.Map;


public class VolleyHelper {
    private Context context;
    public VolleyResponse delegate = null;
    Helper helper;
    ErrorListener errorListener = new VolleyErrorListener();
    //Listener jsonArrayResponseListener = new JsonArrayResponseListener();
    //Listener jsonObjectResponseListener = new JsonObjectResponseListener();
    Listener stringResponseListener = new StringResponseListener();

    public interface VolleyResponse {
        void onError(VolleyError volleyError);

        void onResponse(String str);
    }

    class StringResponseListener implements Listener<String> {
        StringResponseListener() {
        }

        public void onResponse(String response) {
            VolleyHelper.this.delegate.onResponse(response);
        }
    }

    /*
    class JsonObjectResponseListener implements Listener<JSONObject> {
        JsonObjectResponseListener() {
        }

        public void onResponse(JSONObject response) {
        }
    }

    class JsonArrayResponseListener implements Listener<JSONArray> {
        JsonArrayResponseListener() {
        }

        public void onResponse(JSONArray response) {
        }
    }
    */

    class VolleyErrorListener implements ErrorListener {
        VolleyErrorListener() {
        }

        public void onErrorResponse(VolleyError error) {
            VolleyHelper.this.delegate.onError(error);
            if (error.getClass() == TimeoutError.class) {
            }
            if (error.getClass() == ServerError.class) {
            }
            if (error.getClass() != NetworkError.class) {
            }
        }
    }

    private class CountRequestsInFlight implements RequestFilter {

        Object tag;
        int count = 0;

        public CountRequestsInFlight(Object tag) {
            this.tag = tag;
        }

        public boolean apply(Request<?> request) {
            if (request.getTag().equals(this.tag)) {
                this.count++;
            }
            return false;

        }

        public int getCount() {
            return this.count;
        }
    }

    public VolleyHelper(VolleyResponse delegate, Context conetext) {
        this.delegate = delegate;
        this.context = conetext;
        helper = new Helper(context);
    }

    public void makeStringRequest(String url, String TAG) {
        StringRequest strReq = new StringRequest(StringRequest.Method.POST, url, this.stringResponseListener, this.errorListener);
        setShouldCache(strReq, true);
        AppController.getInstance().addToRequestQueue(strReq, TAG);
    }

    public void makeStringRequest(String url, String TAG, final Map<String, String> params) {
        final Map<String, String> map = params;

        StringRequest strReq = new StringRequest(StringRequest.Method.POST, url, this.stringResponseListener, this.errorListener) {
           protected Map<String, String> getParams() throws AuthFailureError {
               //Log.v("Volley","Value = " + map.toString());
                return map;
            }
/*

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/xml");
                return headers;
            }


            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");

            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
            */

        };
        setShouldCache(strReq, true);
        AppController.getInstance().addToRequestQueue(strReq, TAG);
    }

    /*
    public void makeJsonRequest(String url, String TAG) {
        AppController.getInstance().addToRequestQueue(new JsonObjectRequest(1, url, null, this.jsonObjectResponseListener, this.errorListener), TAG);
    }

    public void makeJsonRequest(String url, String TAG, Map<String, String> params) {
        final Map<String, String> map = params;
        AppController.getInstance().addToRequestQueue(new JsonObjectRequest(1, url, null, this.jsonObjectResponseListener, this.errorListener) {
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        }, TAG);
    }

    public void makeJsonArrayRequest(String url, String TAG) {
        AppController.getInstance().addToRequestQueue(new JsonArrayRequest(1, url, null, this.jsonArrayResponseListener, this.errorListener), TAG);
    }

    public void makeJsonArrayRequest(String url, String TAG, Map<String, String> params) {
        final Map<String, String> map = params;
        AppController.getInstance().addToRequestQueue(new JsonArrayRequest(1, url, null, this.jsonArrayResponseListener, this.errorListener) {
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        }, TAG);
    }

    */

    public NetworkImageView loadImageInNetworkImageView(NetworkImageView networkImageView, String URL) {
        networkImageView.setImageUrl(URL, AppController.getInstance().getImageLoader());
        return networkImageView;
    }

    public ImageView loadImageInImageView(final ImageView imageView, String URL_IMAGE) {
        AppController.getInstance().getImageLoader().get(URL_IMAGE, new ImageListener() {
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

            public void onResponse(ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    imageView.setImageBitmap(response.getBitmap());
                }
            }
        });
        return imageView;
    }

    public ImageView loadImageInImageViewWithLoaders(ImageView imageView, String URL_IMAGE) {
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        return imageView;
    }

    public String getCachedData(String url) {
        Entry entry = AppController.getInstance().getRequestQueue().getCache().get(url);
        if (entry == null) {
            return null;
        }
        try {
            return new String(entry.data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String invalidateCachedData(String url) {
        AppController.getInstance().getRequestQueue().getCache().invalidate(url, true);
        return "";
    }

    private void setShouldCache(StringRequest stringRequest, boolean should) {
        stringRequest.setShouldCache(false);
    }

    private void setShouldCache(JsonObjectRequest jsonObjectRequest, boolean should) {
        jsonObjectRequest.setShouldCache(false);
    }

    private void setShouldCache(JsonArrayRequest jsonArrayRequest, boolean should) {
        jsonArrayRequest.setShouldCache(false);
    }

    public int countRequestsInFlight(String tag) {
        RequestQueue queue = AppController.getInstance().getRequestQueue();
        CountRequestsInFlight inFlight = new CountRequestsInFlight(tag);
        queue.cancelAll(inFlight);
        return inFlight.getCount();
    }

    public void removeCachedURL(String url) {
        AppController.getInstance().getRequestQueue().getCache().remove(url);
    }

    public void removeCache() {
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void cancelRequest(String TAG) {
        AppController.getInstance().getRequestQueue().cancelAll((Object) TAG);
    }
}
