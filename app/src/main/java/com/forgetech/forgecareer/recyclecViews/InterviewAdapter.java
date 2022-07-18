package com.forgetech.forgecareer.recyclecViews;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.forgetech.forgecareer.EditApplicationActivity;
import com.forgetech.forgecareer.R;
import com.forgetech.forgecareer.db.Application;

import java.util.ArrayList;
import java.util.Map;

public class InterviewAdapter extends RecyclerView.Adapter<InterviewAdapter.InterviewVH>{
    private static final String TAG = "InterviewAdapter";
    private ArrayList<Map.Entry<String, Application>> applicationEntries;

    public InterviewAdapter(ArrayList<Map.Entry<String, Application>> applicationEntries) {
        this.applicationEntries = applicationEntries;

    }

    @NonNull
    @Override
    public InterviewAdapter.InterviewVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interview_row, parent, false);
        return new InterviewAdapter.InterviewVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterviewAdapter.InterviewVH holder, int position) {
        Map.Entry<String, Application> entry = applicationEntries.get(position);
        Application application = entry.getValue();
        boolean isExpanded = applicationEntries.get(position).getValue().isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.companyTextView.setText(application.getCompanyName());
        holder.jobTypeTextView.setText(application.getJobType());
        holder.positionTextView.setText(application.getPositionType());
        holder.priorityTextView.setText(application.getPriority());
        holder.notesTextView.setText(application.getNotes());
        holder.refererTextView.setText(application.getReferer());
        holder.startDateTextView.setText(application.getStartDate());
        holder.appDateTextView.setText(application.getApplicationDate());
        holder.currentInterviewDateTextView.setText(application.getInterviewDate());

        holder.applicationKey = entry.getKey();


    }

    @Override
    public int getItemCount() {
        return applicationEntries.size();
    }











    class InterviewVH extends RecyclerView.ViewHolder {
        private static final String TAG = "InterviewVH";
        TextView listBackgroundTextView;
        TextView companyTextView, currentInterviewDateTextView, positionTextView, refererTextView, startDateTextView, appDateTextView, notesTextView, jobTypeTextView, priorityTextView;
        ConstraintLayout expandableLayout;

        String applicationKey;

        public InterviewVH(@NonNull final View itemView) {
            super(itemView);


            listBackgroundTextView = itemView.findViewById(R.id.listBackgroundTextView);
            companyTextView = itemView.findViewById(R.id.interviewCompanyTextView);
            currentInterviewDateTextView = itemView.findViewById(R.id.currentInterviewDateTextView);
            positionTextView = itemView.findViewById(R.id.positionTextView);
            priorityTextView = itemView.findViewById(R.id.priorityTextView);
            notesTextView = itemView.findViewById(R.id.notesTextView);
            refererTextView = itemView.findViewById(R.id.refererTextView);
            startDateTextView = itemView.findViewById(R.id.startDateTextView);
            appDateTextView = itemView.findViewById(R.id.appDateTextView);
            jobTypeTextView = itemView.findViewById(R.id.jobTypeTextView);

            expandableLayout = itemView.findViewById(R.id.expandableLayout);

            listBackgroundTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Application application = applicationEntries.get(getAdapterPosition()).getValue();
                    application.setExpanded(!application.isExpanded());
                    notifyItemChanged(getAdapterPosition());

                }
            });
            listBackgroundTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(v.getContext(), EditApplicationActivity.class);
                    intent.putExtra("applicationKey", applicationKey);
                    v.getContext().startActivity(intent);
                    return false;
                }
            });
        }
    }
}
