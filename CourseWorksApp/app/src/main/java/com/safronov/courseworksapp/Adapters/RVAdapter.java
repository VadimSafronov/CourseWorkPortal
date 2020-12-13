package com.safronov.courseworksapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.safronov.courseworksapp.Fragments.WorksDetailFragment;
import com.safronov.courseworksapp.MainActivity;
import com.safronov.courseworksapp.Models.CourseWork;
import com.safronov.courseworksapp.R;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.WorksViewHolder> {

    private static List<CourseWork> works;

    private static Context context;
    private static WorksDetailFragment detail_fragment;

    public RVAdapter(List<CourseWork> works, Context context) {
        this.works = works;
        this.context = context;
        detail_fragment = new WorksDetailFragment();
    }

    public void notifyChanged(List<CourseWork> works) {
        this.works = works;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public WorksViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_item, viewGroup, false);
        WorksViewHolder pvh = new WorksViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(WorksViewHolder worksViewHolder, final int i) {
        worksViewHolder.workName.setText(works.get(i).name);
        worksViewHolder.workFile.setText("File: " + "\"" + works.get(i).file_name + "\"");

        worksViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof MainActivity) {

                    FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                    FragmentTransaction transact = fragmentManager.beginTransaction();

                    Bundle args = new Bundle();
                    args.putInt("id", works.get(i).id);
                    detail_fragment.setArguments(args);

                    transact.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    transact.replace(R.id.container, detail_fragment);
                    transact.addToBackStack(null);
                    transact.commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return works.size();
    }

    public static class WorksViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView workName;
        TextView workFile;

        WorksViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            workName = itemView.findViewById(R.id.work_name);
            workFile = itemView.findViewById(R.id.work_file);
        }
    }
}
