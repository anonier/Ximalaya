package com.example.ximalaya.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ximalaya.R;
import com.example.ximalaya.adapters.PlayListAdapter;
import com.example.ximalaya.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public class SobPopWindow extends PopupWindow {

        private final View mPopView;
        private View mClossBtn;
        private RecyclerView mTracksList;
        private PlayListAdapter mPlayListAdapter;
        private TextView mPlayModeTV;
        private ImageView mPlayModeIV;
        private View mPlayModeContatiner;

        public SobPopWindow(){
                //设置它宽高
        super(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置点击关闭
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        //载进来View
                mPopView = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.pop_play_list, null);
        //设置它的宽高
        setContentView(mPopView);
        //设置窗口进入和退出的动画
                setAnimationStyle(R.style.pop_animation);
                
                //关闭的点击
                initView();
                initEvent();
        }

        private void initEvent() {
                //点击关闭之后窗口消失
                mClossBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                SobPopWindow.this.dismiss();
                        }
                });
        }

        private void initView() {
                mClossBtn = mPopView.findViewById(R.id.play_list_close_btn);
                //先找到控件
                mTracksList = mPopView.findViewById(R.id.play_list_rv);
                //设置布局管理器
                LinearLayoutManager layoutManager = new LinearLayoutManager(BaseApplication.getAppContext());
                mTracksList.setLayoutManager(layoutManager);
                //设置适配器
                mPlayListAdapter = new PlayListAdapter();
                mTracksList.setAdapter(mPlayListAdapter);
                //播放模式相关
           /*     mPlayModeTV = mPopView.findViewById(R.id.play_list_play_mode_tv);
                mPlayModeIV = mPopView.findViewById(R.id.play_list_play_mode_iv);
                mPlayModeContatiner = mPopView.findViewById(R.id.play_list_play_mode_container);*/
        }
        //给适配器设置数据
        public void setListData(List<Track> data){
                if (mPlayListAdapter != null) {

                mPlayListAdapter.setData(data);
                }
        }
        public void setCurrentPlayPosition(int position){
                if (mPlayListAdapter != null) {
                        mPlayListAdapter.setCurrentPlayPostion(position);
                        mTracksList.scrollToPosition(position);
                }
        }

        public void setPlayListItemClickListener(PlayListItemClickListener listener) {
                mPlayListAdapter.setOnItemClickListener(listener);
        }
        public interface PlayListItemClickListener {
                void onItemClick(int position);
        }
}
