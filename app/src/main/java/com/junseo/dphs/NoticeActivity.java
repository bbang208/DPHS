package com.junseo.dphs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.junseo.dphs.adapter.NoticeBBSAdapter;
import com.junseo.dphs.data.NoticeBBSData;
import com.junseo.dphs.parse.HtmlParser;
import com.junseo.dphs.util.Util;
import com.junseo.dphs.var.FixedVar;

/**
 * Created by Junseo on 16. 4. 3..
 */
public class NoticeActivity extends AppCompatActivity implements AbsListView.OnScrollListener {

    private NoticeBBSAdapter noticeBBSAdapter;
    private int preLast;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        toolbar = (Toolbar) findViewById(R.id.toolbar); //툴바설정
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);//액션바와 같게 만들어줌
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });


        ListView listView = (ListView) findViewById(R.id.listView);
        noticeBBSAdapter = new NoticeBBSAdapter(this);
        listView.setAdapter(noticeBBSAdapter);

        new HtmlParser(this, noticeBBSAdapter).execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoticeBBSData noticeBBSData = noticeBBSAdapter.getItem(position);
                Util.openBroswer(NoticeActivity.this, FixedVar.HP_ROOT + noticeBBSData.getUrl());
            }
        });
        listView.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView lw, final int firstVisibleItem,
                         final int visibleItemCount, final int totalItemCount) {

        switch (lw.getId()) {
            case R.id.listView:
                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount) {
                    if (preLast != lastItem) { //to avoid multiple calls for last item
                        Log.d("Last", "Last");
                        new HtmlParser(this, noticeBBSAdapter).execute();
                        preLast = lastItem;
                    }
                }
        }
    }
}
