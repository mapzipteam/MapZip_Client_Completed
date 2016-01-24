package com.mapzip.ppang.mapzipproject.model;

/**
 * Created by ljs93kr on 2015-07-27.
 */
public class SystemMain {

    /*
    *  network protocol
    */
    // Server URL
    public static final String SERVER_ROOT_URL = "http://ljs93kr.cafe24.com/mapzip";
    public static final String SERVER_JOIN_URL = SERVER_ROOT_URL+"/login/joincheck.php";
    public static final String SERVER_LOGIN_URL = SERVER_ROOT_URL+"/login/logincheck.php";
    public static final String SERVER_MAPSETTING_URL = SERVER_ROOT_URL+"/map_setting/client_map_setting.php";
    public static final String SERVER_MAPSEARCH_URL = SERVER_ROOT_URL+"/map_search/map_search.php";
    public static final String SERVER_REVIEWENROLL_URL = SERVER_ROOT_URL+"/client_data/client_review_enroll.php";
    public static final String SERVER_REVIEWENROLL2_URL = SERVER_ROOT_URL+"/client_data/client_mkdir.php";
    public static final String SERVER_REVIEWENROLL3_URL = SERVER_ROOT_URL+"/client_data/save_image.php";
    public static final String SERVER_HOMETOMAP_URL = SERVER_ROOT_URL+"/client_data/map_meta.php";
    public static final String SERVER_MAPTOREVIEW_URL = SERVER_ROOT_URL+"/client_data/map_detail.php";
    public static final String SERVER_ADDFRIENDSEARCH_URL = SERVER_ROOT_URL+"/friend/friend_search.php";
    public static final String SERVER_ADDFRIENDENROLL_URL = SERVER_ROOT_URL+"/friend/friend_enroll.php";
    public static final String SERVER_FRIENDLIST_URL = SERVER_ROOT_URL+"/friend/friend_show.php";
    public static final String SERVER_FRIENDHOME_URL = SERVER_ROOT_URL+"/friend/friend_home.php";
    public static final String SERVER_NOTICE_URL = SERVER_ROOT_URL+"/interact/get_patch_update.php";
    public static final String SERVER_SUGGEST_URL = SERVER_ROOT_URL +"/interact/post_user_sound.php";
    public static final String SERVER_REVIEWDELETE_URL = SERVER_ROOT_URL+"/client_data/client_review_delete.php";
    public static final String SERVER_REVIEWMODIFY_URL = SERVER_ROOT_URL+"/client_data/client_review_update.php";
    public static final String SERVER_DELETEUSER_URL = SERVER_ROOT_URL+"/client_data/user_leave.php";

    // state define -1~
    public static final int NON_KNOWN_ERROR = -1;

    // state define 100~ : about Database
    public static final int DB_CONNECTION_ERROR = 101; // db connection error
    public static final int SQL_QUERY_ERROR = 103; // sql query fail

    // state define 200~ : about Login/Join/Leave
    public static final int LOGIN_SUCCESS = 200; // login success
    public static final int LOGIN_FAIL = 201; // login fail
    public static final int JOIN_SUCCESS = 210; // join success
    public static final int JOIN_FAIL_ALREADY_ERROR = 211; // already joined id
    public static final int JOIN_FAIL_INSERT_ERROR = 212; // cannot insert info to DB
    public static final int LEAVE_ALL_SUCCESS = 221; // 회원 탈퇴 성공
    public static final int LEAVE_ERROR_IGNORE = 222; // 회원 탈퇴 중 무시할 수 있는 오류 발생
    public static final int LEAVE_FAIL_SERIOUS = 223; // 회원 탈퇴 중 치명적 오류 발생

    // state define 500~ : about map_search
    public static final int MAP_SEARCH_SUCCESS = 501;
    public static final int MAP_SEARCH_NO_MORE = 502;
    public static final int MAP_SEARCH_FAIL = 503;

    // state define 600~ : about client_data
    public static final int CLIENT_REVIEW_DATA_ENROLL_SUCCESS = 601; // text data enroll success
    public static final int CLIENT_REVIEW_IMAGE_MKDIR_SUCCESS = 602; // image data dir create success
    public static final int CLIENT_REVIEW_IMAGE_ENROLL_SUCCESS = 603; // image data enroll success
    public static final int CLIENT_REVIEW_DATA_DELETE_SUCCESS = 604; // 리뷰 삭제 텍스트 데이터 삭제 성공
    public static final int CLIENT_REVIEW_IMAGE_RMDIR_SUCCESS = 605; // 리뷰 삭제 이미지 데이터 및 디렉토리 삭제 성공
    public static final int CLIENT_REVIEW_IMAGE_RMDIR_NONE	 = 606; // 리뷰 삭제 이미지 디렉토리가 애초에 없음(이미지없음)
    public static final int CLIENT_REVIEW_DATA_UPDATE_SUCCESS = 607; // 리뷰 갱신 텍스트 데이터 갱신 성공

    public static final int CLIENT_REVIEW_DATA_ENROLL_FAIL = 611; // text data enroll fail
    public static final int CLIENT_REVIEW_DATA_ENROLL_EXIST = 612; // review is overlapped
    public static final int CLIENT_REVIEW_IMAGE_MKDIR_EXIST = 621; // image dir is overlapped
    public static final int CLIENT_REVIEW_IMAGE_MKDIR_FAIL = 622;  // image data dir create fail

    // state define 700~ : about client_data/map_meta
    public static final int CLIENT_REVIEW_META_DOWN_SUCCESS = 701; // 가게 위경도, 가게 이름 정보 전달 성공, 로그인 성공 까지
    public static final int CLIENT_REVIEW_META_DOWN_EMPTY = 711; // 가게 위경도, 가게 이름이 해당 지도에 없음

    public static final int CLIENT_REVIEW_DETAIL_DOWN_SUCCESS = 702; // map_detail success

    // state define 800~ : about friend_home.php
    public static final int FRIEND_HOME_SUCCESS = 801; // success

    // state define 900~ : about friend_show.php
    public static final int FRIEND_ITEM_SHOW_SUCCESS = 901; // success to show user's friend list
    public static final int FRIEND_ITEM_SHOW_EMPTY = 902; // if user's friend list is empty..

    // state define 1000~ : about interact with users
    public static final int USER_SOUND_INSERT_SUCCESS = 1001; // success to insert in user sound to mz_user_sound table
    public static final int USER_SOUND_INSERT_FAIL = 1002; // fail it


    /*
     * local
     */
    // map count DEFAULT
    public static final int DEFAULT_MAP_COUNT = 2;

    // location number DEFAULT
    public static final int SEOUL_MAP_NUM = 1;
    public static final int INCHEUON_MAP_NUM = 2;

    // map color count DEFAULT
    public static final int MAP_RED_NUM = 5;
    public static final int MAP_YELLOW_NUM = 1;

    // for imageadapter
    public static final int justuser = 1;
    public static final int justfuser = 2;

    // GU number DEFAULT
    public static final int SeoulGuCount = 25;
    public static final int DoBong = 1;
    public static final int NoWon = 2;
    public static final int GangBuk = 3;
    public static final int SungBuk = 4;
    public static final int ZongRang = 5;
    public static final int EunPhung = 6;
    public static final int ZongRo = 7;
    public static final int DongDaeMon = 8;
    public static final int SuDaeMon = 9;
    public static final int Zhong = 10;
    public static final int SungDong = 11;
    public static final int GangZin = 12;
    public static final int GangDong = 13;
    public static final int MaPho = 14;
    public static final int YongSan = 15;
    public static final int GangSue = 16;
    public static final int YangChen = 17;
    public static final int GuRo = 18;
    public static final int YongDengPo = 19;
    public static final int DongJack = 20;
    public static final int GemChun = 21;
    public static final int GanAk = 22;
    public static final int SeoCho = 23;
    public static final int GangNam = 24;
    public static final int SongPa = 25;

}
