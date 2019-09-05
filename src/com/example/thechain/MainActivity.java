package com.example.thechain;

import java.util.Iterator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	//TODO: WARNING Feels like a hack
	static MainActivity instance;
	private OperationList outputExpression;
	private Symbol x;
	private TextView txt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_main);
		
		txt = (TextView) findViewById(R.id.txt);
		x = Symbol.getSymbol("x");
		outputExpression = new Product(new ExplicitValue(1)); //Product();
		
		txt.setText(outputExpression.toString());
		
		/*
		Sum s = new Sum();
		Symbol x = SymbolFactory.factory().getSymbol("x");
		Symbol y = SymbolFactory.factory().getSymbol("y");
		s.add(x);
		s.add(x);
		Product p = new Product();
		p.multiply(x);
		p.multiply(y);
		p.multiply(new ExplicitValue(2));
		p.multiply(new ExplicitValue(3));
		p.collectCoefficient();
		s.add(p);
		Product px = new Product();
		px.multiply(x);
		px.multiply(y);
		Sum t = new Sum();
		t.add(y);
		t.add(x);
		s.add(t);
		s.flatten();
		
		p.additiveCollect(px);
		
		s.collect(0);
		
		txt.setText(s.toString() + " x:" + s.countFactor(x) + " y:" + s.countFactor(y) + " product coeff: " + p.getCoefficient().getValue());
		*/
	}
	
	public static Context getContext() {
		return instance.getApplicationContext();
	}
	
	public void buttonAdd(View v) {
		outputExpression = outputExpression.add(x);
		txt.setText(outputExpression.toString());
	}
	
	public void buttonMultiply(View v) {
		outputExpression = outputExpression.multiply(new Exponent(x,1));
		txt.setText(outputExpression.toString());
	}
	
	public void buttonSubtract(View v) {
		Product p = new Product(x, new ExplicitValue(-1));
		p.collectCoefficient();
		outputExpression = outputExpression.add(p);
		txt.setText(outputExpression.toString());
	}
	
	public void buttonSimplify(View v) {
		outputExpression = outputExpression.deepSimplify();
		txt.setText(outputExpression.toString());
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
