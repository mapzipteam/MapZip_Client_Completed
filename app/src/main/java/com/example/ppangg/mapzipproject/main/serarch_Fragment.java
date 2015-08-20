package com.example.ppangg.mapzipproject.main;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ppangg.mapzipproject.R;

import java.util.ArrayList;

public class serarch_Fragment extends Fragment implements AbsListView.OnScrollListener{


    private View v;

    private ArrayList<MyItem> marItem;
    private MyListAdapter 	  mMyAdapte;
    private ListView mListView;
    private MyItem 			  items;

    // 스크롤 로딩
    private LayoutInflater mInflater;
    private boolean mLockListView;

    public serarch_Fragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

       v = inflater.inflate(R.layout.fragment_search, container, false);

        mListView = (ListView)v.findViewById(R.id.searchList);
        marItem = new ArrayList<MyItem>();

        mLockListView = true;

        // 푸터를 등록. setAdapter 이전에 해야함.
        mInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListView.addFooterView(mInflater.inflate(R.layout.listview_footer, null));

        // 스크롤 리스너 등록
        mListView.setOnScrollListener(this);

        mMyAdapte = new MyListAdapter(getActivity(), R.layout.custom_listview, marItem);
        mListView.setAdapter(mMyAdapte);

        // 임시 데이터 등록
        addItems(10);
         
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }
    // 리스트뷰 출력 항목
    class MyItem
    {
        MyItem(String _coustId)
        {
            sCustId = _coustId;
        }
        String sCustId;
    }

    // 어댑터 클래스
    class MyListAdapter extends BaseAdapter
    {
        Context cContext;
        LayoutInflater lInflater;
        ArrayList<MyItem> alSrc;
        int layout;

        public MyListAdapter(Context _context, int _layout, ArrayList<MyItem> _arrayList)
        {
            cContext  = _context;
            lInflater = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            alSrc	  = _arrayList;
            layout    = _layout;
        }

        @Override
        public int getCount()
        {
            return alSrc.size();
        }

        @Override
        public Object getItem(int position)
        {
            return alSrc.get(position).sCustId;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        // 각 뷰의 항목 생성
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            final int pos = position;
            if(convertView == null)
            {
                convertView = lInflater.inflate(layout, parent, false);
            }

            final String getCustId = alSrc.get(pos).sCustId;

            TextView tvCustId = (TextView)convertView.findViewById(R.id.tvCoustId);
            tvCustId.setText(alSrc.get(position).sCustId);

            Button btSending = (Button)convertView.findViewById(R.id.listBtn);
            btSending.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                }
            });

            return convertView;
        }
    }

    // 더미 아이템 추가
    private void addItems(final int size)
    {
        // 아이템을 추가하는 동안 중복 요청을 방지하기 위해 락을 걸어둡니다.
        mLockListView = true;
        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                for(int i = 0 ; i < size ; i++)
                {
                    items = new MyItem("more " + i);
                    marItem.add(items);
                }
                // 모든 데이터를 로드하여 적용하였다면 어댑터에 알리고
                // 리스트뷰의 락을 해제합니다.
                mMyAdapte.notifyDataSetChanged();
                mLockListView = false;
            }
        };
        // 속도의 딜레이를 구현하기 위한 꼼수
        Handler handler = new Handler();
        handler.postDelayed(run, 3000);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        // 현재 가장 처음에 보이는 셀번호와 보여지는 셀번호를 더한값이
        // 전체의 숫자와 동일해지면 가장 아래로 스크롤 되었다고 가정합니다.
        int count = totalItemCount - visibleItemCount;

        if(firstVisibleItem >= count && totalItemCount != 0 && mLockListView == false)
        {
            Log.i("list", "Loading next items");
            addItems(10);
        }

    }
}
