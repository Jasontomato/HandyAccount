package com.jason.handyaccount;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    ViewPager viewPager;
    BottomNavigationView navigation;//底部导航栏对象
    List<Fragment> listFragment;//存储页面对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        viewPager = findViewById(R.id.view_pager);
        navigation = findViewById(R.id.nav_view);

        //向ViewPager添加各页面
        listFragment = new ArrayList<>();
    //    listFragment.add(new DiaryListFragment());
        listFragment.add(new DiaryFragment());
     /*   listFragment.add(new AccountFragment());
        listFragment.add(new NoticeFragment());*/

        MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager(), this, listFragment);
        viewPager.setAdapter(myAdapter);

        //导航栏点击事件和ViewPager滑动事件,让两个控件相互关联
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //这里设置为：当点击到某子项，ViewPager就滑动到对应位置
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_dashboard:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_notifications:
                        viewPager.setCurrentItem(2);
                        return true;

                    default:
                        break;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //注意这个方法滑动时会调用多次，下面是参数解释：
                //position当前所处页面索引,滑动调用的最后一次绝对是滑动停止所在页面
                //positionOffset:表示从位置的页面偏移的[0,1]的值。
                //positionOffsetPixels:以像素为单位的值，表示与位置的偏移
            }

            @Override
            public void onPageSelected(int position) {
                //该方法只在滑动停止时调用，position滑动停止所在页面位置
                //当滑动到某一位置，导航栏对应位置被按下
                navigation.getMenu().getItem(position).setChecked(true);
                //这里使用navigation.setSelectedItemId(position);无效，
                //setSelectedItemId(position)的官网原句：Set the selected
                // menu item ID. This behaves the same as tapping on an item
                //未找到原因
            }


            @Override
            public void onPageScrollStateChanged(int state) {
                //这个方法在滑动是调用三次，分别对应下面三种状态
                // 这个方法对于发现用户何时开始拖动，
                // 何时寻呼机自动调整到当前页面，或何时完全停止/空闲非常有用。
                //                state表示新的滑动状态，有三个值：
                //                SCROLL_STATE_IDLE：开始滑动（空闲状态->滑动），实际值为0
                //                SCROLL_STATE_DRAGGING：正在被拖动，实际值为1
                //                SCROLL_STATE_SETTLING：拖动结束,实际值为2
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected SCell getCellInfo() throws Exception {
        SCell cell = new SCell();

        /** 调用API获取基站信息 */
        TelephonyManager mTelNet = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.i(TAG, "getCellInfo: 授权失败");

        }
        CdmaCellLocation location = (CdmaCellLocation) mTelNet.getCellLocation();
        if (location == null) {
            throw new Exception("获取基站信息失败");
        }
        String operator = mTelNet.getNetworkOperator();
        String mcc = mTelNet.getNetworkOperator().substring(0, 3);
        String mnc = String.valueOf(location.getSystemId());
        int lac = location.getNetworkId();
        int cid = location.getBaseStationId();

        /** 将获得的数据放到结构体中 */
        cell.MCC = mcc;
        cell.MNC = mnc;
        cell.LAC = lac;
        cell.CID = cid;

        return cell;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void show_it(View view) {
        /** 弹出一个等待状态的框 */
        ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在获取中...");
        System.out.println("qiguail");
        Log.i(TAG, "show_it: ????????????????????????????????????????");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();

        try {
            /** 获取基站数据 */
            SCell cell = getCellInfo();
            Log.i(TAG, "show_it: MCC =" + cell.MCC);
            Log.i(TAG, "show_it: MCC =" + cell.MNC);
            Log.i(TAG, "show_it: MCC =" + cell.CID);
            Log.i(TAG, "show_it: MCC =" + cell.LAC);

            /** 根据基站数据获取经纬度 */
            SItude itude = getItude(cell);
            Log.i(TAG, "show_it: latitude = " +itude.latitude);
            Log.i(TAG, "show_it: longitude = " +itude.longitude);
            /** 获取地理位置 */
            String location = getLocation(itude);



            /** 关闭对话框 */
            mProgressDialog.dismiss();
        }catch (Exception e) {
            /** 关闭对话框 */
            mProgressDialog.dismiss();
            /** 显示错误 */

        }
    }

    /** 获取经纬度 */
    protected  SItude getItude(SCell cell) throws Exception {
        SItude itude = new SItude();

        /** 采用Android默认的HttpClient */
        HttpClient client = new DefaultHttpClient();
        /** 采用POST方法 */
        HttpPost post = new HttpPost("http://www.google.com/loc/json");
        try {
            /** 构造POST的JSON数据 */
            JSONObject holder = new JSONObject();
            holder.put("version", "1.1.0");
            holder.put("host", "maps.google.com");
            holder.put("address_language", "zh_CN");
            holder.put("request_address", true);
            holder.put("radio_type", "cdma");
            holder.put("carrier", "HTC");

            JSONObject tower = new JSONObject();
            tower.put("mobile_country_code", cell.MCC);
            tower.put("mobile_network_code", cell.MNC);
            tower.put("cell_id", cell.CID);
            tower.put("location_area_code", cell.LAC);

            JSONArray towerarray = new JSONArray();
            towerarray.put(tower);
            holder.put("cell_towers", towerarray);

            StringEntity query = new StringEntity(holder.toString());
            post.setEntity(query);

            /** 发出POST数据并获取返回数据 */
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuffer strBuff = new StringBuffer();
            String result = null;
            while ((result = buffReader.readLine()) != null) {
                strBuff.append(result);
            }

            /** 解析返回的JSON数据获得经纬度 */
            JSONObject json = new JSONObject(strBuff.toString());
            JSONObject subjosn = new JSONObject(json.getString("location"));

            itude.latitude = subjosn.getString("latitude");
            itude.longitude = subjosn.getString("longitude");

            Log.i("Itude", itude.latitude + itude.longitude);

        } catch (Exception e) {
            Log.e(e.getMessage(), e.toString());
            throw new Exception("获取经纬度出现错误:"+e.getMessage());
        } finally{
            post.abort();
            client = null;
        }

        return itude;
    }

    /** 获取地理位置 */
    protected  String getLocation(SItude itude) throws Exception {
        String resultString = "";

        /** 这里采用get方法，直接将参数加到URL上 */
        String urlString = String.format("http://maps.google.cn/maps/geo?key=abcdefg&q=%s,%s", itude.latitude, itude.longitude);
        Log.i("URL", urlString);

        /** 新建HttpClient */
        HttpClient client = new DefaultHttpClient();
        /** 采用GET方法 */
        HttpGet get = new HttpGet(urlString);
        try {
            /** 发起GET请求并获得返回数据 */
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuffer strBuff = new StringBuffer();
            String result = null;
            while ((result = buffReader.readLine()) != null) {
                strBuff.append(result);
            }
            resultString = strBuff.toString();

            /** 解析JSON数据，获得物理地址 */
            if (resultString != null && resultString.length() > 0) {
                JSONObject jsonobject = new JSONObject(resultString);
                JSONArray jsonArray = new JSONArray(jsonobject.get("Placemark").toString());
                resultString = "";
                for (int i = 0; i < jsonArray.length(); i++) {
                    resultString = jsonArray.getJSONObject(i).getString("address");
                }
            }
        } catch (Exception e) {
            throw new Exception("获取物理位置出现错误:" + e.getMessage());
        } finally {
            get.abort();
            client = null;
        }

        return resultString;
    }


}
