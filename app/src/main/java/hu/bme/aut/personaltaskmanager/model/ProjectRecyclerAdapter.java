package hu.bme.aut.personaltaskmanager.model;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hu.bme.aut.personaltaskmanager.R;

public class ProjectRecyclerAdapter extends RecyclerView.Adapter<ProjectRecyclerAdapter.ProjectViewHolder>{

    private List<Project> projects;

    public ProjectRecyclerAdapter(){
        projects = DataManager.getInstance().getProjects();
        //projects = new ArrayList<>();
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item, parent, false);
        ProjectViewHolder viewHolder = new ProjectViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, final int position) {
        Project project = projects.get(position);

        holder.titleTextView.setText(project.getTitle());
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Project p = projects.remove(position);
                //p.delete();
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public void addProject(Project p) {
        projects.add(p);
        //p.save();
        notifyItemInserted(projects.size() - 1);
    }

    //public void update(List<Project> shoppingItems) {
    //    projects.clear();
    //    projects.addAll(shoppingItems);
    //    notifyDataSetChanged();
    //}


    public class ProjectViewHolder extends RecyclerView.ViewHolder{

        ImageView iconImageView;
        TextView titleTextView;
        ImageButton removeButton;

        public ProjectViewHolder(View itemView) {
            super(itemView);

            iconImageView = (ImageView) itemView.findViewById(R.id.ProjectFolderIconImageView);
            titleTextView = (TextView) itemView.findViewById(R.id.ProjectTitle);
            removeButton = (ImageButton) itemView.findViewById(R.id.ProjectRemoveButton);
        }
    }
}
