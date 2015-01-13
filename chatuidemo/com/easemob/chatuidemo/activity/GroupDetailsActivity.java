/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo.activity;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.easemob.chatuidemo.widget.ExpandGridView;
import com.easemob.util.EMLog;
import com.easemob.util.NetUtils;

public class GroupDetailsActivity extends BaseActivity {
	private static final String TAG = "GroupDetailsActivity";
	private static final int REQUEST_CODE_ADD_USER = 0;
	private static final int REQUEST_CODE_EXIT = 1;
	private static final int REQUEST_CODE_EXIT_DELETE = 2;
	private static final int REQUEST_CODE_CLEAR_ALL_HISTORY=3;
	
	private ExpandGridView userGridview;
	private String groupId;
	private ProgressBar loadingPB;
	private Button exitBtn;
	private Button deleteBtn;
	private EMGroup group;
	private GridAdapter adapter;
	private int referenceWidth;
	private int referenceHeight;
	private ProgressDialog progressDialog;
	
	public static GroupDetailsActivity instance;
	
	//娓呯┖鎵�鏈夎亰澶╄褰�
	private RelativeLayout clearAllHistory;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_details);
		instance = this;
		clearAllHistory=(RelativeLayout) findViewById(R.id.clear_all_history);
		userGridview = (ExpandGridView) findViewById(R.id.gridview);
		loadingPB = (ProgressBar) findViewById(R.id.progressBar);
		exitBtn = (Button) findViewById(R.id.btn_exit_grp);
		deleteBtn = (Button) findViewById(R.id.btn_exitdel_grp);

		Drawable referenceDrawable = getResources().getDrawable(R.drawable.smiley_add_btn);
		referenceWidth = referenceDrawable.getIntrinsicWidth();
		referenceHeight = referenceDrawable.getIntrinsicHeight();

		// 鑾峰彇浼犺繃鏉ョ殑groupid
		groupId = getIntent().getStringExtra("groupId");
		group = EMGroupManager.getInstance().getGroup(groupId);

		// 濡傛灉鑷繁鏄兢涓伙紝鏄剧ず瑙ｆ暎鎸夐挳
		if(group.getOwner() == null || "".equals(group.getOwner())){
			exitBtn.setVisibility(View.GONE);
			deleteBtn.setVisibility(View.GONE);
		}
		if (EMChatManager.getInstance().getCurrentUser().equals(group.getOwner())) {
			exitBtn.setVisibility(View.GONE);
			deleteBtn.setVisibility(View.VISIBLE);
		}
		((TextView) findViewById(R.id.group_name)).setText(group.getGroupName()+"("+group.getAffiliationsCount()+"浜�)");
		adapter = new GridAdapter(this, R.layout.grid, group.getMembers());
		userGridview.setAdapter(adapter);

		// 淇濊瘉姣忔杩涜鎯呯湅鍒扮殑閮芥槸鏈�鏂扮殑group
		updateGroup();

		// 璁剧疆OnTouchListener
		userGridview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (adapter.isInDeleteMode) {
						adapter.isInDeleteMode = false;
						adapter.notifyDataSetChanged();
						return true;
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
		
		clearAllHistory.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(GroupDetailsActivity.this, AlertDialog.class);
				intent.putExtra("cancel",true);
				intent.putExtra("titleIsCancel", true);
				intent.putExtra("msg","纭畾鍒犻櫎缇ょ殑鑱婂ぉ璁板綍鍚楋紵");
				startActivityForResult(intent, REQUEST_CODE_CLEAR_ALL_HISTORY);
			}
		});
		
		
	}

	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(GroupDetailsActivity.this);
				progressDialog.setMessage("姝ｅ湪娣诲姞...");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
			}
			switch (requestCode) {
			case REQUEST_CODE_ADD_USER:// 娣诲姞缇ゆ垚鍛�
				final String[] newmembers = data.getStringArrayExtra("newmembers");
				addMembersToGroup(newmembers);

				break;
			case REQUEST_CODE_EXIT: // 閫�鍑虹兢
				progressDialog.setMessage("姝ｅ湪閫�鍑虹兢鑱�...");
				exitGrop();
				break;
			case REQUEST_CODE_EXIT_DELETE: // 瑙ｆ暎缇�
				progressDialog.setMessage("姝ｅ湪瑙ｆ暎缇よ亰...");
				deleteGrop();
				break;
			case REQUEST_CODE_CLEAR_ALL_HISTORY:
				//鍒犻櫎姝ょ兢鑱婄殑鑱婂ぉ璁板綍
				progressDialog.setMessage("姝ｅ湪鍒犻櫎缇ゆ秷鎭�...");
				
				deleteGroupHistory();
				
				
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 鐐瑰嚮閫�鍑虹兢缁勬寜閽�
	 * 
	 * @param view
	 */
	public void exitGroup(View view) {
		startActivityForResult(new Intent(this, ExitGroupDialog.class), REQUEST_CODE_EXIT);

	}

	/**
	 * 鐐瑰嚮瑙ｆ暎缇ょ粍鎸夐挳
	 * 
	 * @param view
	 */
	public void exitDeleteGroup(View view) {
		startActivityForResult(new Intent(this, ExitGroupDialog.class).putExtra("deleteToast", getString(R.string.dissolution_group_hint)),
				REQUEST_CODE_EXIT_DELETE);

	}

	
	
	
	/**
	 * 鍒犻櫎缇よ亰澶╄褰�
	 */
	public void deleteGroupHistory(){
		
		
		EMChatManager.getInstance().deleteConversation(group.getGroupId());
		progressDialog.dismiss();
//		adapter.refresh(EMChatManager.getInstance().getConversation(toChatUsername));
		
		
		
	}
	
	
	/**
	 * 閫�鍑虹兢缁�
	 * 
	 * @param groupId
	 */
	private void exitGrop() {
		new Thread(new Runnable() {
			public void run() {
				try {
					EMGroupManager.getInstance().exitFromGroup(groupId);
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							setResult(RESULT_OK);
							finish();
							ChatActivity.activityInstance.finish();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							Toast.makeText(getApplicationContext(), "閫�鍑虹兢鑱婂け璐�: " + e.getMessage(), 1).show();
						}
					});
				}
			}
		}).start();
	}

	/**
	 * 瑙ｆ暎缇ょ粍
	 * 
	 * @param groupId
	 */
	private void deleteGrop() {
		new Thread(new Runnable() {
			public void run() {
				try {
					EMGroupManager.getInstance().exitAndDeleteGroup(groupId);
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							setResult(RESULT_OK);
							finish();
							ChatActivity.activityInstance.finish();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							Toast.makeText(getApplicationContext(), "瑙ｆ暎缇よ亰澶辫触: " + e.getMessage(), 1).show();
						}
					});
				}
			}
		}).start();
	}

	/**
	 * 澧炲姞缇ゆ垚鍛�
	 * 
	 * @param newmembers
	 */
	private void addMembersToGroup(final String[] newmembers) {
		new Thread(new Runnable() {

			public void run() {
				try {
					//鍒涘缓鑰呰皟鐢╝dd鏂规硶
					if(EMChatManager.getInstance().getCurrentUser().equals(group.getOwner())){
						EMGroupManager.getInstance().addUsersToGroup(groupId, newmembers);
					}else{
						//涓�鑸垚鍛樿皟鐢╥nvite鏂规硶
						EMGroupManager.getInstance().inviteUser(groupId, newmembers, null);
					}
					runOnUiThread(new Runnable() {
						public void run() {
							adapter.notifyDataSetChanged();
							((TextView) findViewById(R.id.group_name)).setText(group.getGroupName()+"("+group.getAffiliationsCount()+"浜�)");
							progressDialog.dismiss();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							Toast.makeText(getApplicationContext(), "娣诲姞缇ゆ垚鍛樺け璐�: " + e.getMessage(), 1).show();
						}
					});
				}
			}
		}).start();
	}

	/**
	 * 缇ょ粍鎴愬憳gridadapter
	 * 
	 * @author admin_new
	 * 
	 */
	private class GridAdapter extends ArrayAdapter<String> {

		private int res;
		public boolean isInDeleteMode;
		private List<String> objects;

		public GridAdapter(Context context, int textViewResourceId, List<String> objects) {
			super(context, textViewResourceId, objects);
			this.objects = objects;
			res = textViewResourceId;
			isInDeleteMode = false;
		}

		@Override
		public View getView(final int position, View convertView, final ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(res, null);
			}
			final Button button = (Button) convertView.findViewById(R.id.button_avatar);
			// 鏈�鍚庝竴涓猧tem锛屽噺浜烘寜閽�
			if (position == getCount() - 1) {
				button.setText("");
				// 璁剧疆鎴愬垹闄ゆ寜閽�
				button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.smiley_minus_btn, 0, 0);
				// 濡傛灉涓嶆槸鍒涘缓鑰呮垨鑰呮病鏈夌浉搴旀潈闄愶紝涓嶆彁渚涘姞鍑忎汉鎸夐挳
				if (!group.getOwner().equals(MyApplication.getInstance().getUserName())) {
					// if current user is not group admin, hide add/remove btn
					convertView.setVisibility(View.INVISIBLE);
				} else { // 鏄剧ず鍒犻櫎鎸夐挳
					if (isInDeleteMode) {
						// 姝ｅ浜庡垹闄ゆā寮忎笅锛岄殣钘忓垹闄ゆ寜閽�
						convertView.setVisibility(View.INVISIBLE);
					} else {
						// 姝ｅ父妯″紡
						convertView.setVisibility(View.VISIBLE);
						convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
					}
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							EMLog.d(TAG, "鍒犻櫎鎸夐挳琚偣鍑�");
							isInDeleteMode = true;
							notifyDataSetChanged();
						}
					});
				}
			} else if (position == getCount() - 2) { // 娣诲姞缇ょ粍鎴愬憳鎸夐挳
				button.setText("");
				button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.smiley_add_btn, 0, 0);
				//濡傛灉涓嶆槸鍒涘缓鑰呮垨鑰呮病鏈夌浉搴旀潈闄�
				if (!group.isAllowInvites() && !group.getOwner().equals(MyApplication.getInstance().getUserName())) {
					// if current user is not group admin, hide add/remove btn
					convertView.setVisibility(View.INVISIBLE);
				} else {
					// 姝ｅ浜庡垹闄ゆā寮忎笅,闅愯棌娣诲姞鎸夐挳
					if (isInDeleteMode) {
						convertView.setVisibility(View.INVISIBLE);
					} else {
						convertView.setVisibility(View.VISIBLE);
						convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
					}
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							EMLog.d(TAG, "娣诲姞鎸夐挳琚偣鍑�");
							// 杩涘叆閫変汉椤甸潰
							startActivityForResult(
									(new Intent(GroupDetailsActivity.this, GroupPickContactsActivity.class).putExtra("groupId", groupId)),
									REQUEST_CODE_ADD_USER);
						}
					});
				}
			} else { // 鏅�歩tem锛屾樉绀虹兢缁勬垚鍛�
				final String username = getItem(position);
				button.setText(username);
				convertView.setVisibility(View.VISIBLE);
				button.setVisibility(View.VISIBLE);
				Drawable avatar = getResources().getDrawable(R.drawable.default_avatar);
				avatar.setBounds(0, 0, referenceWidth, referenceHeight);
				button.setCompoundDrawables(null, avatar, null, null);
				// demo缇ょ粍鎴愬憳鐨勫ご鍍忛兘鐢ㄩ粯璁ゅご鍍忥紝闇�鐢卞紑鍙戣�呰嚜宸卞幓璁剧疆澶村儚
				if (isInDeleteMode) {
					// 濡傛灉鏄垹闄ゆā寮忎笅锛屾樉绀哄噺浜哄浘鏍�
					convertView.findViewById(R.id.badge_delete).setVisibility(View.VISIBLE);
				} else {
					convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
				}
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isInDeleteMode) {
							// 濡傛灉鏄垹闄よ嚜宸憋紝return
							if (EMChatManager.getInstance().getCurrentUser().equals(username)) {
								startActivity(new Intent(GroupDetailsActivity.this, AlertDialog.class).putExtra("msg", "涓嶈兘鍒犻櫎鑷繁"));
								return;
							}
							if (!NetUtils.hasNetwork(getApplicationContext())) {
								Toast.makeText(getApplicationContext(), getString(R.string.network_unavailable), 0).show();
								return;
							}
							EMLog.d("group", "remove user from group:" + username);
							deleteMembersFromGroup(username);
						} else {
							// 姝ｅ父鎯呭喌涓嬬偣鍑籾ser锛屽彲浠ヨ繘鍏ョ敤鎴疯鎯呮垨鑰呰亰澶╅〉闈㈢瓑绛�
							// startActivity(new
							// Intent(GroupDetailsActivity.this,
							// ChatActivity.class).putExtra("userId",
							// user.getUsername()));
						}
					}
					
					/**
					 * 鍒犻櫎缇ゆ垚鍛�
					 * @param username
					 */
					protected void deleteMembersFromGroup(final String username) {
						final ProgressDialog deleteDialog = new ProgressDialog(GroupDetailsActivity.this);
						deleteDialog.setMessage("姝ｅ湪绉婚櫎...");
						deleteDialog.setCanceledOnTouchOutside(false);
						deleteDialog.show();
						new Thread(new Runnable() {

							@Override
							public void run() {
								try {
									// 鍒犻櫎琚�変腑鐨勬垚鍛�
									EMGroupManager.getInstance().removeUserFromGroup(groupId, username);
									isInDeleteMode = false;
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											deleteDialog.dismiss();
											notifyDataSetChanged();
											((TextView) findViewById(R.id.group_name)).setText(group.getGroupName()+"("+group.getAffiliationsCount()+"浜�)");
										}
									});
								} catch (final Exception e) {
									deleteDialog.dismiss();
									runOnUiThread(new Runnable() {
										public void run() {
											Toast.makeText(getApplicationContext(), "鍒犻櫎澶辫触锛�" + e.getMessage(), 1).show();
										}
									});
								}

							}
						}).start();
					}
				});
			}
			return convertView;
		}

		@Override
		public int getCount() {
			return super.getCount() + 2;
		}
	}
	
	protected void updateGroup() {
		new Thread(new Runnable() {
			public void run() {
				try {
					EMGroup returnGroup = EMGroupManager.getInstance().getGroupFromServer(groupId);
					//鏇存柊鏈湴鏁版嵁
					EMGroupManager.getInstance().createOrUpdateLocalGroup(returnGroup);
					
					runOnUiThread(new Runnable() {
						public void run() {
							((TextView) findViewById(R.id.group_name)).setText(group.getGroupName()+"("+group.getAffiliationsCount()+"浜�)");
							loadingPB.setVisibility(View.INVISIBLE);
							adapter.notifyDataSetChanged();
							if (EMChatManager.getInstance().getCurrentUser().equals(group.getOwner())) {
								//鏄剧ず瑙ｆ暎鎸夐挳
								exitBtn.setVisibility(View.GONE);
								deleteBtn.setVisibility(View.VISIBLE);
							}else{
								//鏄剧ず閫�鍑烘寜閽�
								exitBtn.setVisibility(View.VISIBLE);
								deleteBtn.setVisibility(View.GONE);
								
							}
						}
					});

				} catch (Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							loadingPB.setVisibility(View.INVISIBLE);
						}
					});
				}
			}
		}).start();
	}

	public void back(View view) {
		setResult(RESULT_OK);
		finish();
	}



	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		finish();
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}
	
	
	
	
}
