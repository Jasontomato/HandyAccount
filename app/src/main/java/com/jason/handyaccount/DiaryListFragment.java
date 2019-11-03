package com.jason.handyaccount;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DiaryListFragment extends ListFragment {


    public static ArrayList<HashMap<String,String>> listItems;
    public static SimpleAdapter listItemAdapter;

    private OnFragmentInteractionListener mListener;
    private static final String TAG = "diarylist";

    public DiaryListFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListView();
        ;
        this.setListAdapter(listItemAdapter);

    }


    private void initListView(){
        //取出日记
        DiaryManager manager = new DiaryManager(getContext());
        List<DiaryItem> testlist = manager.listall();
        listItems = new ArrayList<HashMap<String,String>>();

        for(DiaryItem i : testlist){
            Log.i(TAG, "diary: 取出数据[id= "+i.getId()+"]diary="+i.getCurStory()+"  date is="+i.getWriting_date());
            HashMap<String,String>map = new HashMap<String, String>();
            map.put("Id", String.valueOf(i.getId()));
            map.put("Story",i.getCurStory());
            map.put("date",i.getWriting_date());
            listItems.add(map);
        }

        //adapter Item 和list 元素对应
        listItemAdapter = new SimpleAdapter(getContext(),listItems,
                R.layout.fragment_list,
                new String[]{"Story","date"},
                new int[]{R.id.item_story,R.id.item_date});

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_show, container, false);
    }





    public void onListItemClick(ListView l, View v, int position, long id) {

         super.onListItemClick(l, v, position, id);
         Log.i(TAG, "onListItemClick: listview = "+l);
         Log.i(TAG, "onListItemClick: view ="+v);
         Log.i(TAG, "onListItemClick: position = "+position);
         Log.i(TAG, "onListItemClick: id = "+id);
         HashMap <String,String> map = (HashMap<String, String>) getListView().getItemAtPosition(position);
         String strstory = map.get("Story");
         String strdate = map.get("date");
         String strid = map.get("Id");
         Log.i(TAG, "onListItemClick: strstory="+strstory);
         Log.i(TAG, "onListItemClick: strdate="+strdate);
         String posi = String.valueOf(position);

        Intent edit = new Intent(getActivity(),EditActivity.class);
        edit.putExtra("diary",strstory);
        edit.putExtra("date",strdate);
        edit.putExtra("Id",strid);
        edit.putExtra("position",posi);
        startActivityForResult(edit,1);

             }



    @Override
    //修改返回值的结果
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            switch (resultCode) {
                case 2:   //edit
             /*   listItems.remove(getSelectedItemPosition());
                listItemAdapter.notifyDataSetChanged();*/
                    String diary = data.getStringExtra("diary");
                    String date = data.getStringExtra("date");
                    String id =  data.getStringExtra("Id");
                    String position = data.getStringExtra("position");

                    HashMap<String,String>map = new HashMap<String, String>();
                    map.put("Id", String.valueOf(id));
                    map.put("Story",diary);
                    map.put("date",date);
                    Log.i(TAG, "onActivityResult: listsize: "+listItems.size());
                    listItems.set(Integer.parseInt(position),map);
                    listItemAdapter.notifyDataSetChanged();//hashmap
                    Log.i(TAG, "onActivityResult: edit success");
                    break;
                case 3:
                    String pose = data.getStringExtra("position");
                    Log.i(TAG, "onActivityResult: position"+pose);
                    Log.i(TAG, "onActivityResult: listsize = " +listItems.size());
                    listItems.remove(listItems.get(Integer.parseInt(pose)));
                    Log.i(TAG, "onActivityResult: listsize2 = " +listItems.size());
                    listItemAdapter.notifyDataSetChanged();
                    Log.i(TAG, "onActivityResult: delete successs!!!!");
                    break;
                default:
                    break;

            }

        }else {
            Log.i(TAG, "onActivityResult: success");
        }
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
