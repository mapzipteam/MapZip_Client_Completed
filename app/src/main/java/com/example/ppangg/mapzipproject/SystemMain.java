package com.example.ppangg.mapzipproject;

import java.util.ArrayList;

/**
 * Created by ljs93kr on 2015-07-27.
 * �� ���� ������ ���������� ����� ������� �Լ����� static���� ������ ��
 */
public class SystemMain {

    /**
     * Server ���ӽ� ����ϴ� URL���� ����
     */
    public static final String SERVER_ROOT_URL = "http://ljs93kr.cafe24.com/mapzip"; // ������ ��Ʈurl
    public static final String SERVER_JOIN_URL = SERVER_ROOT_URL+"/login/joincheck.php"; // ������ join url
    public static final String SERVER_LOGIN_URL = SERVER_ROOT_URL+"/login/logincheck.php";
    public static final String SERVER_MAPSETTING_URL = SERVER_ROOT_URL+"/map_setting/client_map_setting.php";
    public static final String SERVER_MAPSEARCH_URL = SERVER_ROOT_URL+"/map_search/map_search.php";
    public static final String SERVER_REVIEWENROLL_URL = SERVER_ROOT_URL+"/client_data/client_review_enroll.php";
    public static final String SERVER_REVIEWENROLL2_URL = SERVER_ROOT_URL+"/client_data/client_mkdir.php";
    public static final String SERVER_REVIEWENROLL3_URL = SERVER_ROOT_URL+"/client_data/get_image.php";

    // 지도 갯수 디폴트값
    public static final int DEFAULT_MAP_COUNT = 2;

    // 지역 디폴트 넘버
    public static final int SEOUL_MAP_NUM = 1;
    public static final int INCHEUON_MAP_NUM = 2;

}
