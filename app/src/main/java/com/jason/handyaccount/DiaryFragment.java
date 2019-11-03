package com.jason.handyaccount;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryFragment extends Fragment {
    private static final String TAG = "DiarySegment";
    private String diary[];
    private FragmentManager manager;
    private FragmentTransaction transaction;

    public DiaryFragment() {
        // Required empty public constructor
    }




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView: this is my diary part");

        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        DiaryListFragment frgImpl = new DiaryListFragment();

        transaction.add(R.id.fragment1, frgImpl, "frgImpl");
        transaction.commit();


        return inflater.inflate(R.layout.fragment_diary, container, false);
    }

    //在Fragment中将button的监听放到一下方法中
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button addnote = (Button) getActivity().findViewById(R.id.add_note);

        addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "nihao", Toast.LENGTH_LONG).show();

                //开启Compose活动，向数据库写一份日记
                Intent compose = new Intent(getContext(),Compose.class);
                // startActivity(config);
                startActivityForResult(compose,1);



            }});

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String diary = data.getStringExtra("diary");
        String date = data.getStringExtra("date");
        Log.i(TAG, "onActivityResult: diary =" +diary);
        Log.i(TAG, "onActivityResult: date = "+date);
        HashMap<String,String>map = new HashMap<String, String>();

        map.put("Story",diary);
        map.put("date",date);
        Log.i(TAG, "onActivityResult: listsize: "+ DiaryListFragment.listItems.size());
        DiaryListFragment.listItems.add(map);
        DiaryListFragment.listItemAdapter.notifyDataSetChanged();//hashmap
    }
}
 /* //数据库测试
                DiaryItem item1 = new DiaryItem("aaa","234");
                DiaryManager manager = new DiaryManager(getContext());
                manager.add(item1);
                manager.add(new DiaryItem("bbb","23.6"));
                Log.i(TAG, "onOptionsItemSelected: 写入数据完毕");

                //查询所有数据
                List<DiaryItem> testlist = manager.listall();
                for(DiaryItem i : testlist){
                    Log.i(TAG, "onOptionsItemSelected: 取出数据[id= "+i.getId()+"]name="+i.getCurStory()+"rate="+i.getWriting_date());
                    }*/