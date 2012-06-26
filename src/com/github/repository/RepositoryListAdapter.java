package com.github.repository;

import java.util.ArrayList;

import com.github.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class RepositoryListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	public String str;
	private static ArrayList<RepositoryDataModel> teamArrayList;

	public RepositoryListAdapter(Context context,
			ArrayList<RepositoryDataModel> data) {
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

		holder.name.setText(teamArrayList.get(position).getName());

		return convertView;
	}

	static class ViewHolder {
		TextView name;

	}
}
