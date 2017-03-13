package com.plmz.fl.tool;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Jump {
	//下面介绍怎么判断手机已安装某程序的方法：
	public static boolean isAvilible(Context context, String packageName){
		final PackageManager packageManager = context.getPackageManager();//获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
		List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名
		//从pinfo中将包名字逐一取出，压入pName list中
		if(pinfo != null){
			for(int i = 0; i < pinfo.size(); i++){
				String pn = pinfo.get(i).packageName;
				pName.add(pn);
			}
		}
		return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
	}
	/**
	 * 启动第三方 app

	 * @param mcontext
	 * @param packagename
	 */
	public static void doStartApplicationWithPackageName (Context mcontext, String packagename,String uriStr,int fromType) {
		// 通过包名获取此 APP 详细信息，包括 Activities、 services 、versioncode 、 name等等
		PackageInfo packageinfo = null;
		try {
			packageinfo = mcontext.getPackageManager().getPackageInfo(packagename, 0 );
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace() ;
		}
		if (packageinfo == null) {
			return;
		}

		// 创建一个类别为 CATEGORY_LAUNCHER 的该包名的 Intent
		Intent resolveIntent = new Intent(Intent. ACTION_MAIN, null) ;
		resolveIntent.setFlags(Intent. FLAG_ACTIVITY_NEW_TASK ) ;
		resolveIntent.addCategory(Intent. CATEGORY_LAUNCHER );
		resolveIntent.setPackage(packageinfo. packageName );

		// 通过 getPackageManager()的 queryIntentActivities 方法遍历
		List<ResolveInfo> resolveinfoList = mcontext.getPackageManager()
				.queryIntentActivities(resolveIntent, 0) ;

		ResolveInfo resolveinfo = resolveinfoList.iterator().next();
		if (resolveinfo != null ) {
			// packagename = 参数 packname
			String packageName = resolveinfo.activityInfo.packageName;
			// 这个就是我们要找的该 APP 的LAUNCHER 的 Activity[组织形式： packagename.mainActivityname]
//			String className = resolveinfo. activityInfo .name ;
			// LAUNCHER Intent
//			Intent intent = new Intent(Intent. ACTION_MAIN) ;
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
//			intent.addCategory(Intent.CATEGORY_DEFAULT);

//			Intent intent = new Intent();
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			intent.setAction(Intent.ACTION_VIEW);
//			intent.addCategory(Intent.CATEGORY_DEFAULT);
//			intent.setClassName(packageName, "tv.danmaku.ijk.media.demo.MainActivity");
//			Bundle bundle = new Bundle();
//			bundle.putString("uri", "http://f:p@ftp-pan.com:88/7652/我的照顧幫手.rmvb");
//			intent.putExtras(bundle);

			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriStr));//"xg://f:p@ftp-pan.com:88/7652/我的照顧幫手.rmvb"
			intent.setAction(Intent.ACTION_VIEW);
			String activityStr = "";
			switch (fromType){
				case 1:
					activityStr = "tv.danmaku.ijk.media.demo.MainActivity";
					break;
				case 2:
					activityStr = "com.xfplay.play.gui.MainActivity";
					break;
			}
			intent.setClassName(packageName,activityStr);
			mcontext.startActivity(intent) ;
		}
	}

	/**
	 * 下载第三方 app
	 * @param mContext
	 */
	public static void downloadApp (Context mContext,int fromType) {
		String downloadUrl = "";
		switch (fromType){
			case 1:
				downloadUrl = "http://zhushou.360.cn/detail/index/soft_id/3032510?recrefer=SE_D_%E8%A5%BF%E7%93%9C#nogo";
				break;
			case 2:
				downloadUrl = "http://shouji.baidu.com/software/10380040.html";
				break;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
		mContext.startActivity(intent) ;
	}
}
