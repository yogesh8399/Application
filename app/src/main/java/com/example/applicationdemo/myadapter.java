package com.example.applicationdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myadapter extends FirebaseRecyclerAdapter<model,myadapter.myviewholder> {


    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder myviewholder, int i, @NonNull model model) {
       myviewholder.fname.setText(model.getFname());
        myviewholder.lname.setText(model.getLname());
        myviewholder.gender.setText(model.getGender());
        myviewholder.countery.setText(model.getCountry());
        myviewholder.age.setText(String.valueOf(model.getAge()));
       //System.out.println(myviewholder.age);




       //Glide.with(myviewholder.imageView.getContext()).load(model.getUrl()).into(myviewholder.imageView);

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_design,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView fname;
        TextView lname;
        TextView gender;
        TextView countery;
        TextView age;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imgView);
            fname=itemView.findViewById(R.id.fName);
            lname=itemView.findViewById(R.id.lName);
            gender=itemView.findViewById(R.id.gender1);
            countery=itemView.findViewById(R.id.country1);
            age=itemView.findViewById(R.id.age1);
        }
    }
}
