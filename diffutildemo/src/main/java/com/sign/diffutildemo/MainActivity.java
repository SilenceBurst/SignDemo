package com.sign.diffutildemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<TestBean> list;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);

        initList();

        myAdapter = new MyAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }

    private void initList() {
        list = new ArrayList<>();
        list.add(new TestBean(0, "我是测试数据 0"));
        list.add(new TestBean(1, "我是测试数据 1"));
        list.add(new TestBean(2, "我是测试数据 2"));
        list.add(new TestBean(3, "我是测试数据 3"));
        list.add(new TestBean(4, "我是测试数据 4"));
        list.add(new TestBean(5, "我是测试数据 5"));
        list.add(new TestBean(6, "我是测试数据 6"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            ArrayList<TestBean> newList = new ArrayList<>();
            for (TestBean s : list) {
                newList.add(new TestBean(s.getId(), s.getName()));
            }
            newList.add(new TestBean(7, "我是新数据 7"));//添加数据
            newList.get(2).setName("我是测试数据 2-》22222");//修改数据
            TestBean testBean = newList.get(0);
            newList.remove(testBean);
            newList.add(testBean);

//            list = newList;
//            myAdapter.setList(list);
//            myAdapter.notifyDataSetChanged();

            //文艺青年新宠
            //利用DiffUtil.calculateDiff()方法，传入一个规则DiffUtil.Callback对象，和是否检测移动item的 boolean变量，得到DiffUtil.DiffResult 的对象
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(list, newList));
            //别忘了将新数据给Adapter
            myAdapter.setList(newList);
            //利用DiffUtil.DiffResult对象的dispatchUpdatesTo（）方法，传入RecyclerView的Adapter，轻松成为文艺青年
            diffResult.dispatchUpdatesTo(myAdapter);
            list = newList;
        } else if (item.getItemId() == R.id.menu_add) {
            list.remove(0);
            myAdapter.notifyItemRemoved(0);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
