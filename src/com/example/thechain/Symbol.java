package com.example.thechain;

import java.util.ArrayList;
import java.util.Iterator;

public class Symbol implements AlgebraObject {
	private String representation;
	private String symbol;
	private static ArrayList<Symbol> listSymbols;
	
	//Factory method
	public static Symbol getSymbol(String strep) {
		if(null==listSymbols) listSymbols = new ArrayList<Symbol>();
		Iterator<Symbol> i = listSymbols.iterator();
		while(i.hasNext()) {
			Symbol s = i.next();
			if(s.getRepresentation() == strep) return s;
		}
		Symbol s = new Symbol(strep);
		listSymbols.add(s);
		return s;
	}
	
	private Symbol(String pStrRep) {
		representation = pStrRep;
		symbol = representation;
	}
	
	public String toString() {
		return representation;
	}
	
	public int countFactor(Symbol s) {
		return s==this? 1 : 0;
	}
	
	public String getRepresentation() {
		return representation;
	}
	
	public boolean hasSymbolicFactors(Product f) {
		if(f.size()>1) return false;
		return f.countFactor(this)==1? true:false;
	}
	
	public boolean equivalent(AlgebraObject x) {
		if(x == this) return true;
		return this.hasSymbolicFactors(new Product(x)) && x.hasSymbolicFactors(new Product(this));
	}
	
}

