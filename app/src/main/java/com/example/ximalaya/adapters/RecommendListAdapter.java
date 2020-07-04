package com.example.ximalaya.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ximalaya.R;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.InnerHolder> {
    private static final String TAG = "RecommendListAdapter";
    private List<Album> mData = new ArrayList<>();
    private OnRecommendItemClickListner mItemClickListener = null;
    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    //加载View
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend,parent,false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
    //这里是设置数据
        holder.itemView.setTag(position);
        //设置点击
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    int clickPosition = (int) v.getTag();
                    mItemClickListener.onItemClick(clickPosition, mData.get(clickPosition));
                }
                Log.d(TAG, "holder.itemView click -- > " + v.getTag());
            }
        });
        holder.setData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        //返回要显示的个数
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public void setData(List<Album> albumList) {
        if (mData != null) {
            mData.clear();
            mData.addAll(albumList);
        }
        //更新一下UI
        notifyDataSetChanged();
    }

    public class InnerHolder extends  RecyclerView.ViewHolder{

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

            public void setData(Album album) {
                //找到各个控件，设置数据
                //专辑封面
                ImageView albumCoverIv = itemView.findViewById(R.id.album_cover);
                //title
                TextView albumTitleTv = itemView.findViewById(R.id.album_title_tv);
                //描述
                TextView albumDesTv = itemView.findViewById(R.id.album_description_tv);
                //播放数量
                TextView albumPlayCountTv = itemView.findViewById(R.id.album_play_count);
                //专辑内容数量
                TextView albumContentCountTv = itemView.findViewById(R.id.album_content_size);

                albumTitleTv.setText(album.getAlbumTitle());
                albumDesTv.setText(album.getAlbumIntro());
                albumPlayCountTv.setText(album.getPlayCount() + "");
                albumContentCountTv.setText(album.getIncludeTrackCount() + "");
            Picasso.with(itemView.getContext()).load(album.getCoverUrlLarge()).into(albumCoverIv);
        }
    }
    public void setOnRecommendItemClickListener(OnRecommendItemClickListner listener) {
        this.mItemClickListener = listener;
    }
    public interface OnRecommendItemClickListner {
        void onItemClick(int position, Album album);
    }
}
