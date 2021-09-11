package com.yudiz.beacondemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class PositionDB {
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private PositionDBHelper mHelper;
    private final static String TB_D="TBDES";//目標資料表
    private final static String TB_P="TBPOI";//定位點資料表
    private final static String TB_B="TBBEA";//BEACON資料表
    private final static String TB_C="TBCON";//點點連結資料表

    public PositionDB(Context context) {
        mContext = context;
    }

    public void open() throws SQLException {
        mHelper = new PositionDBHelper(mContext);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void close() {
        mHelper.close();
    }

    public ContentValues add_destination(String destination, String point) {
        ContentValues values=new ContentValues();
        //values.put("_classroom",classroom.toString());//載入資料系k
        //values.put("_point",point.toString());//載入資料a1
        values.put("_destination",destination.toString());//載入資料0
        values.put("_point",point.toString());//載入資料30

        return values;

    }

    public ContentValues add_point(String point, String x,String y,String major,String vote) {
        ContentValues values=new ContentValues();
        values.put("_point",point.toString());//載入資料系k
        values.put("_x",x.toString());//載入資料a1
        values.put("_y",y.toString());//載入資料0
        values.put("_major",major.toString());//載入資料30
        values.put("_vote",vote.toString());

        return values;
    }

    public ContentValues add_beacon(String uuid, String major,String minor,String x,String y,String r0,String n,String nearest_point,String right_point,String left_point) {
        ContentValues values=new ContentValues();
        values.put("_uuid",uuid.toString());//載入資料系k
        values.put("_major",major.toString());//載入資料a1
        values.put("_minor",minor.toString());//載入資料0
        values.put("_x",x.toString());//載入資料30
        values.put("_y",y.toString());
        values.put("_r0",r0.toString());
        values.put("_n",n.toString());
        values.put("_np",nearest_point.toString());
        values.put("_rp",right_point.toString());
        values.put("_lp",left_point.toString());

        return values;
    }

    public ContentValues add_connection(String start, String connect_1,String connect_2,String connect_3,String connect_4) {
        ContentValues values=new ContentValues();
        //values.put("_classroom",classroom.toString());//載入資料系k
        //values.put("_point",point.toString());//載入資料a1
        values.put("_start",start.toString());//載入資料0
        values.put("_connect_1",connect_1.toString());//載入資料30
        values.put("_connect_2",connect_2.toString());
        values.put("_connect_3",connect_3.toString());
        values.put("_connect_4",connect_4.toString());

        return values;
    }

   /* public void deleteAll(String TABLE_NAME){
        mDatabase.delete(TABLE_NAME, null, null);
    }*/


    /*public long addRow(ContentValues values) {
        return mDatabase.insert(TodoEntry.TABLE_NAME, null, values);
    }*/

    /*public boolean deleteRow(long rowId) {
        return mDatabase.delete(TodoEntry.TABLE_NAME,
                TodoEntry._ID + "=" + rowId, null) > 0;
    }*/

    /*public boolean updateRow(long rowId, ContentValues values) {
        return mDatabase.update(TodoEntry.TABLE_NAME, values,
                TodoEntry._ID + "=" + rowId, null) > 0;
    }*/

    /*private static String[] columns = {
            TodoEntry._ID,
            TodoEntry.COLUMN_TITLE,
            TodoEntry.COLUMN_BODY,
            TodoEntry._ID
    };*/

    public Cursor destinationDestinationQuaryAll(){
        return mDatabase.query(TB_D,
                new String[]{"_id", "_destination", "_point"},
                null, null, null, null, null,null);

    }

    public Cursor pointQueryAll() {
        return mDatabase.query(TB_P,
                new String[]{"_id", "_point", "_x", "_y", "_major","_vote"},
                null, null, null, null, null,null);
    }

    public Cursor beaconQueryAll() {
        return mDatabase.query(TB_B,
                new String[]{"_id","_uuid", "_major", "_minor", "_x", "_y", "_r0", "_n","_np","_rp","_lp"},
                null, null, null, null, null);
    }

    public Cursor connectQueryAll() {
        return mDatabase.query(TB_C,
                new String[]{"_id","_start", "_connect_1","_connect_2","_connect_3","_connect_4"},
                null, null, null, null, null);
    }



    /*public Cursor query(long rowId) throws SQLException {
        Cursor cursor = mDatabase.query(true, TodoEntry.TABLE_NAME, columns,
                TodoEntry._ID + "=" + rowId, null, null,
                null, null, null);
        cursor.moveToFirst();
        return cursor;
    }*/

    /*ContentValues createContentValues(String title, String body, int state) {
        ContentValues values = new ContentValues();
        values.put(TodoEntry.COLUMN_TITLE, title);
        values.put(TodoEntry.COLUMN_BODY, body);
        values.put(TodoEntry.COLUMN_STATE, state);
        return values;
    }*/

    /*public static abstract class TodoEntry implements BaseColumns {
        public static final String TABLE_NAME = "todo";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_STATE = "state";
        public static final int INDEX_ID = 0;
        public static final int INDEX_TITLE = 1;
        public static final int INDEX_BODY = 2;
        public static final int INDEX_STATE = 3;
    }*/

    public class PositionDBHelper extends SQLiteOpenHelper {

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "positionIIM.db";//資料庫


        /*private static final String SQL_CREATE_ENTRIES =
                "create table " + TodoEntry.TABLE_NAME + "(" +
                        TodoEntry._ID + " integer primary key autoincrement, " +
                        TodoEntry.COLUMN_TITLE + " text not null, " +
                        TodoEntry.COLUMN_BODY + " text not null, " +
                        TodoEntry.COLUMN_STATE + " integer " +
                        ");";*/

        public PositionDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String SQL="CREATE TABLE IF NOT EXISTS "+TB_D+"" +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT ,_destination VARCHAR(50),_point VARCHAR(10))";
            db.execSQL(SQL);


            db.insert(TB_D,null,add_destination("61100 (系K)","a134"));
            db.insert(TB_D,null,add_destination("61100A","a110"));
            db.insert(TB_D,null,add_destination("61101F","a110"));
            db.insert(TB_D,null,add_destination("廁所一A","a116"));
            db.insert(TB_D,null,add_destination("61102B","a93"));
            db.insert(TB_D,null,add_destination("樓梯一A","a101"));
            db.insert(TB_D,null,add_destination("61101B","a84"));
            db.insert(TB_D,null,add_destination("61103F","a84"));
            db.insert(TB_D,null,add_destination("61102F","a79"));
            db.insert(TB_D,null,add_destination("61104B","a79"));
            db.insert(TB_D,null,add_destination("61103B","a73"));
            db.insert(TB_D,null,add_destination("61104F","a67"));
            db.insert(TB_D,null,add_destination("61106","a67"));
            db.insert(TB_D,null,add_destination("61108","a54"));
            db.insert(TB_D,null,add_destination("樓梯一B(出口)","a55"));
            db.insert(TB_D,null,add_destination("樓梯一B(上二樓)","a56"));
            db.insert(TB_D,null,add_destination("圓桌","a49"));
            db.insert(TB_D,null,add_destination("61X06","a44"));
            db.insert(TB_D,null,add_destination("61110(系會辦)","a44"));
            db.insert(TB_D,null,add_destination("61112","a38"));
            db.insert(TB_D,null,add_destination("61114","a36"));
            db.insert(TB_D,null,add_destination("61116","a33"));
            db.insert(TB_D,null,add_destination("61118","a30"));
            db.insert(TB_D,null,add_destination("61105","a29"));
            db.insert(TB_D,null,add_destination("61107","a27"));
            db.insert(TB_D,null,add_destination("61120","a26"));
            db.insert(TB_D,null,add_destination("61122","a24"));
            db.insert(TB_D,null,add_destination("61109","a22"));
            db.insert(TB_D,null,add_destination("61111","a21"));
            db.insert(TB_D,null,add_destination("61124","a20"));
            db.insert(TB_D,null,add_destination("61113","a8"));
            db.insert(TB_D,null,add_destination("61115","a8"));
            db.insert(TB_D,null,add_destination("廁所一B","a15"));
            db.insert(TB_D,null,add_destination("樓梯一C(出口)","a16"));
            db.insert(TB_D,null,add_destination("樓梯一C(上二樓)","a17"));
            db.insert(TB_D,null,add_destination("61117","a1"));
            db.insert(TB_D,null,add_destination("61119","a1"));
            db.insert(TB_D,null,add_destination("61200","b8"));
            db.insert(TB_D,null,add_destination("小電腦教室","b12"));
            db.insert(TB_D,null,add_destination("主機室","b21"));
            db.insert(TB_D,null,add_destination("廁所二A","b25"));
            db.insert(TB_D,null,add_destination("樓梯二A上","b28"));
            db.insert(TB_D,null,add_destination("樓梯二A下","b29"));
            db.insert(TB_D,null,add_destination("大電腦教室","b36"));
            db.insert(TB_D,null,add_destination("61202","b36"));
            db.insert(TB_D,null,add_destination("61204","b43"));
            db.insert(TB_D,null,add_destination("61206B","b45"));
            db.insert(TB_D,null,add_destination("61206F","b51"));
            db.insert(TB_D,null,add_destination("空調室","b52"));
            db.insert(TB_D,null,add_destination("61208B","b53"));
            db.insert(TB_D,null,add_destination("61208F","b61"));
            db.insert(TB_D,null,add_destination("61210","b69"));
            db.insert(TB_D,null,add_destination("樓梯二B上","b75"));
            db.insert(TB_D,null,add_destination("樓梯二B下","b76"));
            db.insert(TB_D,null,add_destination("61212","b84"));
            db.insert(TB_D,null,add_destination("61214","b84"));
            db.insert(TB_D,null,add_destination("61216","b81"));
            db.insert(TB_D,null,add_destination("61218","b88"));
            db.insert(TB_D,null,add_destination("61205","b99"));
            db.insert(TB_D,null,add_destination("61220","b94"));
            db.insert(TB_D,null,add_destination("61222","b97"));
            db.insert(TB_D,null,add_destination("61224","b100"));
            db.insert(TB_D,null,add_destination("61226","b104"));
            db.insert(TB_D,null,add_destination("61203","b96"));
            db.insert(TB_D,null,add_destination("61205","b99"));
            db.insert(TB_D,null,add_destination("61207","b102"));
            db.insert(TB_D,null,add_destination("61209","b105"));
            db.insert(TB_D,null,add_destination("61211","b108"));
            db.insert(TB_D,null,add_destination("61213","b111"));
            db.insert(TB_D,null,add_destination("61215","b114"));
            db.insert(TB_D,null,add_destination("61217","b114"));
            db.insert(TB_D,null,add_destination("廁所二B","b120"));
            db.insert(TB_D,null,add_destination("樓梯二C上","b121"));
            db.insert(TB_D,null,add_destination("樓梯二C下","b122"));
            db.insert(TB_D,null,add_destination("樓梯三A","c115"));
            db.insert(TB_D,null,add_destination("廁所三A","c1"));
            db.insert(TB_D,null,add_destination("61301","c9"));
            db.insert(TB_D,null,add_destination("61302","c23"));
            db.insert(TB_D,null,add_destination("61303","c12"));
            db.insert(TB_D,null,add_destination("61304","c32"));
            db.insert(TB_D,null,add_destination("61305","c15"));
            db.insert(TB_D,null,add_destination("61306","c42"));
            db.insert(TB_D,null,add_destination("61307","c18"));
            db.insert(TB_D,null,add_destination("61308","c50"));
            db.insert(TB_D,null,add_destination("61309","c21"));
            db.insert(TB_D,null,add_destination("61311","c24"));
            db.insert(TB_D,null,add_destination("61312","c63"));
            db.insert(TB_D,null,add_destination("61313","c27"));
            db.insert(TB_D,null,add_destination("61314","c65"));
            db.insert(TB_D,null,add_destination("61315","c30"));
            db.insert(TB_D,null,add_destination("61316","c67"));
            db.insert(TB_D,null,add_destination("61317","c33"));
            db.insert(TB_D,null,add_destination("61318","c69"));
            db.insert(TB_D,null,add_destination("61319","c36"));
            db.insert(TB_D,null,add_destination("61320","c73"));
            db.insert(TB_D,null,add_destination("61321","c39"));
            db.insert(TB_D,null,add_destination("樓梯三B","c118"));
            db.insert(TB_D,null,add_destination("61322","c78"));
            db.insert(TB_D,null,add_destination("61323","c73"));
            db.insert(TB_D,null,add_destination("61324","c81"));
            db.insert(TB_D,null,add_destination("61325","c82"));
            db.insert(TB_D,null,add_destination("61326","c85"));
            db.insert(TB_D,null,add_destination("61327","c87"));
            db.insert(TB_D,null,add_destination("61328","c89"));
            db.insert(TB_D,null,add_destination("61329","c91"));
            db.insert(TB_D,null,add_destination("61330","c91"));
            db.insert(TB_D,null,add_destination("61331","c94"));
            db.insert(TB_D,null,add_destination("61333","c98"));
            db.insert(TB_D,null,add_destination("61335","c103"));
            db.insert(TB_D,null,add_destination("61337","c103"));
            db.insert(TB_D,null,add_destination("樓梯三C","c112"));
            db.insert(TB_D,null,add_destination("廁所三B","c109"));





            SQL="CREATE TABLE IF NOT EXISTS "+TB_P+"" +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT ,_point VARCHAR(50),_x VARCHAR(10),_y VARCHAR(10),_major VARCHAR(10),_vote VARCHAR(10))";
            db.execSQL(SQL);


            db.insert(TB_P,null,add_point("a1","5","77","1","1"));
            db.insert(TB_P,null,add_point("a2","5","78","1","2"));
            db.insert(TB_P,null,add_point("a3","5","79","1","2"));
            db.insert(TB_P,null,add_point("a4","6","79","1","2"));
            db.insert(TB_P,null,add_point("a5","7","79","1","2"));
            db.insert(TB_P,null,add_point("a6","8","79","1","3"));
            db.insert(TB_P,null,add_point("a7","9","79","1","3"));
            db.insert(TB_P,null,add_point("a8","10","79","1","4"));
            db.insert(TB_P,null,add_point("a9","11","79","1","4"));
            db.insert(TB_P,null,add_point("a10","11","80","1","3"));
            db.insert(TB_P,null,add_point("a11","11","81","1","2"));
            db.insert(TB_P,null,add_point("a12","11","82","1","3"));
            db.insert(TB_P,null,add_point("a13","11","83","1","3"));
            db.insert(TB_P,null,add_point("a14","11","84","1","3"));
            db.insert(TB_P,null,add_point("a15","11","85","1","2"));
            db.insert(TB_P,null,add_point("a16","8","80","1","1"));
            db.insert(TB_P,null,add_point("a17","8","84","1","1"));
            db.insert(TB_P,null,add_point("a18","12","79","1","3"));
            db.insert(TB_P,null,add_point("a19","13","79","1","2"));
            db.insert(TB_P,null,add_point("a20","14","79","1","3"));
            db.insert(TB_P,null,add_point("a21","15","79","1","3"));
            db.insert(TB_P,null,add_point("a22","16","79","1","3"));
            db.insert(TB_P,null,add_point("a23","17","79","1","3"));
            db.insert(TB_P,null,add_point("a24","18","79","1","3"));
            db.insert(TB_P,null,add_point("a25","19","79","1","3"));
            db.insert(TB_P,null,add_point("a26","20","79","1","3"));
            db.insert(TB_P,null,add_point("a27","21","79","1","3"));
            db.insert(TB_P,null,add_point("a28","22","79","1","3"));
            db.insert(TB_P,null,add_point("a29","23","79","1","3"));
            db.insert(TB_P,null,add_point("a30","24","79","1","3"));
            db.insert(TB_P,null,add_point("a31","25","79","1","3"));
            db.insert(TB_P,null,add_point("a32","26","79","1","3"));
            db.insert(TB_P,null,add_point("a33","27","79","1","3"));
            db.insert(TB_P,null,add_point("a34","28","79","1","3"));
            db.insert(TB_P,null,add_point("a35","29","79","1","3"));
            db.insert(TB_P,null,add_point("a36","30","79","1","2"));
            db.insert(TB_P,null,add_point("a37","31","79","1","3"));
            db.insert(TB_P,null,add_point("a38","32","79","1","3"));
            db.insert(TB_P,null,add_point("a39","33","79","1","3"));
            db.insert(TB_P,null,add_point("a40","34","79","1","2"));
            db.insert(TB_P,null,add_point("a41","35","79","1","2"));
            db.insert(TB_P,null,add_point("a42","36","79","1","2"));
            db.insert(TB_P,null,add_point("a43","37","79","1","2"));
            db.insert(TB_P,null,add_point("a44","38","79","1","1"));
            db.insert(TB_P,null,add_point("a45","33","78","1","5"));
            db.insert(TB_P,null,add_point("a46","33","77","1","2"));
            db.insert(TB_P,null,add_point("a47","33","76","1","3"));
            db.insert(TB_P,null,add_point("a48","33","75","1","5"));
            db.insert(TB_P,null,add_point("a49","33","74","1","5"));
            db.insert(TB_P,null,add_point("a50","33","73","1","5"));
            db.insert(TB_P,null,add_point("a51","33","72","1","3"));
            db.insert(TB_P,null,add_point("a52","33","71","1","3"));
            db.insert(TB_P,null,add_point("a53","33","70","1","3"));
            db.insert(TB_P,null,add_point("a54","33","69","1","3"));
            db.insert(TB_P,null,add_point("a55","37","72","1","10"));
            db.insert(TB_P,null,add_point("a56","37","76","1","10"));
            db.insert(TB_P,null,add_point("a57","35","68","1","10"));
            db.insert(TB_P,null,add_point("a58","35","67","1","10"));
            db.insert(TB_P,null,add_point("a59","35","66","1","10"));
            db.insert(TB_P,null,add_point("a60","35","65","1","10"));
            db.insert(TB_P,null,add_point("a61","35","64","1","10"));
            db.insert(TB_P,null,add_point("a62","35","63","1","10"));
            db.insert(TB_P,null,add_point("a63","35","62","1","10"));
            db.insert(TB_P,null,add_point("a64","35","61","1","10"));
            db.insert(TB_P,null,add_point("a65","35","60","1","10"));
            db.insert(TB_P,null,add_point("a66","35","59","1","10"));
            db.insert(TB_P,null,add_point("a67","35","58","1","10"));
            db.insert(TB_P,null,add_point("a68","35","57","1","10"));
            db.insert(TB_P,null,add_point("a69","35","56","1","10"));
            db.insert(TB_P,null,add_point("a70","35","55","1","10"));
            db.insert(TB_P,null,add_point("a71","35","54","1","10"));
            db.insert(TB_P,null,add_point("a72","35","53","1","10"));
            db.insert(TB_P,null,add_point("a73","35","52","1","10"));
            db.insert(TB_P,null,add_point("a74","35","51","1","10"));
            db.insert(TB_P,null,add_point("a75","35","50","1","10"));
            db.insert(TB_P,null,add_point("a76","35","49","1","10"));
            db.insert(TB_P,null,add_point("a77","35","48","1","10"));
            db.insert(TB_P,null,add_point("a78","35","47","1","10"));
            db.insert(TB_P,null,add_point("a79","35","46","1","10"));
            db.insert(TB_P,null,add_point("a80","35","45","1","10"));
            db.insert(TB_P,null,add_point("a81","35","44","1","10"));
            db.insert(TB_P,null,add_point("a82","35","43","1","10"));
            db.insert(TB_P,null,add_point("a83","35","42","1","10"));
            db.insert(TB_P,null,add_point("a84","35","41","1","10"));
            db.insert(TB_P,null,add_point("a85","35","40","1","10"));
            db.insert(TB_P,null,add_point("a86","35","39","1","10"));
            db.insert(TB_P,null,add_point("a87","35","38","1","10"));
            db.insert(TB_P,null,add_point("a88","35","37","1","10"));
            db.insert(TB_P,null,add_point("a89","35","36","1","10"));
            db.insert(TB_P,null,add_point("a90","35","35","1","10"));
            db.insert(TB_P,null,add_point("a91","35","34","1","10"));
            db.insert(TB_P,null,add_point("a92","35","33","1","10"));
            db.insert(TB_P,null,add_point("a93","35","32","1","10"));
            db.insert(TB_P,null,add_point("a94","36","32","1","10"));
            db.insert(TB_P,null,add_point("a95","37","32","1","10"));
            db.insert(TB_P,null,add_point("a96","38","32","1","10"));
            db.insert(TB_P,null,add_point("a97","39","32","1","10"));
            db.insert(TB_P,null,add_point("a98","40","32","1","10"));
            db.insert(TB_P,null,add_point("a99","40","31","1","10"));
            db.insert(TB_P,null,add_point("a100","40","30","1","10"));
            db.insert(TB_P,null,add_point("a101","40","29","1","10"));
            db.insert(TB_P,null,add_point("a102","35","31","1","10"));
            db.insert(TB_P,null,add_point("a103","35","30","1","10"));
            db.insert(TB_P,null,add_point("a104","35","29","1","10"));
            db.insert(TB_P,null,add_point("a105","35","28","1","10"));
            db.insert(TB_P,null,add_point("a106","35","27","1","10"));
            db.insert(TB_P,null,add_point("a107","35","26","1","10"));
            db.insert(TB_P,null,add_point("a108","35","25","1","10"));
            db.insert(TB_P,null,add_point("a109","35","24","1","10"));
            db.insert(TB_P,null,add_point("a110","35","23","1","10"));
            db.insert(TB_P,null,add_point("a111","36","23","1","10"));
            db.insert(TB_P,null,add_point("a112","37","23","1","10"));
            db.insert(TB_P,null,add_point("a113","38","23","1","10"));
            db.insert(TB_P,null,add_point("a114","39","23","1","10"));
            db.insert(TB_P,null,add_point("a115","40","23","1","10"));
            db.insert(TB_P,null,add_point("a116","41","23","1","10"));
            db.insert(TB_P,null,add_point("a117","35","22","1","10"));
            db.insert(TB_P,null,add_point("a118","35","21","1","10"));
            db.insert(TB_P,null,add_point("a119","35","20","1","10"));
            db.insert(TB_P,null,add_point("a120","35","19","1","10"));
            db.insert(TB_P,null,add_point("a121","35","18","1","10"));
            db.insert(TB_P,null,add_point("a122","35","17","1","10"));
            db.insert(TB_P,null,add_point("a123","35","16","1","10"));
            db.insert(TB_P,null,add_point("a124","35","15","1","10"));
            db.insert(TB_P,null,add_point("a125","35","14","1","10"));
            db.insert(TB_P,null,add_point("a126","35","13","1","10"));
            db.insert(TB_P,null,add_point("a127","35","12","1","10"));
            db.insert(TB_P,null,add_point("a128","35","11","1","10"));
            db.insert(TB_P,null,add_point("a129","35","10","1","10"));
            db.insert(TB_P,null,add_point("a130","35","9","1","10"));
            db.insert(TB_P,null,add_point("a131","35","8","1","10"));
            db.insert(TB_P,null,add_point("a132","35","7","1","10"));
            db.insert(TB_P,null,add_point("a133","35","6","1","10"));
            db.insert(TB_P,null,add_point("a134","35","5","1","10"));

            db.insert(TB_P,null,add_point("b1","35","9","2","10"));
            db.insert(TB_P,null,add_point("b2","35","10","2","10"));
            db.insert(TB_P,null,add_point("b3","35","11","2","10"));
            db.insert(TB_P,null,add_point("b4","35","12","2","10"));
            db.insert(TB_P,null,add_point("b5","35","13","2","10"));
            db.insert(TB_P,null,add_point("b6","35","14","2","10"));
            db.insert(TB_P,null,add_point("b7","35","15","2","10"));
            db.insert(TB_P,null,add_point("b8","35","16","2","10"));
            db.insert(TB_P,null,add_point("b9","35","17","2","10"));
            db.insert(TB_P,null,add_point("b10","35","18","2","10"));
            db.insert(TB_P,null,add_point("b11","35","19","2","10"));
            db.insert(TB_P,null,add_point("b12","35","20","2","10"));
            db.insert(TB_P,null,add_point("b13","35","21","2","10"));
            db.insert(TB_P,null,add_point("b14","35","22","2","10"));
            db.insert(TB_P,null,add_point("b15","35","23","2","10"));
            db.insert(TB_P,null,add_point("b16","35","24","2","10"));
            db.insert(TB_P,null,add_point("b17","35","25","2","10"));
            db.insert(TB_P,null,add_point("b18","35","26","2","10"));
            db.insert(TB_P,null,add_point("b19","35","27","2","10"));
            db.insert(TB_P,null,add_point("b20","35","28","2","10"));
            db.insert(TB_P,null,add_point("b21","35","29","2","10"));
            db.insert(TB_P,null,add_point("b22","36","29","2","10"));
            db.insert(TB_P,null,add_point("b23","37","29","2","10"));
            db.insert(TB_P,null,add_point("b24","38","29","2","10"));
            db.insert(TB_P,null,add_point("b25","39","29","2","10"));
            db.insert(TB_P,null,add_point("b26","40","29","2","10"));
            db.insert(TB_P,null,add_point("b27","41","29","2","10"));
            db.insert(TB_P,null,add_point("b28","39","26","2","10"));
            db.insert(TB_P,null,add_point("b29","37","26","2","10"));
            db.insert(TB_P,null,add_point("b30","35","30","2","10"));
            db.insert(TB_P,null,add_point("b31","35","31","2","10"));
            db.insert(TB_P,null,add_point("b32","35","32","2","10"));
            db.insert(TB_P,null,add_point("b33","35","33","2","10"));
            db.insert(TB_P,null,add_point("b34","35","34","2","10"));
            db.insert(TB_P,null,add_point("b35","35","35","2","10"));
            db.insert(TB_P,null,add_point("b36","35","36","2","10"));
            db.insert(TB_P,null,add_point("b37","35","37","2","10"));
            db.insert(TB_P,null,add_point("b38","35","38","2","10"));
            db.insert(TB_P,null,add_point("b39","35","39","2","10"));
            db.insert(TB_P,null,add_point("b40","35","40","2","10"));
            db.insert(TB_P,null,add_point("b41","35","41","2","10"));
            db.insert(TB_P,null,add_point("b42","35","42","2","10"));
            db.insert(TB_P,null,add_point("b43","35","43","2","10"));
            db.insert(TB_P,null,add_point("b44","35","44","2","10"));
            db.insert(TB_P,null,add_point("b45","35","45","2","10"));
            db.insert(TB_P,null,add_point("b46","35","46","2","10"));
            db.insert(TB_P,null,add_point("b47","35","47","2","10"));
            db.insert(TB_P,null,add_point("b48","35","48","2","10"));
            db.insert(TB_P,null,add_point("b49","35","49","2","10"));
            db.insert(TB_P,null,add_point("b50","35","50","2","10"));
            db.insert(TB_P,null,add_point("b51","35","51","2","10"));
            db.insert(TB_P,null,add_point("b52","35","52","2","10"));
            db.insert(TB_P,null,add_point("b53","35","53","2","10"));
            db.insert(TB_P,null,add_point("b54","35","54","2","10"));
            db.insert(TB_P,null,add_point("b55","35","55","2","10"));
            db.insert(TB_P,null,add_point("b56","35","56","2","10"));
            db.insert(TB_P,null,add_point("b57","35","57","2","10"));
            db.insert(TB_P,null,add_point("b58","35","58","2","10"));
            db.insert(TB_P,null,add_point("b59","35","59","2","10"));
            db.insert(TB_P,null,add_point("b60","35","60","2","10"));
            db.insert(TB_P,null,add_point("b61","35","61","2","10"));
            db.insert(TB_P,null,add_point("b62","35","62","2","10"));
            db.insert(TB_P,null,add_point("b63","35","63","2","10"));
            db.insert(TB_P,null,add_point("b64","35","64","2","10"));
            db.insert(TB_P,null,add_point("b65","35","65","2","10"));
            db.insert(TB_P,null,add_point("b66","35","66","2","10"));
            db.insert(TB_P,null,add_point("b67","35","67","2","10"));
            db.insert(TB_P,null,add_point("b68","35","68","2","10"));
            db.insert(TB_P,null,add_point("b69","35","69","2","10"));
            db.insert(TB_P,null,add_point("b70","35","70","2","10"));
            db.insert(TB_P,null,add_point("b71","35","71","2","10"));
            db.insert(TB_P,null,add_point("b72","35","72","2","10"));
            db.insert(TB_P,null,add_point("b73","35","73","2","10"));
            db.insert(TB_P,null,add_point("b74","35","74","2","10"));
            db.insert(TB_P,null,add_point("b75","38","73","2","10"));
            db.insert(TB_P,null,add_point("b76","38","75","2","10"));
            db.insert(TB_P,null,add_point("b77","35","75","2","10"));
            db.insert(TB_P,null,add_point("b78","35","79","2","10"));
            db.insert(TB_P,null,add_point("b79","35","77","2","10"));
            db.insert(TB_P,null,add_point("b80","35","78","2","10"));
            db.insert(TB_P,null,add_point("b81","35","79","2","10"));
            db.insert(TB_P,null,add_point("b82","36","79","2","10"));
            db.insert(TB_P,null,add_point("b83","37","79","2","10"));
            db.insert(TB_P,null,add_point("b84","38","79","2","10"));
            db.insert(TB_P,null,add_point("b85","34","79","2","10"));
            db.insert(TB_P,null,add_point("b86","33","79","2","10"));
            db.insert(TB_P,null,add_point("b87","32","79","2","10"));
            db.insert(TB_P,null,add_point("b88","31","79","2","10"));
            db.insert(TB_P,null,add_point("b89","30","79","2","10"));
            db.insert(TB_P,null,add_point("b90","29","79","2","10"));
            db.insert(TB_P,null,add_point("b91","28","79","2","10"));
            db.insert(TB_P,null,add_point("b92","27","79","2","10"));
            db.insert(TB_P,null,add_point("b93","26","79","2","10"));
            db.insert(TB_P,null,add_point("b94","25","79","2","10"));
            db.insert(TB_P,null,add_point("b95","24","79","2","10"));
            db.insert(TB_P,null,add_point("b96","23","79","2","10"));
            db.insert(TB_P,null,add_point("b97","22","79","2","10"));
            db.insert(TB_P,null,add_point("b98","21","79","2","10"));
            db.insert(TB_P,null,add_point("b99","20","79","2","10"));
            db.insert(TB_P,null,add_point("b100","19","79","2","10"));
            db.insert(TB_P,null,add_point("b101","18","79","2","10"));
            db.insert(TB_P,null,add_point("b102","17","79","2","10"));
            db.insert(TB_P,null,add_point("b103","16","79","2","10"));
            db.insert(TB_P,null,add_point("b104","15","79","2","10"));
            db.insert(TB_P,null,add_point("b105","14","79","2","10"));

            db.insert(TB_P,null,add_point("b106","13","79","2","10"));
            db.insert(TB_P,null,add_point("b107","12","79","2","10"));
            db.insert(TB_P,null,add_point("b108","11","79","2","10"));
            db.insert(TB_P,null,add_point("b109","10","79","2","10"));
            db.insert(TB_P,null,add_point("b110","9","79","2","10"));
            db.insert(TB_P,null,add_point("b111","8","79","2","10"));
            db.insert(TB_P,null,add_point("b112","7","79","2","10"));
            db.insert(TB_P,null,add_point("b113","6","79","2","10"));
            db.insert(TB_P,null,add_point("b114","5","79","2","10"));
            db.insert(TB_P,null,add_point("b115","11","80","2","10"));
            db.insert(TB_P,null,add_point("b116","11","81","2","10"));
            db.insert(TB_P,null,add_point("b117","11","82","2","10"));
            db.insert(TB_P,null,add_point("b118","11","83","2","10"));
            db.insert(TB_P,null,add_point("b119","11","84","2","10"));
            db.insert(TB_P,null,add_point("b120","11","85","2","10"));
            db.insert(TB_P,null,add_point("b121","8","81","2","10"));
            db.insert(TB_P,null,add_point("b122","8","83","2","10"));


            db.insert(TB_P,null,add_point("c1","43","32","3","10"));
            db.insert(TB_P,null,add_point("c2","42","32","3","10"));
            db.insert(TB_P,null,add_point("c3","41","32","3","10"));
            db.insert(TB_P,null,add_point("c4","40","32","3","10"));
            db.insert(TB_P,null,add_point("c5","39","32","3","10"));
            db.insert(TB_P,null,add_point("c6","38","32","3","10"));
            db.insert(TB_P,null,add_point("c7","37","32","3","10"));
            db.insert(TB_P,null,add_point("c8","36","32","3","10"));
            db.insert(TB_P,null,add_point("c9","35","25","3","10"));
            db.insert(TB_P,null,add_point("c10","35","26","3","10"));
            db.insert(TB_P,null,add_point("c11","35","27","3","10"));
            db.insert(TB_P,null,add_point("c12","35","28","3","10"));
            db.insert(TB_P,null,add_point("c13","35","29","3","10"));
            db.insert(TB_P,null,add_point("c14","35","30","3","10"));
            db.insert(TB_P,null,add_point("c15","35","31","3","10"));
            db.insert(TB_P,null,add_point("c16","35","32","3","10"));
            db.insert(TB_P,null,add_point("c17","35","33","3","10"));
            db.insert(TB_P,null,add_point("c18","35","34","3","10"));
            db.insert(TB_P,null,add_point("c19","35","35","3","10"));
            db.insert(TB_P,null,add_point("c20","35","36","3","10"));
            db.insert(TB_P,null,add_point("c21","35","37","3","10"));
            db.insert(TB_P,null,add_point("c22","35","38","3","10"));
            db.insert(TB_P,null,add_point("c23","35","39","3","10"));
            db.insert(TB_P,null,add_point("c24","35","40","3","10"));
            db.insert(TB_P,null,add_point("c25","35","41","3","10"));
            db.insert(TB_P,null,add_point("c26","35","42","3","10"));
            db.insert(TB_P,null,add_point("c27","35","43","3","10"));
            db.insert(TB_P,null,add_point("c28","35","44","3","10"));
            db.insert(TB_P,null,add_point("c29","35","45","3","10"));
            db.insert(TB_P,null,add_point("c30","35","46","3","10"));
            db.insert(TB_P,null,add_point("c31","35","47","3","10"));
            db.insert(TB_P,null,add_point("c32","35","48","3","10"));
            db.insert(TB_P,null,add_point("c33","35","49","3","10"));
            db.insert(TB_P,null,add_point("c34","35","50","3","10"));
            db.insert(TB_P,null,add_point("c35","35","51","3","10"));
            db.insert(TB_P,null,add_point("c36","35","52","3","10"));
            db.insert(TB_P,null,add_point("c37","35","53","3","10"));
            db.insert(TB_P,null,add_point("c38","35","54","3","10"));
            db.insert(TB_P,null,add_point("c39","35","55","3","10"));
            db.insert(TB_P,null,add_point("c40","35","56","3","10"));
            db.insert(TB_P,null,add_point("c41","35","57","3","10"));
            db.insert(TB_P,null,add_point("c42","35","58","3","10"));
            db.insert(TB_P,null,add_point("c43","35","59","3","10"));
            db.insert(TB_P,null,add_point("c44","35","60","3","10"));
            db.insert(TB_P,null,add_point("c45","35","61","3","10"));
            db.insert(TB_P,null,add_point("c46","35","62","3","10"));
            db.insert(TB_P,null,add_point("c47","35","63","3","10"));
            db.insert(TB_P,null,add_point("c48","35","64","3","10"));
            db.insert(TB_P,null,add_point("c49","35","65","3","10"));
            db.insert(TB_P,null,add_point("c50","35","66","3","10"));
            db.insert(TB_P,null,add_point("c51","35","67","3","10"));
            db.insert(TB_P,null,add_point("c52","35","68","3","10"));
            db.insert(TB_P,null,add_point("c53","35","69","3","10"));
            db.insert(TB_P,null,add_point("c54","35","70","3","10"));
            db.insert(TB_P,null,add_point("c55","35","71","3","10"));
            db.insert(TB_P,null,add_point("c56","35","72","3","10"));
            db.insert(TB_P,null,add_point("c57","35","73","3","10"));
            db.insert(TB_P,null,add_point("c58","35","74","3","10"));
            db.insert(TB_P,null,add_point("c59","35","75","3","10"));
            db.insert(TB_P,null,add_point("c60","35","76","3","10"));
            db.insert(TB_P,null,add_point("c61","35","77","3","10"));
            db.insert(TB_P,null,add_point("c62","35","78","3","10"));
            db.insert(TB_P,null,add_point("c63","42","79","3","10"));
            db.insert(TB_P,null,add_point("c64","41","79","3","10"));
            db.insert(TB_P,null,add_point("c65","40","79","3","10"));
            db.insert(TB_P,null,add_point("c66","39","79","3","10"));
            db.insert(TB_P,null,add_point("c67","38","79","3","10"));
            db.insert(TB_P,null,add_point("c68","37","79","3","10"));
            db.insert(TB_P,null,add_point("c69","36","79","3","10"));
            db.insert(TB_P,null,add_point("c70","35","79","3","10"));
            db.insert(TB_P,null,add_point("c71","34","79","3","10"));
            db.insert(TB_P,null,add_point("c72","33","79","3","10"));
            db.insert(TB_P,null,add_point("c73","32","79","3","10"));
            db.insert(TB_P,null,add_point("c74","31","79","3","10"));
            db.insert(TB_P,null,add_point("c75","30","79","3","10"));
            db.insert(TB_P,null,add_point("c76","29","79","3","10"));
            db.insert(TB_P,null,add_point("c77","28","79","3","10"));
            db.insert(TB_P,null,add_point("c78","27","79","3","10"));
            db.insert(TB_P,null,add_point("c79","26","79","3","10"));
            db.insert(TB_P,null,add_point("c80","25","79","3","10"));
            db.insert(TB_P,null,add_point("c81","24","79","3","10"));
            db.insert(TB_P,null,add_point("c82","23","79","3","10"));
            db.insert(TB_P,null,add_point("c83","22","79","3","10"));
            db.insert(TB_P,null,add_point("c84","21","79","3","10"));
            db.insert(TB_P,null,add_point("c85","20","79","3","10"));
            db.insert(TB_P,null,add_point("c86","19","79","3","10"));
            db.insert(TB_P,null,add_point("c87","18","79","3","10"));
            db.insert(TB_P,null,add_point("c88","17","79","3","10"));
            db.insert(TB_P,null,add_point("c89","16","79","3","10"));
            db.insert(TB_P,null,add_point("c90","15","79","3","10"));
            db.insert(TB_P,null,add_point("c91","14","79","3","10"));
            db.insert(TB_P,null,add_point("c92","13","79","3","10"));
            db.insert(TB_P,null,add_point("c93","12","79","3","10"));
            db.insert(TB_P,null,add_point("c94","11","79","3","10"));
            db.insert(TB_P,null,add_point("c95","10","79","3","10"));
            db.insert(TB_P,null,add_point("c96","9","79","3","10"));
            db.insert(TB_P,null,add_point("c97","8","79","3","10"));
            db.insert(TB_P,null,add_point("c98","7","79","3","10"));
            db.insert(TB_P,null,add_point("c99","6","79","3","10"));
            db.insert(TB_P,null,add_point("c100","5","79","3","10"));
            db.insert(TB_P,null,add_point("c101","5","78","3","10"));
            db.insert(TB_P,null,add_point("c102","5","77","3","10"));
            db.insert(TB_P,null,add_point("c103","5","76","3","10"));
            db.insert(TB_P,null,add_point("c104","11","80","3","10"));
            db.insert(TB_P,null,add_point("c105","11","81","3","10"));
            db.insert(TB_P,null,add_point("c106","11","82","3","10"));
            db.insert(TB_P,null,add_point("c107","11","83","3","10"));
            db.insert(TB_P,null,add_point("c108","11","84","3","10"));
            db.insert(TB_P,null,add_point("c109","11","85","3","10"));
            db.insert(TB_P,null,add_point("c110","10","82","3","10"));
            db.insert(TB_P,null,add_point("c111","9","82","3","10"));
            db.insert(TB_P,null,add_point("c112","8","82","3","10"));

            db.insert(TB_P,null,add_point("c113","39","31","3","10"));
            db.insert(TB_P,null,add_point("c114","39","30","3","10"));
            db.insert(TB_P,null,add_point("c115","39","29","3","10"));

            db.insert(TB_P,null,add_point("c116","36","74","3","10"));
            db.insert(TB_P,null,add_point("c117","37","74","3","10"));
            db.insert(TB_P,null,add_point("c118","38","74","3","10"));







            SQL="CREATE TABLE IF NOT EXISTS "+TB_B+"" +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT ,_uuid VARCHAR(50),_major VARCHAR(10),_minor VARCHAR(10),_x VARCHAR(10),_y VARCHAR(10),_r0 VARCHAR(10),_n VARCHAR(10),_np VARCHAR(10),_rp VARCHAR(10),_lp VARCHAR(10))";
            db.execSQL(SQL);
            String n="2";
            String m="2.5";


            db.insert(TB_B,null,add_beacon("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0","2","0","10","71","-76","2.5","b108","b115","b109"));
            db.insert(TB_B,null,add_beacon("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0","2","1","11","69","-73.5","3","b108","b107","b109"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","2","2","12","71","-72","2.9","b108","b115","b107"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","2","3","12.6","69","-69","2.9","b106","b105","b107"));
            db.insert(TB_B,null,add_beacon("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0","2","4","14","71","-71.5","1.2","b105","b106","-1"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","2","5","9","69","-69","2.9","b110","b111","b109"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","2","6","8","71","-69","2.9","b111","b112","b110"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","2","7","6.5","69","-71","2.9","b112","b113","b111"));
            db.insert(TB_B,null,add_beacon("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0","2","8","6","71","-72","3","b113","b114","b112"));
            db.insert(TB_B,null,add_beacon("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0","1","9","6","76","-78.5","1.9","a145","a144","-1"));
            //minor=9 沒有佈
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","10","37","76","-75",n,"a50","a48","49"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","11","37","72","-75",n,"a48","a49","a50"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","12","5","80","-75",n,"a3","a4","a2"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","13","5.4","78","-75",n,"a2","a1","a3"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","14","7","80","-75",n,"a5","a6","a4"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","15","8","78","-75",n,"a6","a5","a7"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","16","9","80","-75",n,"a7","a8","a6"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","17","9.8","78","-75",n,"a8","a7","a9"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","18","10","80","-75",n,"a8","a9","a10"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","19","11","78","-75",n,"a9","a8","a18"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","20","12.4","80","-75",n,"a9","a10","a18"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","21","12.4","81","-75",n,"a11","a10","a12"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","22","10","82","-75",m,"a12","a13","a11"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","23","12.4","83","-75",n,"a13","a12","a14"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","24","10","84","-75",n,"a14","a15","a13"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","25","11","85.3","-75",n,"a15","a14","-1"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","26","8","80","-75",n,"a16","-1","-1"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","27","8","84","-75",n,"a17","-1","-1"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","28","13","78","-75",n,"a19","a18","a20"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","29","14","80","-75",n,"a20","a21","a19"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","30","15.6","78","-75",n,"a21","a20","a22"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","31","16","80","-75",n,"a22","a23","a21"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","32","17","78","-75",n,"a23","a22","a24"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","33","18.5","80","-75",n,"a24","a25","a23"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","34","19","78","-75",n,"a25","a24","a26"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","35","20","80","-75",n,"a26","a27","a25"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","36","21","78","-75",n,"a27","a26","a28"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","37","22","80","-75",n,"a28","a27","a29"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","38","23","78","-75",n,"a29","a28","a30"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","39","23.4","80","-75",n,"a30","a31","a29"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","40","25","78","-75",n,"a31","a30","a32"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","41","26","80","-75",n,"a32","a33","a31"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","42","27","78","-75",m,"a33","a32","a34"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","43","28","80","-75",n,"a34","a35","a33"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","44","29","78","-75",m,"a35","a34","a36"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","45","30.4","80","-75",n,"a36","a37","a35"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","46","31","78","-75",n,"a37","a38","a45"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","47","32","80","-75",n,"a38","a39","a37"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","48","34","80","-75",n,"a40","a39","a45"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","49","35","78","-75",n,"a41","a40","a45"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","50","36","80","-75",n,"a42","a41","a43"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","51","37","78","-75",n,"a43","a42","a44"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","52","33","78","-75",n,"a39","a45","a38"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","53","31","77","-75",m,"a46","a45","a47"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","54","35","76","-75",n,"a47","a48","a46"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","55","31","75","-75",m,"a48","a47","a49"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","56","35","74","-75",m,"a49","a50","a48"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","57","31","73","-75",m,"a50","a49","a51"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","58","35","72","-75",n,"a51","a52","a50"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","59","31","71","-75",n,"a52","a51","a53"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","60","35","70","-75",n,"a53","a54","a52"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","61","31","69","-75",n,"a54","a53","-1"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","1","62","35","68","-75",n,"a54","-1","-1"));


            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","2","63","8","72","-75",n,"b121","b117","-1"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","2","64","8","74","-75",n,"b122","b117","-1"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","2","65","10","75","-75",n,"b118","b119","b120"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","2","66","12","74","-75",n,"b117","b118","b119"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","2","67","10","73","-75",n,"b116","b117","b118"));
            db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","2","68","12","72","-75",n,"b115","b116","b117"));
            //db.insert(TB_B,null,add_beacon("FDA50693-A4E2-4FB1-AFCF-C6EB07647825","2","69","10","71","-75",n,"b115","b108","b109"));


            SQL="CREATE TABLE IF NOT EXISTS "+TB_C+"" +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT ,_start VARCHAR(50),_connect_1 VARCHAR(50),_connect_2 VARCHAR(50),_connect_3 VARCHAR(50),_connect_4 VARCHAR(50))";
            db.execSQL(SQL);

            db.insert(TB_C,null,add_connection("a1","a2","-1","-1","-1"));
            db.insert(TB_C,null,add_connection("a2","a1","a3","-1","-1"));
            db.insert(TB_C,null,add_connection("a3","a2","a4","-1","-1"));
            db.insert(TB_C,null,add_connection("a4","a3","a5","-1","-1"));
            db.insert(TB_C,null,add_connection("a5","a4","a6","-1","-1"));
            db.insert(TB_C,null,add_connection("a6","a5","a7","-1","-1"));
            db.insert(TB_C,null,add_connection("a7","a6","a8","-1","-1"));
            db.insert(TB_C,null,add_connection("a8","a7","a9","-1","-1"));
            db.insert(TB_C,null,add_connection("a9","a8","a10","a18","-1"));
            db.insert(TB_C,null,add_connection("a10","a9","a11","-1","-1"));
            db.insert(TB_C,null,add_connection("a11","a10","a12","-1","-1"));
            db.insert(TB_C,null,add_connection("a12","a11","a13","a16","a17"));
            db.insert(TB_C,null,add_connection("a13","a12","a14","-1","-1"));
            db.insert(TB_C,null,add_connection("a14","a13","a15","-1","-1"));
            db.insert(TB_C,null,add_connection("a15","a14","-1","-1","-1"));
            db.insert(TB_C,null,add_connection("a16","a12","b122","-1","-1"));
            db.insert(TB_C,null,add_connection("a17","a12","-1","-1","-1"));
            db.insert(TB_C,null,add_connection("a18","a9","a19","-1","-1"));
            db.insert(TB_C,null,add_connection("a19","a18","a20","-1","-1"));
            db.insert(TB_C,null,add_connection("a20","a19","a21","-1","-1"));
            db.insert(TB_C,null,add_connection("a21","a20","a22","-1","-1"));
            db.insert(TB_C,null,add_connection("a22","a21","a23","-1","-1"));
            db.insert(TB_C,null,add_connection("a23","a22","a24","-1","-1"));
            db.insert(TB_C,null,add_connection("a24","a23","a25","-1","-1"));
            db.insert(TB_C,null,add_connection("a25","a24","a26","-1","-1"));
            db.insert(TB_C,null,add_connection("a26","a25","a27","-1","-1"));
            db.insert(TB_C,null,add_connection("a27","a26","a28","-1","-1"));
            db.insert(TB_C,null,add_connection("a28","a27","a29","-1","-1"));
            db.insert(TB_C,null,add_connection("a29","a28","a30","-1","-1"));
            db.insert(TB_C,null,add_connection("a30","a29","a31","-1","-1"));
            db.insert(TB_C,null,add_connection("a31","a30","a32","-1","-1"));
            db.insert(TB_C,null,add_connection("a32","a31","a33","-1","-1"));
            db.insert(TB_C,null,add_connection("a33","a32","a34","-1","-1"));
            db.insert(TB_C,null,add_connection("a34","a33","a35","-1","-1"));
            db.insert(TB_C,null,add_connection("a35","a34","a36","-1","-1"));
            db.insert(TB_C,null,add_connection("a36","a35","a37","-1","-1"));
            db.insert(TB_C,null,add_connection("a37","a36","a38","-1","-1"));
            db.insert(TB_C,null,add_connection("a38","a37","a39","-1","-1"));
            db.insert(TB_C,null,add_connection("a39","a38","a40","a45","-1"));
            db.insert(TB_C,null,add_connection("a40","a39","a41","-1","-1"));
            db.insert(TB_C,null,add_connection("a41","a40","a42","-1","-1"));
            db.insert(TB_C,null,add_connection("a42","a41","a43","-1","-1"));
            db.insert(TB_C,null,add_connection("a43","a42","a44","-1","-1"));
            db.insert(TB_C,null,add_connection("a44","a43","-1","-1","-1"));
            db.insert(TB_C,null,add_connection("a45","a46","a39","-1","-1"));
            db.insert(TB_C,null,add_connection("a46","a45","a47","-1","-1"));
            db.insert(TB_C,null,add_connection("a47","a46","a48","-1","-1"));
            db.insert(TB_C,null,add_connection("a48","a47","a49","-1","-1"));
            db.insert(TB_C,null,add_connection("a49","a48","a50","a55","a56"));
            db.insert(TB_C,null,add_connection("a50","a49","a51","-1","-1"));
            db.insert(TB_C,null,add_connection("a51","a50","a52","-1","-1"));
            db.insert(TB_C,null,add_connection("a52","a51","a53","-1","-1"));
            db.insert(TB_C,null,add_connection("a53","a52","a54","-1","-1"));
            db.insert(TB_C,null,add_connection("a54","a53","a57","-1","-1"));
            db.insert(TB_C,null,add_connection("a55","a49","-1","-1","-1"));
            db.insert(TB_C,null,add_connection("a56","a49","b75","-1","-1"));

            //1013: 非正確
            db.insert(TB_C,null,add_connection("a57","a54","a58","-1","-1"));
            db.insert(TB_C,null,add_connection("a58","a57","a59","-1","-1"));
            db.insert(TB_C,null,add_connection("a59","a58","a60","-1","-1"));
            db.insert(TB_C,null,add_connection("a60","a59","a61","-1","-1"));
            db.insert(TB_C,null,add_connection("a61","a60","a62","-1","-1"));
            db.insert(TB_C,null,add_connection("a62","a61","a63","-1","-1"));
            db.insert(TB_C,null,add_connection("a63","a62","a64","-1","-1"));
            db.insert(TB_C,null,add_connection("a64","a63","a65","-1","-1"));
            db.insert(TB_C,null,add_connection("a65","a64","a66","-1","-1"));
            db.insert(TB_C,null,add_connection("a66","a65","a67","-1","-1"));
            db.insert(TB_C,null,add_connection("a67","a66","a68","-1","-1"));
            db.insert(TB_C,null,add_connection("a68","a67","a69","-1","-1"));
            db.insert(TB_C,null,add_connection("a69","a68","a70","-1","-1"));
            db.insert(TB_C,null,add_connection("a70","a69","a71","-1","-1"));
            db.insert(TB_C,null,add_connection("a71","a70","a72","-1","-1"));
            db.insert(TB_C,null,add_connection("a72","a71","a73","-1","-1"));
            db.insert(TB_C,null,add_connection("a73","a72","a74","-1","-1"));
            db.insert(TB_C,null,add_connection("a74","a73","a75","-1","-1"));
            db.insert(TB_C,null,add_connection("a75","a74","a76","-1","-1"));
            db.insert(TB_C,null,add_connection("a76","a75","a77","-1","-1"));
            db.insert(TB_C,null,add_connection("a77","a76","a78","-1","-1"));
            db.insert(TB_C,null,add_connection("a78","a77","a79","-1","-1"));
            db.insert(TB_C,null,add_connection("a79","a78","a80","-1","-1"));
            db.insert(TB_C,null,add_connection("a80","a79","a81","-1","-1"));
            db.insert(TB_C,null,add_connection("a81","a80","a82","-1","-1"));
            db.insert(TB_C,null,add_connection("a82","a81","a83","-1","-1"));
            db.insert(TB_C,null,add_connection("a83","a82","a84","-1","-1"));
            db.insert(TB_C,null,add_connection("a84","a83","a85","-1","-1"));
            db.insert(TB_C,null,add_connection("a85","a84","a86","-1","-1"));
            db.insert(TB_C,null,add_connection("a86","a85","a87","-1","-1"));
            db.insert(TB_C,null,add_connection("a87","a86","a88","-1","-1"));
            db.insert(TB_C,null,add_connection("a88","a87","a89","-1","-1"));
            db.insert(TB_C,null,add_connection("a89","a88","a90","-1","-1"));
            db.insert(TB_C,null,add_connection("a90","a89","a91","-1","-1"));
            db.insert(TB_C,null,add_connection("a91","a90","a92","-1","-1"));
            db.insert(TB_C,null,add_connection("a92","a91","a93","-1","-1"));
            db.insert(TB_C,null,add_connection("a93","a92","a94","a102","-1"));
            db.insert(TB_C,null,add_connection("a94","a93","a95","-1","-1"));
            db.insert(TB_C,null,add_connection("a95","a94","a96","-1","-1"));
            db.insert(TB_C,null,add_connection("a96","a95","a97","-1","-1"));
            db.insert(TB_C,null,add_connection("a97","a96","a98","-1","-1"));
            db.insert(TB_C,null,add_connection("a98","a97","a99","-1","-1"));
            db.insert(TB_C,null,add_connection("a99","a98","a100","-1","-1"));
            db.insert(TB_C,null,add_connection("a100","a99","a101","-1","-1"));
            db.insert(TB_C,null,add_connection("a101","a100","b29","-1","-1"));
            db.insert(TB_C,null,add_connection("a102","a93","a103","-1","-1"));
            db.insert(TB_C,null,add_connection("a103","a102","a104","-1","-1"));
            db.insert(TB_C,null,add_connection("a104","a103","a105","-1","-1"));
            db.insert(TB_C,null,add_connection("a105","a104","a106","-1","-1"));
            db.insert(TB_C,null,add_connection("a106","a105","a107","-1","-1"));
            db.insert(TB_C,null,add_connection("a107","a106","a108","-1","-1"));
            db.insert(TB_C,null,add_connection("a108","a107","a109","-1","-1"));
            db.insert(TB_C,null,add_connection("a109","a108","a110","-1","-1"));
            db.insert(TB_C,null,add_connection("a110","a109","a111","a117","-1"));
            db.insert(TB_C,null,add_connection("a111","a110","a112","-1","-1"));
            db.insert(TB_C,null,add_connection("a112","a111","a113","-1","-1"));
            db.insert(TB_C,null,add_connection("a113","a112","a114","-1","-1"));
            db.insert(TB_C,null,add_connection("a114","a113","a115","-1","-1"));
            db.insert(TB_C,null,add_connection("a115","a114","a116","-1","-1"));
            db.insert(TB_C,null,add_connection("a116","a115","-1","-1","-1"));
            db.insert(TB_C,null,add_connection("a117","a110","a118","-1","-1"));
            db.insert(TB_C,null,add_connection("a118","a117","a119","-1","-1"));
            db.insert(TB_C,null,add_connection("a119","a118","a120","-1","-1"));
            db.insert(TB_C,null,add_connection("a120","a119","a121","-1","-1"));
            db.insert(TB_C,null,add_connection("a121","a120","a122","-1","-1"));
            db.insert(TB_C,null,add_connection("a122","a121","a123","-1","-1"));
            db.insert(TB_C,null,add_connection("a123","a122","a124","-1","-1"));
            db.insert(TB_C,null,add_connection("a124","a123","a125","-1","-1"));
            db.insert(TB_C,null,add_connection("a125","a124","a126","-1","-1"));
            db.insert(TB_C,null,add_connection("a126","a125","a127","-1","-1"));
            db.insert(TB_C,null,add_connection("a127","a126","a128","a137","-1"));
            db.insert(TB_C,null,add_connection("a128","a127","a129","-1","-1"));
            db.insert(TB_C,null,add_connection("a129","a128","a130","-1","-1"));
            db.insert(TB_C,null,add_connection("a130","a129","a131","a134","-1"));
            db.insert(TB_C,null,add_connection("a131","a130","a132","-1","-1"));
            db.insert(TB_C,null,add_connection("a132","a131","a133","-1","-1"));
            db.insert(TB_C,null,add_connection("a133","a132","a134","-1","-1"));
            db.insert(TB_C,null,add_connection("a134","a133","-1","-1","-1"));

            db.insert(TB_C,null,add_connection("b1","b2","-1","-1","-1"));
            db.insert(TB_C,null,add_connection("b2","b1","b3","-1","-1"));
            db.insert(TB_C,null,add_connection("b3","b2","b4","-1","-1"));
            db.insert(TB_C,null,add_connection("b4","b3","b5","-1","-1"));
            db.insert(TB_C,null,add_connection("b5","b4","b6","-1","-1"));
            db.insert(TB_C,null,add_connection("b6","b5","b7","-1","-1"));
            db.insert(TB_C,null,add_connection("b7","b6","b8","-1","-1"));
            db.insert(TB_C,null,add_connection("b8","b7","b9","-1","-1"));
            db.insert(TB_C,null,add_connection("b9","b8","b10","-1","-1"));
            db.insert(TB_C,null,add_connection("b10","b9","b11","-1","-1"));
            db.insert(TB_C,null,add_connection("b11","b10","b12","-1","-1"));
            db.insert(TB_C,null,add_connection("b12","b11","b13","-1","-1"));
            db.insert(TB_C,null,add_connection("b13","b12","b14","-1","-1"));
            db.insert(TB_C,null,add_connection("b14","b13","b15","-1","-1"));
            db.insert(TB_C,null,add_connection("b15","b14","b16","-1","-1"));
            db.insert(TB_C,null,add_connection("b16","b15","b17","-1","-1"));
            db.insert(TB_C,null,add_connection("b17","b16","b18","-1","-1"));
            db.insert(TB_C,null,add_connection("b18","b17","b19","-1","-1"));
            db.insert(TB_C,null,add_connection("b19","b18","b20","-1","-1"));
            db.insert(TB_C,null,add_connection("b20","b19","b21","-1","-1"));
            db.insert(TB_C,null,add_connection("b21","b20","b22","b30","-1"));
            db.insert(TB_C,null,add_connection("b22","b21","b23","-1","-1"));
            db.insert(TB_C,null,add_connection("b23","b22","b24","-1","-1"));
            db.insert(TB_C,null,add_connection("b24","b23","b25","b28","b29"));
            db.insert(TB_C,null,add_connection("b25","b24","b26","-1","-1"));
            db.insert(TB_C,null,add_connection("b26","b25","b27","-1","-1"));
            db.insert(TB_C,null,add_connection("b27","b26","b28","-1","-1"));
            db.insert(TB_C,null,add_connection("b28","b24","c115","-1","-1"));
            db.insert(TB_C,null,add_connection("b29","b24","a101","-1","-1"));
            db.insert(TB_C,null,add_connection("b30","b21","b31","-1","-1"));
            db.insert(TB_C,null,add_connection("b31","b30","b32","-1","-1"));
            db.insert(TB_C,null,add_connection("b32","b31","b33","-1","-1"));
            db.insert(TB_C,null,add_connection("b33","b32","b34","-1","-1"));
            db.insert(TB_C,null,add_connection("b34","b33","b35","-1","-1"));
            db.insert(TB_C,null,add_connection("b35","b34","b36","-1","-1"));
            db.insert(TB_C,null,add_connection("b36","b35","b37","-1","-1"));
            db.insert(TB_C,null,add_connection("b37","b36","b38","-1","-1"));
            db.insert(TB_C,null,add_connection("b38","b37","b39","-1","-1"));
            db.insert(TB_C,null,add_connection("b39","b38","b40","-1","-1"));
            db.insert(TB_C,null,add_connection("b40","b39","b41","-1","-1"));
            db.insert(TB_C,null,add_connection("b41","b40","b42","-1","-1"));
            db.insert(TB_C,null,add_connection("b42","b41","b43","-1","-1"));
            db.insert(TB_C,null,add_connection("b43","b42","b44","-1","-1"));
            db.insert(TB_C,null,add_connection("b44","b43","b45","-1","-1"));
            db.insert(TB_C,null,add_connection("b45","b44","b46","-1","-1"));
            db.insert(TB_C,null,add_connection("b46","b45","b47","-1","-1"));
            db.insert(TB_C,null,add_connection("b47","b46","b48","-1","-1"));
            db.insert(TB_C,null,add_connection("b48","b47","b49","-1","-1"));
            db.insert(TB_C,null,add_connection("b49","b48","b50","-1","-1"));
            db.insert(TB_C,null,add_connection("b50","b49","b51","-1","-1"));
            db.insert(TB_C,null,add_connection("b51","b50","b52","-1","-1"));
            db.insert(TB_C,null,add_connection("b52","b51","b53","-1","-1"));
            db.insert(TB_C,null,add_connection("b53","b52","b54","-1","-1"));
            db.insert(TB_C,null,add_connection("b54","b53","b55","-1","-1"));
            db.insert(TB_C,null,add_connection("b55","b54","b56","-1","-1"));
            db.insert(TB_C,null,add_connection("b56","b55","b57","-1","-1"));
            db.insert(TB_C,null,add_connection("b57","b56","b58","-1","-1"));
            db.insert(TB_C,null,add_connection("b58","b57","b59","-1","-1"));
            db.insert(TB_C,null,add_connection("b59","b58","b60","-1","-1"));
            db.insert(TB_C,null,add_connection("b60","b59","b61","-1","-1"));
            db.insert(TB_C,null,add_connection("b61","b60","b62","-1","-1"));
            db.insert(TB_C,null,add_connection("b62","b61","b63","-1","-1"));
            db.insert(TB_C,null,add_connection("b63","b62","b64","-1","-1"));
            db.insert(TB_C,null,add_connection("b64","b63","b65","-1","-1"));
            db.insert(TB_C,null,add_connection("b65","b64","b66","-1","-1"));
            db.insert(TB_C,null,add_connection("b66","b65","b67","-1","-1"));
            db.insert(TB_C,null,add_connection("b67","b66","b68","-1","-1"));
            db.insert(TB_C,null,add_connection("b68","b67","b69","-1","-1"));
            db.insert(TB_C,null,add_connection("b69","b68","b70","-1","-1"));
            db.insert(TB_C,null,add_connection("b70","b69","b71","-1","-1"));
            db.insert(TB_C,null,add_connection("b71","b70","b72","-1","-1"));
            db.insert(TB_C,null,add_connection("b72","b71","b73","-1","-1"));
            db.insert(TB_C,null,add_connection("b73","b72","b74","-1","-1"));
            db.insert(TB_C,null,add_connection("b74","b73","b75","b76","b77"));
            db.insert(TB_C,null,add_connection("b75","b74","a56","-1","-1"));
            db.insert(TB_C,null,add_connection("b76","b74","c118","-1","-1"));
            db.insert(TB_C,null,add_connection("b77","b74","b78","-1","-1"));
            db.insert(TB_C,null,add_connection("b78","b77","b79","-1","-1"));
            db.insert(TB_C,null,add_connection("b79","b78","b80","-1","-1"));
            db.insert(TB_C,null,add_connection("b80","b79","b81","-1","-1"));
            db.insert(TB_C,null,add_connection("b81","b80","b82","b85","-1"));
            db.insert(TB_C,null,add_connection("b82","b81","b83","-1","-1"));
            db.insert(TB_C,null,add_connection("b83","b82","b84","-1","-1"));
            db.insert(TB_C,null,add_connection("b84","b83","b85","-1","-1"));
            db.insert(TB_C,null,add_connection("b85","b81","b86","-1","-1"));
            db.insert(TB_C,null,add_connection("b86","b85","b87","-1","-1"));
            db.insert(TB_C,null,add_connection("b87","b86","b88","-1","-1"));
            db.insert(TB_C,null,add_connection("b88","b87","b89","-1","-1"));
            db.insert(TB_C,null,add_connection("b89","b88","b90","-1","-1"));
            db.insert(TB_C,null,add_connection("b90","b89","b91","-1","-1"));
            db.insert(TB_C,null,add_connection("b91","b90","b92","-1","-1"));
            db.insert(TB_C,null,add_connection("b92","b91","b93","-1","-1"));
            db.insert(TB_C,null,add_connection("b93","b92","b94","-1","-1"));
            db.insert(TB_C,null,add_connection("b94","b93","b95","-1","-1"));
            db.insert(TB_C,null,add_connection("b95","b94","b96","-1","-1"));
            db.insert(TB_C,null,add_connection("b96","b95","b97","-1","-1"));
            db.insert(TB_C,null,add_connection("b97","b96","b98","-1","-1"));
            db.insert(TB_C,null,add_connection("b98","b97","b99","-1","-1"));
            db.insert(TB_C,null,add_connection("b99","b98","b100","-1","-1"));
            db.insert(TB_C,null,add_connection("b100","b99","b101","-1","-1"));
            db.insert(TB_C,null,add_connection("b101","b100","b102","-1","-1"));
            db.insert(TB_C,null,add_connection("b102","b101","b103","-1","-1"));
            db.insert(TB_C,null,add_connection("b103","b102","b104","-1","-1"));
            db.insert(TB_C,null,add_connection("b104","b103","b105","-1","-1"));
            db.insert(TB_C,null,add_connection("b105","b106","b104","-1","-1"));
            db.insert(TB_C,null,add_connection("b106","b105","b107","-1","-1"));
            db.insert(TB_C,null,add_connection("b107","b106","b108","-1","-1"));
            db.insert(TB_C,null,add_connection("b108","b107","b109","b115","-1"));
            db.insert(TB_C,null,add_connection("b109","b108","b110","-1","-1"));
            db.insert(TB_C,null,add_connection("b110","b109","b111","-1","-1"));
            db.insert(TB_C,null,add_connection("b111","b110","b112","-1","-1"));
            db.insert(TB_C,null,add_connection("b112","b111","b113","-1","-1"));
            db.insert(TB_C,null,add_connection("b113","b112","b114","-1","-1"));
            db.insert(TB_C,null,add_connection("b114","b113","-1","-1","-1"));
            db.insert(TB_C,null,add_connection("b115","b108","b116","-1","-1"));
            db.insert(TB_C,null,add_connection("b116","b115","b117","-1","-1"));
            db.insert(TB_C,null,add_connection("b117","b116","b118","b121","b122"));
            db.insert(TB_C,null,add_connection("b118","b117","b119","-1","-1"));
            db.insert(TB_C,null,add_connection("b119","b118","b120","-1","-1"));
            db.insert(TB_C,null,add_connection("b120","b119","-1","-1","-1"));
            db.insert(TB_C,null,add_connection("b121","b117","c112","-1","-1"));
            db.insert(TB_C,null,add_connection("b122","b117","a16","-1","-1"));



            db.insert(TB_C,null,add_connection("c1","c2","-1","-1","-1"));
            db.insert(TB_C,null,add_connection("c2","c1","c3","-1","-1"));
            db.insert(TB_C,null,add_connection("c3","c2","c4","-1","-1"));
            db.insert(TB_C,null,add_connection("c4","c3","c5","-1","-1"));
            db.insert(TB_C,null,add_connection("c5","c4","c6","c113","-1"));
            db.insert(TB_C,null,add_connection("c6","c5","c7","-1","-1"));
            db.insert(TB_C,null,add_connection("c7","c6","c8","-1","-1"));
            db.insert(TB_C,null,add_connection("c8","c7","c16","-1","-1"));

            db.insert(TB_C,null,add_connection("c9","c10","-1","-1","-1"));
            db.insert(TB_C,null,add_connection("c10","c9","c11","-1","-1"));
            db.insert(TB_C,null,add_connection("c11","c10","c12","-1","-1"));
            db.insert(TB_C,null,add_connection("c12","c11","c13","-1","-1"));
            db.insert(TB_C,null,add_connection("c13","c12","c14","-1","-1"));
            db.insert(TB_C,null,add_connection("c14","c13","c15","-1","-1"));
            db.insert(TB_C,null,add_connection("c15","c14","c16","-1","-1"));
            db.insert(TB_C,null,add_connection("c16","c15","c17","c8","-1"));
            db.insert(TB_C,null,add_connection("c17","c16","c18","-1","-1"));
            db.insert(TB_C,null,add_connection("c18","c17","c19","-1","-1"));
            db.insert(TB_C,null,add_connection("c19","c18","c20","-1","-1"));
            db.insert(TB_C,null,add_connection("c20","c19","c21","-1","-1"));
            db.insert(TB_C,null,add_connection("c21","c20","c22","-1","-1"));
            db.insert(TB_C,null,add_connection("c22","c21","c23","-1","-1"));
            db.insert(TB_C,null,add_connection("c23","c22","c24","-1","-1"));
            db.insert(TB_C,null,add_connection("c24","c23","c25","-1","-1"));
            db.insert(TB_C,null,add_connection("c25","c24","c26","-1","-1"));
            db.insert(TB_C,null,add_connection("c26","c25","c27","-1","-1"));
            db.insert(TB_C,null,add_connection("c27","c26","c28","-1","-1"));
            db.insert(TB_C,null,add_connection("c28","c27","c29","-1","-1"));
            db.insert(TB_C,null,add_connection("c29","c28","c30","-1","-1"));
            db.insert(TB_C,null,add_connection("c30","c29","c31","-1","-1"));
            db.insert(TB_C,null,add_connection("c31","c30","c32","-1","-1"));
            db.insert(TB_C,null,add_connection("c32","c31","c33","-1","-1"));
            db.insert(TB_C,null,add_connection("c33","c32","c34","-1","-1"));
            db.insert(TB_C,null,add_connection("c34","c33","c35","-1","-1"));
            db.insert(TB_C,null,add_connection("c35","c34","c36","-1","-1"));
            db.insert(TB_C,null,add_connection("c36","c35","c37","-1","-1"));
            db.insert(TB_C,null,add_connection("c37","c36","c38","-1","-1"));
            db.insert(TB_C,null,add_connection("c38","c37","c39","-1","-1"));
            db.insert(TB_C,null,add_connection("c39","c38","c40","-1","-1"));
            db.insert(TB_C,null,add_connection("c40","c39","c41","-1","-1"));
            db.insert(TB_C,null,add_connection("c41","c40","c42","-1","-1"));
            db.insert(TB_C,null,add_connection("c42","c41","c43","-1","-1"));
            db.insert(TB_C,null,add_connection("c43","c42","c44","-1","-1"));
            db.insert(TB_C,null,add_connection("c44","c43","c45","-1","-1"));
            db.insert(TB_C,null,add_connection("c45","c44","c46","-1","-1"));
            db.insert(TB_C,null,add_connection("c46","c45","c47","-1","-1"));
            db.insert(TB_C,null,add_connection("c47","c46","c48","-1","-1"));
            db.insert(TB_C,null,add_connection("c48","c47","c49","-1","-1"));
            db.insert(TB_C,null,add_connection("c49","c48","c50","-1","-1"));
            db.insert(TB_C,null,add_connection("c50","c49","c51","-1","-1"));
            db.insert(TB_C,null,add_connection("c51","c50","c52","-1","-1"));
            db.insert(TB_C,null,add_connection("c52","c51","c53","-1","-1"));
            db.insert(TB_C,null,add_connection("c53","c52","c54","-1","-1"));
            db.insert(TB_C,null,add_connection("c54","c53","c55","-1","-1"));
            db.insert(TB_C,null,add_connection("c55","c54","c56","-1","-1"));
            db.insert(TB_C,null,add_connection("c56","c55","c57","-1","-1"));
            db.insert(TB_C,null,add_connection("c57","c56","c58","-1","-1"));
            db.insert(TB_C,null,add_connection("c58","c57","c59","c116","-1"));
            db.insert(TB_C,null,add_connection("c59","c58","c60","-1","-1"));
            db.insert(TB_C,null,add_connection("c60","c59","c61","-1","-1"));
            db.insert(TB_C,null,add_connection("c61","c57","c62","-1","-1"));
            db.insert(TB_C,null,add_connection("c62","c61","c70","-1","-1"));

            db.insert(TB_C,null,add_connection("c63","c64","-1","-1","-1"));
            db.insert(TB_C,null,add_connection("c64","c63","c65","-1","-1"));
            db.insert(TB_C,null,add_connection("c65","c64","c66","-1","-1"));
            db.insert(TB_C,null,add_connection("c66","c65","c67","-1","-1"));
            db.insert(TB_C,null,add_connection("c67","c66","c68","-1","-1"));
            db.insert(TB_C,null,add_connection("c68","c67","c69","-1","-1"));
            db.insert(TB_C,null,add_connection("c69","c68","c70","-1","-1"));
            db.insert(TB_C,null,add_connection("c70","c69","c71","c62","-1"));
            db.insert(TB_C,null,add_connection("c71","c70","c72","-1","-1"));
            db.insert(TB_C,null,add_connection("c72","c71","c73","-1","-1"));
            db.insert(TB_C,null,add_connection("c73","c72","c74","-1","-1"));
            db.insert(TB_C,null,add_connection("c74","c73","c75","-1","-1"));
            db.insert(TB_C,null,add_connection("c75","c74","c76","-1","-1"));
            db.insert(TB_C,null,add_connection("c76","c75","c77","-1","-1"));
            db.insert(TB_C,null,add_connection("c77","c76","c78","-1","-1"));
            db.insert(TB_C,null,add_connection("c78","c77","c79","-1","-1"));
            db.insert(TB_C,null,add_connection("c79","c78","c80","-1","-1"));
            db.insert(TB_C,null,add_connection("c80","c79","c81","-1","-1"));
            db.insert(TB_C,null,add_connection("c81","c80","c82","-1","-1"));
            db.insert(TB_C,null,add_connection("c82","c81","c83","-1","-1"));
            db.insert(TB_C,null,add_connection("c83","c82","c84","-1","-1"));
            db.insert(TB_C,null,add_connection("c84","c83","c85","-1","-1"));
            db.insert(TB_C,null,add_connection("c85","c84","c86","-1","-1"));
            db.insert(TB_C,null,add_connection("c86","c85","c87","-1","-1"));
            db.insert(TB_C,null,add_connection("c87","c86","c88","-1","-1"));
            db.insert(TB_C,null,add_connection("c88","c87","c89","-1","-1"));
            db.insert(TB_C,null,add_connection("c89","c88","c90","-1","-1"));
            db.insert(TB_C,null,add_connection("c90","c89","c91","-1","-1"));
            db.insert(TB_C,null,add_connection("c91","c90","c92","-1","-1"));
            db.insert(TB_C,null,add_connection("c92","c91","c93","-1","-1"));
            db.insert(TB_C,null,add_connection("c93","c92","c94","-1","-1"));
            db.insert(TB_C,null,add_connection("c94","c93","c95","c104","-1"));
            db.insert(TB_C,null,add_connection("c95","c94","c96","-1","-1"));
            db.insert(TB_C,null,add_connection("c96","c95","c97","-1","-1"));
            db.insert(TB_C,null,add_connection("c97","c96","c98","-1","-1"));
            db.insert(TB_C,null,add_connection("c98","c97","c99","-1","-1"));
            db.insert(TB_C,null,add_connection("c99","c98","c100","-1","-1"));
            db.insert(TB_C,null,add_connection("c100","c99","c101","-1","-1"));
            db.insert(TB_C,null,add_connection("c101","c100","c102","-1","-1"));
            db.insert(TB_C,null,add_connection("c102","c101","c103","-1","-1"));
            db.insert(TB_C,null,add_connection("c103","c102","-1","-1","-1"));

            db.insert(TB_C,null,add_connection("c104","c94","c105","-1","-1"));
            db.insert(TB_C,null,add_connection("c105","c104","c106","-1","-1"));
            db.insert(TB_C,null,add_connection("c106","c105","c107","c110","-1"));
            db.insert(TB_C,null,add_connection("c107","c106","c108","-1","-1"));
            db.insert(TB_C,null,add_connection("c108","c107","c109","-1","-1"));
            db.insert(TB_C,null,add_connection("c109","c108","-1","-1","-1"));

            db.insert(TB_C,null,add_connection("c110","c106","c111","-1","-1"));
            db.insert(TB_C,null,add_connection("c111","c110","c112","-1","-1"));
            db.insert(TB_C,null,add_connection("c112","c111","b121","-1","-1"));

            db.insert(TB_C,null,add_connection("c113","c114","c5","-1","-1"));
            db.insert(TB_C,null,add_connection("c114","c113","c115","-1","-1"));
            db.insert(TB_C,null,add_connection("c115","c114","b28","-1","-1"));

            db.insert(TB_C,null,add_connection("c116","c58","c117","-1","-1"));
            db.insert(TB_C,null,add_connection("c117","c116","c118","-1","-1"));
            db.insert(TB_C,null,add_connection("c118","c117","b76","-1","-1"));
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}

