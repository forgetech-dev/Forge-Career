package com.example.forgecareer.recyclecViews;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forgecareer.EditApplicationActivity;
import com.example.forgecareer.R;
import com.example.forgecareer.db.Application;

import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationVH>{
    private static final String TAG = "ApplicationAdapter";
    List<Application> applicationList;
    List<String> keyList;


    /**
     * Constructor
     * @param applicationList
     */
    public ApplicationAdapter(List<Application> applicationList, List<String> keyList) {
        this.applicationList = applicationList;
        this.keyList = keyList;
    }

    @NonNull
    @Override
    public ApplicationAdapter.ApplicationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_row, parent, false);
        return new ApplicationAdapter.ApplicationVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationAdapter.ApplicationVH holder, int position) {

        Application application = applicationList.get(position);
        holder.companyTextView.setText(application.getCompanyName());
        holder.jobTypeTextView.setText(application.getJobType());
        holder.positionTextView.setText(application.getPositionType());
        holder.priorityTextView.setText(application.getPriority());
        holder.notesTextView.setText(application.getNotes());
        holder.refererTextView.setText(application.getReferer());
        holder.startDateTextView.setText(application.getStartDate());
        holder.applicationDateTextView.setText(application.getApplicationDate());
        holder.interviewTextView.setText(application.getInterviewDate());

        holder.applicationKey = keyList.get(position);



        switch (application.getStatus()) {
            case "Interested":
                holder.statusImageView.setImageResource(R.drawable.status_interested);
                break;
            case "Applied":
                holder.statusImageView.setImageResource(R.drawable.status_applied);
                break;
            case "OA":
                holder.statusImageView.setImageResource(R.drawable.status_oa);
                break;
            case "Interview":
                holder.statusImageView.setImageResource(R.drawable.status_interview);
                break;
            case "Rejected":
                holder.statusImageView.setImageResource(R.drawable.status_reject);
                break;
            case "Offer":
                holder.statusImageView.setImageResource(R.drawable.status_offer);
                break;
        }


        boolean isExpanded = applicationList.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    class ApplicationVH extends RecyclerView.ViewHolder {

        private static final String TAG = "ApplicationVH";

        ConstraintLayout expandableLayout;
        ImageView statusImageView;
        TextView listBackgroundTextView, companyTextView, jobTypeTextView, positionTextView, priorityTextView, notesTextView;
        TextView refererTextView, startDateTextView, applicationDateTextView, interviewTextView;

        String applicationKey;

        public ApplicationVH(@NonNull final View itemView) {
            super(itemView);

            statusImageView = itemView.findViewById(R.id.statusImageView);

            listBackgroundTextView = itemView.findViewById(R.id.listBackgroundTextView);
            companyTextView = itemView.findViewById(R.id.companyTextView);
            jobTypeTextView = itemView.findViewById(R.id.jobTypeTextView);
            positionTextView = itemView.findViewById(R.id.positionTextView);
            priorityTextView = itemView.findViewById(R.id.priorityTextView);
            notesTextView = itemView.findViewById(R.id.notesTextView);
            refererTextView = itemView.findViewById(R.id.refererTextView);
            startDateTextView = itemView.findViewById(R.id.startDateTextView);
            applicationDateTextView = itemView.findViewById(R.id.appDateTextView);
            interviewTextView = itemView.findViewById(R.id.interviewDateTextView);

            expandableLayout = itemView.findViewById(R.id.expandableLayout);


            listBackgroundTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Application application = applicationList.get(getAdapterPosition());
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
