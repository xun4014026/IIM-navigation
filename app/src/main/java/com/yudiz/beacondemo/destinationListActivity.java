package com.yudiz.beacondemo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.yudiz.beacondemo.MainActivity.destination_num;
import static com.yudiz.beacondemo.MainActivity.destinationcursor;


public class destinationListActivity extends ListActivity {
    private ArrayAdapter<String> adapter;
    private static final int REQUEST_CODE_DEST = 2; //讓 list activity 有辦法跳回來地圖這頁(main activity)
    private static final int default_value = 0;
    private static final int REQUEST_CODE_RETURN = 3; //當使用者按下實體返回鍵
    private int previous_choice = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_list);

        List<String> data = new ArrayList<String>();

        //xin1116
        destinationcursor.moveToFirst(); //移至第一筆
        destination_num=destinationcursor.getCount();
        if(destination_num!=0){
            for(int i=0;i<destination_num;i++){
                data.add(destinationcursor.getString(1));
                destinationcursor.moveToNext();
            }
//            data.add("我的位置");
        }


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String content = (String)getListAdapter().getItem(position);
        //Toast.makeText(this, content, Toast.LENGTH_LONG).show();


//        int pos = (int)getListAdapter().getItem(id);

//        Intent intent = new Intent();
//        intent.putExtra("ListItemPosition", position);
//        intent.setClass(destinationListActivity.this,MainActivity.class);
//        startActivity(intent);

        Log.d("leosu", "onListItemClick_previous_choice_1: "+previous_choice);

        previous_choice = position;
        Log.d("leosu", "onListItemClick_previous_choice_2: "+previous_choice);

        // 換成新的寫法
        Intent intent = getIntent();
        intent.putExtra("ListItemContent", content);//xin1116
        setResult(REQUEST_CODE_DEST, intent);
        finish();
    }
    @Override
    public void onBackPressed()
    {   // 按了 Android 裝置的實體返回鍵
        // 準備回傳的資料


        Intent mIntent = getIntent();
        mIntent.putExtra("ListItemPosition", previous_choice);
        Log.d("leosu", "onBackPressed_previous_choice: "+previous_choice);
        setResult(REQUEST_CODE_DEST, mIntent);
        Log.d("leosu", "onBackPressed");

        super.onBackPressed();
    }
}
