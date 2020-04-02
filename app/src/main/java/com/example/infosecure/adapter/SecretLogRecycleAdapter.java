package com.example.infosecure.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.infosecure.R;
import com.example.infosecure.entity.SecretLog;

import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

public class SecretLogRecycleAdapter extends RecyclerView.Adapter<SecretLogRecycleAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<SecretLog> mList;
    private View minflater;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    public SecretLogRecycleAdapter(Context context, ArrayList<SecretLog>list){
        this.context=context;
        this.mList =list;
    }


    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClicked(View v ,int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.mOnItemClickListener = clickListener;
    }
    public void setmOnItemLongClickListener(OnItemLongClickListener longclickListener) {
        this.mOnItemLongClickListener = longclickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        minflater=LayoutInflater.from(context).inflate(R.layout.item_secret_log,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(minflater,mOnItemClickListener, mOnItemLongClickListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,int position){
        holder.textTime.setText(mList.get(position).getTime());
        holder.textTitle.setText(mList.get(position).getContent());
        holder.textContent.setText(mList.get(position).getContent());
    }

    @Override
    public int getItemCount(){
        return mList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textTitle, textTime,textContent;
        public MyViewHolder(View itemView,final OnItemClickListener onClickListener,final OnItemLongClickListener onItemLongClickListener){
            super(itemView);
            textTitle=(TextView)itemView.findViewById(R.id.textV_log_title);
            textTime =(TextView)itemView.findViewById(R.id.textV_log_time);
            textContent=(TextView)itemView.findViewById(R.id.textV_log_content);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (onClickListener != null) {
                        int position = getAdapterPosition();
                        //确保position值有效
                        if (position != RecyclerView.NO_POSITION) {
                            onClickListener.onItemClicked(v, position);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        int position = getAdapterPosition();
                        //确保position值有效
                        if (position != RecyclerView.NO_POSITION) {
                            onItemLongClickListener.onItemLongClicked(v, position);
                        }
                    }
                    return false;
                }
            });
        }
    }
}
