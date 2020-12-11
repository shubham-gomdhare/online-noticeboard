package com.sghost.diems;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;
    private OnItemClickListener mListener;
    public NoticeAdapter(Context context, List<Upload> uploads){
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
        return new NoticeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        int height = uploadCurrent.getHeight();
        int width = uploadCurrent.getWidth();
        holder.imageViewNotice.requestLayout();
        holder.imageViewNotice.getLayoutParams().height = height;
        holder.imageViewNotice.getLayoutParams().width = width;
        holder.textViewTitle.setText(uploadCurrent.getmTitle());
        Picasso.with(mContext)
                .load(uploadCurrent.getmUrl())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imageViewNotice);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class NoticeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView textViewTitle;
        private ImageView imageViewNotice;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            imageViewNotice = itemView.findViewById(R.id.image_view_upload);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem download = menu.add(Menu.NONE,2,2,"Download Notice");
            MenuItem show = menu.add(Menu.NONE,3,3,"Show Notice");
            download.setOnMenuItemClickListener(this);
            show.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){

                    switch (item.getItemId()){
                        case 1:
                            mListener.onItemClick(position);
                            return true;
                        case 2:
                            try {
                                mListener.onDownloadClick(position);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return true;
                        case 3:
                            mListener.onShowClick(position);
                            return true;
                    }

                }
            }
            return false;
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}