package com.example.thechain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import android.util.Log;

public class Sum extends OperationList {

	private Sum() {
		super();
	}
	
	public Sum(LinkedList<AlgebraObject> param_contents) { super(param_contents); }
	
	public Sum(AlgebraObject x) { super(x); }
	
	public Sum(AlgebraObject x, AlgebraObject y) { super(x, y); }
	
	
	public String toString() {
		String out = new String();
		Iterator<AlgebraObject> i = contents.iterator();
		out = "Sum(";
		while(i.hasNext()) {
			out += i.next().toString();
			if(i.hasNext()) out += ", ";
		}
		out += ")";
		return out;
	}
	
	@Override
	public Sum clone() {
		Sum s = new Sum();
		Iterator<AlgebraObject> i = contents.iterator();
		while (i.hasNext()) {
			s.append(i.next());
		}
		return s;
	}
	
	public int countFactor(Symbol s) {
		LinkedList<Integer> f = new LinkedList<Integer>();
		Iterator<AlgebraObject> i = contents.iterator();
		while(i.hasNext()) {
			f.add(i.next().countFactor(s));
		}
		return Collections.min(f);
	}
	
	public boolean hasSymbolicFactors(Product f) {
		Iterator<AlgebraObject> ti = this.iterator();
		while(ti.hasNext()) {
			if(!ti.next().hasSymbolicFactors(f)) return false;
		}
		return true;
	}
		
	//public Sum addBang(AlgebraObject x) { this.append(x); return this; }
	public Sum add(AlgebraObject x) {
		Sum s = (Sum)(this.clone());
		s.append(x);
		return s;
		}
	public Product multiply(AlgebraObject x) {
		Product p = new Product(this, x);
		return p;
		} 
	
	/*public boolean collect(int location) {
		//Returns true if changed, false otherwise.  collect() will break if this return value is incorrect.
		boolean changed = false;
		Product x = new Product(contents.remove(location));
		Iterator<AlgebraObject> i = contents.iterator();
		while(i.hasNext()) {
			AlgebraObject y = i.next();
			if(x.additiveCollect(y)) {
				changed = true;
				i.remove();
			}
		}
		x.collectCoefficient();
		this.insert(0,x);
		return changed;
	}

	public boolean collect(AlgebraObject x) {
		if(contents.contains(x)) {
			return this.collect(contents.indexOf(x));
		} else return false;
	}

	public void collect() {
		//Relies heavily on collect(int) returning the right value
		boolean changed = false;
		for(int i=0; i < contents.size(); i++) {
			changed = this.collect(i);
			if(changed) break;
		}
		if(changed) this.collect();
	}*/
	
	public Sum additiveCollect(Product x) {
		Iterator<AlgebraObject> i = this.iterator();
		int newCoeff = 0;
		Sum output = new Sum();
		x = x.withoutCoefficient();
		
		while(i.hasNext()) {
			AlgebraObject item = i.next();
			Product term = new Product(item);
			term.flatten();
			if( term.hasSymbolicFactors(x) && x.hasSymbolicFactors(term) )
				newCoeff += term.getCoefficient().getValue();
			else
				output.append(item);
		}
		Product newTerm = new Product(new ExplicitValue(newCoeff), x);
		Log.i("Sum.additiveCollect","Found " + newCoeff + " copies of " + x.toString() + ", combined to make " + newTerm.toString());
		newTerm.flatten();
		newTerm.getCoefficient();
		if(0 != newCoeff) output.append(newTerm);
		return output;
	}
	
	public Sum additiveCollect(AlgebraObject x) {
		return additiveCollect(new Product(x));
	}
	
	public Sum additiveCollect() {
		//Collects all similar terms
		HashSet<AlgebraObject> terms = new HashSet<AlgebraObject>();
		Iterator<AlgebraObject> i = contents.iterator();
		while(i.hasNext()) {
			AlgebraObject x = i.next();
			if(!terms.contains(x)) terms.add(x);
		}
		
		Sum output = ((Sum)this.clone());
		i = terms.iterator();
		while(i.hasNext()) {
			output = output.additiveCollect(i.next());
		}
		return output;
	}
	
	//TODO: DRY: same method exists in Product (generalising is a bitch though)
	public Sum deepSimplify() {
		Log.i("recursiveSimplify","Simplifying " + this.toString());
		Iterator<AlgebraObject> i = this.iterator();
		Sum output = new Sum();
		while(i.hasNext()) {
			AlgebraObject item = i.next();
			if(Utils.isClass(item, OperationList.class)) output.append(((OperationList)item).deepSimplify().unwrap());
			else output.append(item);
		}
		output.flatten();
		
		return ((Sum)output).additiveCollect();
	}


}
