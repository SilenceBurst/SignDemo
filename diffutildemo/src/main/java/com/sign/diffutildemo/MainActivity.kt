package com.sign.diffutildemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private var mList: ArrayList<String> = ArrayList()
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRecyclerView = findViewById(R.id.recycler_view)
        for (item in 0..100) {
            mList.add("old data$item")
        }
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                val textView = TextView(parent.context)
                textView.gravity = Gravity.CENTER
                textView.layoutParams =
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200)
                return object : RecyclerView.ViewHolder(textView) {}
            }

            override fun getItemCount(): Int {
                return mList.size
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                (holder.itemView as TextView).text = mList[position]
            }
        }
        mRecyclerView.adapter = mAdapter
        findViewById<View>(R.id.btn_refresh_data).setOnClickListener {
            val newList: ArrayList<String> = ArrayList()
            for (item in 0..100) {
                newList.add("new data$item")
            }
            val diffResult: DiffUtil.DiffResult =
                DiffUtil.calculateDiff(DiffCallback(mList, newList))

            diffResult.dispatchUpdatesTo(mAdapter)
            mList = newList
        }
    }


    class DiffCallback constructor(
        private var oldList: ArrayList<String>,
        private var newList: ArrayList<String>
    ) : DiffUtil.Callback() {

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return TextUtils.equals(oldList[oldItemPosition], newList[newItemPosition])
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return TextUtils.equals(
                oldList[oldItemPosition].javaClass.name,
                newList[newItemPosition].javaClass.name
            )
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

    }
}
