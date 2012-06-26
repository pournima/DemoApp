package com.github.commits;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.R;

public class CommitListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	public String str;
	private static ArrayList<CommitsDataModel> teamArrayList;

	public CommitListAdapter(Context context,
			ArrayList<CommitsDataModel> data) {
		teamArrayList = data;
		mInflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		return teamArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.commit_custom_text, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.textName);
			holder.date=(TextView) convertView.findViewById(R.id.textDate);
			holder.message=(TextView) convertView.findViewById(R.id.textMessage);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(teamArrayList.get(position).getName());
		holder.date.setText(teamArrayList.get(position).getDate());
		holder.message.setText(teamArrayList.get(position).getMessage());
		
		return convertView;
	}

	static class ViewHolder {
		TextView name;
		TextView date;
		TextView message;

	}
}
