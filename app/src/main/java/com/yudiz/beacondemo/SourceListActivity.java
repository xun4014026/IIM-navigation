package com.yudiz.beacondemo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.yudiz.beacondemo.Dijkstra.node;
import static com.yudiz.beacondemo.Dijkstra.point_num;
import static com.yudiz.beacondemo.MainActivity.destination_num;
import static com.yudiz.beacondemo.MainActivity.destinationcursor;

public class SourceListActivity extends ListActivity {

    private ArrayAdapter<String> sourceAdapter;
    private static final int REQUEST_CODE_SRC = 1; //讓 list activity 有辦法跳回來地圖這頁(main activity)
    private static final int default_value = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_list);

        List<String> sourceData = new ArrayList<String>();

        //xin1116
        destinationcursor.moveToFirst(); //移至第一筆
        destination_num=destinationcursor.getCount();
        if(destination_num!=0){
            sourceData.add("我的位置");
            for(int i=0;i<destination_num;i++){
                sourceData.add(destinationcursor.getString(1));
                destinationcursor.moveToNext();
            }
//            data.add("我的位置");
        }


        sourceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sourceData);
        setListAdapter(sourceAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String content = (String)getListAdapter().getItem(position);
        Toast.makeText(this, content, Toast.LENGTH_LONG).show();


//        int pos = (int)getListAdapter().getItem(id);

//        Intent intent = new Intent();
//        intent.putExtra("sourcePosition", position);
//        intent.setClass(SourceListActivity.this,MainActivity.class);
//        startActivity(intent);

        // 換成新的寫法
        Intent intent = getIntent();
        intent.putExtra("src_ListItemContent", content);
        setResult(REQUEST_CODE_SRC, intent);
        finish();
    }

    @Override
    public void onBackPressed()
    {   // 按了 Android 裝置的實體返回鍵
        // 準備回傳的資料

        Intent mIntent = new Intent();
//        mIntent.putExtra("src_ListItemPosition", default_value);
        setResult(REQUEST_CODE_SRC, mIntent);
        super.onBackPressed();
    }
}
