package com.example.thechain;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;


//TODO: do I really need this class?
public class ExplicitValue implements AlgebraObject {
	private int value;
	
	public ExplicitValue(int v) {
		//Symbol.getSymbol(MainActivity.getContext().getString(R.string.SymbolicNumber));
		value = v;
	}
	
	public int getValue() {
		return value;
	}
	
	public String toString() {
		return Integer.toString(value);
	}
	
	public ExplicitValue add(int i) {
		Log.i("ExplicitValue.add", "Adding " + i + " to " + value);
		return new ExplicitValue(value + i);
	}
	
	public ExplicitValue add(ExplicitValue i) {
		return add(i.getValue());
	}
	
	public ExplicitValue multiply(int i) {
		Log.i("ExplicitValue.multiply", "Multiplying " + i + " onto " + value);
		return new ExplicitValue(value * i);
	}
	
	public ExplicitValue multiply(ExplicitValue i) {
		return multiply(i.getValue());
	}
	
	@Override
	public boolean hasSymbolicFactors(Product f) {
		//TODO
		return false;
	}
	
	//TODO: numbers probably ought to be atomic, just like symbols
	public boolean eq(AlgebraObject n){
		//TODO: add some kind of getValue to other classes
		if(!ExplicitValue.class.isAssignableFrom(n.getClass())) return false;
		else return ((ExplicitValue)n).getValue() == value;
	}

	//@Override
	public boolean equivalent(AlgebraObject x) {
		return eq(x);
	}
	
	@Override
	public int countFactor(Symbol symbolFactor) {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
