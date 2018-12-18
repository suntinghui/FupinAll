package com.jkrm.fupin.ui.activity.upnp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jkrm.fupin.R;
import com.jkrm.fupin.base.BaseActivity;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.interfaces.IUpnpDeviceChangeListener;
import com.jkrm.fupin.receiver.UpnpReceiver;
import com.jkrm.fupin.upnp.com.zxt.dlna.application.ConfigData;
import com.jkrm.fupin.upnp.dmc.GenerateXml;
import com.jkrm.fupin.upnp.dmp.ContentItem;
import com.jkrm.fupin.upnp.dmp.DeviceItem;
import com.jkrm.fupin.upnp.dms.ContentBrowseActionCallback;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUpnpUtil;
import com.jkrm.fupin.widgets.CustomToolbar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.container.Container;
import org.seamless.util.MimeType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by hzw on 2018/8/16.
 */

public class UpnpContentActivity extends BaseActivity implements Handler.Callback, IUpnpDeviceChangeListener {

    public static final int CONTENT_GET_FAIL = 0;
    public static final int CONTENT_GET_SUC = 1;
    private ListView mContentLv;
    private ProgressBar mProgressBarPreparing;
    private ArrayList<ContentItem> mContentList = new ArrayList<ContentItem>();
    private ContentAdapter mContentAdapter;
    private MyApp mBaseApplication;
    private AndroidUpnpService upnpService;
    private String currentContentFormatMimeType = "";
    private Context mContext;
    private Map<Integer, ArrayList<ContentItem>> mSaveDirectoryMap;
    private Integer mCounter = 0;
    private String mLastDevice;
    private String mThumbUri;
    DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private Handler mHandler;
    private UpnpReceiver mUpnpReceiver;

    private Boolean isToNextLevel = false;

    @Override
    protected int getContentId() {
        return R.layout.activity_upnp_content;
    }

    @Override
    protected void initView() {
        super.initView();
//        mToolbar.setToolbarTitle(mBaseApplication.deviceItem.toString());
        mToolbar.setToolbarTitle(getString(R.string.title_my_file));
        mToolbar.setLeftImg(R.mipmap.back);
        mToolbar.hideRightView();
        mToolbar.setOnLeftClickListener(new CustomToolbar.OnLeftClickListener() {
            @Override
            public void onLeftTextClick() {
                if(isToNextLevel) {
                    initContentData();
                    isToNextLevel = false;
                } else {
                    finish();
                }
            }
        });
        mProgressBarPreparing = (ProgressBar) findViewById(R.id.player_prepairing);
        mContentLv = (ListView) findViewById(R.id.content_list);
        mContentAdapter = new ContentAdapter(mActivity, mContentList);
        mContentLv.setAdapter(mContentAdapter);
        mContentLv.setOnItemClickListener(contentItemClickListener);
    }

    @Override
    public void onBackPressed() {
        if(isToNextLevel) {
            initContentData();
            isToNextLevel = false;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mHandler = new Handler(this);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.icon_image)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
                .build();
        MyUpnpUtil.registerReceiver(mActivity, mUpnpReceiver, this);
    }

    private void initContentData() {

        mCounter = 0;
        if (null == mSaveDirectoryMap) {
            mSaveDirectoryMap = new HashMap<Integer, ArrayList<ContentItem>>();
        } else {
            mSaveDirectoryMap.clear();
        }
        upnpService = mBaseApplication.upnpService;
        Device device = mBaseApplication.deviceItem.getDevice();
        Service service = device.findService(new UDAServiceType(
                "ContentDirectory"));
        upnpService.getControlPoint().execute(
                new ContentBrowseActionCallback(mActivity,
                        service, createRootContainer(service), mContentList,
                        mHandler));

        mLastDevice = mBaseApplication.deviceItem.toString();
    }

    protected Container createRootContainer(Service service) {
        Container rootContainer = new Container();
        rootContainer.setId("0");
        rootContainer.setTitle("Content Directory on "
                + service.getDevice().getDisplayString());
        return rootContainer;
    }

    @Override
    protected void onResume() {
        mContext = mActivity;
        mBaseApplication = MyApp.getInstance();
        if (null == mBaseApplication.deviceItem) {
            showMsg(getString(R.string.not_select_dev));
        } else if (null == mLastDevice || "" == mLastDevice
                || mLastDevice != mBaseApplication.deviceItem.toString()) {
            initContentData();
            isToNextLevel = false;
        } else {

        }
        super.onResume();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case CONTENT_GET_FAIL: {
                showMsg("获取资源列表失败, 请检查数据源是否已关闭或数据异常需重新加载");
                LocalBroadcastManager.getInstance(MyApp.getInstance()).sendBroadcast(new Intent(MyConstants.Action.ACTION_DMS_DATA_GET_FAIL));
                finish();
                break;
            }
            case CONTENT_GET_SUC: {
                mContentAdapter.notifyDataSetChanged();
                mProgressBarPreparing.setVisibility(View.GONE);
                mCounter++;
                ArrayList<ContentItem> tempContentList = new ArrayList<ContentItem>();
                tempContentList.addAll(mContentList);
                mSaveDirectoryMap.put(mCounter - 1, tempContentList);
                break;
            }

            default:
                break;
        }
        return false;
    }

    AdapterView.OnItemClickListener contentItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            ContentItem content = mContentList.get(position);
            if (content.isContainer()) {
                isToNextLevel = true;
                mProgressBarPreparing.setVisibility(View.VISIBLE);
                upnpService.getControlPoint()
                        .execute(
                                new ContentBrowseActionCallback(
                                        mActivity, content
                                        .getService(), content
                                        .getContainer(), mContentList,
                                        mHandler));
            } else {
                LinearLayout linearLayout = (LinearLayout) getViewByPosition(position, mContentLv);
                ImageView imageView = (ImageView) linearLayout.findViewById(R.id.icon_folder);
                if(imageView.getDrawable().getCurrent().getConstantState()==getResources().getDrawable(R.drawable.ic_error).getConstantState()){
                    MyLog.d("UPNP contentItemClickListener 图片是ic_error, 不支持播放");
                    showDialog("该文件无法播放");
                    return;
                }
                MimeType localMimeType = content.getItem().getResources()
                        .get(0).getProtocolInfo().getContentFormatMimeType();
                MyLog.d("UPNP contentItemClickListener localMimeType: " + localMimeType );
                if (null == localMimeType) {
                    return;
                }
                String type = localMimeType.getType();
                if (null == type) {
                    return;
                }
                currentContentFormatMimeType = localMimeType.toString();
                MyLog.d("UPNP contentItemClickListener type: " + type + " ,currentContentFormatMimeType: " + currentContentFormatMimeType);
                Intent intent = new Intent();
                if (type.equals("image")) {
                    ConfigData.photoPosition = position;
                    jumpToImage(content);
                } else {
//                    jumpToControl(content);
                    jumpToUpnpControl(content, type.equals("audio"));
                }

            }
        }
    };

    private void jumpToUpnpControl(ContentItem localContentItem, boolean isAudio) {
        isToNextLevel = true;
        String uri = localContentItem.getItem().getFirstResource().getValue();
        Bundle bundle = new Bundle();
        bundle.putString("playURI", uri);
        bundle.putBoolean("isAudio", isAudio);
        bundle.putString("name", localContentItem.toString());
        MyLog.d("UPNP jumpToUpnpControl: " + uri + " ,name: " + localContentItem.toString());
        toClass(UpnpControlActivity.class, bundle, false);
    }

    private void jumpToImage(ContentItem localContentItem) {
        isToNextLevel = true;
//        Intent localIntent = new Intent(mActivity, ImageDisplay.class);
        Intent localIntent = new Intent(mActivity, UpnpImgActivity.class);
        localIntent.putExtra("name", localContentItem.toString());
        localIntent.putExtra("playURI", localContentItem.getItem()
                .getFirstResource().getValue());
        localIntent.putExtra("currentContentFormatMimeType",
                currentContentFormatMimeType);
        try {
            // localIntent.putExtra("metaData",
            // new GenerateXml().generate(localContentItem));
            localIntent.putExtra("metaData",
                    new GenerateXml().generate(localContentItem));
        } catch (Exception e) {
            e.printStackTrace();
        }
        startActivity(localIntent);
    }

    @Override
    public void deviceUpdate(DeviceItem deviceItem, boolean isAdd) {
        if(null == deviceItem) {
            showMsg("数据源数据更新，请重新加载数据源");
            finish();
        } else {
            if(!isAdd) {
                DeviceItem deviceItemCurrent = MyApp.deviceItem;
                if(deviceItemCurrent != null && deviceItemCurrent.getUdn().equals(deviceItem.getUdn())) {
                    showMsg("请检查数据源是否已关闭");
                    finish();
                    //                toClass(MainActivity.class, null, true);
                    //                try {
                    //                    if(MyUtil.isForeground(mActivity, UpnpControlActivity.class.getName())) {
                    //                        showDialogToFinish("当前数据源设备已断开，请点击确定离开本页面");
                    //                    } else {
                    //                        finish();
                    //                    }
                    //                } catch (Exception e) {
                    //                    e.printStackTrace();
                    //                    showMsg("请检查数据源是否已关闭");
                    //                    finish();
                    //                }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        MyUpnpUtil.unRegisterReceiver(mActivity, mUpnpReceiver);
        super.onDestroy();
    }

    class ContentAdapter extends BaseAdapter {

        private static final String TAG = "ContentAdapter";

        private Context context;

        private LayoutInflater mInflater;

        private Bitmap imageIcon;

        private Bitmap videoIcon;

        private Bitmap audioIcon;

        private Bitmap folderIcon;

        public int dmrPosition = 0;

        private ArrayList<ContentItem> mDeviceItems;

        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        public ContentAdapter(Context paramContext,
                              ArrayList<ContentItem> paramArrayList) {
            this.mInflater = LayoutInflater.from(mActivity);
            this.context = paramContext;
            this.mDeviceItems = paramArrayList;
            imageIcon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.icon_image);
            videoIcon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.icon_video);
            audioIcon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.icon_audio);
            folderIcon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.icon_folder);
        }

        public int getCount() {
            return this.mDeviceItems.size();
        }

        public Object getItem(int paramInt) {
            return this.mDeviceItems.get(paramInt);
        }

        public long getItemId(int paramInt) {
            return paramInt;
        }

        public View getView(int paramInt, View paramView,
                            ViewGroup paramViewGroup) {
            final ContentAdapter.ContentHolder localHolder;
            if (paramView == null) {
                paramView = this.mInflater.inflate(R.layout.item_upnp_content, null);
                localHolder = new ContentAdapter.ContentHolder();
                localHolder.ll_layout = (LinearLayout) paramView
                        .findViewById(R.id.ll_layout);
                localHolder.filename = (TextView) paramView
                        .findViewById(R.id.content_title_tv);
                localHolder.folder = (ImageView) paramView
                        .findViewById(R.id.icon_folder);
                localHolder.arrow = (ImageView) paramView
                        .findViewById(R.id.icon_arrow);
                paramView.setTag(localHolder);
            } else {
                localHolder = (ContentAdapter.ContentHolder) paramView.getTag();
            }
            ContentItem contentItem = (ContentItem) this.mDeviceItems
                    .get(paramInt);

            localHolder.filename.setText(contentItem.toString());

            if (!contentItem.isContainer()) {
                String imageUrl = null;
                if (null != contentItem.getItem().getResources().get(0)
                        .getProtocolInfo().getContentFormatMimeType()) {
                    String type = contentItem.getItem().getResources().get(0)
                            .getProtocolInfo().getContentFormatMimeType()
                            .getType();
                    if (type.equals("image")) {
                        localHolder.folder.setImageBitmap(imageIcon);
                        // if is image, display it
                        imageUrl = contentItem.getItem().getFirstResource()
                                .getValue();
                        int i = contentItem.getItem().getProperties().size();
                        for (int j = 0; j < i; j++) {
                            if (null != contentItem.getItem()
                                    && null != contentItem.getItem().getProperties()
                                    && null != contentItem.getItem().getProperties()
                                    .get(j)
                                    && ((DIDLObject.Property) contentItem.getItem()
                                    .getProperties().get(j))
                                    .getDescriptorName().equals("albumArtURI")) {

                                imageUrl = ((DIDLObject.Property) contentItem.getItem()
                                        .getProperties().get(j)).getValue().toString();
                                break;
                            }
                        }
                        imageLoader.displayImage(imageUrl, localHolder.folder, options,
                                animateFirstListener);
                    } else if (type.equals("video")) {
                        localHolder.folder.setImageBitmap(videoIcon);
                    } else if (type.equals("audio")) {
                        localHolder.folder.setImageBitmap(audioIcon);
                    }

                }

//                int i = contentItem.getItem().getProperties().size();
//                for (int j = 0; j < i; j++) {
//                    if (null != contentItem.getItem()
//                            && null != contentItem.getItem().getProperties()
//                            && null != contentItem.getItem().getProperties()
//                            .get(j)
//                            && ((DIDLObject.Property) contentItem.getItem()
//                            .getProperties().get(j))
//                            .getDescriptorName().equals("albumArtURI")) {
//
//                        imageUrl = ((DIDLObject.Property) contentItem.getItem()
//                                .getProperties().get(j)).getValue().toString();
//                        break;
//                    }
//                }
//                imageLoader.displayImage(imageUrl, localHolder.folder, options,
//                        animateFirstListener);
                localHolder.arrow.setVisibility(View.GONE);
                if(localHolder.folder.getDrawable().getCurrent().getConstantState()==getResources().getDrawable(R.drawable.ic_error).getConstantState()){
                    localHolder.ll_layout.setVisibility(View.GONE);
                }
            } else {
                localHolder.folder.setImageBitmap(folderIcon);
                localHolder.arrow.setVisibility(View.VISIBLE);

            }
            return paramView;
        }

        class ContentHolder {

            public LinearLayout ll_layout;

            public TextView filename;

            public ImageView folder;

            public ImageView arrow;

            public ContentHolder() {
            }
        }

    }

    private static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
