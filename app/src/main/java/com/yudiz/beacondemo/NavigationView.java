package com.yudiz.beacondemo;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.yudiz.beacondemo.Dijkstra.node;
import static com.yudiz.beacondemo.Dijkstra.point_num;
import static com.yudiz.beacondemo.MainActivity.destinationMarkerHeight;
import static com.yudiz.beacondemo.MainActivity.destinationMarkerWidth;
import static com.yudiz.beacondemo.MainActivity.destinationNode;
import static com.yudiz.beacondemo.MainActivity.destinationClass;
import static com.yudiz.beacondemo.MyView.heightOffset;
import static com.yudiz.beacondemo.MyView.widthOffset;
import static com.yudiz.beacondemo.MainActivity.beacon_xyr_5;



public class NavigationView extends AppCompatActivity {


    private static final String TAG = "MAIN";
    public static Node sourceNode;
    public static Node navigation_destinationNode;
    public static Node last_1_Node;
    public static Node last_2_Node;
    public static Node last_3_Node;
    public static int floor_now=1;
    private ImageView markerview;
    private ImageView destinationmarker;
    //private Button move;
    private Button destBtn;
    private Button sourceBtn;
    private ImageButton exchangeBtn;
    private ImageButton searchBtn;
    private ImageButton retargetBtn;
    private Dijkstra dijkstra;
    private MyView myView;
    private DrawLine drawLine;
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

    private TextView text_dest;
    private TextView text_src;
    private ListView floor_list;

    private FrameLayout groupView;
    private RelativeLayout bigGroupView;


    private TextView now_location_view;

    double standardwidth;
    double standardheight;

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

    //stair[0]表一樓樓梯口的beacon(minor=24
    //stair[1]表二樓樓梯口的(minor=44

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_main);
        initView();
        initTask();



    }

    // 新的收list傳回來的所選目的地的方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_SRC:
                String src_listItemContent = data.getStringExtra("src_ListItemContent");
                Log.d("leosu", "src_listItemPosition: " + src_listItemContent);
                if(src_listItemContent.equals("我的位置")){ //代表選到了”我的位置“
                    floor_user=false;
                    calculatePath=true;
                    startScan();
                }else{
                    if(src_listItemContent!=null){
                        String src_listItemPoint=this.getPointName(src_listItemContent);//找出教室對應的定位點
                        Log.d("leosu", "src_listItemPosition: " + src_listItemPoint);
                        for(int k=0;k<point_num;k++){//透過定位點名稱找出對應的node[i]
                            if(node[k].getName().equals(src_listItemPoint)){
                                sourceNode=node[k];
                                break;
                            }
                        }

                        stopScan();
                    }
                    //sourceNode=node[src_listItemPosition-1]; //因為index=0被“我的位置”用走了，所以對回去node的時候要減一
                    if(!(destBtn.getText().equals("選擇目的地"))){
                        Log.d(TAG, "onActivityResult: "+"~~~hello");
                        dijkstraPath();
                        Log.d(TAG, "onActivityResult: "+"~~~hello2");
                        //drawLine.update();
                    }


                }



//                sourceNode=node[src_listItemPosition];
//                dijkstraPath();

                //把目前選擇的 src show 在螢幕上
                setMarker();
                sourceBtn.setText(src_listItemContent);

                break;

            case REQUEST_CODE_DEST:
                String listItemContent = data.getStringExtra("ListItemContent");//xin1116
                Log.d("leosu", "dest_listItemPosition: " + listItemContent);
                destinationClass=listItemContent;
                if(listItemContent!=null){
                    String listItemPoint=this.getPointName(listItemContent);//找出教室對應的定位點
                    Log.d("leosu", "dest_listItemPosition: " + listItemPoint);
                    for(int k=0;k<point_num;k++){//透過定位點名稱找出對應的node[i]
                        if(node[k].getName().equals(listItemPoint)){
                            navigation_destinationNode=node[k];
                            break;
                        }
                    }
                    stopScan();
                }
                if(!(sourceBtn.getText().equals("選擇起點"))){
                    dijkstraPath();
                }

                setDestinationMarker();
                //把目前選擇的 dest show 在螢幕上
                destBtn.setText(listItemContent);
                Log.d("leosu", "REQUEST_CODE_DEST");

                break;

            case REQUEST_CODE_RETURN:
//                int listItemPosition = data.getIntExtra("ListItemPosition", 0);
//                Log.d("leosu", "dest_listItemPosition: " + listItemPosition);
//                destinationNode=node[listItemPosition];
//                dijkstraPath();
//                //把目前選擇的 dest show 在螢幕上
//                TextView text_dest = findViewById(R.id.text_destination);
//                text_dest.setText(destinationNode.getName());
                Log.d("leosu", "REQUEST_CODE_RETURN");

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

    private void dijkstraPath(){
        /*if(sourceNode.getFloor()==1){
            myView.setImageName("iim_1f");
            floor_now=1;
        }else if(sourceNode.getFloor()==2){
            myView.setImageName("project2");
            floor_now=2;
        }*/
        Log.d(TAG, "yydijkstraPath: "+navigation_destinationNode.getX()+","+navigation_destinationNode.getY());
        String path=dijkstra.setPath(sourceNode,navigation_destinationNode);
        //Toast.makeText(NavigationView.this, path, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "dijkstraPath: "+dijkstra.printPath(sourceNode,navigation_destinationNode));
        drawLine.update();
        setMarker();
        setDestinationMarker();

    }

    private void initTask() {
        /*widthOffset = width / 50;
        heightOffset = height/ 90;*/

        Log.d("marker","~~~~init");

        markerview.setX(-9999999*widthOffset);
        markerview.setY(-9999999*heightOffset);



        if(!destinationClass.equals("選擇目的地")){
            destBtn.setText(destinationClass);
            navigation_destinationNode=destinationNode;
            sourceNode=destinationNode;
            changeFloor(destinationNode.getFloor());
            drawLine.update();
            setDestinationMarker();
            Log.d(TAG, "initTask: "+destinationmarker.getX()+","+destinationmarker.getY());

        }else{
            sourceNode=node[9];
            navigation_destinationNode=node[9];
        }



        for(int i=0;i<100;i++)
        {
            kalman_list[i]=new Kalman(9.0,100.0);
        }



        groupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(NavigationView.this, "click", Toast.LENGTH_SHORT).show();
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
                intent.setClass(NavigationView.this , destinationSearchActivity.class);
                //這邊要多加 for result，才能讓 list 跳回 main 時，傳使用者選哪一個回來
                startActivityForResult(intent, REQUEST_CODE_DEST);
            }
        });

        /*
        點擊按鈕顯示出發點的 listview 選單
        */
        sourceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sourceIntent = new Intent();
                sourceIntent.setClass(NavigationView.this, sourceSearchActivity.class);
                //這邊要多加 for result，才能讓 list 跳回 main 時，傳使用者選哪一個回來
                startActivityForResult(sourceIntent, REQUEST_CODE_SRC);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dijkstraPath();
            }
        });

        exchangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Node temp=sourceNode;
                CharSequence temp_str=sourceBtn.getText();
                sourceBtn.setText(destBtn.getText());
                destBtn.setText(temp_str);
                sourceNode=navigation_destinationNode;
                navigation_destinationNode=temp;
                dijkstraPath();


            }
        });

        floor_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                changeFloor(position+1);//position0表1樓，以此類推
                setMarker();
                setDestinationMarker();
                drawLine.update();
                floor_user=true;//表示使用者對樓層有控制權

            }
        });

        retargetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floor_now=sourceNode.getFloor();
                changeFloor(floor_now);
                drawLine.update();
                setMarker();
                setDestinationMarker();
                floor_user=false;
            }
        });




        //*************************************//

        /*
        把初始化時的 src and dest show 在螢幕上
         */


        //destBtn.setText(destinationNode.getName());//如果要一開始不是顯示a10改這裡
        //sourceBtn.setText(sourceNode.getName());//如果要一開始不是顯示a10改這裡

        final String[] floor={"1","2","3"};
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


        dijkstra=new Dijkstra();//node[i]也會被建起來

        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> beacons) {


                if(beacons.isEmpty()==true) {
                    Log.d(TAG,"WHY FUCK！！！！");
                    Log.d("fuck","GENIUS,00000000000000000000000000");
                }else{


                    Beacon beacons_now = null;
                    int beacons_length=beacons.size();
                    int beacons_idx_now=beacons_length-1;
                    Log.d( "kalman_","beacons_idx_now___"+beacons_idx_now);

                    for(int i=0;i<beacons_length;i++)//for debug
                    {
                        Log.d("fuck","GENIUS, That's what i want :"+i+" "+beacons.get(i).getMinor()+";"+beacons.get(i).getRssi());

                    }
                    Log.d("fuck","GENIUS, That's what i want ");

                    //換樓層:看收到的beacon哪個樓層多+一樓二樓的RSSI值平均，以確保不會亂跳樓層
                    int number_first_change_floor=0;
                    int number_second_change_floor=0;
                    double RSSI_first_change_floor=0;
                    double RSSI_second_change_floor=0;
                    for(int i=0;i<beacons_length;i++)
                    {
                        if(beacons.get(i).getMinor()<70){
                            if(beacons.get(i).getMajor()==1){
                                number_first_change_floor++;
                                RSSI_first_change_floor+=beacons.get(i).getRssi();
                            }else if(beacons.get(i).getMajor()==2){
                                number_second_change_floor++;
                                RSSI_second_change_floor+=beacons.get(i).getRssi();
                            }
                        }

                    }
                    RSSI_first_change_floor=(RSSI_first_change_floor/number_first_change_floor);
                    RSSI_second_change_floor=(RSSI_second_change_floor/number_second_change_floor);
                    if(number_first_change_floor>=number_second_change_floor && RSSI_first_change_floor>RSSI_second_change_floor){
                        //表應該要是一樓
                        if (floor_now!=1 && !(floor_user)/*表樓層控制權不再使用者*/){
                            floor_now=1;
                            /*changeFloor(1);
                            setMarker();
                            setDestinationMarker();
                            drawLine.update();*/
                        }

                    }else if (number_first_change_floor<=number_second_change_floor && RSSI_first_change_floor<RSSI_second_change_floor) {//表應該要是二樓
                        if (floor_now!=2 && !(floor_user)){
                            floor_now=2;
                            /*changeFloor(2);
                            drawLine.update();
                            setMarker();
                            setDestinationMarker();*/
                        }
                    }//沒有三樓beacon

                    //換樓層結束
                    //
                    //
                    //投票法定位

                    sourceNode=votePosition(beacons,beacons_length);
                    Log.d("1028","sourceNode: "+sourceNode.getName());
                    if(!(sourceNode.getName().equals("-1"))){//表有透過投票找到最多票數的point
                        if(sourceNode.getFloor()== floor_now && !(floor_user)){
                            changeFloor(floor_now);
                            drawLine.update();
                        }else if(sourceNode.getFloor()!= floor_now && !(floor_user)){
                            floor_now=sourceNode.getFloor();
                            changeFloor(floor_now);
                            drawLine.update();
                        }
                        /*if(floor_now==1){
                            if(sourceNode.getFloor()== floor_now && !(floor_user)){
                                changeFloor(floor_now);
                                drawLine.update();
                            }else if(sourceNode.getFloor()!= floor_now && !(floor_user)){
                                floor_now=sourceNode.getFloor();
                                changeFloor(floor_now);
                                drawLine.update();
                            }
                        }else if(floor_now==2){
                            if(sourceNode.getFloor()== floor_now && !(floor_user)){
                                changeFloor(floor_now);
                                drawLine.update();
                            }else if(sourceNode.getFloor()!= floor_now && !(floor_user)){
                                floor_now=sourceNode.getFloor();
                                changeFloor(floor_now);
                                drawLine.update();
                            }

                        }*/


                        //firstTime_Dijkstra;
                        if(calculatePath){
                            dijkstraPath();
                        }
                        calculatePath=false;
                        //now_location_view.setText("X: "+midpointX+" Y: "+midpointY);
                        //now_location_view.setText("X: "+String.valueOf(sourceNode.getX())+" Y: "+String.valueOf(sourceNode.getY()));
                        setMarker();
                        setDestinationMarker();
                    }
                    //投票法結束


                    //
                    //
                    //舊方法
                    /*
                    while(beacons_idx_now < beacons_length && beacons_idx_now>=0){//讀每一run的RSSI，從後面往前讀

                        Log.d( "kalman_","beacons_idx_now"+beacons_idx_now+";"+beacons_length);


                        if(kalman_list[beacons_idx_now].startValue==0)
                        {
                            kalman_list[beacons_idx_now].startValue=beacons.get(0).getRssi();
                        }

                        beacons_now=beacons.get(beacons_idx_now);

                        beacons_now_Kalmanrssi=kalman_list[beacons_idx_now].KalmanFilter(beacons_now.getRssi());


                        Log.d("fuck","GENIUS, That's what i want~~0929。Minor:"+beacons_now.getMinor()+" RSSI: "+beacons_now.getRssi());
                        Log.d("fuck","GENIUS, That's what i want~~0929_Kalman。"+beacons_now_Kalmanrssi);



                        if(beacons_now.getMinor()>70 || beacons_now.getMinor()<0){
                            beacons_idx_now--;
                            continue;
                        }


                        //在f1局表示，f1count是index, f2count是顆數
                        //在f2局表示，f2count是index, f1count是顆數
                        boolean f2_minor_exist=false;
                        boolean f1_minor_exist=false;


                        if(beacons_now.getMajor()==2){

                            //判斷minor是不是已經在f2 array
                            for(int i=0;i<4;i++){
                                if(f2[i].getMinor()==beacons_now.getMinor()){
                                    f2_minor_exist=true;
                                    break;
                                }
                            }


                            if (!f2_minor_exist) {//如果掃到已經在f2-array就不存(不是取代

                                //存進f2 arrey
                                f2[f2count].setRssi(beacons_now_Kalmanrssi);

                                //算訊號平均比較大小
                                int f2tot=0;
                                int f1tot=0;
                                if(f2count>=2 && f1count>=2){
                                    for (int i=0;i<=f2count;i++){
                                        f2tot+=f2[i].getRssi();
                                    }
                                    f2tot=f2tot/(f2count+1);
                                    for (int i=0;i<=f1count;i++){
                                        f1tot+=f1[i].getRssi();
                                    }
                                    f1tot=f1tot/(f1count);
                                }else if(f2count>=3 && f1count<=1){
                                    f2tot=999;//很大的數
                                }
                                Log.d("tot", "f1tot: "+f1tot+" f2tot: "+f2tot);
                                Log.d("tot","f1"+f1[0].getRssi()+";"+f1[1].getRssi()+";"+f1[2].getRssi()+";"+f1[3].getRssi()+";");
                                Log.d("tot","f2"+f2[0].getRssi()+";"+f2[1].getRssi()+";"+f2[2].getRssi()+";"+f2[3].getRssi()+";");

                                if(f2tot>f1tot){
                                    myView.setImageName("project2");
                                    floor_now=2;
                                    drawLine.update();
                                    setMarker();
                                    Log.d("tot","1>>2");
                                }else if(f2tot<f1tot){
                                    myView.setImageName("iim_1f");
                                    floor_now=1;
                                    drawLine.update();
                                    setMarker();
                                    Log.d("tot","2>>1");
                                }

                                //滿了就清空，重頭存

                                if(f2count==3){
                                    f2count=0;
                                }else{
                                    f2count=f2count+1;
                                }
                                Log.d("tot","f1count "+f1count+" ;f2count "+f2count);

                            }

                            double n;//根據ＲＳＳＩ值換Ｎ值
                            if(beacons_now_Kalmanrssi>=-68)
                                n=4.95;
                            else if(beacons_now_Kalmanrssi>=-76)
                                n=2.9;
                            else if(beacons_now_Kalmanrssi>=-83)
                                n=2.9;
                            else if(beacons_now_Kalmanrssi>=-87)
                                n=3.65;
                            else if(beacons_now_Kalmanrssi>=-93)
                                n=4;
                            else
                                n=4.65;

                            if(beacons_now_Kalmanrssi>-90){

                                //Major=2就存進circle_2f array裡面，之後定位用
                                Log.d("tot","f2_circle"+f2_circle);
                                circle_2f[f2_circle].setX(beacon_xyr_5[beacons_now.getMinor()].getX());//應該要可以用Minor直接查資料庫反推
                                circle_2f[f2_circle].setY(beacon_xyr_5[beacons_now.getMinor()].getY());
                                circle_2f[f2_circle].setR(beacons_now_Kalmanrssi);
                                circle_2f[f2_circle].setD(Math.pow(10, (Math.abs(beacons_now_Kalmanrssi)+beacon_xyr_5[beacons_now.getMinor()].getR()) / (10 * 5)));
                                circle_2f[f2_circle].setNearPoint(beacon_xyr_5[beacons_now.getMinor()].getNearPoint());
                                circle_2f[f2_circle].setRightPoint(beacon_xyr_5[beacons_now.getMinor()].getRightPoint());
                                circle_2f[f2_circle].setLeftPoint(beacon_xyr_5[beacons_now.getMinor()].getLeftPoint());
                                f2_circle++;

                                Log.d("limit","minor~"+beacons_now.getMinor()+"~rssi~"+beacons_now_Kalmanrssi);

                            }

                            //beacons_idx_now++;

                            Log.d("fuck","GEN，index: "+beacons_idx_now);

                            if(f2_circle==5){
                                f2_circle=0;
                            }//存滿了就取代，然後繼續存

                            //Major=2就存進circle_2f array裡面，之後定位用

                        }else if(beacons_now.getMajor()==1){//1f的情況，比照2f

                            for(int i=0;i<4;i++){
                                if(f1[i].getMinor()==beacons_now.getMinor()){
                                    f1_minor_exist=true;
                                    break;
                                }
                            }

                            if (!f1_minor_exist) {
                                f1[f1count].setRssi(beacons_now_Kalmanrssi);
                                int f2tot=0;
                                int f1tot=0;
                                if(f2count>=2 && f1count>=2){
                                    for (int i=0;i<=f2count;i++){
                                        f2tot+=f2[i].getRssi();
                                    }
                                    f2tot=f2tot/(f2count);
                                    for (int i=0;i<=f1count;i++){
                                        f1tot+=f1[i].getRssi();
                                    }
                                    f1tot=f1tot/(f1count+1);

                                }else if(f2count<=1&&f1count>=3){
                                    f1tot=999;//很大的數
                                }

                                Log.d("tot", "f1tot: "+f1tot+" f2tot: "+f2tot);
                                Log.d("tot","f1"+f1[0].getRssi()+";"+f1[1].getRssi()+";"+f1[2].getRssi()+";"+f1[3].getRssi()+";");
                                Log.d("tot","f2"+f2[0].getRssi()+";"+f2[1].getRssi()+";"+f2[2].getRssi()+";"+f2[3].getRssi()+";");

                                if(f2tot>f1tot){
                                    myView.setImageName("project2");
                                    floor_now=2;
                                    drawLine.update();
                                    setMarker();
                                    Log.d("tot","1>>2");
                                }else if(f2tot<f1tot){
                                    myView.setImageName("iim_1f");
                                    floor_now=1;
                                    drawLine.update();
                                    setMarker();
                                    Log.d("tot","2>>1");
                                }//兩個都是零不會比=不會換樓層


                                if(f1count==3){
                                    f1count=0;
                                }else{
                                    f1count=f1count+1;
                                }

                                Log.d("tot","f1count "+f1count+" ;f2count "+f2count);

                            }

                            double n;//根據ＲＳＳＩ值換Ｎ值
                            if(beacons_now_Kalmanrssi>=-68)
                                n=4.95;
                            else if(beacons_now_Kalmanrssi>=-76)
                                n=2.9;
                            else if(beacons_now_Kalmanrssi>=-83)
                                n=2.9;
                            else if(beacons_now_Kalmanrssi>=-87)
                                n=3.65;
                            else if(beacons_now_Kalmanrssi>=-93)
                                n=4;
                            else
                                n=4.65;

                            if(beacons_now_Kalmanrssi>-90) {
                                Log.d("tot","f1_circle"+f1_circle);
                                circle_1f[f1_circle].setX(beacon_xyr_5[beacons_now.getMinor()].getX());//應該要可以用Minor直接查資料庫反推
                                circle_1f[f1_circle].setY(beacon_xyr_5[beacons_now.getMinor()].getY());
                                circle_1f[f1_circle].setR(beacons_now_Kalmanrssi);
                                circle_1f[f1_circle].setD(Math.pow(10, (Math.abs(beacons_now_Kalmanrssi) + beacon_xyr_5[beacons_now.getMinor()].getR()) / (10 * 5)));
                                circle_1f[f1_circle].setNearPoint(beacon_xyr_5[beacons_now.getMinor()].getNearPoint());
                                circle_1f[f1_circle].setRightPoint(beacon_xyr_5[beacons_now.getMinor()].getRightPoint());
                                circle_1f[f1_circle].setLeftPoint(beacon_xyr_5[beacons_now.getMinor()].getLeftPoint());
                                f1_circle++;
                                Log.d("limit","minor~"+beacons_now.getMinor()+"~rssi~"+beacons_now_Kalmanrssi);
                            }



                            Log.d("fuck","GEN，index: "+beacons_idx_now);

                            if(f1_circle==5){
                                f1_circle=0;
                            }


                        }

                        beacons_idx_now--;

                    }

                    if(beacons_now.getMajor()==floor_now){

                        if(floor_now==1 && circle_1f[4].getD()!=0){

                            Log.d("tot","major~"+beacons_now.getMajor()+"~floor_now~"+floor_now+"~f1circle~"+f1_circle+"~f2cicle~"+f2_circle);
                            circle_1f[findMin(circle_1f)]=circle_1f[3];
                            circle_1f[findMin(circle_1f)]=circle_1f[4];
                            if (circle_1f[0].getD()>0 && circle_1f[1].getD()>0 && circle_1f[2].getD()>0 ){
                                boolean intersect=false;
                                int x=2;
                                while(intersect==false && x<=4){
                                    double[] intersect01=intersect(circle_1f[0].getX(),circle_1f[1].getX(),circle_1f[0].getY(),circle_1f[1].getY(),circle_1f[0].getD(),circle_1f[1].getD());
                                    Log.d("fuck","("+circle_1f[0].getX()+","+circle_1f[0].getY()+")-"+circle_1f[0].getD()+"("+circle_1f[1].getX()+","+circle_1f[1].getY()+")"+"-"+circle_1f[1].getD());
                                    double[] intersect02=intersect(circle_1f[0].getX(),circle_1f[2].getX(),circle_1f[0].getY(),circle_1f[2].getY(),circle_1f[0].getD(),circle_1f[2].getD());
                                    Log.d("fuck","("+circle_1f[0].getX()+","+circle_1f[0].getY()+")-"+circle_1f[0].getD()+"("+circle_1f[2].getX()+","+circle_1f[2].getY()+")"+"-"+circle_1f[2].getD());
                                    double[] intersect21=intersect(circle_1f[2].getX(),circle_1f[1].getX(),circle_1f[2].getY(),circle_1f[1].getY(),circle_1f[2].getD(),circle_1f[1].getD());
                                    Log.d("fuck","("+circle_1f[1].getX()+","+circle_1f[1].getY()+")-"+circle_1f[1].getD()+"("+circle_1f[2].getX()+","+circle_1f[2].getY()+")"+"-"+circle_1f[2].getD());

                                    if(intersect01!=null && intersect02!=null && intersect21!=null){
                                        Log.d("hello","three");
                                        intersect=true;
                                        midpointX=(intersect01[0]+intersect01[2]+intersect02[0]+intersect02[2]+intersect21[0]+intersect21[2])/6;
                                        midpointY=(intersect01[1]+intersect01[3]+intersect02[1]+intersect02[3]+intersect21[1]+intersect21[3])/6;
                                    }else if(intersect01!=null && intersect02!=null){
                                        Log.d("hello","two1");
                                        intersect=true;
                                        midpointX=(intersect01[0]+intersect01[2]+intersect02[0]+intersect02[2])/4;
                                        midpointY=(intersect01[1]+intersect01[3]+intersect02[1]+intersect02[3])/4;
                                    }else if(intersect01!=null && intersect21!=null){
                                        Log.d("hello","two2");
                                        intersect=true;
                                        midpointX=(intersect01[0]+intersect01[2]+intersect21[0]+intersect21[2])/4;
                                        midpointY=(intersect01[1]+intersect01[3]+intersect21[1]+intersect21[3])/4;
                                    }else if(intersect02!=null && intersect21!=null){
                                        Log.d("hello","two3");
                                        intersect=true;
                                        midpointX=(intersect02[0]+intersect02[2]+intersect21[0]+intersect21[2])/4;
                                        midpointY=(intersect02[1]+intersect02[3]+intersect21[1]+intersect21[3])/4;
                                    }else if (intersect01!=null){
                                        Log.d("hello","one1");
                                        intersect=true;
                                        midpointX=(intersect01[0]+intersect01[2])/2;
                                        midpointY=(intersect01[1]+intersect01[3])/2;
                                    }else if(intersect02!=null){
                                        Log.d("hello","one2");
                                        intersect=true;
                                        midpointX=(intersect02[0]+intersect02[2])/2;
                                        midpointY=(intersect02[1]+intersect02[3])/2;
                                    }else if(intersect21!=null){
                                        Log.d("hello","one3");
                                        intersect=true;
                                        midpointX=(intersect21[0]+intersect21[2])/2;
                                        midpointY=(intersect21[1]+intersect21[3])/2;
                                    }else{
                                        Log.d("hello","zero");
                                        intersect=false;
                                        if(x<4){
                                            circle_1f[x]=circle_1f[x+1];
                                        }
                                        x=x+1;

                                    }

                                }



                                if(intersect) {

                                    double min_num=Integer.MAX_VALUE;

                                    for(int i=0;i<point_num;i++){
                                        for(int j=0;j<3;j++){
                                            //Log.d("fuck",circle_3[j].getNearPoint()+circle_3[j].getLeftPoint()+circle_3[j].getRightPoint());
                                            if( circle_1f[j].getNearPoint().equals(node[i].getName())||circle_1f[j].getLeftPoint().equals(node[i].getName())||circle_1f[j].getRightPoint().equals(node[i].getName())){
                                                if(pointDistance(node[i].getX(),node[i].getY(),midpointX,midpointY)<min_num){
                                                    //Log.d("node",node[i].getName()+" X: "+node[i].getX()+"; Y: "+node[i].getY());
                                                    min_num=pointDistance(node[i].getX(),node[i].getY(),midpointX,midpointY);
                                                    midpoint=node[i];
                                                }
                                            }

                                        }
                                    }

                                    sourceNode=midpoint;
                                    if(sourceNode.getFloor()==1){
                                        myView.setImageName("iim_1f");
                                        floor_now=1;
                                    }else if(sourceNode.getFloor()==2){
                                        myView.setImageName("project2");
                                        floor_now=2;
                                    }
                                    //stopScan();
                                    if(calculatePath){
                                        dijkstraPath();
                                    }
                                    calculatePath=false;
                                }


                            }

                            //now_location_view.setText("X: "+midpointX+" Y: "+midpointY);
                            now_location_view.setText("X: "+String.valueOf(midpoint.getX())+" Y: "+String.valueOf(midpoint.getY()));
                            setMarker();

                            if(sourceNode.getFloor()!=floor_now){
                                Log.d("tot","cursormissing: position: ("+sourceNode.getX()+","+sourceNode.getY()+") & position floor: "+sourceNode.getFloor()+",,,,,floor now: "+floor_now);
                            }


                        }else if(floor_now==2 && circle_2f[4].getD()!=0){

                            Log.d("tot","major~"+beacons_now.getMajor()+"~floor_now~"+floor_now+"~f1circle~"+f1_circle+"~f2cicle~"+f2_circle);


                            circle_2f[findMin(circle_2f)]=circle_2f[3];
                            circle_2f[findMin(circle_2f)]=circle_2f[4];
                            if (circle_2f[0].getD()>0 && circle_2f[1].getD()>0 && circle_2f[2].getD()>0 ){

                                boolean intersect=false;
                                int x=2;
                                while(intersect==false && x<=4){
                                    double[] intersect01=intersect(circle_2f[0].getX(),circle_2f[1].getX(),circle_2f[0].getY(),circle_2f[1].getY(),circle_2f[0].getD(),circle_2f[1].getD());
                                    Log.d("fuck","("+circle_2f[0].getX()+","+circle_2f[0].getY()+")-"+circle_2f[0].getD()+"("+circle_2f[1].getX()+","+circle_2f[1].getY()+")"+"-"+circle_2f[1].getD());
                                    double[] intersect02=intersect(circle_2f[0].getX(),circle_2f[2].getX(),circle_2f[0].getY(),circle_2f[2].getY(),circle_2f[0].getD(),circle_2f[2].getD());
                                    Log.d("fuck","("+circle_2f[0].getX()+","+circle_2f[0].getY()+")-"+circle_2f[0].getD()+"("+circle_2f[2].getX()+","+circle_2f[2].getY()+")"+"-"+circle_2f[2].getD());
                                    double[] intersect21=intersect(circle_2f[2].getX(),circle_2f[1].getX(),circle_2f[2].getY(),circle_2f[1].getY(),circle_2f[2].getD(),circle_2f[1].getD());
                                    Log.d("fuck","("+circle_2f[1].getX()+","+circle_2f[1].getY()+")-"+circle_2f[1].getD()+"("+circle_2f[2].getX()+","+circle_2f[2].getY()+")"+"-"+circle_2f[2].getD());

                                    if(intersect01!=null && intersect02!=null && intersect21!=null){
                                        Log.d("hello","three");
                                        intersect=true;
                                        midpointX=(intersect01[0]+intersect01[2]+intersect02[0]+intersect02[2]+intersect21[0]+intersect21[2])/6;
                                        midpointY=(intersect01[1]+intersect01[3]+intersect02[1]+intersect02[3]+intersect21[1]+intersect21[3])/6;
                                    }else if(intersect01!=null && intersect02!=null){
                                        Log.d("hello","two1");
                                        intersect=true;
                                        midpointX=(intersect01[0]+intersect01[2]+intersect02[0]+intersect02[2])/4;
                                        midpointY=(intersect01[1]+intersect01[3]+intersect02[1]+intersect02[3])/4;
                                    }else if(intersect01!=null && intersect21!=null){
                                        Log.d("hello","two2");
                                        intersect=true;
                                        midpointX=(intersect01[0]+intersect01[2]+intersect21[0]+intersect21[2])/4;
                                        midpointY=(intersect01[1]+intersect01[3]+intersect21[1]+intersect21[3])/4;
                                    }else if(intersect02!=null && intersect21!=null){
                                        Log.d("hello","two3");
                                        intersect=true;
                                        midpointX=(intersect02[0]+intersect02[2]+intersect21[0]+intersect21[2])/4;
                                        midpointY=(intersect02[1]+intersect02[3]+intersect21[1]+intersect21[3])/4;
                                    }else if (intersect01!=null){
                                        Log.d("hello","one1");
                                        intersect=true;
                                        midpointX=(intersect01[0]+intersect01[2])/2;
                                        midpointY=(intersect01[1]+intersect01[3])/2;
                                    }else if(intersect02!=null){
                                        Log.d("hello","one2");
                                        intersect=true;
                                        midpointX=(intersect02[0]+intersect02[2])/2;
                                        midpointY=(intersect02[1]+intersect02[3])/2;
                                    }else if(intersect21!=null){
                                        Log.d("hello","one3");
                                        intersect=true;
                                        midpointX=(intersect21[0]+intersect21[2])/2;
                                        midpointY=(intersect21[1]+intersect21[3])/2;
                                    }else{
                                        Log.d("hello","zero");
                                        intersect=false;
                                        if(x<4){
                                            circle_2f[x]=circle_2f[x+1];
                                        }
                                        x=x+1;
                                    }

                                }




                                if(intersect) {

                                    double min_num=Integer.MAX_VALUE;

                                    for(int i=0;i<point_num;i++){
                                        for(int j=0;j<3;j++){
                                            //Log.d("fuck",circle_3[j].getNearPoint()+circle_3[j].getLeftPoint()+circle_3[j].getRightPoint());
                                            if( circle_2f[j].getNearPoint().equals(node[i].getName())||circle_2f[j].getLeftPoint().equals(node[i].getName())||circle_2f[j].getRightPoint().equals(node[i].getName())){
                                                if(pointDistance(node[i].getX(),node[i].getY(),midpointX,midpointY)<min_num){
                                                    //Log.d("node",node[i].getName()+" X: "+node[i].getX()+"; Y: "+node[i].getY());
                                                    min_num=pointDistance(node[i].getX(),node[i].getY(),midpointX,midpointY);
                                                    midpoint=node[i];
                                                }
                                            }

                                        }
                                    }

                                    sourceNode=midpoint;
                                    if(sourceNode.getFloor()==1){
                                        myView.setImageName("iim_1f");
                                        floor_now=1;
                                    }else if(sourceNode.getFloor()==2){
                                        myView.setImageName("project2");
                                        floor_now=2;
                                    }
                                    //stopScan();
                                    if(calculatePath){
                                        dijkstraPath();
                                    }
                                    calculatePath=false;
                                }


                            }

                            //now_location_view.setText("X: "+midpointX+" Y: "+midpointY);
                            now_location_view.setText("X: "+String.valueOf(midpoint.getX())+" Y: "+String.valueOf(midpoint.getY()));
                            setMarker();

                            if(sourceNode.getFloor()!=floor_now){
                                Log.d("tot","cursormissing: position: ("+sourceNode.getX()+","+sourceNode.getY()+") & position floor: "+sourceNode.getFloor()+",,,,,floor now: "+floor_now);
                            }




                            Log.d("fuck","00000000"+f2_circle);

                        }

                    }*/


                       /* ranging_count++;
                        if(ranging_count>5){
                            for(int i=0;i<3;i++){
                                circle_3[i].setAll();
                            }
                            ranging_count=0;
                            Log.d(TAG,"enter");

                        }*/
/*
                        int min_idx=findMin(circle_3);
                        if(beacons.get(0).getRssi()>circle_3[min_idx].getR()){

                            circle_3[min_idx].setX(beacon_xyr_5[beacons.get(0).getMinor()].getX());//應該要可以用Minor直接查資料庫反推
                            circle_3[min_idx].setY(beacon_xyr_5[beacons.get(0).getMinor()].getY());
                            circle_3[min_idx].setR(beacons.get(0).getRssi());
                            circle_3[min_idx].setD(Math.pow(10, (Math.abs(beacons.get(0).getRssi())+beacon_xyr_5[beacons.get(0).getMinor()].getR()) / (10 * beacon_xyr_5[beacons.get(0).getMinor()].getN())));
                            circle_3[min_idx].setNearPoint(beacon_xyr_5[beacons.get(0).getMinor()].getNearPoint());
                            circle_3[min_idx].setRightPoint(beacon_xyr_5[beacons.get(0).getMinor()].getRightPoint());
                            circle_3[min_idx].setLeftPoint(beacon_xyr_5[beacons.get(0).getMinor()].getLeftPoint());




                        }*/

                }

                //}


            }


        });



        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
            @Override
            public void onEnteredRegion(BeaconRegion region, List<Beacon> beacons) {
                //beaconAdapter.addItems(beacons);



                Log.d(TAG, "onEnteredRegion: " + region);
                Log.d("fuck", "onEnteredRegion:minor "+beacons.get(0).getMinor());
                /*if(beacons.get(0).getMinor()==23){//1F
                    if(stair[1]){
                        if(myView.getImageName()=="project2"){
                            myView.setImageName("iim_1f");
                            floor_now=1;
                            drawLine.update();
                            setMarker();
                            stair[1]=false;
                        }

                    }else{
                        stair[0]=true;
                    }
                }else if (beacons.get(0).getMinor()==64){//2F
                    if(stair[0]){
                        if(myView.getImageName()=="iim_1f"){
                            myView.setImageName("project2");
                            floor_now=2;
                            drawLine.update();
                            setMarker();
                            stair[0]=false;
                        }
                    }else{
                        stair[1]=true;
                    }
                }else{*/

                    /*

                    if(beacons.get(0).getRssi()>circle_3[min_idx].getR()){
                        circle_3[min_idx].setX(beacon_xyr_5[beacons.get(0).getMinor()].getX());
                        circle_3[min_idx].setY(beacon_xyr_5[beacons.get(0).getMinor()].getY());
                        circle_3[min_idx].setR(beacons.get(0).getRssi());
                        circle_3[min_idx].setD(Math.pow(10, (Math.abs(beacons.get(0).getRssi())+beacon_xyr_5[beacons.get(0).getMinor()].getR()) / (10 * beacon_xyr_5[beacons.get(0).getMinor()].getN())));
                        Log.d("rssi", "rssi: "+beacons.get(0).getRssi());
                        Log.d("rssi", "r0: "+beacon_xyr_5[beacons.get(0).getMinor()].getR());
                        Log.d("rssi", "d: "+circle_3[min_idx].getD());
                        Log.d("rssi", "n: "+beacon_xyr_5[beacons.get(0).getMinor()].getN());


                        int max_idx[]=findMAX(circle_3);
                        if (circle_3[0].getD()>0 && circle_3[1].getD()>0 && circle_3[2].getD()>0 ){
                            double[] intersect01=intersect(circle_3[0].getX(),circle_3[1].getX(),circle_3[0].getY(),circle_3[1].getY(),circle_3[0].getD(),circle_3[1].getD());
                            double[] intersect02=intersect(circle_3[0].getX(),circle_3[2].getX(),circle_3[0].getY(),circle_3[2].getY(),circle_3[0].getD(),circle_3[2].getD());
                            double[] intersect21=intersect(circle_3[2].getX(),circle_3[1].getX(),circle_3[2].getY(),circle_3[1].getY(),circle_3[2].getD(),circle_3[1].getD());

                            boolean intersect=true;

                            if(intersect01!=null && intersect02!=null && intersect21!=null){
                                Log.d("hello","three");
                                midpointX=(intersect01[0]+intersect01[2]+intersect02[0]+intersect02[2]+intersect21[0]+intersect21[2])/6;
                                midpointY=(intersect01[1]+intersect01[3]+intersect02[1]+intersect02[3]+intersect21[1]+intersect21[3])/6;
                            }else if(intersect01!=null && intersect02!=null){
                                Log.d("hello","two1");
                                midpointX=(intersect01[0]+intersect01[2]+intersect02[0]+intersect02[2])/4;
                                midpointY=(intersect01[1]+intersect01[3]+intersect02[1]+intersect02[3])/4;
                            }else if(intersect01!=null && intersect21!=null){
                                Log.d("hello","two2");
                                midpointX=(intersect01[0]+intersect01[2]+intersect21[0]+intersect21[2])/4;
                                midpointY=(intersect01[1]+intersect01[3]+intersect21[1]+intersect21[3])/4;
                            }else if(intersect02!=null && intersect21!=null){
                                Log.d("hello","two3");
                                midpointX=(intersect02[0]+intersect02[2]+intersect21[0]+intersect21[2])/4;
                                midpointY=(intersect02[1]+intersect02[3]+intersect21[1]+intersect21[3])/4;
                            }else if (intersect01!=null){
                                Log.d("hello","one1");
                                midpointX=(intersect01[0]+intersect01[2])/2;
                                midpointY=(intersect01[1]+intersect01[3])/2;
                            }else if(intersect02!=null){
                                Log.d("hello","one2");
                                midpointX=(intersect02[0]+intersect02[2])/2;
                                midpointY=(intersect02[1]+intersect02[3])/2;
                            }else if(intersect21!=null){
                                Log.d("hello","one3");
                                midpointX=(intersect21[0]+intersect21[2])/2;
                                midpointY=(intersect21[1]+intersect21[3])/2;
                            }else{
                                Log.d("hello","zero");
                                intersect=false;
                            }


                            if(intersect) {


                                double min_num=Integer.MAX_VALUE;

                                for(int i=0;i<point_num;i++){
                                    for(int j=0;j<3;j++){

                                        if( circle_3[j].getNearPoint().equals(node[i].getName())||circle_3[j].getLeftPoint().equals(node[i].getName())||circle_3[j].getRightPoint().equals(node[i].getName())){
                                            if(pointDistance(node[i].getX(),node[i].getY(),midpointX,midpointY)<min_num){

                                                min_num=pointDistance(node[i].getX(),node[i].getY(),midpointX,midpointY);
                                                midpoint=node[i];

                                            }
                                        }

                                    }
                                }
                                sourceNode=midpoint;
                                //stopScan();
                                if(calculatePath){
                                    dijkstraPath();
                                }
                                calculatePath=false;
                            }

                            now_location_view.setText("X: "+String.valueOf(midpoint.getX())+" Y: "+String.valueOf(midpoint.getY()));
                            //now_location_view.setText("X: "+midpointX+" Y: "+midpointY);
                            //Toast.makeText(MainActivity.this, "X: "+String.valueOf(midpointX)+" Y: "+String.valueOf(midpointY), Toast.LENGTH_SHORT).show();
                            setMarker();

                        }

                    }*/



                //}


            }

            @Override
            public void onExitedRegion(BeaconRegion region) {
                //Toast.makeText(NavigationView.this, "onExitedRegion", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onExitedRegion: " + region);
            }
        });

        /*move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFloor();
            }
        });*/

    }


    private Node votePosition(List<Beacon> beacons,int beacon_length){
        ArrayList<VotePosition> position_poit=new ArrayList<VotePosition>();
        int position_point_length=0;
        int beacons_now_Kalmanrssi;
        double vote_weight=0;
        double vote_d=0;


        for(int i=0;i<beacon_length;i++){

            boolean nearPoint=true;
            boolean leftPoint=true;
            boolean rightPoint=true;
            if(beacons.get(i).getMinor()<70){
                if(kalman_list[beacons.get(i).getMinor()].startValue==0)
                {
                    kalman_list[beacons.get(i).getMinor()].startValue=beacons.get(0).getRssi();
                }


                beacons_now_Kalmanrssi=kalman_list[beacons.get(i).getMinor()].KalmanFilter(beacons.get(i).getRssi());

                vote_d=Math.pow(10, (Math.abs(beacons_now_Kalmanrssi)+beacon_xyr_5[beacons.get(i).getMinor()].getR()) / (10 * 2.9));
                vote_weight=Math.exp((-(Math.pow(vote_d,2)))/2/(Math.pow(3,2))); //Math.exp((-(Math.pow(vote_d,2)))/2/(2^2)); //3是可以調的參數
            }

            for(int j=0;j<position_point_length;j++){
                if(beacons.get(i).getMinor()<70){//我們有0~69的beacon

                    //if(beacons.get(i).getRssi()>-90){

                    Log.d("GENIUS", "vote_d: "+vote_d+" ,vote_weight: "+vote_weight);
                    if(position_poit.get(j).getPositionName().equals(beacon_xyr_5[beacons.get(i).getMinor()].getLeftPoint())){

                        //正規化票數
                        int voteNumber=10;//設一個大數
                        for(int k=0;k<point_num;k++){
                            if(node[k].getName().equals(position_poit.get(j).getPositionName())){
                                voteNumber=node[k].getVote();
                                break;
                            }
                        }
                        position_poit.get(j).setVoteNumber((position_poit.get(j).getVoteNumber()+(vote_weight/voteNumber)));
                        leftPoint=false;
                        //Log.d("GENIUS", "left");


                    }else if(position_poit.get(j).getPositionName().equals(beacon_xyr_5[beacons.get(i).getMinor()].getRightPoint())){

                        //正規化票數
                        int voteNumber=10;//設一個大數
                        for(int k=0;k<point_num;k++){
                            if(node[k].getName().equals(position_poit.get(j).getPositionName())){
                                voteNumber=node[k].getVote();
                                break;
                            }
                        }

                        position_poit.get(j).setVoteNumber((position_poit.get(j).getVoteNumber()+(vote_weight/voteNumber)));
                        rightPoint=false;
                        //Log.d("GENIUS", "right");
                    }else if(position_poit.get(j).getPositionName().equals(beacon_xyr_5[beacons.get(i).getMinor()].getNearPoint())){

                        //正規化票數
                        int voteNumber=10;//設一個大數
                        for(int k=0;k<point_num;k++){
                            if(node[k].getName().equals(position_poit.get(j).getPositionName())){
                                voteNumber=node[k].getVote();
                                break;
                            }
                        }

                        position_poit.get(j).setVoteNumber((position_poit.get(j).getVoteNumber()+(vote_weight/voteNumber)));
                        nearPoint=false;
                        //Log.d("GENIUS", "near");
                    }
                    //}else{
                    //leftPoint=false;
                    //rightPoint=false;
                    //nearPoint=false;
                    //}
                }

            }
            if(beacons.get(i).getMinor()<70){
                if(nearPoint && !(beacon_xyr_5[beacons.get(i).getMinor()].getNearPoint().equals("-1"))){

                    //正規化票數
                    int voteNumber=10;//設一個大數
                    for(int k=0;k<point_num;k++){
                        if(node[k].getName().equals(beacon_xyr_5[beacons.get(i).getMinor()].getNearPoint())){
                            voteNumber=node[k].getVote();
                            break;
                        }
                    }

                    VotePosition countVote=new VotePosition(beacon_xyr_5[beacons.get(i).getMinor()].getNearPoint(),(vote_weight/voteNumber));
                    //Log.d("GENIUS", "__near: "+beacon_xyr_5[beacons.get(i).getMinor()].getNearPoint()+" , "+ vote_weight);
                    position_poit.add(countVote);
                    position_point_length++;

                }
                if(leftPoint && !(beacon_xyr_5[beacons.get(i).getMinor()].getLeftPoint().equals("-1"))){


                    //正規化票數
                    int voteNumber=10;//設一個大數
                    for(int k=0;k<point_num;k++){
                        if(node[k].getName().equals(beacon_xyr_5[beacons.get(i).getMinor()].getLeftPoint())){
                            voteNumber=node[k].getVote();
                            break;
                        }
                    }

                    VotePosition countVote=new VotePosition(beacon_xyr_5[beacons.get(i).getMinor()].getLeftPoint(),(vote_weight/voteNumber));
                    position_poit.add(countVote);
                    position_point_length++;
                    //Log.d("GENIUS", "__left: "+beacon_xyr_5[beacons.get(i).getMinor()].getNearPoint()+" , "+ vote_weight);
                }
                if(rightPoint && !(beacon_xyr_5[beacons.get(i).getMinor()].getRightPoint().equals("-1"))){

                    //正規化票數
                    int voteNumber=10;//設一個大數
                    for(int k=0;k<point_num;k++){
                        if(node[k].getName().equals(beacon_xyr_5[beacons.get(i).getMinor()].getRightPoint())){
                            voteNumber=node[k].getVote();
                            break;
                        }
                    }

                    VotePosition countVote=new VotePosition(beacon_xyr_5[beacons.get(i).getMinor()].getRightPoint(),(vote_weight/voteNumber));
                    position_poit.add(countVote);
                    position_point_length++;
                    //Log.d("GENIUS", "__right: "+beacon_xyr_5[beacons.get(i).getMinor()].getNearPoint()+" , "+ vote_weight);
                }
            }


        }
        int voteMAX_index=0;
        for (int l=0;l<position_point_length;l++){
            Log.d("fuck", "GENIUS_vote ("+position_poit.get(l).getVoteNumber()+" , "+position_poit.get(voteMAX_index).getVoteNumber()+" )");
            if(position_poit.get(l).getVoteNumber()>position_poit.get(voteMAX_index).getVoteNumber()){
                Log.d("fuck", "GENIUS_point ("+position_poit.get(l).getPositionName()+" , "+position_poit.get(l).getVoteNumber()+" )");
                voteMAX_index=l;
            }
            //如果全部平票表voteMAX_index=0，表取訊號最強的nearPoint
        }
        Node source=new Node("-1",-1,-1,-1,-1);
        for(int k=0;k<point_num;k++){
            if(node[k].getName().equals(position_poit.get(voteMAX_index).getPositionName())){
                source=node[k];
                break;
            }
        }
        Log.d("fuck", "GENIUS( "+source.getX()+" , "+source.getY()+" )");
        return source;
    }

    private void setMarker(){
        if(sourceNode.getFloor()==floor_now){
            markerview.setX((float) (sourceNode.getX()*widthOffset)-markerview.getWidth()/2);
            markerview.setY((float) (sourceNode.getY()*heightOffset)-markerview.getHeight()/2);
            Log.d("tot","pos:("+sourceNode.getX()+","+sourceNode.getY()+")~floor~"+sourceNode.getFloor()+"~floor_now~"+floor_now);
        }else{
            markerview.setX((float) (999*widthOffset));
            markerview.setY((float) (999*heightOffset));
            Log.d("tot","pos:("+sourceNode.getX()+","+sourceNode.getY()+")~floor~"+sourceNode.getFloor()+"~floor_now~"+floor_now);
        }
    }

    private void setDestinationMarker(){
        if(navigation_destinationNode.getFloor()==floor_now){
            destinationmarker.setX((float) (navigation_destinationNode.getX()*widthOffset)-destinationMarkerWidth/2);
            destinationmarker.setY((float) (navigation_destinationNode.getY()*heightOffset)-destinationMarkerHeight/2);
            Log.d("tot_n","pos:("+destinationMarkerHeight+","+destinationMarkerWidth+")~floor~"+navigation_destinationNode.getFloor()+"~floor_now~"+floor_now);
            Log.d("tot_n","pos:("+destinationmarker.getHeight()+","+destinationmarker.getWidth()+")~floor~");
        }else{
            destinationmarker.setX((float) (999*widthOffset));
            destinationmarker.setY((float) (999*heightOffset));
            Log.d("tot","pos:("+navigation_destinationNode.getX()+","+navigation_destinationNode.getY()+")~floor~"+navigation_destinationNode.getFloor()+"~floor_now~"+floor_now);
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

        /*if(myView.getImageName()=="iim_1f"){
            myView.setImageName("project2");
            floor_now=2;
            drawLine.update();
        }else if(myView.getImageName()=="project2"){
            myView.setImageName("iim_1f");
            floor_now=1;
            drawLine.update();
        }*/
        //setMarker();
        //setDestinationMarker();

    }

    private void initView() {

        markerview=findViewById(R.id.mapmaker);
        destinationmarker=findViewById(R.id.destinationmrker);
        Log.d("tot_n1","pos:("+destinationmarker.getHeight()+","+destinationmarker.getWidth());

        //move=findViewById(R.id.move);
        myView = (MyView) findViewById(R.id.myView);
        drawLine = (DrawLine) findViewById(R.id.drawLine);
        destBtn = (Button)findViewById(R.id.destButton);
        sourceBtn = (Button)findViewById(R.id.sourceBtn);
        searchBtn=findViewById(R.id.search);
        exchangeBtn=findViewById(R.id.exchangeBtn);
        floor_list=findViewById(R.id.floorlist);
        retargetBtn=findViewById(R.id.retarget);
        groupView = findViewById(R.id.groupView);
        bigGroupView = findViewById(R.id.bigGroup);
        //now_location_view=findViewById(R.id.now_location);

    }

    public double pointDistance(double x1, double y1, double x2, double y2){
        return Math.pow((Math.pow((x1-x2),2)+Math.pow((y1-y2),2)),0.5);
    }

    public int findMin(Circle[] c){
        int min =0;
        for(int i=1;i<3;i++){
            if(c[i].getR()<c[min].getR()){
                min=i;
            }
        }

        return min;//回傳最小值的index
    }

    public int find_max_rssi(int[] c){
        int max_idx =0;
        for(int i=1;i<c.length;i++){
            if(c[i]>c[max_idx]){
                max_idx=i;
            }
        }

        return max_idx;//回傳最小值的index
    }

    public int[] find_3_max(List<Beacon> beacons){
        int[] max={0,0,0};
        int beacon_size=beacons.size();
        int[] beacon_rssi=new int[beacon_size];

        for(int i=0;i<3;i++){
            max[i]=find_max_rssi(beacon_rssi);
            beacon_rssi[max[i]]=-9999;
        }

        Log.d(TAG, "find_3_max: "+max[0]+";"+max[1]+";"+max[2]);
        return max;

    }

    public int[] findMAX(Circle[] c){
        int i=findMin(c);
        int[] max=new int[2];
        int h=0;
        for(int r=0;r<3;r++){
            if(r!=i){
                max[h]=r;
                h++;
            }
        }


        return max;
    }

    public double[] intersect(double x1,double x2,double y1,double y2,double r1,double r2){

        // 在一元二次方程中 a*x^2+b*x+c=0
        double a,b,c;

        //x的两个根 x_1 , x_2
        //y的两个根 y_1 , y_2
        double x_1 = 0,x_2=0,y_1=0,y_2=0;

        //判别式的值
        double delta = -1;

        //如果 y1!=y2
        if(y1!=y2){

            //为了方便代入
            double A = (x1*x1 - x2*x2 +y1*y1 - y2*y2 + r2*r2 - r1*r1)/(2*(y1-y2));
            double B = (x1-x2)/(y1-y2);

            a = 1 + B * B;
            b = -2 * (x1 + (A-y1)*B);
            c = x1*x1 + (A-y1)*(A-y1) - r1*r1;

            //下面使用判定式 判断是否有解
            delta=b*b-4*a*c;

            if(delta >0)
            {

                x_1=(-b+Math.sqrt(b*b-4*a*c))/(2*a);
                x_2=(-b-Math.sqrt(b*b-4*a*c))/(2*a);
                y_1 = A - B*x_1;
                y_2 = A - B*x_2;
            }
            else if(delta ==0)
            {
                x_1 = x_2 = -b/(2*a);
                y_1 = y_2 = A - B*x_1;
            }else
            {
                //System.err.println("两个圆不相交");
                Log.d(TAG, "FUCK111, GIVE ME ARRAY " + new double[]{x_1,y_1,x_2,y_2});
                return null;
            }
        }
        else if(x1!=x2){

            //当y1=y2时，x的两个解相等
            x_1 = x_2 = (x1*x1 - x2*x2 + r2*r2 - r1*r1)/(2*(x1-x2));

            a = 1 ;
            b = -2*y1;
            c = y1*y1 - r1*r1 + (x_1-x1)*(x_1-x1);

            delta=b*b-4*a*c;

            if(delta >0)
            {
                y_1 = (-b+Math.sqrt(b*b-4*a*c))/(2*a);
                y_2 = (-b-Math.sqrt(b*b-4*a*c))/(2*a);
            }
            else if(delta ==0)
            {
                y_1=y_2=-b/(2*a);
            }else
            {
                Log.d(TAG, "FUCK111, GIVE ME ARRAY " + new double[]{x_1,y_1,x_2,y_2});
                //System.err.println("两个圆不相交");
                return null;
            }
        }
        else
        {
            Log.d(TAG, "FUCK222, GIVE ME ARRAY " + new double[]{x_1,y_1,x_2,y_2});
            //System.out.println("无解");
            return null;
        }

        Log.d(TAG, "FUCK333, GIVE ME ARRAY " + new double[]{x_1,y_1,x_2,y_2});
        return new double[]{x_1,y_1,x_2,y_2};
    }


    public void startScan(){

        Log.d(TAG,"startScanning");
        Log.d("start","beacon_number"+beacon_number);
        beaconManager.startMonitoring(ALL_BEACONS_REGION);
        beaconManager.startRanging(ALL_BEACONS_REGION);
        /*
        for(int i=0;i<beacon_number;i++) {
            beaconManager.startMonitoring(beaconRegions[i]);
            beaconManager.startRanging(beaconRegions[i]);
        }*/
    }

    public void stopScan(){

        Log.d(TAG,"stopMonitoring");
        beaconManager.stopMonitoring(ALL_BEACONS_REGION.getIdentifier());
        beaconManager.stopRanging(ALL_BEACONS_REGION);
        floor_user=true;
        /*
        for(int i=0;i<beacon_number;i++) {
            beaconManager.stopMonitoring(beaconRegions[i].getIdentifier());
            beaconManager.stopRanging(beaconRegions[i]);
        }*/

    }

    /*@Override
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
    }*/

    @Override
    public void onBackPressed()
    {   // 按了 Android 裝置的實體返回鍵
        // 準備回傳的資料


        Intent mIntent = getIntent();
        mIntent.putExtra("ListItemContent", destBtn.getText());
        setResult(REQUEST_CODE_DEST, mIntent);
        Log.d("leosu", "onBackPressed");
        navigation_destinationNode.setShortestPath(new LinkedList<Node>());//路線歸零

        super.onBackPressed();
    }

}
