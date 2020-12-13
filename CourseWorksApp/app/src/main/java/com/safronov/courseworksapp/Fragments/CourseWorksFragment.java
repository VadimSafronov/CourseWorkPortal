package com.safronov.courseworksapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.safronov.courseworksapp.Adapters.RVAdapter;
import com.safronov.courseworksapp.Models.CourseWork;
import com.safronov.courseworksapp.R;

import java.util.List;

public class CourseWorksFragment extends Fragment {

    private List<CourseWork> courseWorks;
    private RecyclerView rv;
    private RVAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_course_works, container, false);

        rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);

        initializeAdapter();

        return view;
    }

    public void initializeData(List<CourseWork> courseWorks) {
        this.courseWorks = courseWorks;
    }

    public void initializeAdapter() {
        adapter = new RVAdapter(courseWorks, getActivity());
        rv.setAdapter(adapter);
    }

    public void notifyChanges() {
        adapter.notifyChanged(courseWorks);
    }
}
