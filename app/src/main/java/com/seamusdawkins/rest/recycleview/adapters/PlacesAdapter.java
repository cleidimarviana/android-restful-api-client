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
package com.seamusdawkins.rest.recycleview.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.seamusdawkins.rest.R;
import com.seamusdawkins.rest.recycleview.models.CollectionPlaceResult;
import com.seamusdawkins.rest.recycleview.models.Place;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by cleidimar on 30/12/15.
 * e-mail: cleidimarviana@gmail.com
 */
public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    private Boolean empty;
    private String type = "";
    private String color;
    private CollectionPlaceResult collection;
    private Activity context;
    int pos;
    boolean carregando;

    // Adapter's Constructor
    public PlacesAdapter(Activity context, ArrayList<Place> designs, Boolean empty, int pos) {
        this.context = context;
        this.empty = empty;
        this.pos = pos;
        this.collection = new CollectionPlaceResult();
        this.collection.ar = designs;
    }

    public void Atualizar(Boolean empty, int pos) {

        this.empty = empty;
        this.pos = pos;
        notifyDataSetChanged();
    }

    public void AtualizarArraySearch(ArrayList<Place> designs) {
        this.collection = new CollectionPlaceResult();
        this.collection.ar = designs;
    }

    public void removeItem(int position) {
        collection.ar.remove(position);
        notifyItemRemoved(position);
    }

    public void setCarregando(boolean carregando) {

        this.carregando = carregando;
    }

    // Create new views. This is invoked by the layout manager.
    @Override
    public PlacesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view by inflating the row item xml.

        View v;
        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recyclerview_cards_empty, parent, false);
            //Toast.makeText(context.getApplicationContext(), "" + context.getString(R.string.msg_result_error), Toast.LENGTH_SHORT).show();
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recyclerview_cards_list, parent, false);
        }
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int positionT) {

        if (this.empty) {
            holder.textViewStatusResult.setText(context.getString(R.string.msg_result_error));
          } else {
            color = "#f1f1f1";
            ColorDrawable colors = new ColorDrawable(Color.parseColor(color));
            holder.imageViewImage.setBackgroundDrawable(colors);


            ViewGroup.LayoutParams layoutParams = holder.imageViewImage.getLayoutParams();
            layoutParams.width = 460;
            layoutParams.height = 360;
            holder.imageViewImage.setLayoutParams(layoutParams);

            holder.textViewTitle.setGravity(Gravity.TOP);

            ViewGroup.LayoutParams cardLayout = holder.frameLayoutCardView.getLayoutParams();
            cardLayout.height = 360;
            holder.frameLayoutCardView.setLayoutParams(cardLayout);

            holder.textViewTitle.setText(Html.fromHtml(collection.ar.get(positionT).getTitle()));

            holder.textViewLocation.setText(collection.ar.get(positionT).getCityName());

            if (!collection.ar.get(positionT).getImagePath().equals("null")) {
                holder.imageViewDefault.setVisibility(View.GONE);
                Picasso.with(context).load(collection.ar.get(positionT).getImagePath()).resize(500, 500).centerCrop().into(holder.imageViewImage);
            } else {
                Picasso.with(context).load("img_default").resize(500, 500).centerCrop().error(colors).into(holder.imageViewImage);
            }

            holder.toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!carregando) {
                        Log.e("LOG",""+collection.ar.get(positionT).getTitle());
//                            Intent it = new Intent(context, ActivityAdDetails.class);
//                            it.putExtra("object", collection);
//                            it.putExtra("pos", pos);
//                            it.putExtra("position", positionT);
//                            it.putExtra("retur", false);
//                            context.startActivity(it);
//                            context.overridePendingTransition(0, 0);
                    }
                }
            });


        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && this.empty)

            return 0;
        return 1;
    }

    @Override
    public int getItemCount() {
        return collection.ar == null ? 0 : collection.ar.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        protected TextView textViewTitle;
        protected TextView textViewLocation;
        protected ImageView imageViewDefault;
        protected ImageView imageViewImage;
        protected ToggleButton toggleButton;
        protected FrameLayout frameLayoutCardView;
        protected TextView textViewStatusResult;

        ViewHolder(View v) {
            super(v);
            imageViewDefault = (ImageView) v.findViewById(R.id.img_icon_default_anuncio);
            textViewTitle = (TextView) v.findViewById(R.id.tv_title_image);
            textViewLocation = (TextView) v.findViewById(R.id.tv_localization);
            imageViewImage = (ImageView) v.findViewById(R.id.img_anuncio);
            toggleButton = (ToggleButton) v.findViewById(R.id.toggleButtonDrawer);
            frameLayoutCardView = (FrameLayout) v.findViewById(R.id.framelayout_cardview);

            textViewStatusResult = (TextView) v.findViewById(R.id.status_result);

        }
    }
}
