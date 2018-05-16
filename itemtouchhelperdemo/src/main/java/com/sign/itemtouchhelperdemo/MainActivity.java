package com.sign.itemtouchhelperdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> stringList = new ArrayList<>();
    private Context mContext;
    private MyAdapter myAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        initListener();
    }

    private void initListener() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MyItemTouchHelperCallback(myAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        myAdapter.setItemDragListener(new MyAdapter.ItemDragListener() {
            @Override
            public void onStartDrags(RecyclerView.ViewHolder viewHolder) {
                //drag 指与列表滚动方向一致的方向 swipe 指与列表滚动方向垂直的方向
                itemTouchHelper.startDrag(viewHolder);
            }
        });
    }

    private void initView() {
        for (int i = 0; i < 15; i++) {
            stringList.add("测试数据" + i);
        }
        mContext = this;
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        myAdapter = new MyAdapter(stringList);
        recyclerView.setAdapter(myAdapter);
    }

}