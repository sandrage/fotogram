package com.project.fotogram.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.fotogram.R;
import com.project.fotogram.model.Friend;
import com.project.fotogram.utility.UtilityMethods;

import java.util.ArrayList;
import java.util.List;

public class SearchListAdapter extends ArrayAdapter<Friend> implements Filterable {
    private List<Friend> friends;
    private int layout;
    private Context callerContext;
    private FriendsFilter friendsFilter = new FriendsFilter();

    public SearchListAdapter(Context context, int layoutId) {
        super(context, layoutId);
        this.friends = new ArrayList<>();
        this.layout = layoutId;
        this.callerContext = context;
    }

    public SearchListAdapter(Context context, int layoutId, List<Friend> friendsList) {
        super(context, layoutId, friendsList);
        this.friends = friendsList;
        this.layout = layoutId;
        this.callerContext = context;
    }

    public void setData(List<Friend> friends) {
        this.friends = new ArrayList<>();
        this.friends.addAll(friends);
    }

    @Override
    public int getCount() {
        return this.friends.size();
    }

    @Override
    public Friend getItem(int position) {
        return this.friends.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        Log.d("fotogram", "entro quiii getBview");
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
        }
        Friend friend = getItem(position);
        try {
            if (friend != null) {
                TextView username = (TextView) view.findViewById(R.id.partialresult_username);
                ImageView userImage = (ImageView) view.findViewById(R.id.partialresult_userimage);
                username.setText(getItem(position).getName());
                if (friend.getPicture() != null) {
                    if (friend.getPicture() != null) {
                        Log.d("fotogramLogs", "profileName:" + friend.getName() + "\t" + "profilePhoto string: " + friend.getPicture());
                        byte[] decodedPostImageString = Base64.decode(friend.getPicture(), Base64.DEFAULT);
                        Bitmap bitm = BitmapFactory.decodeByteArray(decodedPostImageString, 0, decodedPostImageString.length);
                        userImage.setImageBitmap(UtilityMethods.getRoundedBitmapToDisplay(bitm, bitm.getWidth(), bitm.getHeight()));
                    }
                } else {
                    userImage.setImageBitmap(null);
                    userImage.setImageDrawable(this.callerContext.getResources().getDrawable(R.drawable.ic_round_account_circle_24px, null));
                }
            }
        } catch (Exception e) {
            Log.e("fotogramLogs", "Unexpected exception: ", e);
        }

        return view;
    }

    @Override
    public FriendsFilter getFilter() {
        return this.friendsFilter;
    }

    private class FriendsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            Log.d("fotogram", "contraint: " + constraint);
            if (constraint != null) {
                Log.d("fotogramLogs", "constraint:: " + constraint.toString());
                filterResults.count = friends.size();
                filterResults.values = friends;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults result) {
            if (result != null && result.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
