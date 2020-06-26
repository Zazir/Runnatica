package com.runnatica.runnatica;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Controlador_Captcha extends Application
{

    private static Controlador_Captcha mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private Controlador_Captcha(Context context)
    {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized Controlador_Captcha getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new Controlador_Captcha(context);
        }

        return mInstance;
    }

    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        getRequestQueue().add(req);
    }

}