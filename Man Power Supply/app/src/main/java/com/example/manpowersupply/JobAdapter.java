package com.example.manpowersupply;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    private List<Job> jobList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Job job);
    }


    public interface OnItemLongClickListener {
        void onItemLongClick(Job job);
    }

    private OnItemLongClickListener longClickListener;

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    public JobAdapter(List<Job> jobList, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
        this.jobList = jobList;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.bind(job, listener, longClickListener);
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewJobTitle, textViewJobDescription, textViewDeadline;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewJobTitle = itemView.findViewById(R.id.textViewJobTitle);
            textViewJobDescription = itemView.findViewById(R.id.textViewJobDescription);
            textViewDeadline = itemView.findViewById(R.id.deadLine_date);
        }




        public void bind(final Job job, final OnItemClickListener listener,final OnItemLongClickListener longClickListener) {
            textViewJobTitle.setText(job.getTitle());
            textViewJobDescription.setText("Experiance: " + job.getSkills());
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date = inputFormat.parse(job.getDeadline());
                String formattedDate = outputFormat.format(date);
                textViewDeadline.setText(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
                textViewDeadline.setText("Deadline: N/A");
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(job);
                }
            });

            itemView.setOnLongClickListener(v -> {
                longClickListener.onItemLongClick(job);
                return true;
            });
        }

    }
}
