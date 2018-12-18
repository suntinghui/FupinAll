package com.jkrm.fupin.ui.activity;

import android.Manifest;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkrm.fupin.R;
import com.jkrm.fupin.adapter.LocalResAdapter;
import com.jkrm.fupin.base.BaseActivity;
import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.bean.LocalResFileBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.interfaces.IPermissionListener;
import com.jkrm.fupin.upnp.dms.ContentNode;
import com.jkrm.fupin.upnp.dms.ContentTree;
import com.jkrm.fupin.upnp.util.ImageUtil;
import com.jkrm.fupin.util.MyFileUtil;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.util.PermissionUtil;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.PersonWithRole;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.WriteStatus;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.ImageItem;
import org.fourthline.cling.support.model.item.MusicTrack;
import org.fourthline.cling.support.model.item.VideoItem;
import org.seamless.util.MimeType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.provider.MediaStore.Video.Thumbnails.MINI_KIND;

/**
 * Created by hzw on 2018/9/13.
 */

public class LocalResActivity extends BaseActivity implements Handler.Callback {

    @BindView(R.id.tv_titleName)
    TextView mTvTitleName;
    @BindView(R.id.rcv_data)
    RecyclerView mRcvData;
    @BindView(R.id.rcv_data2)
    RecyclerView mRcvData2;
    @BindView(R.id.tv_emptyView)
    TextView mTvEmptyView;
    @BindView(R.id.tv_emptyView2)
    TextView mTvEmptyView2;
    private static final int SEARCH_START_VIDEO = 0;
    private static final int SEARCH_FINISH_VIDEO = 1;
    private static final int CREATE_THUMB_START = 2;
    private static final int CREATE_THUMB_FINISH = 3;
    private List<LocalResFileBean> mVideoList = new ArrayList<>();
    private List<LocalResFileBean> mAudioList = new ArrayList<>();
    private Handler mHandler;
    private LocalResAdapter mVideoAdapter, mAudioAdapter;
    private List<File> mSearchFileList = new ArrayList<>();
    private String[] imageThumbColumns = new String[]{
            MediaStore.Images.Thumbnails.IMAGE_ID,
            MediaStore.Images.Thumbnails.DATA};

    @Override
    protected int getContentId() {
        return R.layout.activity_local_res;
    }

    @Override
    protected void initView() {
        super.initView();
        mTvTitleName.setText("硬盘资源");
    }

    @Override
    protected void initData() {
        super.initData();
        mHandler = new Handler(this);
        initRcv1();
        initRcv2();
        mSearchFileList = MyApp.localSaveDiskPath;
        if(MyUtil.isEmptyList(mSearchFileList)) {
            showDialog("获取硬盘资源失败，请检查是否已内置硬盘设备");
            MyUtil.showEmptyView(mTvEmptyView, mRcvData, false);
            MyUtil.showEmptyView(mTvEmptyView, mRcvData2, false);
        } else {
            PermissionUtil.getInstance().checkPermission(mActivity, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, new IPermissionListener() {
                @Override
                public void granted() {
                    getLocalVideoList();
                }

                @Override
                public void shouldShowRequestPermissionRationale() {
                    showDialog("需读写存储卡权限，已被禁用， 请至系统设置打开该APP的读写权限后才可使用");
                    //                                    PermissionUtil.toAppSetting(mActivity);
                }

                @Override
                public void needToSetting() {
                    showDialog("需读写存储卡权限，已被禁用， 请至系统设置打开该APP的读写权限后才可使用");
                    //                                    PermissionUtil.toAppSetting(mActivity);
                }
            });
        }

    }

    private void initRcv1() {
        mVideoAdapter = new LocalResAdapter();
        MyUtil.setRecyclerViewGridlayoutCommon(mActivity, mRcvData, mVideoAdapter, true);
        mVideoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LocalResFileBean bean = mVideoList.get(position);
                if (TextUtils.isEmpty(bean.getFile().getAbsolutePath())) {
                    showMsg("当前资源地址为空, 无法播放");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(MyConstants.Params.COMMON_PARAMS_BEAN, bean.getFile());
                toClass(MediaPlayActivity.class, bundle, false);
            }
        });
    }

    private void initRcv2() {
        mAudioAdapter = new LocalResAdapter();
        MyUtil.setRecyclerViewGridlayoutCommon(mActivity, mRcvData2, mAudioAdapter, true);
        mAudioAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LocalResFileBean bean = mAudioList.get(position);
                if (TextUtils.isEmpty(bean.getFile().getAbsolutePath())) {
                    showMsg("当前资源地址为空, 无法播放");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(MyConstants.Params.COMMON_PARAMS_BEAN, bean.getFile());
                toClass(MediaPlayActivity.class, bundle, false);
            }
        });
    }

    private void getLocalVideoList() {
        mHandler.sendEmptyMessage(SEARCH_START_VIDEO);
        new Thread(new Runnable() {
            @Override
            public void run() {
//                checkLocalRes();
                checkFile();
                mHandler.sendEmptyMessage(SEARCH_FINISH_VIDEO);
            }
        }).start();
    }

    private void checkFile(File file) {// 遍历文件，在这里是遍历sdcard
        if (file.isDirectory()) {// 判断是否是文件夹
            File[] files = file.listFiles();// 以该文件夹的子文件或文件夹生成一个数组
            if (files != null) {// 如果文件夹不为空
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    checkFile(f);// 递归调用
                }
            }
        } else if (file.isFile()) {// 判断是否是文件
            if (MyUtil.isVideoType(file.getName())) {
                mVideoList.add(createLocalResFileBean(file));
            } else if (MyUtil.isAudioType(file.getName())) {
                mAudioList.add(createLocalResFileBean(file));
            }
        }
    }

    private void checkFile() {// 遍历文件，在这里是遍历sdcard
        List<File> checkList = new ArrayList<>();
        for(File file : mSearchFileList) {
            File tempFile = new File(file.getAbsolutePath() + MyConstants.Constant.DISK_DIRECTORY);
            if(tempFile.exists())
                checkList.add(tempFile);
        }
        for(File file : mSearchFileList) {
            File tempFile = new File(file.getAbsolutePath() + MyConstants.Constant.DISK_DIRECTORY_APP);
            if(tempFile.exists())
                checkList.add(tempFile);
        }
        for(File file : checkList) {
            if (file.isDirectory()) {// 判断是否是文件夹
                File[] files = file.listFiles();// 以该文件夹的子文件或文件夹生成一个数组
                if (files != null) {// 如果文件夹不为空
                    for (int i = 0; i < files.length; i++) {
                        File f = files[i];
                        checkFile(f);// 递归调用
                    }
                }
            } else if (file.isFile()) {// 判断是否是文件
                if (MyUtil.isVideoType(file.getName())) {
                    mVideoList.add(createLocalResFileBean(file));
                } else if (MyUtil.isAudioType(file.getName())) {
                    mAudioList.add(createLocalResFileBean(file));
                }
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case SEARCH_START_VIDEO:
                showLoadingDialog();
                break;
            case SEARCH_FINISH_VIDEO:
                showMsg("本机资源文件搜索完毕");
//                showMsg("视频文件加载完成");
                MyLog.d("LocalResActivity mVideoList size: " + mVideoList.size());
                MyLog.d("LocalResActivity mAudioList size: " + mAudioList.size());
//                getLocalVideoList(false);
                mVideoAdapter.addAllData(mVideoList);
                mAudioAdapter.addAllData(mAudioList);
                MyUtil.showEmptyView(mTvEmptyView, mRcvData, !MyUtil.isEmptyList(mVideoList));
                MyUtil.showEmptyView(mTvEmptyView2, mRcvData2, !MyUtil.isEmptyList(mAudioList));
                createThumb();
                dismissLoadingDialog();
                break;
            case CREATE_THUMB_START:
                MyLog.d("LocalResActivity 创建缩略图开始");
                break;
            case CREATE_THUMB_FINISH:
                MyLog.d("LocalResActivity 创建缩略图结束");
                mVideoAdapter.addAllData(mVideoList);
//                showMsg("视频缩略图加载完毕");
                break;
        }
        return false;
    }

    private LocalResFileBean createLocalResFileBean(File file) {
        String orginalName = file.getName();
        //检测是否本机下载过
        if(file.getAbsolutePath().contains(MyConstants.Constant.DISK_DIRECTORY_APP)) {
            String dbDataLocal = MyFileUtil.inDb(file.getAbsolutePath());
            if (null != dbDataLocal) {
                orginalName = dbDataLocal;
            }
        }
        Bitmap bitmap = null;
        LocalResFileBean localResFileBean = new LocalResFileBean();
        localResFileBean.setFile(file);
        if(bitmap != null) {
            localResFileBean.setBitmap(bitmap);
        }
        localResFileBean.setOriginName(orginalName);
        return localResFileBean;
    }

    private LocalResFileBean createLocalResFileBean(String path, Bitmap bitmap) {
        LocalResFileBean localResFileBean = new LocalResFileBean();
        localResFileBean.setFile(new File(path));
        if(bitmap != null) {
            localResFileBean.setBitmap(bitmap);
        }
        return localResFileBean;
    }

    private void createThumb() {
        if(MyUtil.isEmptyList(mVideoList)) {
            return;
        }
        mHandler.sendEmptyMessage(CREATE_THUMB_START);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(LocalResFileBean tempBean : mVideoList) {
                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(tempBean.getFile().getAbsolutePath(), MINI_KIND);
                    tempBean.setBitmap(bitmap);
                }
                mHandler.sendEmptyMessage(CREATE_THUMB_FINISH);
            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        mHandler.removeMessages(SEARCH_START_VIDEO);
        mHandler.removeMessages(SEARCH_FINISH_VIDEO);
        mHandler.removeMessages(CREATE_THUMB_START);
        mHandler.removeMessages(CREATE_THUMB_FINISH);
        super.onDestroy();
    }


    private void checkLocalRes() {
        Cursor thumbCursor = getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                imageThumbColumns, null, null, null);
        HashMap<Integer, String> imageThumbs = new HashMap<Integer, String>();
        if (null != thumbCursor && thumbCursor.moveToFirst()) {
            do {
                imageThumbs
                        .put(thumbCursor.getInt(0), thumbCursor.getString(1));
            } while (thumbCursor.moveToNext());

            if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                thumbCursor.close();
            }
        }
        Bitmap bitmap = null;
        Cursor cursor;
        String[] videoColumns = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.ARTIST,
                MediaStore.Video.Media.MIME_TYPE, MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.RESOLUTION,
                MediaStore.Video.Media.DESCRIPTION };
        cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                videoColumns, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String id = ContentTree.VIDEO_PREFIX
                        + cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Video.Media._ID));
                String title = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                String filePath = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                if(!checkPathFilter(filePath, 0)) {
                    continue;
                }
                if(imageThumbs.containsKey(id)) {
                    try {
                        FileInputStream fis = new FileInputStream(imageThumbs.get(id));
                        bitmap = BitmapFactory.decodeStream(fis);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    bitmap = null;
                }
                mVideoList.add(createLocalResFileBean(filePath, bitmap));
            } while (cursor.moveToNext());
        }

        // Audio Container

        String[] audioColumns = { MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM };
        cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                audioColumns, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String id = ContentTree.AUDIO_PREFIX
                        + cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String filePath = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

                if(!checkPathFilter(filePath, 1)) {
                    continue;
                }
                if(imageThumbs.containsKey(id)) {
                    try {
                        FileInputStream fis = new FileInputStream(imageThumbs.get(id));
                        bitmap = BitmapFactory.decodeStream(fis);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    bitmap = null;
                }
                mVideoList.add(createLocalResFileBean(filePath, bitmap));
            } while (cursor.moveToNext());
        }
        try {
            if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param path 文件路径
     * @param checkTag 检测类型 视频0 音频1 图片2
     * @return
     */
    private boolean checkPathFilter(String path, int checkTag) {
        MyLog.d("upnp service checkPathFilter checkTag: " + checkTag + " ,path: " + path);
        if(path == null)
            return false;
        if(path.contains("android-sdk-windows")) {
            MyLog.d("upnp service checkPathFilter ,path: " + path + " ,path路径不符合规则1, 返回false");
            return false;
        }
        if(!path.contains(MyConstants.Constant.DISK_DIRECTORY) && !path.contains(MyConstants.Constant.DISK_DIRECTORY_APP)) {
            MyLog.d("upnp service checkPathFilter ,path: " + path + " ,path路径不符合规则2, 返回false");
            return false;
        }
        //MediaStore 存在垃圾数据, 已删除的有可能仍然存在, 检测在存储空间是否存在
        File file = new File(path);
        if(!file.exists()) {
            MyLog.d("upnp service checkPathFilter ,path: " + path + " ,path 文件不存在, 返回false");
            return false;
        }
        switch (checkTag) {
            case 0:
                if(!MyUtil.isVideoType(path)) {
                    MyLog.d("upnp service checkPathFilter checkTag: " + checkTag + " ,path: " + path + " ,不是视频, 返回false");
                    return false;
                }
                break;
            case 1:
                if(!MyUtil.isAudioType(path)) {
                    MyLog.d("upnp service checkPathFilter checkTag: " + checkTag + " ,path: " + path + " ,不是音频, 返回false");
                    return false;
                }
                break;
            case 2:
                if(!MyUtil.isImageType(path)) {
                    MyLog.d("upnp service checkPathFilter checkTag: " + checkTag + " ,path: " + path + " ,不是图片, 返回false");
                    return false;
                }
                break;
        }
        return true;
    }
}
