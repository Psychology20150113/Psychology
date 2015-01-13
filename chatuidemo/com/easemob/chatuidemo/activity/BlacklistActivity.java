package com.easemob.chatuidemo.activity;

import java.util.Collections;
import java.util.List;

import com.easemob.chat.EMContactManager;
import com.dcy.psychology.R;
import com.easemob.chatuidemo.adapter.ContactAdapter;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.exceptions.EaseMobException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * 榛戝悕鍗曞垪琛ㄩ〉闈�
 * 
 */
public class BlacklistActivity extends Activity {
	private ListView listView;
	private BlacklistAdapater adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black_list);

		listView = (ListView) findViewById(R.id.list);

		List<String> blacklist = null;
		try {
			// 鑾峰彇榛戝悕鍗�
			blacklist = EMContactManager.getInstance().getBlackListUsernames();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 鏄剧ず榛戝悕鍗曞垪琛�
		if (blacklist != null) {
			Collections.sort(blacklist);
			adapter = new BlacklistAdapater(this, 1, blacklist);
			listView.setAdapter(adapter);
		}

		// 娉ㄥ唽涓婁笅鏂囪彍鍗�
		registerForContextMenu(listView);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.remove_from_blacklist, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.remove) {
			final String tobeRemoveUser = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			// 鎶婄洰鏍噓ser绉诲嚭榛戝悕鍗�
			removeOutBlacklist(tobeRemoveUser);
			return true;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * 绉诲嚭榛戞皯鍗�
	 * 
	 * @param tobeRemoveUser
	 */
	void removeOutBlacklist(final String tobeRemoveUser) {
		try {
			// 绉诲嚭榛戞皯鍗�
			EMContactManager.getInstance().deleteUserFromBlackList(tobeRemoveUser);
			adapter.remove(tobeRemoveUser);
		} catch (EaseMobException e) {
			e.printStackTrace();
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(getApplicationContext(), "绉诲嚭澶辫触", 0).show();
				}
			});
		}
	}

	/**
	 * adapter
	 * 
	 */
	private class BlacklistAdapater extends ArrayAdapter<String> {

		public BlacklistAdapater(Context context, int textViewResourceId, List<String> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getContext(), R.layout.row_contact, null);
			}

			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(getItem(position));

			return convertView;
		}

	}

	/**
	 * 杩斿洖
	 * 
	 * @param view
	 */
	public void back(View view) {
		finish();
	}
}
