package com.mapzip.ppang.mapzipproject.main;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mapzip.ppang.mapzipproject.model.FriendData;
import com.mapzip.ppang.mapzipproject.R;
import com.mapzip.ppang.mapzipproject.model.SystemMain;
import com.mapzip.ppang.mapzipproject.model.UserData;
import com.mapzip.ppang.mapzipproject.activity.friend_home;
import com.mapzip.ppang.mapzipproject.activity.addfriend;
import com.mapzip.ppang.mapzipproject.network.MyVolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class friend_Fragment extends Fragment implements AbsListView.OnScrollListener {

    private boolean selectlock;

    private View v;
    private FriendData fuser;
    private UserData user;

    // search
    private int seq;

    // toast
    private View layout_toast;
    private TextView text_toast;

    // list
    private ArrayList<MyItem> marItem;
    private MyListAdapter mMyAdapte;
    private ListView mListView;
    private MyItem items;
    private View footer;
    private Button delmapmark;

    // 스크롤 로딩
    private LayoutInflater mInflater;
    private boolean mLockListView;

    // 친구삭제
    private Button delfriend_btn;
    private boolean delfriend_flag=false;

    private boolean mLockBtn;
    private boolean mSendLock;

    private JSONArray getArray;

    private Handler handler;

    private Resources res;
    public int map;
    public ProgressDialog  asyncDialog;
    private LoadingTask Loading;

    public friend_Fragment() {
        seq = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        selectlock = false;
        res = getResources();
        asyncDialog = new ProgressDialog(this.getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fuser = FriendData.getInstance();
        user = UserData.getInstance();

        //getActivity().getActionBar().setTitle("맵갈피");
        layout_toast = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) getActivity().findViewById(R.id.custom_toast_layout));
        text_toast = (TextView) layout_toast.findViewById(R.id.textToShow);

        v = inflater.inflate(R.layout.fragment_friend, container, false);

        mListView = (ListView) v.findViewById(R.id.friendList_friend);
        mListView.setFocusable(true); // for btn
        mListView.setOnItemClickListener(new ListViewItemClickListener());
        marItem = new ArrayList<MyItem>();

        mLockListView = true;
        mLockBtn = true;
        mSendLock = false;

        delfriend_btn = (Button) v.findViewById(R.id.delBtn_friend);
        delfriend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delfriend_flag)
                    delfriend_flag=false;
                else
                    delfriend_flag=true;

                Log.v("delflag", String.valueOf(delfriend_flag));

               // mMyAdapte = new MyListAdapter(getActivity(), R.layout.custom_listview, marItem);
                //mListView.addFooterView(footer);
                //mListView.setAdapter(mMyAdapte);
                mMyAdapte.notifyDataSetChanged();
            }
        });

        final Button addfriend = (Button) v.findViewById(R.id.addBtn_friend);
        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), addfriend.class);
                startActivity(intent);
            }
        });

        // 푸터를 등록. setAdapter 이전에 해야함.
        mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footer = mInflater.inflate(R.layout.listview_footer, null);
        //mListView.addFooterView(footer);

        // 스크롤 리스너 등록
        mListView.setOnScrollListener(this);

        marItem.clear();
        mMyAdapte = new MyListAdapter(getActivity(), R.layout.custom_listview, marItem);
        mListView.addFooterView(footer);
        mListView.setAdapter(mMyAdapte);
        mMyAdapte.notifyDataSetChanged();
        seq = 0;

        mLockBtn = false;
        DoSearch(v);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    // 리스트뷰 출력 항목
    class MyItem {
        MyItem(String _coustId, String name, String id, String reviewcount) {
            sCustId = _coustId;
            id_s = id;
            name_s = name;
            reviewcount_s = reviewcount;
        }

        String id_s;
        String sCustId;
        String name_s;
        String reviewcount_s;

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

        public String getID(int position) {return alSrc.get(position).id_s;}

        public String getName(int position) {return alSrc.get(position).name_s;}

        public String getReviewCount(int position) {return alSrc.get(position).reviewcount_s;}

        // 각 뷰의 항목 생성
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final int pos = position;
            if (convertView == null) {
                convertView = lInflater.inflate(layout, parent, false);
            }

            delmapmark = (Button) convertView.findViewById(R.id.delete_mapmark);
            if(delfriend_flag)
                delmapmark.setVisibility(View.VISIBLE);
            else
                delmapmark.setVisibility(View.GONE);

            delmapmark.setFocusable(false);
            delmapmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
                    alert_confirm.setMessage(mMyAdapte.getName(position)+"님을 맵갈피에서 삭제하시겠습니까?\n").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'YES' target_id,
                                    RequestQueue queue = MyVolley.getInstance(getActivity()).getRequestQueue();

                                    JSONObject obj = new JSONObject();
                                    try {
                                        obj.put("user_id", user.getUserID());
                                        obj.put("target_id", mMyAdapte.getID(position));
                                        Log.v("mapmark 보내기", obj.toString());
                                    } catch (JSONException e) {
                                        Log.v("제이손", "에러");
                                    }

                                    JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                                            SystemMain.SERVER_REMOVEMAPMARK_URL,
                                            obj,
                                            createMyReqSuccessListener_remove(position),
                                            createMyReqErrorListener()) {
                                    };
                                    queue.add(myReq);
                                }
                            }).setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'No'
                                    return;
                                }
                            });
                    AlertDialog alert = alert_confirm.create();
                    alert.show();
                }
            });

            final String getCustId = alSrc.get(pos).sCustId;

            TextView nameText_search = (TextView) convertView.findViewById(R.id.nameText_search);
            nameText_search.setText(getName(pos));
            nameText_search.append(" (");
            nameText_search.append(getID(pos));
            nameText_search.append(")");

            TextView hashText_search = (TextView) convertView.findViewById(R.id.hashText_search);
            hashText_search.setText("리뷰수: ");
            hashText_search.append(getReviewCount(pos));

            return convertView;
        }

        private Response.Listener<JSONObject> createMyReqSuccessListener_remove(final int position) {
            return new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.v("mapmark 받기", response.toString());

                    marItem.remove(position);
                    mMyAdapte.notifyDataSetChanged();

                }
            };
        }
    }

    // 더미 아이템 추가
    private void addItems(final int size) {
        // 아이템을 추가하는 동안 중복 요청을 방지하기 위해 락을 걸어둡니다.
        mLockListView = true;
        Runnable run = new Runnable() {
            @Override
            public void run() {
                if (mLockBtn == false) {
                    try {
                        for (int i = 0; i < size; i++) {
                            items = new MyItem(String.valueOf(i), getArray.getJSONObject(i).getString("user_name"), getArray.getJSONObject(i).getString("user_id"), getArray.getJSONObject(i).getString("total_review"));
                            marItem.add(items);
                        }
                    } catch (JSONException e) {

                    }
                    // 모든 데이터를 로드하여 적용하였다면 어댑터에 알리고
                    // 리스트뷰의 락을 해제합니다.
                    mMyAdapte.notifyDataSetChanged();
                    mLockListView = false;
                }
            }
        };
        // 속도의 딜레이를 구현하기 위한 꼼수

        handler = new Handler();
        handler.postDelayed(run, 100);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        // 현재 가장 처음에 보이는 셀번호와 보여지는 셀번호를 더한값이
        // 전체의 숫자와 동일해지면 가장 아래로 스크롤 되었다고 가정합니다.
        int count = totalItemCount - visibleItemCount;

        if (firstVisibleItem >= count && totalItemCount != 0 && mLockListView == false && mLockBtn == false && mSendLock == false) {
            Log.i("list", "Loading next items");
            DoSearch(v);

            // addItems(3);
        }

    }

    public void DoSearch(View v) {
        if (mSendLock == false) {
            mSendLock = true;

            RequestQueue queue = MyVolley.getInstance(getActivity()).getRequestQueue();

            JSONObject obj = new JSONObject();
            try {
                obj.put("userid", user.getUserID());
                obj.put("more", seq);
                Log.v("searchmap 보내기", obj.toString());
            } catch (JSONException e) {
                Log.v("제이손", "에러");
            }

            JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                    SystemMain.SERVER_FRIENDLIST_URL,
                    obj,
                    createMyReqSuccessListener(),
                    createMyReqErrorListener()) {
            };
            queue.add(myReq);
        }
    }


    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Log.v("friendlist 받기", response.toString());

                try {
                    int state = response.getInt("state");
                    if (state == 901) {
                        getArray = response.getJSONArray("friend_list");
                        seq++;

                        addItems(6);

                    } else if (state == 902) {
                        mLockBtn = true;
                        mListView.removeFooterView(footer);

                    }

                } catch (JSONException e) {

                }
                mSendLock = false;
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    // toast
                    text_toast.setText("인터넷 연결이 필요합니다.");
                    Toast toast = new Toast(getActivity());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_toast);
                    toast.show();

                    Log.e("friendlist", error.getMessage());
                } catch (NullPointerException ex) {
                    // toast
                    Log.e("friendlist", "nullpointexception");
                }
            }
        };
    }

    private class ListViewItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if(selectlock == false) {
                selectlock = true;

                fuser.initMapData();
                fuser.initmapforpinnum();
                Loading = new LoadingTask();

                Log.v("셀렉트 이름", mMyAdapte.getName(position));
                Log.v("셀렉트 아이디", mMyAdapte.getID(position));

                fuser.inputID(mMyAdapte.getID(position));
                fuser.inputName(mMyAdapte.getName(position));

                GoFriendHome(view, mMyAdapte.getID(position));
            }
            else
                return;
        }
    }

    public void GoFriendHome(View v, String fid) {
        RequestQueue queue = MyVolley.getInstance(getActivity()).getRequestQueue();

        JSONObject obj = new JSONObject();
        try {
            obj.put("target_id", fid);
            Log.v("friendlist_friend 보내기", obj.toString());
        } catch (JSONException e) {
            Log.v("제이손", "에러");
        }

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                SystemMain.SERVER_FRIENDHOME_URL,
                obj,
                createMyReqSuccessListener_friend(),
                createMyReqErrorListener()) {
        };
        queue.add(myReq);
    }

    private Response.Listener<JSONObject> createMyReqSuccessListener_friend() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("friendlist_friend 받기", response.toString());
                try {
                    if (response.get("state").toString().equals("801")) {

                        int mapcount = response.getJSONArray("mapmeta_info").length();
                        map = mapcount;

                        // 지도 순서 맞추기
                        int metaorder[] = new int[mapcount+1];
                        metaorder[0] = -1;
                        for(int i=0; i<mapcount; i++){
                            metaorder[Integer.parseInt(response.getJSONArray("mapmeta_info").getJSONObject(i).getString("map_id"))] = i;
                        }

                        JSONArray newmetaarr = new JSONArray();
                        for(int j=1; j<metaorder.length; j++){
                            newmetaarr.put(response.getJSONArray("mapmeta_info").getJSONObject(metaorder[j]));
                        }

                        fuser.setMapmetaArray(newmetaarr);
                        Log.v("맵메타",String.valueOf(user.getMapmetaArray()));


                        JSONObject jar = response.getJSONObject("gu_enroll_num");
                        Log.v("구넘버", String.valueOf(jar));

                        Log.v("구넘버", "진입");
                        for (int mapnum = 1; mapnum <= mapcount; mapnum++) {
                            if (jar.has(String.valueOf(mapnum))) {
                                JSONObject tmp = jar.getJSONObject(String.valueOf(mapnum));
                                int gunumber = 1;
                                int reviewnum = 0;
                                for (gunumber = 1; gunumber <= 25; gunumber++) {
                                    if (tmp.has(String.valueOf(gunumber))) {
                                        reviewnum = tmp.getInt(String.valueOf(gunumber));
                                        Log.v("구넘버o", tmp.get(String.valueOf(gunumber)).toString());
                                        //배열에 추가
                                    } else {
                                        reviewnum = 0;
                                        Log.v("구넘버x", String.valueOf(gunumber));
                                        //배열에 0 추가
                                    }
                                    fuser.setReviewCount(mapnum, gunumber, reviewnum);
                                }
                            } else {
                                for (int gunumber = 1; gunumber <= 25; gunumber++)
                                    fuser.setReviewCount(mapnum, gunumber, 0);
                            }
                        }

                        Loading.execute();
                    }
                } catch (JSONException e) {
                    Log.v("에러", "제이손");
                }

                selectlock = false;

            }
        };
    }


    protected class LoadingTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중입니다..");
            asyncDialog.setCanceledOnTouchOutside(false);
            // show dialog
            asyncDialog.show();
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            for (int mapnum = 1; mapnum <= map; mapnum++)
                fuser.setMapImage(mapnum, res);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (asyncDialog != null) {
                asyncDialog.dismiss();

                Intent intent = new Intent(getActivity(),friend_home.class);
                startActivity(intent);
            }

            super.onPostExecute(result);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(user.getfriendlock() == false) {
            marItem.clear();
            mMyAdapte = new MyListAdapter(getActivity(), R.layout.custom_listview, marItem);
            mListView.addFooterView(footer);
            mListView.setAdapter(mMyAdapte);
            mMyAdapte.notifyDataSetChanged();
            seq = 0;

            mLockBtn = false;
            DoSearch(v);

            user.setfriendlock(true);
        }

    }
}
