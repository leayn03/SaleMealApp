package com.meal.saleactivity;


import com.igexin.sdk.PushManager;
import com.example.salemealapp.R;
import com.meal.action.SellerManageAction;
import com.meal.activity.BaseActivity;
import com.meal.activity.ipml.AsynThreadImpl;
import com.meal.activity.ipml.UIThreadImpl;
import com.meal.bean.Global;
import com.meal.bean.Seller;
import com.meal.saleglobal.SaleGlobal;
import com.meal.util.DialogUtil;
import com.meal.util.SysUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SaleShowPageActivity extends BaseActivity {

	  private SellerManageAction sellerManage=SellerManageAction.getInstance();  //	初始化实例
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showpage);
		
		PushManager.getInstance().initialize(this.getApplicationContext());
		
		new Handler().postDelayed(new Runnable() {
			public void run() {
				
				
				 initial();
				 startAsynThread("freelogin");
			}
		}, 500);
	}
	
	
	private void initial()
	{
		setUIRefreshConfig(new UIThreadImpl() {
			
			@Override
			public void refresh(Message msg) {
				// TODO Auto-generated method stub
				if ( 0 == msg.arg1 ){
					Intent intent = new Intent();
					//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					SaleGlobal.seller = Global.seller;
					intent.setClass(SaleShowPageActivity.this,SaleInfoActivity.class);
					startActivity(intent);
				}
				else{
		
					Intent intent = new Intent();
					//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					
					intent.setClass(SaleShowPageActivity.this,SaleLoginActivity.class);
					startActivity(intent);
				}
				
			}
		});
		
		setAsynThreadConfig("freelogin", true, new AsynThreadImpl() {

			@Override
			public Message excute() { 

				Message msg = Message.obtain();
				if(sellerManage.freeLogin(SaleShowPageActivity.this)!=null)
				  {
					msg.arg1 = 0;
					
					}else{
						
					msg.arg1 = 1;
					
					}	
				finishAsynThread("freelogin");

				return msg;
			}

		});
		
	}
		
}
