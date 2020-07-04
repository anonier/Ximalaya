package com.example.ximalaya;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ximalaya.adapters.RecommendListAdapter;
import com.example.ximalaya.base.BaseActivity;
import com.example.ximalaya.interfaces.ISearchCallback;
import com.example.ximalaya.presenters.AlbumDetailPresenter;
import com.example.ximalaya.presenters.SearchPresenter;
import com.example.ximalaya.utils.LogUtil;
import com.example.ximalaya.views.FlowTextLayout;
import com.example.ximalaya.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends BaseActivity implements ISearchCallback, RecommendListAdapter.OnRecommendItemClickListner {
    private static final String TAG = "SearchActivity";
    private View mBackBtn;
    private EditText mInputBox;
    private View mSearchBtn;
    private SearchPresenter mSearchPresenter;
    private FlowTextLayout mFlowTextLayout;
    private UILoader mContent;
    private FrameLayout mResultContainer;
    private RecyclerView mResultListView;
    private RecommendListAdapter mRecommendListAdapter;
    private View mDelBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        intiView();
        initEvent();
        initPresenter();
    }

    private void initPresenter() {
        //注册UI更新的接口
        mSearchPresenter = SearchPresenter.getSearchPresenter();
        mSearchPresenter.registerViewCallback(this);
        //拿热词
        mSearchPresenter.getHotWord();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearchPresenter != null) {
            //干掉UI的接口
            mSearchPresenter.unRegisterViewCallback(this);
            mSearchPresenter = null;
        }
    }

    private void initEvent() {
        mRecommendListAdapter.setOnRecommendItemClickListener(this);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去执行搜索调用搜索
                String keyword = mInputBox.getText().toString().trim();
                if (TextUtils.isEmpty(keyword)) {
                    //可以给个提示
                    Toast.makeText(SearchActivity.this, "搜索关键字不能为空.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mSearchPresenter != null) {
                    mSearchPresenter.doSearch(keyword);
                    mContent.updateStatus(UILoader.UIStatus.LOADING);
                }

            }
        });

        mInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputBox.setText("");
            }
        });
    }

        private void intiView () {
            mBackBtn = this.findViewById(R.id.search_back);
            mInputBox = this.findViewById(R.id.search_input);
            mDelBtn = this.findViewById(R.id.search_input_delete);
            mSearchBtn = this.findViewById(R.id.search_btn);
            mResultContainer = this.findViewById(R.id.search_container);

            if (mContent == null) {
                mContent = new UILoader(this) {
                    @Override
                    protected View getSuccessView(ViewGroup container) {
                        return createSuccessView();
                    }
                };
                if (mContent.getParent() instanceof ViewGroup) {
                    ((ViewGroup) mContent.getParent()).removeView(mContent);
                }
                mResultContainer.addView(mContent);

            }
        }
    //创建数据成功的view
    private View createSuccessView() {
        View resultView = LayoutInflater.from(this).inflate(R.layout.search_result_layout, null);
        mResultListView = resultView.findViewById(R.id.result_list_view);
        //设置布局管理器
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mResultListView.setLayoutManager(layoutManager);
        //设置适配器
        mRecommendListAdapter = new RecommendListAdapter();
        mResultListView.setAdapter(mRecommendListAdapter);
        mResultListView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(), 2);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 2);
                outRect.left = UIUtil.dip2px(view.getContext(), 5);
                outRect.right = UIUtil.dip2px(view.getContext(), 5);
            }
        });
        return resultView;
    }

    @Override
    public void onSearchResultLoaded(List<Album> result) {

        if (result != null) {
            if (result.size()==0) {
                if (mContent != null) {
                    mContent.updateStatus(UILoader.UIStatus.EMPTY);
                }
            }else{
                mRecommendListAdapter.setData(result);
                mContent.updateStatus(UILoader.UIStatus.SUCCESS);
            }
        }
    }

    @Override
    public void onHotWordLoaded(List<HotWord> hotWordList) {
    }

    @Override
    public void onLoadMoreResult(List<Album> result, boolean isOkay) {

    }

    @Override
    public void onRecommendWordLoaded(List<QueryResult> keyWordList) {

    }

    @Override
    public void onError(int errorCode, String errorMsg) {
    }

    @Override
    public void onItemClick(int position, Album album) {
        AlbumDetailPresenter.getInstance().setTargetAlbum(album);
        //根据位置拿到数据
        //item被点击了跳转到详细界面
        Intent intent  = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }
}