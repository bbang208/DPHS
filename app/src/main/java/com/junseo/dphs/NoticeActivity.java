package com.junseo.dphs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.junseo.dphs.adapter.NoticeBBSAdapter;
import com.junseo.dphs.parse.DataParser;

/**
 * Created by Junseo on 16. 4. 3..
 */
public class NoticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        ListView listView = (ListView) findViewById(R.id.listView);
        DataParser dataParser = new DataParser(this);
        listView.setAdapter(new NoticeBBSAdapter(this, dataParser.getNoticeDatas()));
    }
}