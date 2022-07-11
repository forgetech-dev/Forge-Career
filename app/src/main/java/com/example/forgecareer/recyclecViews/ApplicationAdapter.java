package com.example.forgecareer.recyclecViews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forgecareer.R;
import com.example.forgecareer.db.Application;

import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationVH>{
    private static final String TAG = "ApplicationAdapter";
    List<Application> applicationList;


    /**
     * Constructor
     * @param applicationList
     */
    public ApplicationAdapter(List<Application> applicationList) {
        this.applicationList = applicationList;
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

        switch (application.getStatus()) {
            case "Interested":
                holder.statusImageView.setImageResource(R.drawable.status_interested);
            case "Applied":
                holder.statusImageView.setImageResource(R.drawable.status_applied);
            case "OA":
                holder.statusImageView.setImageResource(R.drawable.status_oa);
            case "Interview":
                holder.statusImageView.setImageResource(R.drawable.status_interview);
            case "Rejected":
                holder.statusImageView.setImageResource(R.drawable.status_reject);
            case "Offer":
                holder.statusImageView.setImageResource(R.drawable.status_offer);
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
        TextView companyTextView, jobTypeTextView, positionTextView, priorityTextView, notesTextView;

        public ApplicationVH(@NonNull final View itemView) {
            super(itemView);

            statusImageView = itemView.findViewById(R.id.statusImageView);

            companyTextView = itemView.findViewById(R.id.companyTextView);
            jobTypeTextView = itemView.findViewById(R.id.jobTypeTextView);
            positionTextView = itemView.findViewById(R.id.positionTextView);
            priorityTextView = itemView.findViewById(R.id.priorityTextView);
            notesTextView = itemView.findViewById(R.id.notesTextView);

            expandableLayout = itemView.findViewById(R.id.expandableLayout);


            companyTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Application application = applicationList.get(getAdapterPosition());
                    application.setExpanded(!application.isExpanded());
                    notifyItemChanged(getAdapterPosition());

                }
            });
        }
    }

}
