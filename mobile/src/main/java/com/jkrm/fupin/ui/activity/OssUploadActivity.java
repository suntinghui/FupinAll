package com.jkrm.fupin.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkrm.fupin.R;
import com.jkrm.fupin.adapter.UploadNewAdapter;
import com.jkrm.fupin.base.BaseMvpActivity;
import com.jkrm.fupin.bean.AppUploadVideoResultBean;
import com.jkrm.fupin.bean.OssTokenBeanNew;
import com.jkrm.fupin.bean.UploadBean;
import com.jkrm.fupin.bean.VodTypeBean;
import com.jkrm.fupin.bean.request.AppUploadVideoRequestBean;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.mvp.contracts.UploadContract;
import com.jkrm.fupin.mvp.presenters.UploadPresenter;
import com.jkrm.fupin.util.DisplayUtil;
import com.jkrm.fupin.util.FileGetUriPath;
import com.jkrm.fupin.util.ImageUtil;
import com.jkrm.fupin.util.MyFileUtil;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.MyUtil;
import com.jkrm.fupin.util.NetWatchdog;
import com.jkrm.fupin.util.SystemIntentUtil;
import com.jkrm.fupin.util.VideoUtil;
import com.jkrm.fupin.widgets.CommonListPopupWindow;
import com.jkrm.fupin.widgets.CustomToolbar;
import com.jkrm.fupin.widgets.RecycleViewDivider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.provider.MediaStore.Video.Thumbnails.MINI_KIND;

/**
 * Created by hzw on 2018/8/10.
 */

public class OssUploadActivity extends BaseMvpActivity<UploadPresenter> implements UploadContract.View, Handler.Callback {

    @BindView(R.id.tv_classify)
    TextView mTvClassify;
    @BindView(R.id.btn_upload)
    Button mBtnUpload;
    @BindView(R.id.btn_add)
    Button mBtnAdd;
    @BindView(R.id.rcv_data)
    RecyclerView mRcvData;
    private String endpoint = "https://oss-shanghai.aliyuncs.com";
    private String bucket = "out-20180112-voddemo";
    private String accessKeyId = "STS.NHkLGu2c7ZgPf8ZVxBHPBQXTD";
    private String accessKeySecret = "J6MadsLRgSEzVQNKTNnvt42nLuET4ThWwbZhN1MNwnTx";
    private String secretToken = "CAISjgJ1q6Ft5B2yfSjIr4veB/3B37wW7aW7ZB7rsngXRN9uvp3/pjz2IHtKenZsCegav/Q3nW1V7vsdlrBtTJNJSEnDKNF36pkS6g66eIvGvYmz5LkJ0BUzl6EoT0yV5tTbRsmkZu6/E67fUzKpvyt3xqSAO1fGdle5MJqPpId6Z9AMJGeRZiZHA9EkQGkPtsgWZzmzWPG2KUyo+B2yanBloQ1hk2hyxL2iy8mHkHrkgUb91/UeqvaaQPHmTbE1Z88kAofpgrcnJvKfiHYI0XUQqvcq1p4j0Czco9SQD2NW5xi7KOfO+rVtVlQiOPZlR/4c8KmszaQl57SOyJ6U1RFBMexQVD7YQI2wGDdS2XJ/9rwagAGfN801ufyIzfBF57vleHf9PP69jxqIHaKXDKRI1ill0CdBdz59EjU/LBI8fS4mJY1LPTCF3Uat1teJeHH7uS09lqVw8ue5fa/IyvtCZD2NC6b4Ybw3vMAJs6cDYf5U53fNwvO6TOEDMhUjy+ExUcXnAM7g0Lozetg/6HVDu96Qcw==";
    private String expireTime = "2018-08-13T16:58:44Z";
    private String prefix = "shortvideodemo/";

    private static final int CODE_REQUEST_SELECT_FILE = 1;
    private static final int MSG_UPLOAD_IMG = 1;
    private static final int MSG_UPLOAD_CALLBACK = 2;
    private static final String TYPE_VIDEO = "0";
    private static final String TYPE_AUDIO = "1";
    private static final String TYPE_IMG = "2";
    private static final String TYPE_HEAD = "3";
    private static final String TYPE_VIDEO_IMG = "12";
    private List<UploadBean> mList = new ArrayList<>();
    private UploadNewAdapter mAdapter;
    private NetWatchdog netWatchdog;
    private Handler mHandler;
    private int totalCount = 0;
    private int completeCount = 0;
    private OSS oss;
    private String videoPath;
    private String userId = "";
    private String classify = null;//分类id
    private UploadBean mUploadBean;
    private OSSAsyncTask task;
    private List<VodTypeBean> mVodTypeBeanList = new ArrayList<>();

    @Override
    protected UploadPresenter createPresenter() {
        return new UploadPresenter(this);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_upload;
    }

    @Override
    protected void initView() {
        super.initView();
        mToolbar.setToolbarTitle(getString(R.string.title_video_upload));
        mToolbar.setLeftImg(R.mipmap.back);
        mToolbar.hideRightView();
        mToolbar.setOnLeftClickListener(new CustomToolbar.OnLeftClickListener() {
            @Override
            public void onLeftTextClick() {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        userId = getIntent().getExtras().getString(MyConstants.Params.COMMON_PARAMS);
        mHandler = new Handler(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRcvData.setLayoutManager(layoutManager);
        mAdapter = new UploadNewAdapter();
        mAdapter.setNewData(mList);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        mRcvData.addItemDecoration(new RecycleViewDivider(
                mActivity, LinearLayoutManager.HORIZONTAL,
                DisplayUtil.dip2px(mActivity, 0.5f), mActivity.getResources().getColor(R.color.color_ebebeb)));
        mRcvData.setAdapter(mAdapter);

        mPresenter.getVodTypeList();
        mPresenter.getAppOssUrl(userId, TYPE_VIDEO);
        initOss();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mTvClassify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtil.showCommonListPopupWindow(mActivity, mVodTypeBeanList, mToolbar, new CommonListPopupWindow.OnItemClickListener() {
                            @Override
                            public void onClick(Object bean) {
                                if(bean instanceof VodTypeBean) {
                                    setText(mTvClassify, ((VodTypeBean) bean).getName());
                                    classify = ((VodTypeBean) bean).getId();
                                }
                            }
                        });
            }
        });
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mList == null)
                    mList = new ArrayList<>();
                if(mList.size() > 0) {
                    if(mList.get(0).getProgress() != 100) {
                        showDialogWithCancelBtn("目前仅允许同时上传一个视频文件, 是否结束本次上传, 更换其他视频文件?", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(task != null)
                                    task.cancel();
                                mList.remove(0);
                                SystemIntentUtil.selectVideoFile(mActivity, CODE_REQUEST_SELECT_FILE);
                                dismissDialog();
                            }
                        });
                    } else {
                        mList.remove(0);
                        SystemIntentUtil.selectVideoFile(mActivity, CODE_REQUEST_SELECT_FILE);
                    }
                } else {
                    SystemIntentUtil.selectVideoFile(mActivity, CODE_REQUEST_SELECT_FILE);
                }
            }
        });

        mBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(classify)) {
                    showDialog("请先选择视频分类");
                    return;
                }
                if(mList == null || mList.size() <= 0) {
                    showMsg("请先选择要上传的视频文件");
                    return;
                }
                if(mList.get(0).getProgress() == 100) {
                    showDialog("该视频已上传成功, 请勿重复上传");
                    return;
                }
                MyLog.d("上传时accessKeyId: " + accessKeyId + " \naccessKeySecret: " + accessKeySecret +
                        " \nsecretToken: " + secretToken + " \nexpireTime: " + expireTime + " \nendpoint: " + endpoint + " \nBucket: " + bucket + " \nprefix: " + prefix);
                showLoadingDialog();
                totalCount = mList.size();
                for(int i=0; i<mList.size(); i++) {
                    putObject(mList.get(i), TYPE_VIDEO);
                }
            }
        });

        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if(mList.get(0).getProgress() != 100) {
                    showDialogWithCancelBtn("文件正在上传, 确定要删除么?", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(task != null)
                                task.cancel();
                            mList.remove(0);
                            mAdapter.notifyDataSetChanged();
                            dismissDialog();
                        }
                    });
                } else {
                    showDialogWithCancelBtn("确定现在删除该视频上传进度显示么?", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mList.remove(0);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
                return false;
            }
        });
    }

    private void initOss() {
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, secretToken);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setHttpDnsEnable(false);//默认是true 开启，需要关闭可以设置false
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();  //调用此方法即可开启日志
        MyLog.d("oss initOss endpoint: " + endpoint);
        oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider, conf);
    }

    private void putObject(UploadBean bean, final String type) {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucket, bean.getOssName(), bean.getFilePath());
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//                MyLog.d("PutObject", "getObjectKey: " + request.getObjectKey());
//                MyLog.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                if(TYPE_VIDEO.equals(type)) {
                    for (int i = 0; i < mAdapter.getItemCount(); i++) {
                        if (mAdapter.getItem(i).getOssName().equals(request.getObjectKey())) {
                            mAdapter.getItem(i).setProgress(currentSize * 100 / totalSize);
                            //                        mAdapter.getItem(i).setStatus(MyConstants.Upload.STATUS_UPLOADING);
                            mHandler.sendEmptyMessage(0);
                            break;
                        }
                    }
                }

            }
        });
        task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                MyLog.d("PutObject", "UploadSuccess request: " + request.getObjectKey() + " \nresult: " + result.getServerCallbackReturnBody() + " \ntype: " + type);
                dismissLoadingDialog();
                if(TYPE_VIDEO.equals(type)) {
                    //继续上传缩略图
                    mHandler.sendEmptyMessage(MSG_UPLOAD_IMG);
                } else if(TYPE_VIDEO_IMG.equals(type)) {
                    //全部上传后回调到服务器. 添加关联
                    mHandler.sendEmptyMessage(MSG_UPLOAD_CALLBACK);
                }
            }
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    MyLog.e("ErrorCode", serviceException.getErrorCode());
                    MyLog.e("RequestId", serviceException.getRequestId());
                    MyLog.e("HostId", serviceException.getHostId());
                    MyLog.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
        // task.cancel(); // 可以取消任务
        // task.waitUntilFinished(); // 可以等待直到任务完成
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPLOAD_IMG:
                mPresenter.getAppOssUrl(userId, TYPE_VIDEO_IMG);
                break;
            case MSG_UPLOAD_CALLBACK:
                AppUploadVideoRequestBean appUploadVideoRequestBean = new AppUploadVideoRequestBean();
                appUploadVideoRequestBean.setCreateuser(userId);
                appUploadVideoRequestBean.setClassify(classify);
                appUploadVideoRequestBean.setMetatype(mUploadBean.getMetatype());
                appUploadVideoRequestBean.setName(mUploadBean.getFileName());
                appUploadVideoRequestBean.setOrginname(mUploadBean.getFileName());
                appUploadVideoRequestBean.setTitle(mUploadBean.getNormalFileName());
                //http://tianyu-bucket.oss-cn-hangzhou.aliyuncs.com/
                String url = "http://" + bucket + "." + endpoint + "/";
                appUploadVideoRequestBean.setImgurl(url + mUploadBean.getOssImgName());
                appUploadVideoRequestBean.setOsspath(url + mUploadBean.getOssName());
                appUploadVideoRequestBean.setTotaltime(mUploadBean.getDuration());
                mPresenter.appUploadVideo(appUploadVideoRequestBean);
                break;
            default:
                mAdapter.notifyDataSetChanged();
                break;
        }
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODE_REQUEST_SELECT_FILE) {
                mList = new ArrayList<>();
                Uri uri = data.getData();
                MyLog.d("文件路径1: " + uri.getPath().toString());
                videoPath = FileGetUriPath.getPath(mActivity, uri);
                if(!MyUtil.isVideoType(MyFileUtil.getFileNameWithTypeEnd(videoPath))) {
                    showDialog("仅支持上传视频文件, 请重新选择");
                    return;
                }
                MyLog.d("文件路径2: " + videoPath);
                String normalFileName = MyFileUtil.getFileNameWithType(videoPath);
                String fileName = android.os.Build.SERIAL + System.currentTimeMillis() + MyFileUtil.getFileNameWithTypeEnd(videoPath);
                MyLog.d("添加了一个文件: " + fileName);

                mUploadBean = new UploadBean();
                mUploadBean.setNormalFileName(normalFileName);
                mUploadBean.setFileName(fileName);
                mUploadBean.setFilePath(videoPath);
                mUploadBean.setOssName(prefix + fileName);
                mUploadBean.setProgress(0);
                mUploadBean.setMetatype(MyFileUtil.getMIMEType(new File(videoPath)));
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MINI_KIND);
                if(bitmap == null)
                    bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.cover);
                mUploadBean.setThumbnail(bitmap);
                String thumbnailName = android.os.Build.SERIAL + System.currentTimeMillis() + ".png";
                File file = new File(getExternalFilesDir(""), thumbnailName);
                ImageUtil.saveBitmapAsPng(bitmap, file);
                mUploadBean.setThumbnailPath(file.getAbsolutePath());
                mUploadBean.setThumbnailName(thumbnailName);
                mUploadBean.setDuration(VideoUtil.getVideoDuration(videoPath));
                mList.add(mUploadBean);
                MyLog.d("文件集合大小: " + mList.size());
                mAdapter.addAllData(mList);
            }
        }
    }

    @Override
    public void getVodTypeListSuccess(List<VodTypeBean> list) {
        if(MyUtil.isEmptyList(list)) {
            showDialogToFinish("获取视频分类列表失败, 请稍后重试, 暂时无法提供上传服务");
        } else {
            mVodTypeBeanList = list;
        }

    }

    @Override
    public void getVodTypeListFail(String msg) {
        showDialogToFinish("获取视频分类列表失败, 请稍后重试, 暂时无法提供上传服务");
    }

    @Override
    public void getAppOssUrlSuccess(OssTokenBeanNew bean, String type) {
        accessKeyId = bean.getAccessKeyId();
        accessKeySecret = bean.getAccessKeySecret();
        secretToken = bean.getSecurityToken();
        expireTime = bean.getExpiration();
        endpoint = bean.getAppEndPoint();
        bucket = bean.getAppBucket();
        prefix = bean.getDir();
        initOss();
        MyLog.d("上传时accessKeyId: " + accessKeyId + " \naccessKeySecret: " + accessKeySecret +
                " \nsecretToken: " + secretToken + " \nexpireTime: " + expireTime + " \nendpoint: " + endpoint + " \nBucket: " + bucket + " \nprefix: " + prefix);
        if(TYPE_VIDEO_IMG.equals(type)) {
            //视频上传成功后, 继续上传缩略图
            UploadBean imgUploadBean = new UploadBean();
//            String fileName = MyFileUtil.getFileNameWithType(mUploadBean.getThumbnailPath());
            imgUploadBean.setFileName(mUploadBean.getThumbnailName());
            imgUploadBean.setFilePath(mUploadBean.getThumbnailPath());
            String imgOssName = prefix + mUploadBean.getThumbnailName();
            imgUploadBean.setOssName(imgOssName);
            mUploadBean.setOssImgName(imgOssName);
            imgUploadBean.setProgress(0);
            putObject(imgUploadBean, TYPE_VIDEO_IMG);
        }
    }

    @Override
    public void getAppOssUrlFail(String msg) {
        showDialogToFinish(msg);
    }

    @Override
    public void appUploadVideoSuccess(AppUploadVideoResultBean bean) {
        MyLog.d("Oss upload result: " + bean.toString());
        showMsg(bean.getName() + "上传成功");
    }

    @Override
    public void appUploadVideoFail(String msg) {

    }
}
