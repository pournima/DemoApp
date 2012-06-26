package com.github.branch;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.R;


public class BranchListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	public String str;
	private static ArrayList<BranchDataModel> teamArrayList;

	public BranchListAdapter(Context context,
			ArrayList<BranchDataModel> data) {
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
			convertView = mInflater.inflate(R.layout.repository_custom_text, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.textView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(teamArrayList.get(position).getBranchName());

		return convertView;
	}

	static class ViewHolder {
		TextView name;

	}
}
