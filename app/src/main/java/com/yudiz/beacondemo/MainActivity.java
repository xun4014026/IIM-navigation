package com.yudiz.beacondemo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.yudiz.beacondemo.Dijkstra.node;
import static com.yudiz.beacondemo.Dijkstra.point_num;
import static com.yudiz.beacondemo.MyView.heightOffset;
import static com.yudiz.beacondemo.MyView.widthOffset;


public class MainActivity extends AppCompatActivity /*implements View.OnClickListener */{


    private static final String TAG = "MAIN";
    public static Node sourceNode;
    public static Node destinationNode;
    public static Node last_1_Node;
    public static Node last_2_Node;
    public static Node last_3_Node;
    public static int floor_now=1;
    private ImageView markerview;
    private Button destBtn;
    private ImageButton searchBtn;
    private ImageButton retargetBtn;
    private ImageButton navigationBtn;
    private Dijkstra dijkstra;
    private MyView myView;
    //private DrawLine drawLine;
    protected static PositionDB positionDB;
    private boolean calculatePath=false;
    private boolean stair[]={false,false};
    //private ChangeFloorBC f2[]=new ChangeFloorBC[4];
    //private ChangeFloorBC f1[]=new ChangeFloorBC[4];
    private int f2count=0;
    private int f1count=0;
    private int f2_circle=0;
    private int f1_circle=0;
    private static final BeaconRegion ALL_BEACONS_REGION= new BeaconRegion(
            "customRegionName", null, null, null);
    private boolean floor_user=true;//表示使用者對樓層有控制權

    public Kalman kalman_list[]=new Kalman[100];
    private static final int REQUEST_CODE_SRC = 1; //讓 list activity 有辦法跳回來地圖這頁(main activity)
    private static final int REQUEST_CODE_DEST = 2; //讓 list activity 有辦法跳回來地圖這頁(main activity)
    private static final int REQUEST_CODE_RETURN = 3; //當使用者按下實體返回鍵

    public static int destination_num=0;
    public static Cursor destinationcursor =null;

    private ListView floor_list;

    private FrameLayout groupView;
    private FrameLayout bigGroupView;
    public static String destinationClass="選擇目的地";

    protected static float destinationMarkerWidth;
    protected static float destinationMarkerHeight;

    private BeaconManager beaconManager;
    private BeaconRegion[] beaconRegions;

    private BeaconAdapter beaconAdapter;
    private int ranging_count=0;
    private int beacon_number;

    private Node midpoint=new Node();
    private double midpointX=-999999;
    private double midpointY=-999999;

    private Circle circle_2f[ ]=new Circle[5];
    private Circle circle_1f[ ]=new Circle[5];
    protected static Beacon_XYR beacon_xyr_5[];





    //stair[0]表一樓樓梯口的beacon(minor=24
    //stair[1]表二樓樓梯口的(minor=44

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchdestination);
        initView();
        initTask();

    }

    // 新的收list傳回來的所選目的地的方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){

            case REQUEST_CODE_DEST:
                String listItemContent = data.getStringExtra("ListItemContent");//xin1116
                Log.d("leosu", "dest_listItemPosition: " + listItemContent);

                if(listItemContent.equals("選擇目的地")){
                    destBtn.setText(listItemContent);
                    destinationClass=listItemContent;
                }else if(listItemContent!=null){
                    String listItemPoint=this.getPointName(listItemContent);//找出教室對應的定位點
                    Log.d("leosu", "dest_listItemPosition: " + listItemPoint);
                    for(int k=0;k<point_num;k++){//透過定位點名稱找出對應的node[i]
                        if(node[k].getName().equals(listItemPoint)){
                            destinationNode=node[k];
                            break;
                        }
                    }
                    //把目前選擇的 dest show 在螢幕上
                    destBtn.setText(listItemContent);
                    destinationClass=listItemContent;
                    changeFloor(destinationNode.getFloor());
                }


                Log.d("leosu", "REQUEST_CODE_DEST");


                break;
        }
    }

    private String getPointName(String content){
        //xin1116
        String point=null;//用來存content對應TB_D的定位點
        destinationcursor.moveToFirst(); //移至第一筆
        if(destination_num!=0){
            for(int i=0;i<destination_num;i++){
                if(destinationcursor.getString(1).equals(content)){
                    Log.d("leosu", "ListItemPoint"+content);
                    point=destinationcursor.getString(2);
                    break;
                }
                destinationcursor.moveToNext();
            }
        }

        return point;
    }

    private void initTask() {



        groupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });
        GestureViewBinder bind = GestureViewBinder.bind(this, bigGroupView, groupView);
        bind.setFullGroup(true);
        bind.setOnScaleListener(new GestureViewBinder.OnScaleListener() {
            @Override
            public void onScale(float scale) {
                Log.i("TAG", scale + "");
            }
        });

        //*************************************//


         /*
        嘗試點擊按鈕顯示目的地的 listview 選單
        */

        destBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , destinationSearchActivity.class);
                //這邊要多加 for result，才能讓 list 跳回 main 時，傳使用者選哪一個回來
                startActivityForResult(intent, REQUEST_CODE_DEST);
            }
        });

        /*searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dijkstraPath();
            }
        });*/

        floor_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                changeFloor(position+1);//position0表1樓，以此類推
                //drawLine.update();
                floor_user=true;//表示使用者對樓層有控制權

            }
        });

        retargetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//目的地樓層，而不是使用者位置
                floor_now=destinationNode.getFloor();
                changeFloor(floor_now);
                floor_user=false;
            }
        });

        navigationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , NavigationView.class);
                //這邊要多加 for result，才能讓 list 跳回 main 時，傳使用者選哪一個回來
                startActivityForResult(intent, REQUEST_CODE_DEST);
            }
        });





        //*************************************//

        /*
        把初始化時的 src and dest show 在螢幕上
         */


        //destBtn.setText(destinationNode.getName());//如果要一開始不是顯示a10改這裡
        //sourceBtn.setText(sourceNode.getName());//如果要一開始不是顯示a10改這裡

        String[] floor={"1","2","3"};
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,floor){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.BLACK);

                // Generate ListView Item using TextView
                return view;
            }
        };

        floor_list.setAdapter( myAdapter );


        //text_dest = findViewById(R.id.text_destination);
        //text_src = findViewById(R.id.text_source);

        //建資料庫
        positionDB=new PositionDB(this);
        positionDB.open();

        beaconManager = ((MyApp) getApplicationContext()).beaconManager;
        beaconManager.setForegroundScanPeriod(10, 0);
        beaconManager.setBackgroundScanPeriod(10, 0);

        destinationcursor = positionDB.destinationDestinationQuaryAll();
        destination_num=destinationcursor.getCount();

        build_TB_P();//把point table 讀進來放到array


        dijkstra=new Dijkstra();//node[i]也會被建起來

        sourceNode=node[9];
        destinationNode=node[9];

    }

    private void build_TB_P(){
        Cursor cursor = positionDB.beaconQueryAll();
        beacon_number=cursor.getCount();
        //創建資料集
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();//????
        cursor.moveToFirst(); //移至第一筆

        if(cursor.getCount()!=0) {
            beacon_xyr_5=new Beacon_XYR[beacon_number];

            for(int i=0;i<beacon_number;i++) {
                beacon_xyr_5[i]=new Beacon_XYR(
                        Double.parseDouble(cursor.getString(4)),
                        Double.parseDouble(cursor.getString(5)),
                        Double.parseDouble(cursor.getString(6)),
                        Double.parseDouble(cursor.getString(7)),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10)
                );
                cursor.moveToNext();//移下一筆資料
            }

            /*circle_2f[0]=new Circle(-1,-1,-999999,0,"-1","-1","-1");
            circle_2f[1]=new Circle(-1,-1,-999999,0,"-1","-1","-1");
            circle_2f[2]=new Circle(-1,-1,-999999,0,"-1","-1","-1");
            circle_2f[3]=new Circle(-1,-1,-999999,0,"-1","-1","-1");
            circle_2f[4]=new Circle(-1,-1,-999999,0,"-1","-1","-1");
            circle_1f[0]=new Circle(-1,-1,-999999,0,"-1","-1","-1");
            circle_1f[1]=new Circle(-1,-1,-999999,0,"-1","-1","-1");
            circle_1f[2]=new Circle(-1,-1,-999999,0,"-1","-1","-1");
            circle_1f[3]=new Circle(-1,-1,-999999,0,"-1","-1","-1");
            circle_1f[4]=new Circle(-1,-1,-999999,0,"-1","-1","-1");

            f2[0]=new ChangeFloorBC(0,-1);
            f2[1]=new ChangeFloorBC(0,-1);
            f2[2]=new ChangeFloorBC(0,-1);
            f2[3]=new ChangeFloorBC(0,-1);
            f1[0]=new ChangeFloorBC(0,-1);
            f1[1]=new ChangeFloorBC(0,-1);
            f1[2]=new ChangeFloorBC(0,-1);
            f1[3]=new ChangeFloorBC(0,-1);*/


            beaconManager = ((MyApp) getApplicationContext()).beaconManager;

            cursor.moveToFirst(); //移至第一筆
            /*
            beaconRegions=new BeaconRegion[beacon_number];

            //叫出資料庫的資料
            for (int i = 0; i < beacon_number; i++) {


                beaconRegions[i]=(new BeaconRegion(
                        "beacon"+i,
                        UUID.fromString(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)),
                        Integer.parseInt(cursor.getString(3))));


                //來測試BEACONREGION有沒有被建起來

                Log.d("msg","FUCKFUCK, this is beaconRegion UUID"+beaconRegions[i].getProximityUUID());
                Log.d("msg","FUCKFUCK, this is beaconRegion major"+beaconRegions[i].getMajor());
                Log.d("msg","FUCKFUCK, this is beaconRegion minor"+beaconRegions[i].getMinor());

                Log.d("msg","FUCK, beconregion "+i+" constructed");
                Log.d("msg","UUID is "+cursor.getString(1)+"~~");
                Log.d("msg","major is "+cursor.getString(2)+"~~");
                Log.d("msg","minor is "+cursor.getString(3)+"~~");

                cursor.moveToNext();//移下一筆資料
            }*/

        }
    }

    private void setdestinationMarker(){
        destinationMarkerWidth=markerview.getWidth();
        destinationMarkerHeight=markerview.getHeight();

        if(destinationNode.getFloor()==floor_now){
            markerview.setX((float) (destinationNode.getX()*widthOffset)-markerview.getWidth()/2);
            markerview.setY((float) (destinationNode.getY()*heightOffset)-markerview.getHeight()/2);
            Log.d("tot","pos:("+markerview.getHeight()+","+markerview.getWidth()+")~floor~"+destinationNode.getFloor()+"~floor_now~"+floor_now);
        }else{
            markerview.setX((float) (999*widthOffset));
            markerview.setY((float) (999*heightOffset));
            Log.d("tot","pos:("+destinationNode.getX()+","+destinationNode.getY()+")~floor~"+destinationNode.getFloor()+"~floor_now~"+floor_now);
        }
    }

    private void changeFloor(int pos){
        if(pos==1){
            myView.setImageName("iim_1f_1125_50_90");
            floor_now=1;
        }else if (pos==2){
            myView.setImageName("iim_2f_1125_50_90");
            floor_now=2;
        }else if (pos==3){
            myView.setImageName("iim_3f_50_90");
            floor_now=3;
        }
        //drawLine.update();
        setdestinationMarker();
    }

    private void initView() {

        markerview=findViewById(R.id.dest_mapmaker);
        myView = (MyView) findViewById(R.id.dest_myView);
        //drawLine = (DrawLine) findViewById(R.id.dest_drawLine);
        destBtn = (Button)findViewById(R.id.dest_destButton);
        searchBtn=findViewById(R.id.dest_search);
        floor_list=findViewById(R.id.dest_floorlist);
        retargetBtn=findViewById(R.id.dest_retarget);
        navigationBtn=findViewById(R.id.dest_navigation);
        groupView = findViewById(R.id.dest_groupView);
        bigGroupView = findViewById(R.id.bigGroupView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    @Override
    protected void onDestroy() {
        if (beaconManager != null)
            beaconManager.disconnect();
        beaconManager = null;
        super.onDestroy();
    }


}






