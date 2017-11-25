package hu.bme.aut.personaltaskmanager.model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hu.bme.aut.personaltaskmanager.R;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.DateFormatHelper;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.ITaskFilter;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.OnTaskSelectedListener;

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private ITaskFilter filter;
    private OnTaskSelectedListener listener;

    public TaskRecyclerAdapter(ITaskFilter filter, OnTaskSelectedListener listener){
        this.filter = filter;
        tasks = DataManager.getInstance().getTasks(filter);
        this.listener = listener;
    }
    public TaskRecyclerAdapter(ITaskFilter filter){
        this.filter = filter;
        tasks = DataManager.getInstance().getTasks(filter);
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        TaskViewHolder viewHolder = new TaskViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.titleTextView.setText(task.getTitle());
        holder.dateTextView.setText(DateFormatHelper.getFormattedDate(task.getDate(), "yyyy.MM.dd. HH:mm"));
        holder.isDoneCheckbox.setChecked(task.isDone());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void update(){
        tasks = DataManager.getInstance().getTasks(filter);
        notifyDataSetChanged();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder{

        ImageView iconImageView;
        CheckBox isDoneCheckbox;
        TextView titleTextView;
        TextView dateTextView;

        public TaskViewHolder(final View itemView) {
            super(itemView);

            //iconImageView = (ImageView) itemView.findViewById(R.id.TaskFolderIconImageView);
            isDoneCheckbox = (CheckBox) itemView.findViewById(R.id.TaskDoneCheckBox);
            isDoneCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checked = isDoneCheckbox.isChecked();
                    Task t = tasks.get(getAdapterPosition());
                    t.setDone(checked);
                    t.save();
                    update();
                }
            });

            titleTextView = (TextView) itemView.findViewById(R.id.TaskTitle);
            dateTextView = (TextView) itemView.findViewById(R.id.TaskDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onTaskSelected(getAdapterPosition());
                    }
                }
            });
        }
    }
}
