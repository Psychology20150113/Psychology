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
package com.dcy.psychology;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMChatRoomChangeListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.EMValueCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.applib.model.GroupRemoveListener;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatRoom;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.NormalFileMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VideoMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.dcy.psychology.R;
import com.easemob.chatuidemo.adapter.ExpressionAdapter;
import com.easemob.chatuidemo.adapter.ExpressionPagerAdapter;
import com.easemob.chatuidemo.adapter.MessageAdapter;
import com.easemob.chatuidemo.adapter.VoicePlayClickListener;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.easemob.chatuidemo.utils.ImageUtils;
import com.easemob.chatuidemo.utils.SmileUtils;
import com.easemob.chatuidemo.widget.ExpandGridView;
import com.easemob.chatuidemo.widget.PasteEditText;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;
import com.easemob.util.VoiceRecorder;

/**
 * 鑱婂ぉ椤甸潰
 * 
 */
public class ChatActivity extends BaseActivity implements OnClickListener, EMEventListener{
	private static final String TAG = "ChatActivity";
	private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
	public static final int REQUEST_CODE_CONTEXT_MENU = 3;
	private static final int REQUEST_CODE_MAP = 4;
	public static final int REQUEST_CODE_TEXT = 5;
	public static final int REQUEST_CODE_VOICE = 6;
	public static final int REQUEST_CODE_PICTURE = 7;
	public static final int REQUEST_CODE_LOCATION = 8;
	public static final int REQUEST_CODE_NET_DISK = 9;
	public static final int REQUEST_CODE_FILE = 10;
	public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
	public static final int REQUEST_CODE_PICK_VIDEO = 12;
	public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
	public static final int REQUEST_CODE_VIDEO = 14;
	public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
	public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
	public static final int REQUEST_CODE_SEND_USER_CARD = 17;
	public static final int REQUEST_CODE_CAMERA = 18;
	public static final int REQUEST_CODE_LOCAL = 19;
	public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
	public static final int REQUEST_CODE_GROUP_DETAIL = 21;
	public static final int REQUEST_CODE_SELECT_VIDEO = 23;
	public static final int REQUEST_CODE_SELECT_FILE = 24;
	public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

	public static final int RESULT_CODE_COPY = 1;
	public static final int RESULT_CODE_DELETE = 2;
	public static final int RESULT_CODE_FORWARD = 3;
	public static final int RESULT_CODE_OPEN = 4;
	public static final int RESULT_CODE_DWONLOAD = 5;
	public static final int RESULT_CODE_TO_CLOUD = 6;
	public static final int RESULT_CODE_EXIT_GROUP = 7;

	public static final int CHATTYPE_SINGLE = 1;
	public static final int CHATTYPE_GROUP = 2;
	public static final int CHATTYPE_CHATROOM = 3;

	public static final String COPY_IMAGE = "EASEMOBIMG";
	private View recordingContainer;
	private ImageView micImage;
	private TextView recordingHint;
	private ListView listView;
	private PasteEditText mEditTextContent;
	private View buttonSetModeKeyboard;
	private View buttonSetModeVoice;
	private View buttonSend;
	private View buttonPressToSpeak;
	// private ViewPager expressionViewpager;
	private LinearLayout emojiIconContainer;
	private LinearLayout btnContainer;
	private View more;
	private int position;
	private ClipboardManager clipboard;
	private ViewPager expressionViewpager;
	private InputMethodManager manager;
	private List<String> reslist;
	private Drawable[] micImages;
	private int chatType;
	private EMConversation conversation;
	public static ChatActivity activityInstance = null;
	// 缁欒皝鍙戦�佹秷鎭�
	private String toChatUsername;
	private VoiceRecorder voiceRecorder;
	private MessageAdapter adapter;
	private File cameraFile;
	static int resendPos;

	private GroupListener groupListener;

	private ImageView iv_emoticons_normal;
	private ImageView iv_emoticons_checked;
	private RelativeLayout edittext_layout;
	private ProgressBar loadmorePB;
	private boolean isloading;
	private final int pagesize = 20;
	private boolean haveMoreData = true;
	private Button btnMore;
	public String playMsgId;

	private SwipeRefreshLayout swipeRefreshLayout;

	private Handler micImageHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// 鍒囨崲msg鍒囨崲鍥剧墖
			micImage.setImageDrawable(micImages[msg.what]);
		}
	};
	public EMGroup group;
	public EMChatRoom room;
	public boolean isRobot;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		hideTitleView();
		activityInstance = this;
		initView();
		setUpView();
	}

	/**
	 * initView
	 */
	protected void initView() {
		recordingContainer = findViewById(R.id.recording_container);
		micImage = (ImageView) findViewById(R.id.mic_image);
		recordingHint = (TextView) findViewById(R.id.recording_hint);
		listView = (ListView) findViewById(R.id.list);
		mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
		buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
		edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
		buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
		buttonSend = findViewById(R.id.btn_send);
		buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
		expressionViewpager = (ViewPager) findViewById(R.id.vPager);
		emojiIconContainer = (LinearLayout) findViewById(R.id.ll_face_container);
		btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
		iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
		iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
		loadmorePB = (ProgressBar) findViewById(R.id.pb_load_more);
		btnMore = (Button) findViewById(R.id.btn_more);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		more = findViewById(R.id.more);
		edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);

		// 鍔ㄧ敾璧勬簮鏂囦欢,鐢ㄤ簬褰曞埗璇煶鏃�
		micImages = new Drawable[] { getResources().getDrawable(R.drawable.record_animate_01),
				getResources().getDrawable(R.drawable.record_animate_02),
				getResources().getDrawable(R.drawable.record_animate_03),
				getResources().getDrawable(R.drawable.record_animate_04),
				getResources().getDrawable(R.drawable.record_animate_05),
				getResources().getDrawable(R.drawable.record_animate_06),
				getResources().getDrawable(R.drawable.record_animate_07),
				getResources().getDrawable(R.drawable.record_animate_08),
				getResources().getDrawable(R.drawable.record_animate_09),
				getResources().getDrawable(R.drawable.record_animate_10),
				getResources().getDrawable(R.drawable.record_animate_11),
				getResources().getDrawable(R.drawable.record_animate_12),
				getResources().getDrawable(R.drawable.record_animate_13),
				getResources().getDrawable(R.drawable.record_animate_14), };

		// 琛ㄦ儏list
		reslist = getExpressionRes(35);
		// 鍒濆鍖栬〃鎯卾iewpager
		List<View> views = new ArrayList<View>();
		View gv1 = getGridChildView(1);
		View gv2 = getGridChildView(2);
		views.add(gv1);
		views.add(gv2);
		expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
		edittext_layout.requestFocus();
		voiceRecorder = new VoiceRecorder(micImageHandler);
		buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
		mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
				} else {
					edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
				}

			}
		});
		mEditTextContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
				more.setVisibility(View.GONE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
				emojiIconContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.GONE);
			}
		});
		// 鐩戝惉鏂囧瓧妗�
		mEditTextContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(s)) {
					btnMore.setVisibility(View.GONE);
					buttonSend.setVisibility(View.VISIBLE);
				} else {
					btnMore.setVisibility(View.VISIBLE);
					buttonSend.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		 swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);

		 swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
		                 android.R.color.holo_orange_light, android.R.color.holo_red_light);

		 swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

		         @Override
		         public void onRefresh() {
		                 new Handler().postDelayed(new Runnable() {

		                         @Override
		                         public void run() {
		                                 if (listView.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
		                                         List<EMMessage> messages;
		                                         try {
	                                                 if (chatType == CHATTYPE_SINGLE){
                                                         messages = conversation.loadMoreMsgFromDB(adapter.getItem(0).getMsgId(), pagesize);
	                                                 }
	                                                 else{
                                                         messages = conversation.loadMoreGroupMsgFromDB(adapter.getItem(0).getMsgId(), pagesize);
	                                                 }
		                                         } catch (Exception e1) {
	                                                 swipeRefreshLayout.setRefreshing(false);
	                                                 return;
		                                         }
		                                         
		                                         if (messages.size() > 0) {
	                                                 adapter.notifyDataSetChanged();
	                                                 adapter.refreshSeekTo(messages.size() - 1);
	                                                 if (messages.size() != pagesize){
	                                                     haveMoreData = false;
	                                                 }
		                                         } else {
		                                             haveMoreData = false;
		                                         }
		                                         
		                                         isloading = false;

		                                 }else{
		                                     Toast.makeText(ChatActivity.this, getResources().getString(R.string.no_more_messages), Toast.LENGTH_SHORT).show();
		                                 }
		                                 swipeRefreshLayout.setRefreshing(false);
		                         }
		                 }, 1000);
		         }
		 });
	}

	private void setUpView() {
		iv_emoticons_normal.setOnClickListener(this);
		iv_emoticons_checked.setOnClickListener(this);
		// position = getIntent().getIntExtra("position", -1);
		clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(
				PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
		// 鍒ゆ柇鍗曡亰杩樻槸缇よ亰
		chatType = getIntent().getIntExtra("chatType", CHATTYPE_SINGLE);

		if (chatType == CHATTYPE_SINGLE) { // 鍗曡亰
			toChatUsername = getIntent().getStringExtra("userId");
			((TextView) findViewById(R.id.name)).setText(toChatUsername);
//			Map<String,RobotUser> robotMap=((DemoHXSDKHelper)HXSDKHelper.getInstance()).getRobotList();
//			if(robotMap!=null&&robotMap.containsKey(toChatUsername)){
//				isRobot = true;
//				String nick = robotMap.get(toChatUsername).getNick();
//				if(!TextUtils.isEmpty(nick)){
//					((TextView) findViewById(R.id.name)).setText(nick);
//				}else{
//					((TextView) findViewById(R.id.name)).setText(toChatUsername);
//				}
//			}else{
//				((TextView) findViewById(R.id.name)).setText(toChatUsername);
//			}
		} else {
			// 缇よ亰
			findViewById(R.id.container_to_group).setVisibility(View.VISIBLE);
			findViewById(R.id.container_remove).setVisibility(View.GONE);
			toChatUsername = getIntent().getStringExtra("groupId");

			if(chatType == CHATTYPE_GROUP){
			    onGroupViewCreation();
			}else{ 
			    onChatRoomViewCreation();
			}
		}
        
		// for chatroom type, we only init conversation and create view adapter on success
		if(chatType != CHATTYPE_CHATROOM){
		    onConversationInit();
	        
	        onListViewCreation();
	        
	        // show forward message if the message is not null
	        String forward_msg_id = getIntent().getStringExtra("forward_msg_id");
	        if (forward_msg_id != null) {
	            // 鏄剧ず鍙戦�佽杞彂鐨勬秷鎭�
	            forwardMessage(forward_msg_id);
	        }
		}
	}

	protected void onConversationInit(){
	    if(chatType == CHATTYPE_SINGLE){
	        conversation = EMChatManager.getInstance().getConversationByType(toChatUsername,EMConversationType.Chat);
	    }else if(chatType == CHATTYPE_GROUP){
	        conversation = EMChatManager.getInstance().getConversationByType(toChatUsername,EMConversationType.GroupChat);
	    }else if(chatType == CHATTYPE_CHATROOM){
	        conversation = EMChatManager.getInstance().getConversationByType(toChatUsername,EMConversationType.ChatRoom);
	    }
	     
        // 鎶婃浼氳瘽鐨勬湭璇绘暟缃负0
        conversation.markAllMessagesAsRead();

        // 鍒濆鍖杁b鏃讹紝姣忎釜conversation鍔犺浇鏁扮洰鏄痝etChatOptions().getNumberOfMessagesLoaded
        // 杩欎釜鏁扮洰濡傛灉姣旂敤鎴锋湡鏈涜繘鍏ヤ細璇濈晫闈㈡椂鏄剧ず鐨勪釜鏁颁笉涓�鏍凤紝灏卞鍔犺浇涓�浜�
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            if (chatType == CHATTYPE_SINGLE) {
                conversation.loadMoreMsgFromDB(msgId, pagesize);
            } else {
                conversation.loadMoreGroupMsgFromDB(msgId, pagesize);
            }
        }
        
        EMChatManager.getInstance().addChatRoomChangeListener(new EMChatRoomChangeListener(){

            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                if(roomId.equals(toChatUsername)){
                    finish();
                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {                
            }

            @Override
            public void onMemberExited(String roomId, String roomName,
                    String participant) {
                
            }

            @Override
            public void onMemberKicked(String roomId, String roomName,
                    String participant) {
                if(roomId.equals(toChatUsername)){
                    String curUser = EMChatManager.getInstance().getCurrentUser();
                    if(curUser.equals(participant)){
                        EMChatManager.getInstance().leaveChatRoom(toChatUsername);
                        finish();
                    }
                }
            }
            
        });
	}
	
	protected void onListViewCreation(){
        adapter = new MessageAdapter(ChatActivity.this, toChatUsername, chatType);
        // 鏄剧ず娑堟伅
        listView.setAdapter(adapter);
        
        listView.setOnScrollListener(new ListScrollListener());
        adapter.refreshSelectLast();

        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
                return false;
            }
        });
	}
	
	protected void onGroupViewCreation(){
	    group = EMGroupManager.getInstance().getGroup(toChatUsername);
        
        if (group != null){
            ((TextView) findViewById(R.id.name)).setText(group.getGroupName());
        }else{
            ((TextView) findViewById(R.id.name)).setText(toChatUsername);
        }
        
        // 鐩戝惉褰撳墠浼氳瘽鐨勭兢鑱婅В鏁ｈT浜嬩欢
        groupListener = new GroupListener();
        EMGroupManager.getInstance().addGroupChangeListener(groupListener);
	}
	
	protected void onChatRoomViewCreation(){
        findViewById(R.id.container_to_group).setVisibility(View.GONE);
        
        final ProgressDialog pd = ProgressDialog.show(this, "", "Joining......");
        EMChatManager.getInstance().joinChatRoom(toChatUsername, new EMValueCallBack<EMChatRoom>() {
        
        @Override
        public void onSuccess(EMChatRoom value) {
            // TODO Auto-generated method stub
             runOnUiThread(new Runnable(){
                   @Override
                   public void run(){
                        pd.dismiss();
                        room = EMChatManager.getInstance().getChatRoom(toChatUsername);
                        if(room !=null){
                            ((TextView) findViewById(R.id.name)).setText(room.getName());
                        }else{
                            ((TextView) findViewById(R.id.name)).setText(toChatUsername);
                        }
                        EMLog.d(TAG, "join room success : " + room.getName());
                        
                        onConversationInit();
                        
                        onListViewCreation();
                   }
               });
        }
        
        @Override
        public void onError(final int error, String errorMsg) {
                // TODO Auto-generated method stub
                EMLog.d(TAG, "join room failure : " + error);
               runOnUiThread(new Runnable(){
                   @Override
                   public void run(){
                       pd.dismiss();
                   }
               });
               finish();
            }
        });
	}
	
	/**
	 * onActivityResult
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CODE_EXIT_GROUP) {
			setResult(RESULT_OK);
			finish();
			return;
		}
		if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
			switch (resultCode) {
			case RESULT_CODE_COPY: // 澶嶅埗娑堟伅
				EMMessage copyMsg = adapter.getItem(data.getIntExtra("position", -1));
				// clipboard.setText(SmileUtils.getSmiledText(ChatActivity.this,
				// ((TextMessageBody) copyMsg.getBody()).getMessage()));
				clipboard.setText(((TextMessageBody) copyMsg.getBody()).getMessage());
				break;
			case RESULT_CODE_DELETE: // 鍒犻櫎娑堟伅
				EMMessage deleteMsg = adapter.getItem(data.getIntExtra("position", -1));
				conversation.removeMessage(deleteMsg.getMsgId());
				adapter.refreshSeekTo(data.getIntExtra("position", adapter.getCount()) - 1);
				break;

//			case RESULT_CODE_FORWARD: // 杞彂娑堟伅
//				EMMessage forwardMsg = (EMMessage) adapter.getItem(data.getIntExtra("position", 0));
//				Intent intent = new Intent(this, ForwardMessageActivity.class);
//				intent.putExtra("forward_msg_id", forwardMsg.getMsgId());
//				startActivity(intent);
//				
//				break;

			default:
				break;
			}
		}
		if (resultCode == RESULT_OK) { // 娓呯┖娑堟伅
			if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
				// 娓呯┖浼氳瘽
				EMChatManager.getInstance().clearConversation(toChatUsername);
				adapter.refresh();
			} else if (requestCode == REQUEST_CODE_CAMERA) { // 鍙戦�佺収鐗�
				if (cameraFile != null && cameraFile.exists())
					sendPicture(cameraFile.getAbsolutePath());
			} else if (requestCode == REQUEST_CODE_SELECT_VIDEO) { // 鍙戦�佹湰鍦伴�夋嫨鐨勮棰�

				int duration = data.getIntExtra("dur", 0);
				String videoPath = data.getStringExtra("path");
				File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
				Bitmap bitmap = null;
				FileOutputStream fos = null;
				try {
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
					if (bitmap == null) {
						EMLog.d("chatactivity", "problem load video thumbnail bitmap,use default icon");
						bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_panel_video_icon);
					}
					fos = new FileOutputStream(file);

					bitmap.compress(CompressFormat.JPEG, 100, fos);

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						fos = null;
					}
					if (bitmap != null) {
						bitmap.recycle();
						bitmap = null;
					}

				}
				sendVideo(videoPath, file.getAbsolutePath(), duration / 1000);

			} else if (requestCode == REQUEST_CODE_LOCAL) { // 鍙戦�佹湰鍦板浘鐗�
				if (data != null) {
					Uri selectedImage = data.getData();
					if (selectedImage != null) {
						sendPicByUri(selectedImage);
					}
				}
			} else if (requestCode == REQUEST_CODE_SELECT_FILE) { // 鍙戦�侀�夋嫨鐨勬枃浠�
				if (data != null) {
					Uri uri = data.getData();
					if (uri != null) {
						sendFile(uri);
					}
				}

			} else if (requestCode == REQUEST_CODE_MAP) { // 鍦板浘
				double latitude = data.getDoubleExtra("latitude", 0);
				double longitude = data.getDoubleExtra("longitude", 0);
				String locationAddress = data.getStringExtra("address");
				if (locationAddress != null && !locationAddress.equals("")) {
				    toggleMore(more);
					sendLocationMsg(latitude, longitude, "", locationAddress);
				} else {
					String st = getResources().getString(R.string.unable_to_get_loaction);
					Toast.makeText(this, st, 0).show();
				}
				// 閲嶅彂娑堟伅
			} else if (requestCode == REQUEST_CODE_TEXT || requestCode == REQUEST_CODE_VOICE
					|| requestCode == REQUEST_CODE_PICTURE || requestCode == REQUEST_CODE_LOCATION
					|| requestCode == REQUEST_CODE_VIDEO || requestCode == REQUEST_CODE_FILE) {
				resendMessage();
			} else if (requestCode == REQUEST_CODE_COPY_AND_PASTE) {
				// 绮樿创
				if (!TextUtils.isEmpty(clipboard.getText())) {
					String pasteText = clipboard.getText().toString();
					if (pasteText.startsWith(COPY_IMAGE)) {
						// 鎶婂浘鐗囧墠缂�鍘绘帀锛岃繕鍘熸垚姝ｅ父鐨刾ath
						sendPicture(pasteText.replace(COPY_IMAGE, ""));
					}

				}
			} else if (requestCode == REQUEST_CODE_ADD_TO_BLACKLIST) { // 绉诲叆榛戝悕鍗�
				EMMessage deleteMsg = adapter.getItem(data.getIntExtra("position", -1));
				addUserToBlacklist(deleteMsg.getFrom());
			} else if (conversation.getMsgCount() > 0) {
				adapter.refresh();
				setResult(RESULT_OK);
			} else if (requestCode == REQUEST_CODE_GROUP_DETAIL) {
				adapter.refresh();
			}
		}
	}

	/**
	 * 娑堟伅鍥炬爣鐐瑰嚮浜嬩欢
	 * 
	 * @param view
	 */
	@Override
	public void onClick(View view) {
		String st1 = getResources().getString(R.string.not_connect_to_server);
		int id = view.getId();
		if (id == R.id.btn_send) {// 鐐瑰嚮鍙戦�佹寜閽�(鍙戞枃瀛楀拰琛ㄦ儏)
			String s = mEditTextContent.getText().toString();
			sendText(s);
		} else if (id == R.id.btn_take_picture) {
			selectPicFromCamera();// 鐐瑰嚮鐓х浉鍥炬爣
		} else if (id == R.id.btn_picture) {
			selectPicFromLocal(); // 鐐瑰嚮鍥剧墖鍥炬爣
//		} else if (id == R.id.btn_location) { // 浣嶇疆
//			startActivityForResult(new Intent(this, BaiduMapActivity.class), REQUEST_CODE_MAP);
		} else if (id == R.id.iv_emoticons_normal) { // 鐐瑰嚮鏄剧ず琛ㄦ儏妗�
			more.setVisibility(View.VISIBLE);
			iv_emoticons_normal.setVisibility(View.INVISIBLE);
			iv_emoticons_checked.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.GONE);
			emojiIconContainer.setVisibility(View.VISIBLE);
			hideKeyboard();
		} else if (id == R.id.iv_emoticons_checked) { // 鐐瑰嚮闅愯棌琛ㄦ儏妗�
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
			emojiIconContainer.setVisibility(View.GONE);
			more.setVisibility(View.GONE);

		} else if (id == R.id.btn_video) {
//			// 鐐瑰嚮鎽勫儚鍥炬爣
//			Intent intent = new Intent(ChatActivity.this, ImageGridActivity.class);
//			startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
		} else if (id == R.id.btn_file) { // 鐐瑰嚮鏂囦欢鍥炬爣
			selectFileFromLocal();
//		} else if (id == R.id.btn_voice_call) { // 鐐瑰嚮璇煶鐢佃瘽鍥炬爣
//			if (!EMChatManager.getInstance().isConnected())
//				Toast.makeText(this, st1, 0).show();
//			else{
//				startActivity(new Intent(ChatActivity.this, VoiceCallActivity.class).putExtra("username",
//						toChatUsername).putExtra("isComingCall", false));
//				voiceCallBtn.setEnabled(false);
//				toggleMore(null);
//			}
//		} else if (id == R.id.btn_video_call) { // 瑙嗛閫氳瘽
//			if (!EMChatManager.getInstance().isConnected())
//				Toast.makeText(this, st1, 0).show();
//			else{
//				startActivity(new Intent(this, VideoCallActivity.class).putExtra("username", toChatUsername).putExtra(
//						"isComingCall", false));
//				videoCallBtn.setEnabled(false);
//				toggleMore(null);
//			}
		}
	}

	/**
	 * 浜嬩欢鐩戝惉
	 * 
	 * see {@link EMNotifierEvent}
     */
    @Override
    public void onEvent(EMNotifierEvent event) {
        switch (event.getEvent()) {
        case EventNewMessage:
        {
            //鑾峰彇鍒癿essage
            EMMessage message = (EMMessage) event.getData();
            
            String username = null;
            //缇ょ粍娑堟伅
            if(message.getChatType() == ChatType.GroupChat || message.getChatType() == ChatType.ChatRoom){
                username = message.getTo();
            }
            else{
                //鍗曡亰娑堟伅
                username = message.getFrom();
            }

            //濡傛灉鏄綋鍓嶄細璇濈殑娑堟伅锛屽埛鏂拌亰澶╅〉闈�
            if(username.equals(getToChatUsername())){
                refreshUIWithNewMessage();
                //澹伴煶鍜岄渿鍔ㄦ彁绀烘湁鏂版秷鎭�
                HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(message);
            }else{
                //濡傛灉娑堟伅涓嶆槸鍜屽綋鍓嶈亰澶㊣D鐨勬秷鎭�
                HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
            }

            break;
        }
        case EventDeliveryAck:
        {
            //鑾峰彇鍒癿essage
            EMMessage message = (EMMessage) event.getData();
            refreshUI();
            break;
        }
        case EventReadAck:
        {
            //鑾峰彇鍒癿essage
            EMMessage message = (EMMessage) event.getData();
            refreshUI();
            break;
        }
        case EventOfflineMessage:
        {
            //a list of offline messages 
            //List<EMMessage> offlineMessages = (List<EMMessage>) event.getData();
            refreshUI();
            break;
        }
        default:
            break;
        }
        
    }
	
	
	private void refreshUIWithNewMessage(){
	    if(adapter == null){
	        return;
	    }
	    
	    runOnUiThread(new Runnable() {
            @Override
			public void run() {
                adapter.refreshSelectLast();
            }
        });
	}

	private void refreshUI() {
	    if(adapter == null){
            return;
        }
	    
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adapter.refresh();
			}
		});
	}

	/**
	 * 鐓х浉鑾峰彇鍥剧墖
	 */
	public void selectPicFromCamera() {
		if (!CommonUtils.isExitsSdcard()) {
			String st = getResources().getString(R.string.sd_card_does_not_exist);
			Toast.makeText(getApplicationContext(), st, 0).show();
			return;
		}

		cameraFile = new File(PathUtil.getInstance().getImagePath(), System.currentTimeMillis() + ".jpg");
		cameraFile.getParentFile().mkdirs();
		startActivityForResult(
				new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
				REQUEST_CODE_CAMERA);
	}

	/**
	 * 閫夋嫨鏂囦欢
	 */
	private void selectFileFromLocal() {
		Intent intent = null;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("*/*");
			intent.addCategory(Intent.CATEGORY_OPENABLE);

		} else {
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
	}

	/**
	 * 浠庡浘搴撹幏鍙栧浘鐗�
	 */
	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_LOCAL);
	}

	/**
	 * 鍙戦�佹枃鏈秷鎭�
	 * 
	 * @param content
	 *            message content
	 * @param isResend
	 *            boolean resend
	 */
	public void sendText(String content) {

		if (content.length() > 0) {
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			// 濡傛灉鏄兢鑱婏紝璁剧疆chattype,榛樿鏄崟鑱�
			if (chatType == CHATTYPE_GROUP){
			    message.setChatType(ChatType.GroupChat);
			}else if(chatType == CHATTYPE_CHATROOM){
			    message.setChatType(ChatType.ChatRoom);
			}
			if(isRobot){
				message.setAttribute("em_robot_message", true);
			}
			TextMessageBody txtBody = new TextMessageBody(content);
			// 璁剧疆娑堟伅body
			message.addBody(txtBody);
			// 璁剧疆瑕佸彂缁欒皝,鐢ㄦ埛username鎴栬�呯兢鑱奼roupid
			message.setReceipt(toChatUsername);
			// 鎶妋essgage鍔犲埌conversation涓�
			conversation.addMessage(message);
			// 閫氱煡adapter鏈夋秷鎭彉鍔紝adapter浼氭牴鎹姞鍏ョ殑杩欐潯message鏄剧ず娑堟伅鍜岃皟鐢╯dk鐨勫彂閫佹柟娉�
			adapter.refreshSelectLast();
			mEditTextContent.setText("");

			setResult(RESULT_OK);

		}
	}

	/**
	 * 鍙戦�佽闊�
	 * 
	 * @param filePath
	 * @param fileName
	 * @param length
	 * @param isResend
	 */
	private void sendVoice(String filePath, String fileName, String length, boolean isResend) {
		if (!(new File(filePath).exists())) {
			return;
		}
		try {
			final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
			// 濡傛灉鏄兢鑱婏紝璁剧疆chattype,榛樿鏄崟鑱�
			if (chatType == CHATTYPE_GROUP){
				message.setChatType(ChatType.GroupChat);
				}else if(chatType == CHATTYPE_CHATROOM){
				    message.setChatType(ChatType.ChatRoom);
				}
			message.setReceipt(toChatUsername);
			int len = Integer.parseInt(length);
			VoiceMessageBody body = new VoiceMessageBody(new File(filePath), len);
			message.addBody(body);
			if(isRobot){
				message.setAttribute("em_robot_message", true);
			}
			conversation.addMessage(message);
			adapter.refreshSelectLast();
			setResult(RESULT_OK);
			// send file
			// sendVoiceSub(filePath, fileName, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 鍙戦�佸浘鐗�
	 * 
	 * @param filePath
	 */
	private void sendPicture(final String filePath) {
		String to = toChatUsername;
		// create and add image message in view
		final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
		// 濡傛灉鏄兢鑱婏紝璁剧疆chattype,榛樿鏄崟鑱�
		if (chatType == CHATTYPE_GROUP){
			message.setChatType(ChatType.GroupChat);
		}else if(chatType == CHATTYPE_CHATROOM){
		    message.setChatType(ChatType.ChatRoom);
		}

		message.setReceipt(to);
		ImageMessageBody body = new ImageMessageBody(new File(filePath));
		// 榛樿瓒呰繃100k鐨勫浘鐗囦細鍘嬬缉鍚庡彂缁欏鏂癸紝鍙互璁剧疆鎴愬彂閫佸師鍥�
		// body.setSendOriginalImage(true);
		message.addBody(body);
		if(isRobot){
			message.setAttribute("em_robot_message", true);
		}
		conversation.addMessage(message);

		listView.setAdapter(adapter);
		adapter.refreshSelectLast();
		setResult(RESULT_OK);
		// more(more);
	}

	/**
	 * 鍙戦�佽棰戞秷鎭�
	 */
	private void sendVideo(final String filePath, final String thumbPath, final int length) {
		final File videoFile = new File(filePath);
		if (!videoFile.exists()) {
			return;
		}
		try {
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VIDEO);
			// 濡傛灉鏄兢鑱婏紝璁剧疆chattype,榛樿鏄崟鑱�
			if (chatType == CHATTYPE_GROUP){
				message.setChatType(ChatType.GroupChat);
			}else if(chatType == CHATTYPE_CHATROOM){
			    message.setChatType(ChatType.ChatRoom);
			}
			String to = toChatUsername;
			message.setReceipt(to);
			VideoMessageBody body = new VideoMessageBody(videoFile, thumbPath, length, videoFile.length());
			message.addBody(body);
			if(isRobot){
				message.setAttribute("em_robot_message", true);
			}
			conversation.addMessage(message);
			listView.setAdapter(adapter);
			adapter.refreshSelectLast();
			setResult(RESULT_OK);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 鏍规嵁鍥惧簱鍥剧墖uri鍙戦�佸浘鐗�
	 * 
	 * @param selectedImage
	 */
	private void sendPicByUri(Uri selectedImage) {
		// String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(selectedImage, null, null, null, null);
		String st8 = getResources().getString(R.string.cant_find_pictures);
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex("_data");
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			cursor = null;

			if (picturePath == null || picturePath.equals("null")) {
				Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
			sendPicture(picturePath);
		} else {
			File file = new File(selectedImage.getPath());
			if (!file.exists()) {
				Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;

			}
			sendPicture(file.getAbsolutePath());
		}

	}

	/**
	 * 鍙戦�佷綅缃俊鎭�
	 * 
	 * @param latitude
	 * @param longitude
	 * @param imagePath
	 * @param locationAddress
	 */
	private void sendLocationMsg(double latitude, double longitude, String imagePath, String locationAddress) {
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.LOCATION);
		// 濡傛灉鏄兢鑱婏紝璁剧疆chattype,榛樿鏄崟鑱�
		if (chatType == CHATTYPE_GROUP){
			message.setChatType(ChatType.GroupChat);
		}else if(chatType == CHATTYPE_CHATROOM){
		    message.setChatType(ChatType.ChatRoom);
		}
		LocationMessageBody locBody = new LocationMessageBody(locationAddress, latitude, longitude);
		message.addBody(locBody);
		message.setReceipt(toChatUsername);
		if(isRobot){
			message.setAttribute("em_robot_message", true);
		}
		conversation.addMessage(message);
		listView.setAdapter(adapter);
		adapter.refreshSelectLast();
		setResult(RESULT_OK);

	}

	/**
	 * 鍙戦�佹枃浠�
	 * 
	 * @param uri
	 */
	private void sendFile(Uri uri) {
		String filePath = null;
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = getContentResolver().query(uri, projection, null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					filePath = cursor.getString(column_index);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			filePath = uri.getPath();
		}
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			String st7 = getResources().getString(R.string.File_does_not_exist);
			Toast.makeText(getApplicationContext(), st7, 0).show();
			return;
		}
		if (file.length() > 10 * 1024 * 1024) {
			String st6 = getResources().getString(R.string.The_file_is_not_greater_than_10_m);
			Toast.makeText(getApplicationContext(), st6, 0).show();
			return;
		}

		// 鍒涘缓涓�涓枃浠舵秷鎭�
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.FILE);
		// 濡傛灉鏄兢鑱婏紝璁剧疆chattype,榛樿鏄崟鑱�
		if (chatType == CHATTYPE_GROUP){
			message.setChatType(ChatType.GroupChat);
		}else if(chatType == CHATTYPE_CHATROOM){
		    message.setChatType(ChatType.ChatRoom);
		}

		message.setReceipt(toChatUsername);
		// add message body
		NormalFileMessageBody body = new NormalFileMessageBody(new File(filePath));
		message.addBody(body);
		if(isRobot){
			message.setAttribute("em_robot_message", true);
		}
		conversation.addMessage(message);
		listView.setAdapter(adapter);
		adapter.refreshSelectLast();
		setResult(RESULT_OK);
	}

	/**
	 * 閲嶅彂娑堟伅
	 */
	private void resendMessage() {
		EMMessage msg = null;
		msg = conversation.getMessage(resendPos);
		// msg.setBackSend(true);
		msg.status = EMMessage.Status.CREATE;

		adapter.refreshSeekTo(resendPos);
	}

	/**
	 * 鏄剧ず璇煶鍥炬爣鎸夐挳
	 * 
	 * @param view
	 */
	public void setModeVoice(View view) {
		hideKeyboard();
		edittext_layout.setVisibility(View.GONE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeKeyboard.setVisibility(View.VISIBLE);
		buttonSend.setVisibility(View.GONE);
		btnMore.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.VISIBLE);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		btnContainer.setVisibility(View.VISIBLE);
		emojiIconContainer.setVisibility(View.GONE);

	}

	/**
	 * 鏄剧ず閿洏鍥炬爣
	 * 
	 * @param view
	 */
	public void setModeKeyboard(View view) {
		// mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener()
		// {
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// if(hasFocus){
		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		// }
		// }
		// });
		edittext_layout.setVisibility(View.VISIBLE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeVoice.setVisibility(View.VISIBLE);
		// mEditTextContent.setVisibility(View.VISIBLE);
		mEditTextContent.requestFocus();
		// buttonSend.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.GONE);
		if (TextUtils.isEmpty(mEditTextContent.getText())) {
			btnMore.setVisibility(View.VISIBLE);
			buttonSend.setVisibility(View.GONE);
		} else {
			btnMore.setVisibility(View.GONE);
			buttonSend.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 鐐瑰嚮娓呯┖鑱婂ぉ璁板綍
	 * 
	 * @param view
	 */
	public void emptyHistory(View view) {
//		String st5 = getResources().getString(R.string.Whether_to_empty_all_chats);
//		startActivityForResult(new Intent(this, AlertDialog.class).putExtra("titleIsCancel", true).putExtra("msg", st5)
//				.putExtra("cancel", true), REQUEST_CODE_EMPTY_HISTORY);
	}

	/**
	 * 鐐瑰嚮杩涘叆缇ょ粍璇︽儏
	 * 
	 * @param view
	 */
	public void toGroupDetails(View view) {
		if (room == null && group == null) {
			Toast.makeText(getApplicationContext(), R.string.gorup_not_found, 0).show();
			return;
		}
//		if(chatType == CHATTYPE_GROUP){
//			startActivityForResult((new Intent(this, GroupDetailsActivity.class).putExtra("groupId", toChatUsername)),
//					REQUEST_CODE_GROUP_DETAIL);
//		}else{
//			startActivityForResult((new Intent(this, ChatRoomDetailsActivity.class).putExtra("roomId", toChatUsername)),
//					REQUEST_CODE_GROUP_DETAIL);
//		}
	}

	/**
	 * 鏄剧ず鎴栭殣钘忓浘鏍囨寜閽〉
	 * 
	 * @param view
	 */
	public void toggleMore(View view) {
		if (more.getVisibility() == View.GONE) {
			EMLog.d(TAG, "more gone");
			hideKeyboard();
			more.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
			emojiIconContainer.setVisibility(View.GONE);
		} else {
			if (emojiIconContainer.getVisibility() == View.VISIBLE) {
				emojiIconContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.VISIBLE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
			} else {
				more.setVisibility(View.GONE);
			}

		}

	}

	/**
	 * 鐐瑰嚮鏂囧瓧杈撳叆妗�
	 * 
	 * @param v
	 */
	public void editClick(View v) {
		listView.setSelection(listView.getCount() - 1);
		if (more.getVisibility() == View.VISIBLE) {
			more.setVisibility(View.GONE);
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
		}

	}

	private PowerManager.WakeLock wakeLock;

	/**
	 * 鎸変綇璇磋瘽listener
	 * 
	 */
	class PressToSpeakListen implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!CommonUtils.isExitsSdcard()) {
					String st4 = getResources().getString(R.string.Send_voice_need_sdcard_support);
					Toast.makeText(ChatActivity.this, st4, Toast.LENGTH_SHORT).show();
					return false;
				}
				try {
					v.setPressed(true);
					wakeLock.acquire();
					if (VoicePlayClickListener.isPlaying)
						VoicePlayClickListener.currentPlayListener.stopPlayVoice();
					recordingContainer.setVisibility(View.VISIBLE);
					recordingHint.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
					voiceRecorder.startRecording(null, toChatUsername, getApplicationContext());
				} catch (Exception e) {
					e.printStackTrace();
					v.setPressed(false);
					if (wakeLock.isHeld())
						wakeLock.release();
					if (voiceRecorder != null)
						voiceRecorder.discardRecording();
					recordingContainer.setVisibility(View.INVISIBLE);
					Toast.makeText(ChatActivity.this, R.string.recoding_fail, Toast.LENGTH_SHORT).show();
					return false;
				}

				return true;
			case MotionEvent.ACTION_MOVE: {
				if (event.getY() < 0) {
					recordingHint.setText(getString(R.string.release_to_cancel));
					recordingHint.setBackgroundResource(R.drawable.recording_text_hint_bg);
				} else {
					recordingHint.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
				}
				return true;
			}
			case MotionEvent.ACTION_UP:
				v.setPressed(false);
				recordingContainer.setVisibility(View.INVISIBLE);
				if (wakeLock.isHeld())
					wakeLock.release();
				if (event.getY() < 0) {
					// discard the recorded audio.
					voiceRecorder.discardRecording();

				} else {
					// stop recording and send voice file
					String st1 = getResources().getString(R.string.Recording_without_permission);
					String st2 = getResources().getString(R.string.The_recording_time_is_too_short);
					String st3 = getResources().getString(R.string.send_failure_please);
					try {
						int length = voiceRecorder.stopRecoding();
						if (length > 0) {
							sendVoice(voiceRecorder.getVoiceFilePath(), voiceRecorder.getVoiceFileName(toChatUsername),
									Integer.toString(length), false);
						} else if (length == EMError.INVALID_FILE) {
							Toast.makeText(getApplicationContext(), st1, Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(), st2, Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(ChatActivity.this, st3, Toast.LENGTH_SHORT).show();
					}

				}
				return true;
			default:
				recordingContainer.setVisibility(View.INVISIBLE);
				if (voiceRecorder != null)
					voiceRecorder.discardRecording();
				return false;
			}
		}
	}

	/**
	 * 鑾峰彇琛ㄦ儏鐨刧ridview鐨勫瓙view
	 * 
	 * @param i
	 * @return
	 */
	private View getGridChildView(int i) {
		View view = View.inflate(this, R.layout.expression_gridview, null);
		ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
		List<String> list = new ArrayList<String>();
		if (i == 1) {
			List<String> list1 = reslist.subList(0, 20);
			list.addAll(list1);
		} else if (i == 2) {
			list.addAll(reslist.subList(20, reslist.size()));
		}
		list.add("delete_expression");
		final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
		gv.setAdapter(expressionAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String filename = expressionAdapter.getItem(position);
				try {
					// 鏂囧瓧杈撳叆妗嗗彲瑙佹椂锛屾墠鍙緭鍏ヨ〃鎯�
					// 鎸変綇璇磋瘽鍙锛屼笉璁╄緭鍏ヨ〃鎯�
					if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

						if (filename != "delete_expression") { // 涓嶆槸鍒犻櫎閿紝鏄剧ず琛ㄦ儏
							// 杩欓噷鐢ㄧ殑鍙嶅皠锛屾墍浠ユ贩娣嗙殑鏃跺�欎笉瑕佹贩娣哠mileUtils杩欎釜绫�
							Class clz = Class.forName("com.easemob.chatuidemo.utils.SmileUtils");
							Field field = clz.getField(filename);
							mEditTextContent.append(SmileUtils.getSmiledText(ChatActivity.this,
									(String) field.get(null)));
						} else { // 鍒犻櫎鏂囧瓧鎴栬�呰〃鎯�
							if (!TextUtils.isEmpty(mEditTextContent.getText())) {

								int selectionStart = mEditTextContent.getSelectionStart();// 鑾峰彇鍏夋爣鐨勪綅缃�
								if (selectionStart > 0) {
									String body = mEditTextContent.getText().toString();
									String tempStr = body.substring(0, selectionStart);
									int i = tempStr.lastIndexOf("[");// 鑾峰彇鏈�鍚庝竴涓〃鎯呯殑浣嶇疆
									if (i != -1) {
										CharSequence cs = tempStr.substring(i, selectionStart);
										if (SmileUtils.containsKey(cs.toString()))
											mEditTextContent.getEditableText().delete(i, selectionStart);
										else
											mEditTextContent.getEditableText().delete(selectionStart - 1,
													selectionStart);
									} else {
										mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
									}
								}
							}

						}
					}
				} catch (Exception e) {
				}

			}
		});
		return view;
	}

	public List<String> getExpressionRes(int getSum) {
		List<String> reslist = new ArrayList<String>();
		for (int x = 1; x <= getSum; x++) {
			String filename = "ee_" + x;

			reslist.add(filename);

		}
		return reslist;

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		activityInstance = null;
		if(groupListener != null){
		    EMGroupManager.getInstance().removeGroupChangeListener(groupListener);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (group != null)
			((TextView) findViewById(R.id.name)).setText(group.getGroupName());

		 if(adapter != null){
		     adapter.refresh();
	     }

//		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper.getInstance();
//		sdkHelper.pushActivity(this);
		// register the event listener when enter the foreground
		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] { EMNotifierEvent.Event.EventNewMessage,EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventDeliveryAck, EMNotifierEvent.Event.EventReadAck });
	}

	@Override
	protected void onStop() {
		// unregister this event listener when this activity enters the
		// background
		EMChatManager.getInstance().unregisterEventListener(this);

//		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper.getInstance();
//
//		// 鎶婃activity 浠巉oreground activity 鍒楄〃閲岀Щ闄�
//		sdkHelper.popActivity(this);
		
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (wakeLock.isHeld())
			wakeLock.release();
		if (VoicePlayClickListener.isPlaying && VoicePlayClickListener.currentPlayListener != null) {
			// 鍋滄璇煶鎾斁
			VoicePlayClickListener.currentPlayListener.stopPlayVoice();
		}

		try {
			// 鍋滄褰曢煶
			if (voiceRecorder.isRecording()) {
				voiceRecorder.discardRecording();
				recordingContainer.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 闅愯棌杞敭鐩�
	 */
	private void hideKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 鍔犲叆鍒伴粦鍚嶅崟
	 * 
	 * @param username
	 */
	private void addUserToBlacklist(final String username) {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setMessage(getString(R.string.Is_moved_into_blacklist));
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					EMContactManager.getInstance().addUserToBlackList(username, false);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(getApplicationContext(), R.string.Move_into_blacklist_success, 0).show();
						}
					});
				} catch (EaseMobException e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(getApplicationContext(), R.string.Move_into_blacklist_failure, 0).show();
						}
					});
				}
			}
		}).start();
	}

	/**
	 * 杩斿洖
	 * 
	 * @param view
	 */
	public void back(View view) {
		EMChatManager.getInstance().unregisterEventListener(this);
		if(chatType == CHATTYPE_CHATROOM){
			EMChatManager.getInstance().leaveChatRoom(toChatUsername);
		}
		finish();
	}

	/**
	 * 瑕嗙洊鎵嬫満杩斿洖閿�
	 */
	@Override
	public void onBackPressed() {
		if (more.getVisibility() == View.VISIBLE) {
			more.setVisibility(View.GONE);
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
		} else {
			super.onBackPressed();
			if(chatType == CHATTYPE_CHATROOM){
				EMChatManager.getInstance().leaveChatRoom(toChatUsername);
			}
		}
	}

	/**
	 * listview婊戝姩鐩戝惉listener
	 * 
	 */
	private class ListScrollListener implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				/*if (view.getFirstVisiblePosition() == 0 && !isloading && haveMoreData && conversation.getAllMessages().size() != 0) {
					isloading = true;
					loadmorePB.setVisibility(View.VISIBLE);
					// sdk鍒濆鍖栧姞杞界殑鑱婂ぉ璁板綍涓�20鏉★紝鍒伴《鏃跺幓db閲岃幏鍙栨洿澶�					
					List<EMMessage> messages;
					EMMessage firstMsg = conversation.getAllMessages().get(0);
					try {
						// 鑾峰彇鏇村messges锛岃皟鐢ㄦ鏂规硶鐨勬椂鍊欎粠db鑾峰彇鐨刴essages
						// sdk浼氳嚜鍔ㄥ瓨鍏ュ埌姝onversation涓�
						if (chatType == CHATTYPE_SINGLE)
							messages = conversation.loadMoreMsgFromDB(firstMsg.getMsgId(), pagesize);
						else
							messages = conversation.loadMoreGroupMsgFromDB(firstMsg.getMsgId(), pagesize);
					} catch (Exception e1) {
						loadmorePB.setVisibility(View.GONE);
						return;
					}
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
					if (messages.size() != 0) {
						// 鍒锋柊ui
						if (messages.size() > 0) {
							adapter.refreshSeekTo(messages.size() - 1);
						}
						
						if (messages.size() != pagesize)
							haveMoreData = false;
					} else {
						haveMoreData = false;
					}
					loadmorePB.setVisibility(View.GONE);
					isloading = false;

				}*/
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// 鐐瑰嚮notification bar杩涘叆鑱婂ぉ椤甸潰锛屼繚璇佸彧鏈変竴涓亰澶╅〉闈�
		String username = intent.getStringExtra("userId");
		if (toChatUsername.equals(username))
			super.onNewIntent(intent);
		else {
			finish();
			startActivity(intent);
		}

	}

	/**
	 * 杞彂娑堟伅
	 * 
	 * @param forward_msg_id
	 */
	protected void forwardMessage(String forward_msg_id) {
		final EMMessage forward_msg = EMChatManager.getInstance().getMessage(forward_msg_id);
		EMMessage.Type type = forward_msg.getType();
		switch (type) {
		case TXT:
			// 鑾峰彇娑堟伅鍐呭锛屽彂閫佹秷鎭�
			String content = ((TextMessageBody) forward_msg.getBody()).getMessage();
			sendText(content);
			break;
		case IMAGE:
			// 鍙戦�佸浘鐗�
			String filePath = ((ImageMessageBody) forward_msg.getBody()).getLocalUrl();
			if (filePath != null) {
				File file = new File(filePath);
				if (!file.exists()) {
					// 涓嶅瓨鍦ㄥぇ鍥惧彂閫佺缉鐣ュ浘
					filePath = ImageUtils.getThumbnailImagePath(filePath);
				}
				sendPicture(filePath);
			}
			break;
		default:
			break;
		}
		
		if(forward_msg.getChatType() == EMMessage.ChatType.ChatRoom){
			EMChatManager.getInstance().leaveChatRoom(forward_msg.getTo());
		}
	}
	
	/**
	 * 鐩戞祴缇ょ粍瑙ｆ暎鎴栬�呰T浜嬩欢
	 * 
	 */
	class GroupListener extends GroupRemoveListener{

		@Override
		public void onUserRemoved(final String groupId, String groupName) {
			runOnUiThread(new Runnable() {
				String st13 = getResources().getString(R.string.you_are_group);

				@Override
				public void run() {
//					if (toChatUsername.equals(groupId)) {
//						Toast.makeText(ChatActivity.this, st13, 1).show();
//						if (GroupDetailsActivity.instance != null)
//							GroupDetailsActivity.instance.finish();
//						finish();
//					}
				}
			});
		}

		@Override
		public void onGroupDestroy(final String groupId, String groupName) {
//			// 缇ょ粍瑙ｆ暎姝ｅソ鍦ㄦ椤甸潰锛屾彁绀虹兢缁勮瑙ｆ暎锛屽苟finish姝ら〉闈�
//			runOnUiThread(new Runnable() {
//				String st14 = getResources().getString(R.string.the_current_group);
//
//				public void run() {
//					if (toChatUsername.equals(groupId)) {
//						Toast.makeText(ChatActivity.this, st14, 1).show();
//						if (GroupDetailsActivity.instance != null)
//							GroupDetailsActivity.instance.finish();
//						finish();
//					}
//				}
//			});
		}

	}

	public String getToChatUsername() {
		return toChatUsername;
	}

	public ListView getListView() {
		return listView;
	}

}
