package hu.bme.aut.personaltaskmanager.ui.handling_tasks;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hu.bme.aut.personaltaskmanager.R;
import hu.bme.aut.personaltaskmanager.model.DataManager;
import hu.bme.aut.personaltaskmanager.model.Project;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.project_fragments.ProjectDialogFragment;

public class ProjectRecyclerAdapter extends RecyclerView.Adapter<ProjectRecyclerAdapter.ProjectViewHolder>{

    public static final int EDIT_PROJECT_REQUEST_CODE = 101;

    private List<Project> projects;
    private OnProjectSelectedListener listener;
    private Fragment parentFragment;

    public ProjectRecyclerAdapter(Fragment frag, OnProjectSelectedListener listener){
        projects = DataManager.getInstance().getProjects();
        this.parentFragment = frag;
        this.listener = listener;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item, parent, false);
        ProjectViewHolder viewHolder = new ProjectViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        Project project = projects.get(position);
        holder.titleTextView.setText(project.getTitle());
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }


    public void update(){ notifyDataSetChanged(); }


    public class ProjectViewHolder extends RecyclerView.ViewHolder{

        ImageView iconImageView;
        TextView titleTextView;

        public ProjectViewHolder(final View itemView) {
            super(itemView);

            iconImageView = (ImageView) itemView.findViewById(R.id.ProjectFolderIconImageView);
            titleTextView = (TextView) itemView.findViewById(R.id.ProjectTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onProjectSelected(getAdapterPosition());
                    }
                }
            });
            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                    contextMenu.setHeaderTitle(R.string.choose_an_action);
                    contextMenu.add(R.string.edit_project).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            Bundle b = new Bundle();
                            b.putInt(itemView.getContext().getString(R.string.project_position), getAdapterPosition());
                            ProjectDialogFragment dialog = new ProjectDialogFragment();
                            dialog.setTargetFragment(parentFragment, EDIT_PROJECT_REQUEST_CODE);
                            dialog.setArguments(b);
                            dialog.show(parentFragment.getFragmentManager(), ProjectDialogFragment.TAG);
                            return true;
                        }
                    });
                    contextMenu.add(R.string.delete_project).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(itemView.getContext());
                            adb.setMessage(R.string.delete_project_alert_msg);
                            adb.setTitle("Delete " + projects.get(getAdapterPosition()).getTitle() + " Project");
                            adb.setIcon(android.R.drawable.ic_dialog_alert);
                            adb.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Project p = projects.remove(getAdapterPosition());
                                    DataManager.getInstance().removeProject(p);
                                    notifyItemRemoved(getAdapterPosition());
                                }
                            });
                            adb.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    return;
                                }
                            });
                            adb.show();
                            return true;
                        }
                    });
                }
            });
        }
    }
}
