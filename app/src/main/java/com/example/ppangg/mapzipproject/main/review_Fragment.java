package com.example.ppangg.mapzipproject.main;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppangg.mapzipproject.R;
import com.example.ppangg.mapzipproject.SystemMain;
import com.example.ppangg.mapzipproject.UserData;
import com.example.ppangg.mapzipproject.map.Location;
import com.example.ppangg.mapzipproject.map.MapActivity;
import com.example.ppangg.mapzipproject.map.Restaurant;
import com.example.ppangg.mapzipproject.map.RestaurantResult;
import com.example.ppangg.mapzipproject.map.RestaurantSearcher;
import com.example.ppangg.mapzipproject.map.SearchInLocationActivity;
import com.example.ppangg.mapzipproject.review_register;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class review_Fragment extends Fragment implements AbsListView.OnScrollListener {

    private UserData user;

    // toast
    private View layout_toast;
    private TextView text_toast;

    private Button review_regi;
    private Button review_search;
    private Button map_viewBtn;
    private Button review_regi_self;
    private EditText searchedit;

    private RestaurantResult restaurants;
    private Context context;
    private String query;
    private RestaurantSearcher restaurantSearcher;

    // list
    private ArrayList<MyItem> marItem;
    private MyListAdapter mMyAdapte;
    private ListView mListView;
    private MyItem items;
    private View footer;
    private int arrsize;

    // 스크롤 로딩
    private LayoutInflater mInflater;

    private boolean mLockListView;
    private boolean mLockBtn;
    private boolean mSendLock;
    private boolean mBtnLockr_mapview;

    private Handler handler;

    // 선택 이벤트
    private int selectNum;

    public review_Fragment(){}
	


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        user = UserData.getInstance();
        arrsize = 0;
        selectNum = -1;
        getActivity().getActionBar().setTitle("     리뷰");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View v = inflater.inflate(R.layout.fragment_review, container, false);

        layout_toast = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) getActivity().findViewById(R.id.custom_toast_layout));
        text_toast = (TextView) layout_toast.findViewById(R.id.textToShow);

        mListView = (ListView) v.findViewById(R.id.searchList_review);
        mListView.setOnItemClickListener(new ListViewItemClickListener());

        marItem = new ArrayList<MyItem>();

        user.setReviewListlock(false);
        mLockListView = false;
        mLockBtn = true;
        mSendLock = false;
        mBtnLockr_mapview = true;

        // 푸터를 등록. setAdapter 이전에 해야함.
        mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footer = mInflater.inflate(R.layout.listview_footer, null);

        // 스크롤 리스너 등록
        mListView.setOnScrollListener(this);

        searchedit = (EditText) v.findViewById(R.id.searchText_review);

        review_search = (Button) v.findViewById(R.id.searchBtn_review);
        review_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchedit.getText().toString().trim().isEmpty())
                    return;
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(review_search.getWindowToken(), 0);
                ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if(!(mobile.isConnected() || wifi.isConnected()))
                {
                    // toast
                    text_toast.setText("인터넷 연결이 필요합니다.");
                    Toast toast = new Toast(getActivity());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_toast);
                    toast.show();

                    return;
                }

                query = searchedit.getText().toString();
                startSearching();

                marItem.clear();
                arrsize = 0;
                selectNum = -1;
                mMyAdapte = new MyListAdapter(getActivity(), R.layout.custom_listview, marItem);
                mListView.addFooterView(footer);
                mListView.setAdapter(mMyAdapte);
                mMyAdapte.notifyDataSetChanged();

                mLockBtn = false;
            }
        });

        review_regi_self = (Button) v.findViewById(R.id.registerOwnBtn_review);
        review_regi_self.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(searchedit.getWindowToken(), 0);
                searchedit.clearFocus();
                searchedit.setSelectAllOnFocus(false);
                Intent intent = new Intent(getActivity(), SearchInLocationActivity.class);
                startActivity(intent);
            }
        });
        review_regi_self.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    review_regi_self.setBackgroundResource(R.drawable.background_btn);

                if(event.getAction() == MotionEvent.ACTION_UP)
                    review_regi_self.setBackgroundResource(R.drawable.noclick);
                return false;
            }
        });
        review_regi_self.setClickable(true);
        map_viewBtn = (Button) v.findViewById(R.id.mapviewBtn_review);
        map_viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBtnLockr_mapview == true) {
                    // toast
                    text_toast.setText("위치를 확인할 가게를 선택해주세요.");
                    Toast toast = new Toast(getActivity());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout_toast);
                    toast.show();
                    return;
                }
                Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("LNG", restaurants.get(selectNum).getLngX());
                intent.putExtra("LAT", restaurants.get(selectNum).getLatY());
                intent.putExtra("fragment_id", "review");
                intent.putExtra("store_name", restaurants.get(selectNum).getTitle());
                intent.putExtra("store_x", restaurants.get(selectNum).getLngX());
                intent.putExtra("store_y", restaurants.get(selectNum).getLatY());
                startActivity(intent);

            }
        });

        map_viewBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    map_viewBtn.setBackgroundResource(R.drawable.background_btn);

                if(event.getAction() == MotionEvent.ACTION_UP)
                    map_viewBtn.setBackgroundResource(R.drawable.noclick);
                return false;
            }
        });
        map_viewBtn.setClickable(true);

        review_regi = (Button) v.findViewById(R.id.registerBtn_review);
        review_regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectNum == -1) {
                    // toast
                    text_toast.setText("가게를 선택해주세요.");
                    Toast toast = new Toast(getActivity());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout_toast);
                    toast.show();
                    return;
                }

                if (restaurants.get(selectNum).getLngX() == 0 || restaurants.get(selectNum).getLatY() == 0) {
                    // toast
                    text_toast.setText("다시 검색해주세요.");
                    Toast toast = new Toast(getActivity());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout_toast);
                    toast.show();
                    return;
                }

                Intent intent = new Intent(getActivity(), review_register.class);
                intent.putExtra("store_name", restaurants.get(selectNum).getTitle());
                intent.putExtra("store_address", restaurants.get(selectNum).getAdress());
                intent.putExtra("store_contact", restaurants.get(selectNum).getTelephone());
                intent.putExtra("store_x", restaurants.get(selectNum).getLngX());
                intent.putExtra("store_y", restaurants.get(selectNum).getLatY());
                intent.putExtra("store_cx", restaurants.get(selectNum).getKatecX());
                intent.putExtra("store_cy", restaurants.get(selectNum).getKatecY());
                startActivity(intent);
            }
        });
        review_regi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    review_regi.setBackgroundResource(R.drawable.background_btn);

                if(event.getAction() == MotionEvent.ACTION_UP)
                    review_regi.setBackgroundResource(R.drawable.noclick2);
                return false;
            }
        });
        review_regi.setClickable(true);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    public void startSearching(){
        context = getActivity();
        restaurants = new RestaurantResult();
        restaurantSearcher = new RestaurantSearcher(restaurants, query, context);
        restaurantSearcher.UrlRequest();
    }

    // 리스트뷰 출력 항목
    class MyItem {
        MyItem(String _coustId, String name, String address) {
            sCustId = _coustId;
            name_s = name;
            address_s = address;
        }

        String sCustId;
        String name_s;
        String address_s;

    }

    // 어댑터 클래스
    class MyListAdapter extends BaseAdapter {
        Context cContext;
        LayoutInflater lInflater;
        ArrayList<MyItem> alSrc;
        int layout;

        public MyListAdapter(Context _context, int _layout, ArrayList<MyItem> _arrayList) {
            cContext = _context;
            lInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            alSrc = _arrayList;
            layout = _layout;
        }

        @Override
        public int getCount() {
            return alSrc.size();
        }

        @Override
        public Object getItem(int position) {
            return alSrc.get(position).sCustId;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public String getName(int position) {
            return alSrc.get(position).name_s;
        }

        public String getAddress(int position) { return alSrc.get(position).address_s; }

        // 각 뷰의 항목 생성
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            if (convertView == null) {
                convertView = lInflater.inflate(layout, parent, false);
            }

            final String getCustId = alSrc.get(pos).sCustId;

            TextView nameText = (TextView) convertView.findViewById(R.id.nameText_search);
            nameText.setText(getName(pos));
            TextView addressText = (TextView) convertView.findViewById(R.id.hashText_search);
            addressText.setText(getAddress(pos));

            return convertView;
        }
    }

    // 더미 아이템 추가
    private void addItems(final int size) {
        // 아이템을 추가하는 동안 중복 요청을 방지하기 위해 락을 걸어둡니다.
        mLockListView = true;
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Log.v("아이템추가","진입");
                if (mLockBtn == false) {
                    try {
                        Log.v("아이템추가","런");
                        int asize = arrsize;
                        for (int i=asize; i < asize+size; i++) {
                            items = new MyItem(String.valueOf(i), restaurants.getRestaurants().get(i).getTitle(), restaurants.getRestaurants().get(i).getAdress());
                            marItem.add(items);
                            arrsize++;

                            if((arrsize == restaurants.getRestaurants().size()) && (restaurants.getRestaurants().size() != 0)) {
                                mLockBtn = true;
                                mListView.removeFooterView(footer);
                            }
                        }
                    } catch (Exception e) {

                    }
                    // 모든 데이터를 로드하여 적용하였다면 어댑터에 알리고
                    // 리스트뷰의 락을 해제합니다.
                    mMyAdapte.notifyDataSetChanged();
                }
            }
        };

        // 속도의 딜레이를 구현하기 위한 꼼수

        handler = new Handler();
        handler.postDelayed(run, 1000);

        mLockListView = false;

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        int count = totalItemCount - visibleItemCount;
        Log.v("리뷰 리스트뷰락",String.valueOf(user.getReviewListlock()));

        if (firstVisibleItem >= count && totalItemCount != 0 && (mLockBtn == false) && (user.getReviewListlock() == false) && (mLockListView == false)) {
            Log.i("list", "Loading next items");
            addItems(5);
        }

    }

    private class ListViewItemClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            mBtnLockr_mapview = false;
            selectNum = position;
            Log.v("리스트뷰 셀렉트",String.valueOf(position));

        }
    }

}


