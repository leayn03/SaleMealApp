package com.meal.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;

/**
 * @author xiamingxing
 * 
 */
public class DialogUtil {

	/**
	 * @param content
	 * @param activity
	 */
	public static void alert(String content, Activity activity) {

		alert("提示", content, android.R.drawable.arrow_down_float, activity);

	}

	/**
	 * @param title
	 * @param content
	 * @param icon
	 * @param activity
	 */
	public static void alert(String title, String content, int icon,
			Activity activity) {
		Dialog alertDialog = new AlertDialog.Builder(activity)
				.setTitle(title)
				.setMessage(content)
				.setIcon(icon)
				.setPositiveButton("确定",
						new android.content.DialogInterface.OnClickListener() {

							// @Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

								dialog.dismiss();

							}

						}).

				create();

		alertDialog.show();

	}

	/**
	 * @param content
	 * @param activity
	 * @param callBackForDialogBtn
	 */
	public static void prompt(String content, Activity activity,
			final CallBackForDialogBtn callBackForDialogBtn) {

		AlertDialog.Builder builder = new Builder(activity);

		builder.setTitle("提示")
				.setMessage(content)
				.setIcon(android.R.drawable.arrow_down_float)
				.setPositiveButton("确定",
						new android.content.DialogInterface.OnClickListener() {

							// @Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

								callBackForDialogBtn.confirm();
								dialog.dismiss();

							}

						})
				.

				setNegativeButton("取消",
						new android.content.DialogInterface.OnClickListener() {

							// @Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

								callBackForDialogBtn.cancel();
								dialog.dismiss();

							}

						});

		builder.create().show();

	}

	/**
	 * @param title
	 * @param content
	 * @param icon
	 * @param activity
	 * @param callBackForDialogBtn
	 */
	public static void prompt(String title, String content, int icon,
			Activity activity, final CallBackForDialogBtn callBackForDialogBtn) {

		AlertDialog.Builder builder = new Builder(activity);

		builder.setTitle(title)
				.setMessage(content)
				.setIcon(icon)
				.setPositiveButton("确定",
						new android.content.DialogInterface.OnClickListener() {

							// @Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

								callBackForDialogBtn.confirm();
								dialog.dismiss();

							}

						})
				.setNegativeButton("取消",
						new android.content.DialogInterface.OnClickListener() {

							// @Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

								callBackForDialogBtn.cancel();
								dialog.dismiss();

							}

						});

		builder.create().show();

	}

}
