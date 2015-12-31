/*       The MIT License (MIT)
*
*        Copyright (c) 2015 Cleidimar Viana
*
*        Permission is hereby granted, free of charge, to any person obtaining a copy
*        of this software and associated documentation files (the "Software"), to deal
*        in the Software without restriction, including without limitation the rights
*        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
*        copies of the Software, and to permit persons to whom the Software is
*        furnished to do so, subject to the following conditions:
*
*        The above copyright notice and this permission notice shall be included in all
*        copies or substantial portions of the Software.
*
*        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
*        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
*        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
*        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
*        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
*        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
*        SOFTWARE.
*/
package com.seamusdawkins.rest.utils;

import android.app.Activity;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpHelper {
    public static String X_APPLICATION_TOKEN = "X-Application-Token";
    public static String CONNECTION = "Connection";
    public static String CONTENT_TYPE = "Content-Type";

    private static HttpResponse resp;
    private static int status;

    public static JSONArray doGetArray(Activity act, String url, String charset, String token) throws IOException, Exception, JSONException {
        HttpGet conn = new HttpGet(url);

        conn.addHeader(X_APPLICATION_TOKEN, token);
        conn.addHeader(CONNECTION, "close");
        conn.addHeader(CONTENT_TYPE, "application/x-www-form-urlencoded");

        JSONArray o = null;
        StringBuilder sb = new StringBuilder();
        resp = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            resp = httpClient.execute(conn);
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }
        try {
            if (resp != null) {

                status = resp.getStatusLine().getStatusCode();

                if (status == 200) {
                    InputStream content = resp.getEntity().getContent();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));


                    String text;


                    while ((text = buffer.readLine()) != null) {

                        sb.append(text);

                    }
                    o = new JSONArray(sb.toString());
                } else {
                    JSONArray arr = new JSONArray();
                    JSONObject obj = new JSONObject();
                    obj.put("statuscode", String.valueOf(status));
                    arr.put(0, obj);
                    return arr;
                }
            }

        } catch (Exception e) {
            return null;
        }
        return o;

    }
}
