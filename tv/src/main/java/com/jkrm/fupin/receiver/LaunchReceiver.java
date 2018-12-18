package com.jkrm.fupin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;

import com.jkrm.fupin.base.MyApp;
import com.jkrm.fupin.constants.MyConstants;
import com.jkrm.fupin.service.DbCheckService;
import com.jkrm.fupin.service.MediaCheckService;
import com.jkrm.fupin.service.OssResDownloadService;
import com.jkrm.fupin.service.UpnpService;
import com.jkrm.fupin.util.MyLog;
import com.jkrm.fupin.util.PollingUtils;

/**
 * No practical effect(20160713)
 * @author joey.huang
 *
 */
public class LaunchReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
			MyApp.isShutDown = false;
			MyLog.e("开机了");
			PollingUtils.startPollingService(context, MyConstants.Constant.POLLING_INTEVAL_DB_SERVICE, DbCheckService.class, MyConstants.Action.ACTION_SERVICE_CHECK_DB);
			PollingUtils.startPollingService(MyApp.getInstance(), MyConstants.Constant.POLLING_INTEVAL_MEIDA_SERVICE, MediaCheckService.class, MyConstants.Action.ACTION_SERVICE_CHECK_MEDIA);
			PollingUtils.startPollingService(MyApp.getInstance(), MyConstants.Constant.POLLING_INTEVAL_UPNP_SERVICE, UpnpService.class, MyConstants.Action.ACTION_SERVICE_CHECK_UPNP);
			PollingUtils.startPollingService(MyApp.getInstance(), MyConstants.Constant.POLLING_INTEVAL_OSS_DOWNLOAD_SERVICE, OssResDownloadService.class, MyConstants.Action.ACTION_SERVICE_OSS_DOWNLOAD);
		}
		if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
			MyApp.isShutDown = true;
			MyLog.e("关机了");
			PollingUtils.stopPollingService(context, DbCheckService.class, MyConstants.Action.ACTION_SERVICE_CHECK_DB);
			PollingUtils.stopPollingService(context, MediaCheckService.class, MyConstants.Action.ACTION_SERVICE_CHECK_MEDIA);
			PollingUtils.stopPollingService(context, UpnpService.class, MyConstants.Action.ACTION_SERVICE_CHECK_UPNP);
			PollingUtils.stopPollingService(context, OssResDownloadService.class, MyConstants.Action.ACTION_SERVICE_OSS_DOWNLOAD);

		}
	}
}
