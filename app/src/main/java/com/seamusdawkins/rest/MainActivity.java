/*
*    The MIT License (MIT)
*
*   Copyright (c) 2015 Cleidimar Viana
*
*   Permission is hereby granted, free of charge, to any person obtaining a copy
*   of this software and associated documentation files (the "Software"), to deal
*   in the Software without restriction, including without limitation the rights
*   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
*   copies of the Software, and to permit persons to whom the Software is
*   furnished to do so, subject to the following conditions:
*   The above copyright notice and this permission notice shall be included in all
*   copies or substantial portions of the Software.
*   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
*   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
*   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
*   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
*   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
*   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
*   SOFTWARE.
*/
package com.seamusdawkins.rest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.seamusdawkins.rest.recycleview.adapters.PlacesAdapter;
import com.seamusdawkins.rest.recycleview.models.Place;
import com.seamusdawkins.rest.recycleview.utils.AndroidUtils;
import com.seamusdawkins.rest.utils.Consts;
import com.seamusdawkins.rest.utils.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static String TAG = MainActivity.class.getName();

    private PlacesAdapter adapter;

    private String urlPost;
    private JSONArray jsonArrayDevelopContent;

    private boolean broadcast;
    private AsyncTaskNewsParseJson myTask;

    private ArrayList<Place> ar;
    private boolean bolCarregarMais = true;
    private boolean ultimoEstadoConectado = true;
    private Boolean error = false;
    private boolean empty = false;
    private int pos;
    private RecyclerView recyclerView;

    private LinearLayoutManager linearlayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    private BroadcastReceiver broadcastReceiver;
    private boolean carregando = false;
    private IntentFilter filters;
    private int recyclerViewPaddingTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();
        initUI(); //maps xml interface with java

        recyclerViewDevelop(); //setting recycleview
        swipeToRefresh(); // setup swipe to refresh
        checkConnection(); //check connection with internet

        filters = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

    }

    public void checkConnection(){
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean conexao = AndroidUtils.thereConnection(context);
                if (conexao) {
                    broadcast = true;
                    mountUrl();
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    ultimoEstadoConectado = conexao;
                }
            }
        };
    }

    /**
     * This method adjust the Toolbar settings.
     */
    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class AsyncTaskNewsParseJson extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            adapter.setCarregando(true);
            carregando = true;

        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            urlPost = url[0];
            try {

                jsonArrayDevelopContent = HttpHelper.doGetArray(MainActivity.this, urlPost, "UTF-8", Consts.TOKEN);

                if (broadcast) {
                    ar.clear();
                    broadcast = false;
                    adapter.AtualizarArraySearch(ar);
                }

                if (ar == null) {
                    ar = new ArrayList<Place>();
                    adapter.AtualizarArraySearch(ar);
                }

                int sizeJsonArray = jsonArrayDevelopContent.length();

                Log.e(TAG, "sizeJsonArray: " + sizeJsonArray);

                if (sizeJsonArray != 0) {
                        for (int i = 0; i < sizeJsonArray; i++) {

                            Place srm = new Place();

                            srm.setId(Integer.valueOf(jsonArrayDevelopContent.getJSONObject(i).getInt("id")));
                            srm.setTitle(Html.fromHtml(jsonArrayDevelopContent.getJSONObject(i).getString("title")).toString());
                            srm.setImagePath(Html.fromHtml(jsonArrayDevelopContent.getJSONObject(i).getString("image_path")).toString());
                            srm.setCityName(Html.fromHtml(jsonArrayDevelopContent.getJSONObject(i).getString("city_name")).toString());

                            ar.add(srm);
                        }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (error) {
                showSnackbar();

                empty = true;
                error = false;
            }

            if (ar == null || ar.size() == 0) {
                bolCarregarMais = false;
                Place srm = new Place();
                srm.setId(1);
                ar.add(srm);

                empty = true;
            } else {

                if (jsonArrayDevelopContent.length() != 0) {
                    empty = false;
                } else {
                    empty = true;
                }

            }

            swipeRefreshLayout.setRefreshing(false);

            adapter.setCarregando(false);
            carregando = false;
            adapter.Atualizar(empty, pos);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * This method is set recycler view develop
     */
    private void recyclerViewDevelop() {

        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        linearlayoutManager = new LinearLayoutManager(this);
        // use a linear layout manager
        recyclerView.setLayoutManager(linearlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        // Create the recyclerViewAdapter
        adapter = new PlacesAdapter(MainActivity.this, ar, empty, pos);
        recyclerView.setAdapter(adapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();

                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!carregando) {
                    super.onScrolled(recyclerView, dx, dy);
//
                    if (linearlayoutManager.findLastVisibleItemPosition() > adapter.getItemCount() - 5 && bolCarregarMais && ultimoEstadoConectado) {

//                        adapter.notifyDataSetChanged();
//                        mountUrl(filter, "");
//                        progressBar.setVisibility(View.VISIBLE);
//
//                        bolCarregarMais = false;
                    }
                }
            }
        });

        mountUrl();

        progressBar.setVisibility(View.VISIBLE);

    }

    /**
     * This method adjust url
     */
    private void mountUrl() {
        urlPost = Consts.URL_SERVER_DOMAIN;
        myTask = new AsyncTaskNewsParseJson();
        myTask.execute(urlPost);
    }

    private void swipeToRefresh() {
        int start = convertToPx(0), end = recyclerViewPaddingTop + convertToPx(16);
        swipeRefreshLayout.setProgressViewOffset(true, start, end);
        TypedValue typedValueColorPrimary = new TypedValue();
        TypedValue typedValueColorAccent = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValueColorPrimary, true);
        getTheme().resolveAttribute(R.attr.colorAccent, typedValueColorAccent, true);
        final int colorPrimary = typedValueColorPrimary.data, colorAccent = typedValueColorAccent.data;
        swipeRefreshLayout.setColorSchemeColors(colorPrimary, colorAccent);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                error = false;
                empty = false;
                myTask.cancel(true);
                ar.clear();
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
                mountUrl();
            }
        });
    }

    public int convertToPx(int dp) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

    /**
     * This method maps xml interface with java.
     */
    public void initUI() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewDevelop);

    }

    /**
     * Mapping XML interface
     */
    public void showSnackbar() {
        Snackbar
                .make(recyclerView, getString(R.string.msg_connection_error), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.btn_retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        error = false;
                        empty = false;
                        myTask.cancel(true);
                        ar.clear();
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                        mountUrl();
                    }
                }).show();
    }
}
