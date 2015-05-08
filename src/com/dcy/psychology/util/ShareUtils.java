package com.dcy.psychology.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.dcy.psychology.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.BaseShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SimpleShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * Created by dcy123 on 2015/4/8.
 */
public class ShareUtils {
    public final String QQ_APP_ID = "1101848636";
    public final String QQ_APP_KEY = "BgrsEQ0HvHV0UUPc";
    public final String WX_APP_ID = "wx2f302ae144a1014c";
    public final String WX_APP_SECRET = "bafcfe12a87f87b6b4d5f220a4b038a8";

    private static ShareUtils mShare;
    private Activity mContext;
    private UMSocialService mSocialController;
    private String Share_Default_Title = "mechat";

    private enum Platform{
        Platform_QQ, Platform_Qzone, Platform_Circle, Platform_WX, Platform_Sina
    }

    private ShareUtils(Activity context){
        this.mContext = context;
        mSocialController = UMServiceFactory.getUMSocialService("com.umeng.share");
        UMQQSsoHandler umqqSsoHandler = new UMQQSsoHandler(mContext, QQ_APP_ID, QQ_APP_KEY);
        umqqSsoHandler.addToSocialSDK();
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mContext, QQ_APP_ID, QQ_APP_KEY);
        qZoneSsoHandler.addToSocialSDK();
        UMWXHandler umwxHandler = new UMWXHandler(mContext, WX_APP_ID, WX_APP_SECRET);
        umwxHandler.addToSocialSDK();
        UMWXHandler unCircleHandler = new UMWXHandler(mContext, WX_APP_ID, WX_APP_SECRET);
        unCircleHandler.setToCircle(true);
        unCircleHandler.addToSocialSDK();
        mSocialController.getConfig().setSsoHandler(new SinaSsoHandler());
    }

    public static ShareUtils getInstance(Activity context){
        if(mShare == null){
            mShare = new ShareUtils(context);
        }
        return mShare;
    }

    public void shareToWX(String content){
        shareToPlatform(Platform.Platform_WX, Share_Default_Title, content, null);
    }

    public void shareToCircle(String content){
        shareToPlatform(Platform.Platform_Circle, Share_Default_Title, content, null);
    }

    public void shareToQQ(String content){
        shareToPlatform(Platform.Platform_QQ, Share_Default_Title, content, null);
    }

    public void shareToQzone(String content){
        shareToPlatform(Platform.Platform_Qzone, Share_Default_Title, content, null);
    }

    public void shareToSina(String content){
        shareToPlatform(Platform.Platform_Sina, Share_Default_Title, content, null);
    }

    public void shareToMore(String content){
        Intent mIntent = new Intent(Intent.ACTION_SEND);
        mIntent.putExtra(Intent.EXTRA_TEXT, content);
        mIntent.setType("text/plain");
        mContext.startActivity(mIntent);
    }

    private void shareToPlatform(Platform platform, String title, String content, String url){
        BaseShareContent contentObject = null;
        SHARE_MEDIA platformType = null;
        switch (platform) {
            case Platform_WX:
                contentObject = new WeiXinShareContent();
                platformType = SHARE_MEDIA.WEIXIN;
                break;
            case Platform_Circle:
                contentObject = new CircleShareContent();
                platformType = SHARE_MEDIA.WEIXIN_CIRCLE;
                break;
            case Platform_QQ:
                contentObject = new QQShareContent();
                platformType = SHARE_MEDIA.QQ;
                break;
            case Platform_Qzone:
                contentObject = new QZoneShareContent();
                platformType = SHARE_MEDIA.QZONE;
                break;
            case Platform_Sina:
                contentObject = new SinaShareContent();
                platformType = SHARE_MEDIA.SINA;
                break;
        }
        contentObject.setTitle(title);
        contentObject.setShareContent(content);
        if(!TextUtils.isEmpty(url)){
            contentObject.setTargetUrl(url);
        }
        contentObject.setShareImage(new UMImage(mContext, R.drawable.ic_launcher));
        mSocialController.setShareMedia(contentObject);
        mSocialController.postShare(mContext, platformType, umShareListener);
    }

    private SocializeListeners.SnsPostListener umShareListener = new SocializeListeners.SnsPostListener() {
        @Override
        public void onStart() {
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int eCode, SocializeEntity socializeEntity) {
            if (eCode == 200) {
                Toast.makeText(mContext, R.string.share_success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, R.string.share_failed,Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void share(){
        mSocialController.openShare(mContext, false);
    }
}
