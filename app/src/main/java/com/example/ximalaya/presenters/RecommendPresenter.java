package com.example.ximalaya.presenters;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.ximalaya.data.XimalayaApi;
import com.example.ximalaya.interfaces.IRecommendPresenter;
import com.example.ximalaya.interfaces.IRecommendViewCallback;
import com.example.ximalaya.utils.Constants;
import com.example.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendPresenter implements IRecommendPresenter {

    private static final String TAG = "RecommendPresenter";
    private List<Album> mCurrentRecommend = null;
    private List<IRecommendViewCallback> mCallbacks = new ArrayList<>();
    private List<Album> mRecommendList;

    private RecommendPresenter() {
    }

    private static RecommendPresenter sInstance = null;

    /**
     * 获取单例对象
     */
    public static RecommendPresenter getInstance() {
        if(sInstance == null) {
            synchronized(RecommendPresenter.class) {
                if(sInstance == null) {
                    sInstance = new RecommendPresenter();
                }
            }
        }
        return sInstance;
    }
    //获取当前的推进专辑
    public List<Album> getCurrentRecommend() {
        return mCurrentRecommend;
    }
    @Override
    public void getRecommendList() {
        //如果内容不空的话，那么直接使用当前的内容
        if(mRecommendList != null && mRecommendList.size() > 0) {
            LogUtil.d(TAG,"getRecommendList -- > from list.");
            handlerRecommendResult(mRecommendList);
            return;
        }
        //获取推荐内容
        //封装参数
        updateLoading();
        XimalayaApi ximalayaApi = XimalayaApi.getXimalayApi();
        ximalayaApi.getRecommendList(new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(@Nullable GussLikeAlbumList gussLikeAlbumList) {
                LogUtil.d(TAG,"thread name -- >" + Thread.currentThread().getName());
                //数据获取成功
                if(gussLikeAlbumList != null) {
                    LogUtil.d(TAG,"getRecommendList -- > from network..");
                    mRecommendList = gussLikeAlbumList.getAlbumList();
                    //数据回来以后，我们要去更新UI
                    //upRecommendUI(albumList);
                    handlerRecommendResult(mRecommendList);
                }
            }

            @Override
            public void onError(int i,String s) {
                //数据获取出错
                LogUtil.d(TAG,"error  -- > " + i);
                LogUtil.d(TAG,"errorMsg  -- > " + s);
                handlerError();
            }
        });
    }

    private void handlerError() {
        if(mCallbacks != null) {
            for(IRecommendViewCallback callback : mCallbacks) {
                callback.onNetworkError();
            }
        }
    }

    private void handlerRecommendResult(List<Album> albumList) {
        //通知UI更新
        if(albumList != null) {
            //测试，清空一下，让界面显示空
            //albumList.clear();
            if(albumList.size() == 0) {
                for(IRecommendViewCallback callback : mCallbacks) {
                    callback.onEmpty();
                }
            } else {
                for(IRecommendViewCallback callback : mCallbacks) {
                    callback.onRecommendListLoaded(albumList);
                }
                this.mCurrentRecommend = albumList;
            }
        }
    }

    private void updateLoading() {
        for(IRecommendViewCallback callback : mCallbacks) {
            callback.onLoading();
        }
    }


    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void registerViewCallback(IRecommendViewCallback callback) {
        if(mCallbacks != null && !mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unRegisterViewCallback(IRecommendViewCallback callback) {
        if(mCallbacks != null) {
            mCallbacks.remove(callback);
        }
    }
}
