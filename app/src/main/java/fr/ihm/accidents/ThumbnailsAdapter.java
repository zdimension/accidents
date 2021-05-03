package fr.ihm.accidents;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

public class ThumbnailsAdapter extends BaseAdapter {

    private List<Bitmap> thumbnails;
    private LayoutInflater layoutInflater;

    public ThumbnailsAdapter(Context context, List<Bitmap> thumbnails) {
        this.thumbnails = thumbnails;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.thumbnails.size();
    }

    @Override
    public Object getItem(int position) {
        return this.thumbnails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout root = (RelativeLayout) (convertView == null ? layoutInflater.inflate(R.layout.thumbnail_preview, parent, false) : convertView);

        Bitmap thumbnail = this.thumbnails.get(position);

        ImageView imgView = (ImageView) root.findViewById(R.id.thumbnail);

        imgView.setImageBitmap(thumbnail);

        return root;
    }
}
