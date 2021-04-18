package hk.edu.cuhk.ie.iems5722.a2_1155079374;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static hk.edu.cuhk.ie.iems5722.a2_1155079374.MainActivity.UserName;


public class DisplayroomAdapter extends RecyclerView.Adapter<DisplayroomAdapter.DisplayDataViewHolder> {
    //    private Map<String, String> requestStatus = new LinkedHashMap<>();
//    private Map<String, String> DriverMap = new LinkedHashMap<>();
//    private Map<String, Object> boxNumberMap = new LinkedHashMap<>();
//    private Map<Integer, Integer> boxSampleCountMap = new LinkedHashMap<>();
    private List<Data> mDataset = new ArrayList<>();
    private Context context;
    MainActivity main;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class DisplayDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView room;

        //        public TextView sampleCount;
//        public TextView sealId;
//        //        public ImageView status;
//        public TextView have;
//        public TextView serial;
//        public TextView unit;

        public DisplayDataViewHolder(View v) {
            super(v);
            room = v.findViewById(R.id.roomname);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            int id = mDataset.get(getAdapterPosition()).getId();
            String name = mDataset.get(getAdapterPosition()).getName();
            main.toChatRoom(id,name);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DisplayroomAdapter(Context context, List<Data> requestStatus, MainActivity main) {
        this.context = context;
        this.main = main;
        if (requestStatus!=null)
            this.mDataset.addAll(requestStatus);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DisplayroomAdapter.DisplayDataViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        // create a new view
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatroom_card, parent, false);

        DisplayDataViewHolder vh = new DisplayDataViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(DisplayDataViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        if (mDataset.get(position) != null) {
            String roomname;
            roomname = mDataset.get(position).getName().replace(UserName,"");
            roomname = roomname.replace("&","");
            roomname =  roomname.replace(" ","");
            holder.room.setText(roomname);

        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void update(List<Data> requestStatus) {
        this.mDataset.clear();
//        Collections.reverse(requestStatus);
        this.mDataset.addAll(requestStatus);
        notifyDataSetChanged();
    }
}