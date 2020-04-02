package com.example.infosecure.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.infosecure.R;
import com.example.infosecure.entity.ConPerson;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

public class ConPerRecycleAdapter extends RecyclerView.Adapter<ConPerRecycleAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ConPerson> mList;
    private View minflater;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    public ConPerRecycleAdapter(Context context, ArrayList<ConPerson>list){
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
        minflater=LayoutInflater.from(context).inflate(R.layout.item_connect_person,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(minflater,mOnItemClickListener,mOnItemLongClickListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,int position){
        holder.textConPerson.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount(){
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textConPerson;

        public MyViewHolder(View itemView,final OnItemClickListener onItemClickListener,final OnItemLongClickListener onItemLongClickListener){
            super(itemView);
            textConPerson=(TextView)itemView.findViewById(R.id.textV_con_person);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (onItemClickListener!= null) {
                        int position = getAdapterPosition();
                        //确保position值有效
                        if (position!= RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClicked(v, position);
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
