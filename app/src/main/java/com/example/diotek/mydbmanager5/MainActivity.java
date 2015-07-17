package com.example.diotek.mydbmanager5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.provider.Telephony;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.zip.Inflater;


public class MainActivity extends ActionBarActivity {


    public DBManager mDBManager = null;
    public FileManager mFileManager = null;
    String[] colums = new String[] {"_index", "name","phone_number","home_number","company_number"};
    ArrayList<AddressItem> mItems ;

    TextView mActivityText = null;
    ListView mActivityListView = null;
    FrameLayout mActivityFrameLayout = null;

    //insert
    ContentValues mAddRowValue = null;

    LinearLayout mInsertView = null;
    EditText mInsertNameEdit = null;
    EditText mInsertPhoneEdit = null;
    EditText mInsertHomeEdit = null;
    EditText mInsertCompanyEdit = null;
    String mInsertName = null;
    String mInsertPhoneNumber = null;
    String mInsertHomeNumber= null;
    String mInsertCompanyNumber= null;

    //query
    QueryAdapter mQueryAdapter ;
    TextView mQueryIndexText = null;
    TextView mQueryNameText = null;
    TextView mQueryPhoneText = null;
    TextView mQueryHomeText = null;
    TextView mQueryCompanyText = null;

    //update
    UpdateAdapter mUpdateAdapter;
    Button mUpdateBtn = null;
    LinearLayout mUpdateView = null;
    ContentValues mUpdateRowValue = null;
    EditText mUpdateNameEdit = null;
    EditText mUpdatePhoneEdit = null;
    EditText mUpdateHomeEdit = null;
    EditText mUpdateCompanyEdit = null;
    String mUpdateName = null;
    String mUpdatePhoneNumber = null;
    String mUpdateHomeNumber= null;
    String mUpdateCompanyNumber= null;

    //delete
    DeleteAdapter mDeleteAdapter;
    Button mDeleteBtn = null;
    TextView mDeleteIndexText = null;
    TextView mDeleteNameText = null;
    TextView mDeletePhoneText = null;
    TextView mDeleteHomeText = null;
    TextView mDeleteCompanyText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBManager = DBManager.getInstance(this);
        mFileManager = new FileManager(this);

        mActivityFrameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        mActivityListView = (ListView)findViewById(R.id.listView);
        mActivityText = (TextView)findViewById(R.id.editText);

        //insert
        mInsertView = (LinearLayout)View.inflate(MainActivity.this, R.layout.insertview, null);
        mInsertNameEdit = (EditText) mInsertView.findViewById(R.id.editName);
        mInsertPhoneEdit = (EditText)mInsertView.findViewById(R.id.editPhoneNumber);
        mInsertHomeEdit = (EditText)mInsertView.findViewById(R.id.editHomeNumber);
        mInsertCompanyEdit = (EditText)mInsertView.findViewById(R.id.editCompanyNumber);

        //update
        mUpdateView = (LinearLayout)View.inflate(MainActivity.this, R.layout.insertview, null);
        mUpdateNameEdit = (EditText) mUpdateView.findViewById(R.id.editName);
        mUpdatePhoneEdit = (EditText)mUpdateView.findViewById(R.id.editPhoneNumber);
        mUpdateHomeEdit = (EditText)mUpdateView.findViewById(R.id.editHomeNumber);
        mUpdateCompanyEdit = (EditText)mUpdateView.findViewById(R.id.editCompanyNumber);

        mItems = new ArrayList<AddressItem>();
        //어댑터 준비
        mQueryAdapter = new QueryAdapter(this, R.layout.queryview, mItems);
        mDeleteAdapter = new DeleteAdapter(this, R.layout.deleteview, mItems);
        mUpdateAdapter = new UpdateAdapter(this, R.layout.updateview, mItems);
        //어댑터 연결
        mActivityListView.setAdapter(mQueryAdapter);
        mActivityListView.setAdapter(mDeleteAdapter);
        mActivityListView.setAdapter(mUpdateAdapter);
    }

    class AddressItem{

        AddressItem(int aIndex, String aName, String aPhoneNumber, String aHomeNumber, String aCompanyNumber){
            index = aIndex;
            name = aName;
            phoneNumber = aPhoneNumber;
            homeNumber = aHomeNumber;
            companyNumber = aCompanyNumber;
        }
        int index ;
        String name ;
        String phoneNumber ;
        String homeNumber ;
        String companyNumber ;
    }

    class QueryAdapter extends BaseAdapter{
        LayoutInflater mInflater;
        ArrayList<AddressItem> aItems;
        int layout;

        public QueryAdapter(Context context, int alayout, ArrayList<AddressItem> aAddressItems){
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            aItems = aAddressItems;
            layout = alayout;
        }
        @Override
        public int getCount(){
            return aItems.size();
        }
        @Override
        public AddressItem getItem(int position){
            return aItems.get(position);
        }
        @Override
        public long getItemId(int position){
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            //최초 호출이면 항목 뷰를 생성한다.
            //타입별로 뷰를 다르게 디자인할 수 있으며 높이가 달라도 상관없다.
            if (convertView == null) {
                convertView = mInflater.inflate(layout, parent, false);
            }

            mQueryIndexText = (TextView) convertView.findViewById(R.id.queryIndex);
            mQueryIndexText.setText(String.valueOf(aItems.get(position).index));
            mQueryNameText = (TextView) convertView.findViewById(R.id.queryName);
            mQueryNameText.setText(aItems.get(position).name);
            mQueryPhoneText = (TextView) convertView.findViewById(R.id.queryPhoneNumber);
            mQueryPhoneText.setText(aItems.get(position).phoneNumber);
            mQueryHomeText = (TextView) convertView.findViewById(R.id.queryHomeNumber);
            mQueryHomeText.setText(aItems.get(position).homeNumber);
            mQueryCompanyText = (TextView) convertView.findViewById(R.id.queryCompanyNumber);
            mQueryCompanyText.setText(aItems.get(position).companyNumber);

            return convertView;
        }
    }

    class UpdateAdapter extends BaseAdapter{
        LayoutInflater mInflater;
        ArrayList<AddressItem> aItems;
        int layout;

        public UpdateAdapter(Context context, int alayout, ArrayList<AddressItem> aAddressItems){
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            aItems = aAddressItems;
            layout = alayout;
        }
        @Override
        public int getCount(){
            return aItems.size();
        }
        @Override
        public AddressItem getItem(int position){
            return aItems.get(position);
        }
        @Override
        public long getItemId(int position){
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent){
            //최초 호출이면 항목 뷰를 생성한다.
            //타입별로 뷰를 다르게 디자인할 수 있으며 높이가 달라도 상관없다.
            if(convertView == null){
                convertView = mInflater.inflate(layout, parent, false);
            }

            ((TextView) convertView.findViewById(R.id.updateIndex)).setText(String.valueOf(aItems.get(position).index));
            ((TextView) convertView.findViewById(R.id.updateName)).setText(aItems.get(position).name);
            ((TextView) convertView.findViewById(R.id.updatePhoneNumber)).setText(aItems.get(position).phoneNumber);
            ((TextView) convertView.findViewById(R.id.updateHomeNumber)).setText(aItems.get(position).homeNumber);
            ((TextView) convertView.findViewById(R.id.updateCompanyNumber)).setText(aItems.get(position).companyNumber);

            mUpdateBtn = (Button)convertView.findViewById(R.id.updateButton);
            mUpdateBtn.setTag(position);
            mUpdateBtn.setOnClickListener(mUpdateClickListener);

            return convertView;
        }
    }

    class DeleteAdapter extends BaseAdapter{
        LayoutInflater mInflater;
        ArrayList<AddressItem> aItems;
        int layout;

        public DeleteAdapter(Context context, int alayout, ArrayList<AddressItem> aAddressItems){
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            aItems = aAddressItems;
            layout = alayout;
        }
        @Override
        public int getCount(){
            return aItems.size();
        }
        @Override
        public AddressItem getItem(int position){
            return aItems.get(position);
        }
        @Override
        public long getItemId(int position){
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent){
            //최초 호출이면 항목 뷰를 생성한다.
            //타입별로 뷰를 다르게 디자인할 수 있으며 높이가 달라도 상관없다.
            if(convertView == null){
                convertView = mInflater.inflate(layout, parent, false);
            }
            mDeleteIndexText = (TextView) convertView.findViewById(R.id.deleteIndex);
            mDeleteIndexText.setText(String.valueOf(aItems.get(position).index));
            mDeleteNameText = (TextView) convertView.findViewById(R.id.deleteName);
            mDeleteNameText.setText(aItems.get(position).name);
            mDeletePhoneText = (TextView) convertView.findViewById(R.id.deletePhoneNumber);
            mDeletePhoneText.setText(aItems.get(position).phoneNumber);
            mDeleteHomeText = (TextView) convertView.findViewById(R.id.deleteHomeNumber);
            mDeleteHomeText.setText(aItems.get(position).homeNumber);
            mDeleteCompanyText = (TextView) convertView.findViewById(R.id.deleteCompanyNumber);
            mDeleteCompanyText.setText(aItems.get(position).companyNumber);

            mDeleteBtn = (Button)convertView.findViewById(R.id.deleteButton);
            mDeleteBtn.setTag(position);
            mDeleteBtn.setOnClickListener(mDeleteClickListener);

            return convertView;
        }
    }

    private View.OnClickListener mUpdateClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int position = (int)v.getTag();
            AddressItem aItems = mUpdateAdapter.getItem(position);
            Button updateOkBtn = (Button)mUpdateView.findViewById(R.id.insertButton);
            updateOkBtn.setTag(position);
            Button updateCancelBtn = (Button)mUpdateView.findViewById(R.id.cancelButton);

            if (mActivityListView.getVisibility() == View.VISIBLE) {
                mActivityListView.setVisibility(View.INVISIBLE);
            }
            if (mActivityText.getVisibility() == View.VISIBLE) {
                mActivityText.setVisibility(View.INVISIBLE);
            }
            mActivityFrameLayout.addView(mUpdateView);

            mUpdateNameEdit.setText(aItems.name);
            mUpdatePhoneEdit.setText(aItems.phoneNumber);
            mUpdateHomeEdit.setText(aItems.homeNumber);
            mUpdateCompanyEdit.setText(aItems.companyNumber);

            updateOkBtn.setOnClickListener(mUpdateOkClickListener);
            updateCancelBtn.setOnClickListener(mUpdateCancelClickListener);
        }
    };

    private View.OnClickListener mUpdateOkClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int)v.getTag();
            int updateIndex = mUpdateAdapter.getItem(position).index;
            mUpdateRowValue = new ContentValues();

            mUpdateName = mUpdateNameEdit.getText().toString();
            mUpdateNameEdit.setText(mUpdateName);
            mUpdateRowValue.put("name", mUpdateName);

            mUpdatePhoneNumber = mUpdatePhoneEdit.getText().toString();
            mUpdatePhoneEdit.setText(mUpdatePhoneNumber);
            mUpdateRowValue.put("phone_number", mUpdatePhoneNumber);

            mUpdateHomeNumber = mUpdateHomeEdit.getText().toString();
            mUpdateHomeEdit.setText(mUpdateHomeNumber);
            mUpdateRowValue.put("home_number", mUpdateHomeNumber);

            mUpdateCompanyNumber = mUpdateCompanyEdit.getText().toString();
            mUpdateCompanyEdit.setText(mUpdateCompanyNumber);
            mUpdateRowValue.put("company_number", mUpdateCompanyNumber);

            mUpdateAdapter.notifyDataSetChanged();
            mDBManager.update(mUpdateRowValue, "_index=" + updateIndex, null);
            mActivityFrameLayout.removeView(mUpdateView);

            if (mActivityText.getVisibility() == View.INVISIBLE) {
                mActivityText.setVisibility(View.VISIBLE);
            }
            mActivityText.setText("갱신된 레코드 index : "+ updateIndex);
        }
    };

    private View.OnClickListener mUpdateCancelClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            mActivityFrameLayout.removeView(mUpdateView);
        }
    };

    private View.OnClickListener mDeleteClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int position = (int)v.getTag();
            int deleteIndex = mDeleteAdapter.getItem(position).index;

            mItems.remove(position);
            mDeleteAdapter.notifyDataSetChanged();

            mDBManager.delete("_index=" + deleteIndex, null);

            if (mActivityText.getVisibility() == View.INVISIBLE) {
                mActivityText.setVisibility(View.VISIBLE);
            }

            mActivityText.setText("삭제된 레코드 index : "+ deleteIndex);
        }
    };

    private View.OnClickListener mInsertClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
            long insertRecord ;
            mAddRowValue = new ContentValues();

            mInsertName = mInsertNameEdit.getText().toString();
            mInsertPhoneNumber = mInsertPhoneEdit.getText().toString();
            mInsertHomeNumber = mInsertHomeEdit.getText().toString();
            mInsertCompanyNumber = mInsertCompanyEdit.getText().toString();

            mAddRowValue.put("name", mInsertName);
            mAddRowValue.put("phone_number",mInsertPhoneNumber);
            mAddRowValue.put("home_number", mInsertHomeNumber);
            mAddRowValue.put("company_number", mInsertCompanyNumber);

            insertRecord = mDBManager.insert(mAddRowValue);

            if(mActivityText.getVisibility() == View.INVISIBLE){
                mActivityText.setVisibility(View.VISIBLE);
            }
            if(mActivityListView.getVisibility() == View.VISIBLE){
                mActivityListView.setVisibility(View.INVISIBLE);
            }

            //초기화
            mInsertNameEdit.setText("");
            mInsertPhoneEdit.setText("");
            mInsertHomeEdit.setText("");
            mInsertCompanyEdit.setText("");

            mActivityText.setText("추가된 레코드 index : " + insertRecord);
            mActivityFrameLayout.removeView(mInsertView);
        }
    };

    private View.OnClickListener mInsertCancelClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            mInsertNameEdit.setText("");
            mInsertPhoneEdit.setText("");
            mInsertHomeEdit.setText("");
            mInsertCompanyEdit.setText("");
            mActivityFrameLayout.removeView(mInsertView);
        }
    };

    public void onClick(View v) {
        switch (v.getId()) {
            //데이터 삽입
            case R.id.insert :
            {
                Button insertBtn = (Button)mInsertView.findViewById(R.id.insertButton);
                Button insertCancelBtn = (Button)mInsertView.findViewById(R.id.cancelButton);
                mActivityFrameLayout.addView(mInsertView);

                if(mActivityListView.getVisibility() == View.VISIBLE){
                    mActivityListView.setVisibility(View.INVISIBLE);
                }
                if(mActivityText.getVisibility() == View.VISIBLE){
                    mActivityText.setVisibility(View.INVISIBLE);
                }

                insertBtn.setOnClickListener(mInsertClickListener);
                insertCancelBtn.setOnClickListener(mInsertCancelClickListener);

                break;
            }
            //데이터 갱신
            case R.id.update :
            {
                mActivityFrameLayout.removeView(mInsertView);
                mActivityListView.setAdapter(mUpdateAdapter);
                mItems.clear();

                Cursor c = mDBManager.query(colums, null, null, null, null, null);
                if(c != null) {
                    while (c.moveToNext()){
                        int index = c.getInt(c.getColumnIndex("_index"));
                        String name = c.getString(c.getColumnIndex("name"));
                        String phone_number = c.getString(c.getColumnIndex("phone_number"));
                        String home_number = c.getString(c.getColumnIndex("home_number"));
                        String company_number = c.getString(c.getColumnIndex("company_number"));

                        //데이터 원본 준비
                        mItems.add(new AddressItem(index, name, phone_number, home_number, company_number));
                    }

                    mUpdateAdapter.notifyDataSetChanged();

                    if(mActivityListView.getVisibility() == View.INVISIBLE){
                        mActivityListView.setVisibility(View.VISIBLE);
                    }
                    if(mActivityText.getVisibility() == View.INVISIBLE){
                        mActivityText.setVisibility(View.VISIBLE);
                    }
                    if(c == null){
                        if(mActivityText.getVisibility() == View.INVISIBLE){
                            mActivityText.setVisibility(View.VISIBLE);
                        }
                        mActivityText.setText("데이터가 없습니다.");
                    }
                    mActivityText.setText("\n Total : "+c.getCount());
                    c.close();
                }
                else{
                    if(mActivityListView.getVisibility() == View.VISIBLE){
                        mActivityListView.setVisibility(View.INVISIBLE);
                    }
                    if(mActivityText.getVisibility() == View.INVISIBLE){
                        mActivityText.setVisibility(View.VISIBLE);
                    }
                    mActivityText.setText("데이터가 없습니다.");
                    c.close();
                }

                break;
            }
            //데이터 삭제
            case R.id.delete :
            {
                mActivityFrameLayout.removeView(mInsertView);
                mActivityListView.setAdapter(mDeleteAdapter);
                mItems.clear();

                Cursor c = mDBManager.query(colums, null, null, null, null, null);
                if(c != null) {
                    while (c.moveToNext()){
                        int index = c.getInt(c.getColumnIndex("_index"));
                        String name = c.getString(c.getColumnIndex("name"));
                        String phone_number = c.getString(c.getColumnIndex("phone_number"));
                        String home_number = c.getString(c.getColumnIndex("home_number"));
                        String company_number = c.getString(c.getColumnIndex("company_number"));

                        //데이터 원본 준비
                        mItems.add(new AddressItem(index, name, phone_number, home_number, company_number));
                    }

                    mDeleteAdapter.notifyDataSetChanged();

                    if(mActivityListView.getVisibility() == View.INVISIBLE){
                        mActivityListView.setVisibility(View.VISIBLE);
                    }
                    if(mActivityText.getVisibility() == View.INVISIBLE){
                        mActivityText.setVisibility(View.VISIBLE);
                    }
                    if(c == null){
                        if(mActivityText.getVisibility() == View.INVISIBLE){
                            mActivityText.setVisibility(View.VISIBLE);
                        }
                        mActivityText.setText("데이터가 없습니다.");
                    }
                    mActivityText.setText("\n Total : "+c.getCount());
                    c.close();
                }
                else{
                    if(mActivityListView.getVisibility() == View.VISIBLE){
                        mActivityListView.setVisibility(View.INVISIBLE);
                    }
                    if(mActivityText.getVisibility() == View.INVISIBLE){
                        mActivityText.setVisibility(View.VISIBLE);
                    }
                    mActivityText.setText("데이터가 없습니다.");
                    c.close();
                }

                break;
            }
            //데이터 쿼리
            case R.id.query :
            {
                mActivityFrameLayout.removeView(mInsertView);
                mActivityListView.setAdapter(mQueryAdapter);
                mItems.clear();

                Cursor c = mDBManager.query(colums, null, null, null, null, null);
                if(c != null) {
                    while (c.moveToNext()){
                        int index = c.getInt(c.getColumnIndex("_index"));
                        String name = c.getString(c.getColumnIndex("name"));
                        String phone_number = c.getString(c.getColumnIndex("phone_number"));
                        String home_number = c.getString(c.getColumnIndex("home_number"));
                        String company_number = c.getString(c.getColumnIndex("company_number"));

                        //데이터 원본 준비
                        mItems.add(new AddressItem(index, name, phone_number, home_number, company_number));
                    }

                    mQueryAdapter.notifyDataSetChanged();

                    if(mActivityListView.getVisibility() == View.INVISIBLE){
                        mActivityListView.setVisibility(View.VISIBLE);
                    }
                    if(mActivityText.getVisibility() == View.INVISIBLE){
                        mActivityText.setVisibility(View.VISIBLE);
                    }
                    mActivityText.setText("\n Total : "+c.getCount());
                    c.close();
                }

                else{
                    if(mActivityListView.getVisibility() == View.VISIBLE){
                        mActivityListView.setVisibility(View.INVISIBLE);
                    }
                    if(mActivityText.getVisibility() == View.INVISIBLE){
                        mActivityText.setVisibility(View.VISIBLE);
                    }
                    mActivityText.setText("\n Total : "+c.getCount());
                    c.close();
                }
                break;
            }
            //db파일을 SD카드에 저장
            case R.id.saveFile :
            {
                mActivityFrameLayout.removeView(mInsertView);
                mFileManager.saveFile();
                if(mActivityText.getVisibility() == View.INVISIBLE){
                    mActivityText.setVisibility(View.VISIBLE);
                }
                if(mActivityListView.getVisibility() == View.VISIBLE){
                    mActivityListView.setVisibility(View.INVISIBLE);
                }
                if(mFileManager.checkFile()) {
                    mActivityText.setText("파일 저장 성공");
                }
                else mActivityText.setText("파일 저장 실패");
                break;
            }
            //db파일을 SD카드에서 삭제
            case R.id.deleteFile :
            {
                mActivityFrameLayout.removeView(mInsertView);
                if(mActivityText.getVisibility() == View.INVISIBLE){
                    mActivityText.setVisibility(View.VISIBLE);
                }
                if(mActivityListView.getVisibility() == View.VISIBLE){
                    mActivityListView.setVisibility(View.INVISIBLE);
                }
                if(mFileManager.checkFile()) {
                    mFileManager.deleteFile();
                    mActivityText.setText("파일 삭제 완료");
                }
                else {
                    mActivityText.setText("SD카드에 Address.db파일이 존재하지 않습니다.");
                }
                break;
            }
            //SD카드에 있는 db파일을 앱으로 이동
            case R.id.copyFile :
            {
                mActivityFrameLayout.removeView(mInsertView);
                if(mActivityText.getVisibility() == View.INVISIBLE){
                    mActivityText.setVisibility(View.VISIBLE);
                }
                if(mActivityListView.getVisibility() == View.VISIBLE){
                    mActivityListView.setVisibility(View.INVISIBLE);
                }
                if(mFileManager.checkFile()) {
                    mFileManager.copyFile();
                    mActivityText.setText("파일 이동 완료");
                }
                else {
                    mActivityText.setText("SD카드에 Address.db파일이 존재하지 않습니다.");
                }
                break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
