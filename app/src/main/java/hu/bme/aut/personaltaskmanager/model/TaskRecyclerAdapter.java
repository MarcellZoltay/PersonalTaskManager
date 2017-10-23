package hu.bme.aut.personaltaskmanager.model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.personaltaskmanager.R;

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.TaskViewHolder> {

    private final List<Task> items;

    public TaskRecyclerAdapter(){ items = new ArrayList<>(); }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        TaskViewHolder viewHolder = new TaskViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = items.get(position);
    }

    //@Override
    //public void onBindViewHolder(final TaskViewHolder holder, int position) {
    //      Task task = items.get(position);
          //holder.nameTextView.setText(item.name);
          //holder.descriptionTextView.setText(item.description);
          //holder.categoryTextView.setText(item.category.name());
          //holder.priceTextView.setText(item.estimatedPrice + " Ft");
          //holder.iconImageView.setImageResource(getImageResource(item.category));
          //holder.isBoughtCheckBox.setChecked(item.isBought);
    //
          //holder.isBoughtCheckBox.setOnClickListener(new View.OnClickListener() {
          //   @Override
          //   public void onClick(View view) {
          //      boolean checked = holder.isBoughtCheckBox.isChecked();
          //      Log.d(TAG, "onClick: checked = " + checked + " in position "
          //              + holder.getAdapterPosition());
          //      ShoppingItem item = items.get(holder.getAdapterPosition());
          //      item.isBought = checked;
          //      item.save();
          //   }
          //});
    //}

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder{

        public TaskViewHolder(View itemView) {
            super(itemView);
        }
    }
}
