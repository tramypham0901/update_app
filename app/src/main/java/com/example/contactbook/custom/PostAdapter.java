package com.example.contactbook.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.contactbook.R;
import com.example.contactbook.model.Post;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyHolder> {

    List<Post> items;
    List<String> imageList;
    private Context context;
    private int layout;

    public PostAdapter(Context context, List<Post> items, List<String> imageList) {
        super();
        this.context = context;
        this.items = items;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_posts, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        Post post = items.get(position);
        holder.txtTitle.setText(post.getPostName());
        holder.txtUser.setText(post.getCreatedBy());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = format.parse(post.getPostDate());
            SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm dd-MM-YY");
            holder.txtTime.setText(newFormat.format(date));
        } catch (ParseException e) {
            holder.txtTime.setText(post.getPostDate());
            e.printStackTrace();
        }
        holder.txtDes.setText(post.getDescription());
        for(String imgUrl: imageList) {
            if(imgUrl.matches(".+img-\\d+-.+") && imgUrl.contains("img-"+post.getPostId()+"-")){
                Glide.with(holder.itemView.getContext()).load(imgUrl).into(holder.txtImg);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtTime, txtTitle, txtUser, txtDes;
        ImageView txtImg;

        public MyHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.ptitletv);
            txtUser = (TextView) itemView.findViewById(R.id.unametv);
            txtTime = (TextView) itemView.findViewById(R.id.utimetv);
            txtDes = (TextView) itemView.findViewById(R.id.descript);
            txtImg = (ImageView) itemView.findViewById(R.id.pimagetv);
        }
    }

}
