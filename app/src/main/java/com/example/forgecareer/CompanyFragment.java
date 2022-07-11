package com.example.forgecareer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.forgecareer.db.Application;
import com.example.forgecareer.recyclecViews.ApplicationAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;


    List<Application> applicationList;

    public CompanyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompanyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompanyFragment newInstance(String param1, String param2) {
        CompanyFragment fragment = new CompanyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company, container, false);
        initApplicationData();
        recyclerView = view.findViewById(R.id.recyclerView);
        if (recyclerView == null) {
            Log.d("list", "null!!!");
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ApplicationAdapter applicationAdapter = new ApplicationAdapter(applicationList);
        recyclerView.setAdapter(applicationAdapter);

        return view;
    }


    private void initApplicationData() {
        applicationList = new ArrayList<>();
        applicationList.add(new Application("Amazon", "Intern", "SWE", "Summer23", "Nick", "Applied", "09/01/2022", "Ultra", "N/A", "this is the note for Amazon application"));
        applicationList.add(new Application("Google", "FullTime", "SDE", "Summer23", "Tom", "Interview", "09/02/2022", "High", "10/05/2022", "this is the note for google application"));
        applicationList.add(new Application("Waymo", "Intern", "FullStack", "Summer23", "Kshitiz", "Interested", "N/A", "High", "N/A", "This is the note for waymo application test row, trying to make it longer so that it can be a little bit different from the other two rows."));
        applicationList.add(new Application("Meta", "Intern", "SWE", "Summer23", "Ouyang", "Applied", "08/01/2022", "Ultra", "N/A", "This is a note for the meta application"));
    }
}