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
import android.view.View;
import android.view.ViewGroup;
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
import com.example.ppangg.mapzipproject.map.Restaurant;
import com.example.ppangg.mapzipproject.map.RestaurantResult;
import com.example.ppangg.mapzipproject.map.RestaurantSearcher;
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

    private Handler handler;

    public review_Fragment(){}
	


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        user = UserData.getInstance();
        arrsize = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View v = inflater.inflate(R.layout.fragment_review, container, false);

        layout_toast = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) getActivity().findViewById(R.id.custom_toast_layout));
        text_toast = (TextView) layout_toast.findViewById(R.id.textToShow);

        mListView = (ListView) v.findViewById(R.id.searchList_review);

        marItem = new ArrayList<MyItem>();

        user.setReviewListlock(false);
        mLockListView = false;
        mLockBtn = true;
        mSendLock = false;

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
                mMyAdapte = new MyListAdapter(getActivity(), R.layout.custom_listview, marItem);
                mListView.addFooterView(footer);
                mListView.setAdapter(mMyAdapte);
                mMyAdapte.notifyDataSetChanged();

                mLockBtn = false;
            }
        });

        review_regi = (Button) v.findViewById(R.id.registerBtn_review);
        review_regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), review_register.class);
                startActivity(intent);
            }
        });

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
}


