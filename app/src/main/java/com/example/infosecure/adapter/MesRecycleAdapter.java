package com.example.infosecure.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.infosecure.R;
import com.example.infosecure.entity.Message;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

public class MesRecycleAdapter extends RecyclerView.Adapter<MesRecycleAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Message> mList;
    private View minflater;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    public MesRecycleAdapter(Context context, ArrayList<Message>list){
        this.context=context;
        this.mList =list;
    }
    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClicked(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.mOnItemClickListener = clickListener;
    }
    public void setmOnItemLongClickListener(OnItemLongClickListener longclickListener) {
        this.mOnItemLongClickListener = longclickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        minflater=LayoutInflater.from(context).inflate(R.layout.item_message,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(minflater,mOnItemClickListener,mOnItemLongClickListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,int position){
        holder.textDate.setText(mList.get(position).getDate());
        holder.textName.setText(mList.get(position).getName());
        holder.textContent.setText(mList.get(position).getContent());
        holder.imagePerson.setImageResource(R.drawable.ic_launcher_foreground);
    }

    @Override
    public int getItemCount(){
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textName,textContent, textDate;
        ImageView imagePerson;
        public MyViewHolder(View itemView, final OnItemClickListener onClickListener, final OnItemLongClickListener onItemLongClickListener){
            super(itemView);
            textName=(TextView)itemView.findViewById(R.id.textV_personName);
            textContent=(TextView)itemView.findViewById(R.id.textV_mes_content);
            textDate =(TextView)itemView.findViewById(R.id.textV_date);
            imagePerson=(ImageView)itemView.findViewById(R.id.imageV_person);
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
