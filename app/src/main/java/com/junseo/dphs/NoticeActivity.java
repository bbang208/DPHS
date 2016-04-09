package com.junseo.dphs;

import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

    private ConnectivityManager cManager;
    private NetworkInfo mobile;
    private NetworkInfo wifi;


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

        if (isInternetCon()) { //false 반환시 if 문안의 로직 실행
            Toast.makeText(NoticeActivity.this, "인터넷에 연결되지않아 불러오기를 중단합니다.", Toast.LENGTH_SHORT).show();
            finish();
        } else { //인터넷 체크 통과시 실행할 로직
            }

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

    private boolean isInternetCon() {
        cManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); //모바일 데이터 여부
        wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); //와이파이 여부
        return !mobile.isConnected() && !wifi.isConnected(); //결과값을 리턴
    }
}
