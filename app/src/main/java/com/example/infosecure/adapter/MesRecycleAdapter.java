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
    public MesRecycleAdapter(Context context, ArrayList<Message>list){
        this.context=context;
        this.mList =list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        minflater=LayoutInflater.from(context).inflate(R.layout.item_message,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(minflater);
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
        public MyViewHolder(View itemView){
            super(itemView);
            textName=(TextView)itemView.findViewById(R.id.textV_personName);
            textContent=(TextView)itemView.findViewById(R.id.textV_mes_content);
            textDate =(TextView)itemView.findViewById(R.id.textV_date);
            imagePerson=(ImageView)itemView.findViewById(R.id.imageV_person);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                }
            });
        }
    }
}
