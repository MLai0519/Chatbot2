package hk.edu.cuhk.ie.iems5722.a2_1155079374;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import hk.edu.cuhk.ie.iems5722.a2_1155079374.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static hk.edu.cuhk.ie.iems5722.a2_1155079374.MainActivity.UserID;


public class DisplayDataAdapter extends RecyclerView.Adapter<DisplayDataAdapter.DisplayDataViewHolder> {
    //    private Map<String, String> requestStatus = new LinkedHashMap<>();
//    private Map<String, String> DriverMap = new LinkedHashMap<>();
//    private Map<String, Object> boxNumberMap = new LinkedHashMap<>();
//    private Map<Integer, Integer> boxSampleCountMap = new LinkedHashMap<>();
    private List<Message> mDataset = new ArrayList<>();
    private Context context;
    chatActivity main;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class DisplayDataViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView chat,user;
        public TextView otherchat,otheruser;
        public ConstraintLayout other,mine;
        public TextView othertime;

        public TextView time;
        //        public TextView sampleCount;
//        public TextView sealId;
//        //        public ImageView status;
//        public TextView have;
//        public TextView serial;
//        public TextView unit;

        public DisplayDataViewHolder(View v) {
            super(v);
            other = v.findViewById(R.id.other);
            mine = v.findViewById(R.id.mine);
            chat = v.findViewById(R.id.chat);
            time = v.findViewById(R.id.time);
            user = v.findViewById(R.id.user);
            otherchat = v.findViewById(R.id.otherchat);
            othertime = v.findViewById(R.id.othertime);
            otheruser = v.findViewById(R.id.otheruser);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DisplayDataAdapter(Context context, List<Message> requestStatus, chatActivity main) {
        this.context = context;
        this.main = main;
        if (requestStatus!=null)
            this.mDataset.addAll(requestStatus);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DisplayDataAdapter.DisplayDataViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                               int viewType) {
        // create a new view
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_card, parent, false);

        DisplayDataViewHolder vh = new DisplayDataViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(DisplayDataViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        if (mDataset.get(position) != null) {
            Log.d("testing",mDataset.get(position).getUser_id()+"String.valueOf"+(UserID));
            if(mDataset.get(position).getUser_id().equals(String.valueOf(UserID))) {
                holder.mine.setVisibility(View.VISIBLE);
                holder.other.setVisibility(View.GONE);
                holder.chat.setText(mDataset.get(position).getMessage());
                holder.time.setText(mDataset.get(position).getMessage_time());
                holder.user.setText("User:" + mDataset.get(position).getName());
            }
            else{
                holder.mine.setVisibility(View.GONE);
                holder.other.setVisibility(View.VISIBLE);
                holder.otherchat.setText(mDataset.get(position).getMessage());
                holder.othertime.setText(mDataset.get(position).getMessage_time());
                holder.otheruser.setText("User:" + mDataset.get(position).getName());
            }
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void update(List<Message> requestStatus) {
        this.mDataset.clear();
//        Log.d("test",requestStatus.get(0).getId()+"test"+requestStatus.get(requestStatus.size()-1).getId());
//        Collections.reverse(requestStatus);
        this.mDataset.addAll(requestStatus);
        Log.d("test",mDataset.get(0).getId()+"test"+mDataset.get(mDataset.size()-1).getId());
        notifyDataSetChanged();
    }
}