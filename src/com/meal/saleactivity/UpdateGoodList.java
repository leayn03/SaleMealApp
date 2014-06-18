package com.meal.saleactivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salemealapp.R;
import com.meal.action.DealManageAction;
import com.meal.action.GrouponManageAction;
import com.meal.action.MenuConfigAction;
import com.meal.action.SellerManageAction;
import com.meal.activity.BaseActivity;
import com.meal.activity.ipml.AsynThreadImpl;
import com.meal.activity.ipml.UIThreadImpl;
import com.meal.bean.Global;
import com.meal.bean.Groupon;
import com.meal.bean.Menu;
import com.meal.bean.Order;
import com.meal.bean.Seller;
import com.meal.dialog.MyProgressDialog;
import com.meal.saleglobal.SaleGlobal;


public class UpdateGoodList extends BaseActivity {

	List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>(); //当前页面listview
	ArrayList<Object> menuAllList = new ArrayList<Object>();//记录所有商品数据
	ArrayList<Long> menuNumberList=new ArrayList<Long>(); //记录商品号码

	HashMap<Button, EditText> temp = new HashMap<Button, EditText>();

	
	ListView goodsList;  // 商品list
	Button retButton;  //顶部返回按钮
    ImageButton goodListReturnButton;
	ImageView goodsImage;  //商品图片
	TextView goodsName;  //商品名字
	TextView goodsPrice;  //商品价格
	TextView goodsSale;  //商品折扣
    Button editButton;  // 编辑按钮
    
    
	private Context context;
	private AlertDialog dialog;

	private MyProgressDialog myProgressDialog;
	
	
	Seller seller = SaleGlobal.seller;
	long numberSeller=0L;
	long numberMenu=0L;

	private MenuConfigAction menuAction = MenuConfigAction.getInstance();
	private SellerManageAction sellerAction = SellerManageAction.getInstance();
	int pageNum = 1;
	int curpageNum = 1;
	
	
	Button loadNext;
	Button loadPre;
	ArrayList<Long> menuList = new ArrayList<Long>();
	
	String tTmp = "15";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updategoodlist);
		
		 if(SaleGlobal.seller!=null)
		    {
			 numberSeller=SaleGlobal.seller.getSid();	
		    }
		 
		 goodListReturnButton = (ImageButton)findViewById(R.id.goodListReturnButton);//返回键
		 goodListReturnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(UpdateGoodList.this,SaleInfoActivity.class);
				startActivity(intent);
				
			}
		});
		initWidget();
	}

	private void initWidget() {
		myProgressDialog = MyProgressDialog.createDialog(this);
		goodsList = (ListView) findViewById(R.id.goodListView);


		
		initial();
		startAsynThread("goodlistview");
        myProgressDialog.show();
		loadNext = (Button)findViewById(R.id.loadNext);
		loadNext.setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(curpageNum == pageNum)
				{	pageNum++;
					curpageNum = pageNum;
					initial();
					myProgressDialog.show();
					startAsynThread("goodlistview");
				}
				else
				{
					curpageNum++;
					int totalSize = menuAllList.size();  //总条数
					int size = totalSize%5;                 //总页数
					int start = (curpageNum-1) * 5;
					int end = (curpageNum<=pageNum)?(start+4):(start+size-1);
					Log.e("loadNext start", start+"");
					Log.e("loadNext end", end+"");
					Log.e("loadNext curpageNum", curpageNum+"");
					Log.e("loadNext pageNum", pageNum+"");
					Map<String, Object> map = null;
					
					dataList.clear(); //清空当前页面list
					if(end >= totalSize)
					{
						end = totalSize - 1;
					}
					for(int i=start ; i<=end ; i++)
					{
						map = ((HashMap<String, Object>)menuAllList.get(i));

						dataList.add(map);
					}
					goodsList.setAdapter(new MyAdapter
							(UpdateGoodList.this,R.layout.updategoodlist_row, dataList,
									new String[] {"goodsimage", "name", "price", "sale" }, 
									new int[] { R.id.goodItemImage,R.id.goodItemName,  R.id.goodItemPrice ,R.id.goodItemSale}));
				}
				
			}
		});
		
		loadPre = (Button)findViewById(R.id.loadPre);
		loadPre.setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(curpageNum > 1)
				{
					curpageNum--;
					int totalSize = menuAllList.size();
					int size = totalSize%5;
					int start = (curpageNum-1) * 5;
					int end = (curpageNum<pageNum)?(start+4):(start+size-1);
					
					Log.e("loadPre start", start+"");
					Log.e("loadPre end", end+"");
					Log.e("loadPre curpageNum", curpageNum+"");
					Log.e("loadPre pageNum", pageNum+"");
					Map<String, Object> map = null;
					dataList.clear();//清空当前
					for(int i=start ; i<=end ; i++)
					{
						map = ((HashMap<String, Object>)menuAllList.get(i));
						dataList.add(map);
					}
					goodsList.setAdapter(new MyAdapter
							(UpdateGoodList.this,R.layout.updategoodlist_row, dataList,
									new String[] {"goodsimage", "name", "price", "sale" }, 
									new int[] { R.id.goodItemImage,R.id.goodItemName,  R.id.goodItemPrice ,R.id.goodItemSale}));
				}
				else
				{
					Toast.makeText(UpdateGoodList.this, getResources().getString(R.string.failgetgoodslist), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}


	private void initial()
	{
		setUIRefreshConfig(new UIThreadImpl() {
			
			@Override
			public void refresh(Message msg) {
				// TODO Auto-generated method stub
				myProgressDialog.dismiss();
				
				if(0 == msg.arg1)
				{
					pageNum--;
					curpageNum = pageNum;
					Log.e("failget curpageNum", curpageNum+"");
					Log.e("failget pageNum", pageNum+"");
					Toast.makeText(UpdateGoodList.this, getResources().getString(R.string.failgetgoodslist), Toast.LENGTH_SHORT).show();
				}
				if ( 1 == msg.arg1 )//获取商品列表成功，显示在listview上面
				{
					goodsList.setAdapter(new MyAdapter
							(UpdateGoodList.this,R.layout.updategoodlist_row, dataList,
									new String[] {"goodsimage", "name", "price", "sale" }, 
									new int[] { R.id.goodItemImage,R.id.goodItemName,  R.id.goodItemPrice ,R.id.goodItemSale}));
				}
		}
	});
	
		setAsynThreadConfig("goodlistview", true, new AsynThreadImpl() {//点击加载下一页拉取数据，每次拉取10条

			@Override
			public Message excute() {
				// TODO Auto-generated method stub
				
				Message msg = Message.obtain();
				ArrayList<Object> tmp = null;
				tmp = menuAction.getMenuList(numberSeller, pageNum);//获取某一页的商品列表
				if(tmp == null)
				{
					msg.arg1 = 0;//获取列表失败
				}
				else
				{
					msg.arg1 = 1;//获取列表成功
					
					Map<String, Object> map = null;
					dataList.clear();

					for(int i=0 ; i<tmp.size() ; i++)
					{
						map = new HashMap<String, Object>();
						String photo = ((Menu)tmp.get(i)).getPhoto();
						Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.defaultgood);
						if(photo != null && !photo.equals(""))
						{
							bm = menuAction.getMenuPhoto(photo);
						}
						map.put("goodsimage", bm);
						map.put("name", ((Menu)tmp.get(i)).getName());
						map.put("price", ((Menu)tmp.get(i)).getPrice());
						map.put("sale", ((Menu)tmp.get(i)).getDiscount());

					
						dataList.add(map);//记录所有显示的数据
						menuAllList.add(map);
						menuNumberList.add(i, ((Menu)tmp.get(i)).getMid());

					}
					
				}
				
				finishAsynThread("goodlistview");
				return msg;
			}
	
		});	
	}

	
	private void deleteEvent()
	{
		deleteGood();
		startAsynThread("deleteGood");		
	}
	private void deleteGood()
	{
		setUIRefreshConfig(new UIThreadImpl() {
			
			@Override
			public void refresh(Message msg) {
				// TODO Auto-generated method stub
		
				}

		});
		
		setAsynThreadConfig("deleteGood", true, new AsynThreadImpl() {

			@Override
			public Message excute() {
				// TODO Auto-generated method stub
				
			   Message msg=Message.obtain();

		       menuAction.deleteMenu(numberMenu);
			    
				
				finishAsynThread("deleteGood");
				return msg;
			}

		});
		
	}	
	
	public final class ViewHolder {
		ImageView goodsImage;
		TextView goodsName;
		TextView goodsPrice;
		TextView goodsSale;
		Button editButton;
	}

	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private int resId;

		private List<Map<String, Object>> listData;

		private String[] from;

		private int[] to;
		
		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public MyAdapter(Context context, int resId,
				List<Map<String, Object>> listData, String[] from, int[] to) {

			mInflater = LayoutInflater.from(context);
			this.resId = resId;
			this.listData = listData;
			this.from = from;
			this.to = to;

		}
		
		// @Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listData.size();
		}

		// @Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		// @Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		// @Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			if(position > listData.size()-1)
			{
				return convertView;
			}
			HashMap<String, Object> viewItem = null;
			try
			{
				viewItem = (HashMap<String, Object>) listData
						.get(position);
			}catch(Exception e)
			{
				e.printStackTrace();
				return convertView;
			}
			Log.d("listData length ", listData.size()+"");
			
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.updategoodlist_row, null);
				holder = new ViewHolder();
				holder.goodsImage = (ImageView) convertView.findViewById(R.id.goodItemImage);
				holder.goodsName = (TextView) convertView.findViewById(R.id.goodItemName);
				holder.goodsPrice = (TextView) convertView.findViewById(R.id.goodItemPrice);
				holder.goodsSale = (TextView) convertView.findViewById(R.id.goodItemSale);
				holder.editButton = (Button) convertView.findViewById(R.id.goodItemEdit);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Bitmap tempBm = (Bitmap)viewItem.get("goodsimage");
			if(tempBm == null)
			{
				tempBm = BitmapFactory.decodeResource(getResources(), R.drawable.defaultgood);
			}
			holder.goodsImage.setImageBitmap(tempBm);
			holder.goodsName.setText(String.valueOf(viewItem.get("name")));
			holder.goodsPrice.setText("￥"+String.valueOf(viewItem.get("price")));
			holder.goodsSale.setText("折"+String.valueOf(viewItem.get("sale")));
			holder.editButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					Toast.makeText(UpdateGoodList.this, "删除商品中。。。", Toast.LENGTH_SHORT).show();
					   numberMenu=menuNumberList.get(position);
					   deleteEvent();
					   Intent intent=new Intent();
					    intent.setClass(UpdateGoodList.this,UpdateGoodList.class);
						startActivity(intent);
					   
					}
			});

			return convertView;
		}

	}
}
