package com.riotapps.word;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.TabHost.TabContentFactory;

public class RulesTab extends TabActivity{
	
	private TabHost tabHost;
	
    private void setupTabHost() {
    	tabHost = (TabHost) findViewById(android.R.id.tabhost);
    	tabHost.setup();
}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rulestab);
 
		//setupTabHost();
	//	tabHost = (TabHost) findViewById(android.R.id.tabhost);
	//	tabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
		
	//	setupTab(new TextView(this), "Rule Overview");
	//	setupTab(new TextView(this), "Full Rules");
		//setupTab(new TextView(this), "Tab 3");
		
		
 		//Resources resources = getResources(); 
		TabHost tabHost = getTabHost(); 
		tabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
		
		View view1 = LayoutInflater.from(this).inflate(R.layout.tablayout, null);
		TextView tv1 = (TextView) view1.findViewById(R.id.tabsText);
		tv1.setText("rule overview");
		TextView tvSub1 = (TextView) view1.findViewById(R.id.tabsSubText);
		tvSub1.setText("short and sweet");
		
		// Quick tab
		Intent intentQuick = new Intent().setClass(this, QuickRules.class);
		TabSpec tabSpecQuick = tabHost
		  .newTabSpec("Quick")
		  .setIndicator(view1)
		  .setContent(intentQuick);
	//	  .setIndicator("Quick", resources.getDrawable(R.drawable.tab_selector))		
		// Full tab
		
		View view2 = LayoutInflater.from(this).inflate(R.layout.tablayout, null);
		TextView tv2 = (TextView) view2.findViewById(R.id.tabsText);
		tv2.setText("all rules");
		TextView tvSub2 = (TextView) view2.findViewById(R.id.tabsSubText);
		tvSub2.setText("the kitchen sink");
		
		
		Intent intentFull = new Intent().setClass(this, Rules.class);
		TabSpec tabSpecFull = tabHost
		  .newTabSpec("Full")
		  .setIndicator(view2)
		  .setContent(intentFull);
 
		// add all tabs 
		tabHost.addTab(tabSpecQuick);
		tabHost.addTab(tabSpecFull);

 
		//set Windows tab as default (zero based)
		//tabHost.setCurrentTab(0); 
	}
	
	private void setupTab(final View view, final String tag) {
		View tabview = createTabView(tabHost.getContext(), tag);
	        TabSpec setContent = tabHost.newTabSpec(tag).setIndicator(tabview).setContent(new TabContentFactory() {
			public View createTabContent(String tag) {return view;}
		});
	        tabHost.addTab(setContent);
	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tablayout, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}
}
