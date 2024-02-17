package com.akp.janupkar.basic;


import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by USER on 15-01-2017.
 */
public interface VolleyResult {
    public void volleySuccess(String requestType, JSONObject response);
    public void volleyError(String requestType, VolleyError error);
}
