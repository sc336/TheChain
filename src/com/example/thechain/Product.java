package com.example.thechain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import android.util.Log;

public class Product extends OperationList {

	private Product() {
		super();
		//This constructor is private because it's easier than having to remember that an empty product
		//always needs to start with a 1:
		//this.append(new ExplicitValue(1));
	}
	
	public Product(LinkedList<AlgebraObject> param_contents) { super(param_contents); }
	public Product(AlgebraObject x) { super(x); }
	public Product(AlgebraObject x, AlgebraObject y) { super(x, y); }
	
	public static Product repeat(AlgebraObject x, int i) {
		Product p = new Product();
		if(i<0) {
			for(int j = 0; j>i; j--) p.append(new Exponent(x,-1));
			return p;			
		} else if (i>0) {
			for(int j = 0; j<i; j++) p.append(x);
			return p;
		} else return new Product(new ExplicitValue(1));
	}
	
	public String toString() {
		String out = new String();
		Iterator<AlgebraObject> i = contents.iterator();
		out = "Product(";
		while(i.hasNext()) {
			out += i.next().toString();
			if(i.hasNext()) out += ", ";
		}
		out += ")";
		return out;
	}
	
	public int countFactor(Symbol s) {
		Iterator<AlgebraObject> i = contents.iterator();
		int o = 0;
		while(i.hasNext()) {
			o += i.next().countFactor(s);
		}
		return o;
	}
	
	public boolean hasSymbolicFactors(Product f) {
		/*
		Iterator<AlgebraObject> fi = f.iterator();
		// Currently this only deals with atomic factors (ExplicitValue, Symbol).
		// What can we do about everything else?
		while(fi.hasNext()) {
			AlgebraObject item = fi.next();
			if(Symbol.class.isAssignableFrom(item.getClass())) { 
				Symbol s = (Symbol) item;
				if(this.countFactor(s) < f.countFactor(s)) return false;
			} else if (!Utils.isClass(item, ExplicitValue.class))
				return false;
		}
		return true;*/
		return this.listFactors().containsAll(f.listFactors());
	}
	
	public LinkedList<AlgebraObject> listFactors() {  //Returns a *flattened* list of factors - products and exponents are unpacked.  Sums are *not* factorised.
		LinkedList<AlgebraObject> output = new LinkedList<AlgebraObject>();
		Iterator<AlgebraObject> i = this.iterator();
		while( i.hasNext() ) {
			AlgebraObject x = i.next();
			if(Utils.isClass(x, Product.class)) output.addAll(((Product)x).listFactors());
			else if(Utils.isClass(x, Exponent.class)) output.addAll(((Exponent)x).toProduct().listFactors());
			else if(!Utils.isClass(x, ExplicitValue.class)) output.add(x);
		}
		Log.i(this.toString()+".listFactors", output.toString());
		return output;
	}
	
	public boolean collectCoefficient() {
		Log.i("Product.collectCoefficient", this.toString());
		boolean changed = false;
		boolean doneOnce = false;
		Iterator<AlgebraObject> i = contents.iterator();
		ExplicitValue n = new ExplicitValue(1);
		while(i.hasNext()) {
			AlgebraObject x = i.next();
			if(Utils.isClass(x, ExplicitValue.class)) {
				n = n.multiply(((ExplicitValue)x).getValue());
				i.remove();
				changed = doneOnce? true:false;
				doneOnce = true;
			}
		}
		if(doneOnce) this.insert(0,n);
		Log.i("Product.collectCoefficient", "Simplified to " + this.toString());
		return changed;
	}
	
	public AlgebraObject unwrapUnityCoefficient() {
		if(new ExplicitValue(1).eq(this.getCoefficient()) && 2 == contents.size()) {
			if(ExplicitValue.class.isAssignableFrom(contents.get(0).getClass())) return contents.get(1);
			else return contents.get(0);
		}
		else return this;
	}
	
	public ExplicitValue getCoefficient() {
		//TODO: Should be grouped into two methods: collectCoefficient and getCoefficient 
		this.collectCoefficient();
		Iterator<AlgebraObject> i = contents.iterator();
		while(i.hasNext()) {
			AlgebraObject x = i.next();
			if(ExplicitValue.class.isAssignableFrom(x.getClass())) return (ExplicitValue)x;
		}
		this.insert(0, new ExplicitValue(1));
		return (ExplicitValue)this.get(0);
	}
	
	public Product withoutCoefficient() {
		//Warning: currently, this returns an empty product if there are no symbolic factors.
		Product output = new Product();
		Iterator<AlgebraObject> i = contents.iterator();
		while(i.hasNext()) {
			AlgebraObject x = i.next();
			if(!Utils.isClass(x, ExplicitValue.class)) output.append(x);
		}
		return output;
	}
	
	public Integer findCoefficient() {
		//If there is exactly one coefficient term, return its index.  Otherwise return null.
		int i = 0;
		Integer currentIndex = null;
		while(contents.size() > i) {
			AlgebraObject x = contents.get(i);
			if(ExplicitValue.class.isAssignableFrom(x.getClass()))
				if(null == currentIndex) currentIndex = i;
				else return null;
			i++;
		}
		return currentIndex;
	}
	
/* Retired in favour of Sum.additiveCollect
	public boolean additiveCollect(Product x) {
		// Collects with like term x, adding its coefficient on to this, returning true.  If the factors aren't identical, returns false and does nothing.
		if(!(x.hasSymbolicFactors(this) && this.hasSymbolicFactors(x))) return false;
		ExplicitValue newCoeff = this.getCoefficient().add(x.getCoefficient());
		//Relies on getCoefficient shunting coefficient to the front
		this.remove(this.findCoefficient());
		this.insert(0, newCoeff);
		this.getCoefficient();
		this.flatten();
		return true;
	}
	
	public boolean additiveCollect(AlgebraObject x) {
		return additiveCollect(new Product(x));
	}
*/
	
	public Product multiplicativeCollect(AlgebraObject x) {  //Collects all factors symbolically equal to x
		//Collecting coefficients is currently done using getCoefficient.  These should be combined / talk to each other eventually.
		if(Utils.isClass(x, ExplicitValue.class)) return this;
		
		Product output = new Product();
		Iterator<AlgebraObject> i = this.iterator();
		int index = 0;
		while(i.hasNext()) {
			AlgebraObject t = i.next();
			//TODO: AlgebraObject.equals needs to be a thing!
			if( t.hasSymbolicFactors(new Product(x)) && x.hasSymbolicFactors(new Product(t)) ) index++;
			else output.append(t);
		}
		if(1==index) output.append(x);
		else if(0!=index) output.append(new Exponent(x, index));
		output.flatten();
		output.getCoefficient();
		return output;
	}
	
	public Product multiplicativeCollect() {
		//Collects all similar factors
		HashSet<AlgebraObject> factors = new HashSet<AlgebraObject>();
		Iterator<AlgebraObject> i = contents.iterator();
		while(i.hasNext()) {
			AlgebraObject x = i.next();
			if(!(factors.contains(x) || Utils.isClass(x,ExplicitValue.class))) factors.add(x);
		}
		
		Product output = ((Product)this.clone());
		i = factors.iterator();
		while(i.hasNext()) {
			output = output.multiplicativeCollect(i.next());
		}
		return output;
	}
	
	//TODO: DRY: same method exists in Product (generalising is a bitch though)
	public Product deepSimplify() {
		Log.i("recursiveSimplify","Simplifying " + this.toString());
		Iterator<AlgebraObject> i = this.iterator();
		Product output = new Product();
		while(i.hasNext()) {
			AlgebraObject item = i.next();
			if(Utils.isClass(item, OperationList.class)) output.append(((OperationList)item).deepSimplify().unwrap());
			else output.append(item);
		}
		output.flatten();
		return ((Product)output).multiplicativeCollect();
	}
	
	@Override
	public boolean flatten() {
		boolean changed = super.flatten();
		changed = changed || this.collectCoefficient();
		return changed;
	}
		
	@Override
	public Product clone() {
		Product p = new Product();
		Iterator<AlgebraObject> i = contents.iterator();
		while (i.hasNext()) {
			p.append(i.next());
		}
		return p;
	}
	/* Yeah, someday
	private class FactorWithExponent {
		//TODO: extend to rational exponents
		private AlgebraObject factor;
		private int exponent;
		public FactorWithExponent(AlgebraObject f, int e) {
			factor = f;
			exponent = e;
		}
	}
	*/
	
	//Need to figure out return types here
	//public Product multiplyBang(AlgebraObject x) { this.append(x); return this; }
	public Product multiply(AlgebraObject x) { Product p = (Product) (this.clone()); p.append(x); p.flatten(); return p; }
	
	public Sum add(AlgebraObject x) { Sum s = new Sum(this, x); return s; }
	
}
