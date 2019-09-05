package com.example.thechain;

/*
 * Big, big TODO:
 * Consider making the entire AlgebraObject hierarchy immutable.  This will mean rewriting most bits of it.
 */

interface AlgebraObject {
	String toString();
	int countFactor(Symbol symbolFactor); //TODO: needs to extend to products
	boolean hasSymbolicFactors(Product factors);
	//TODO:
	//boolean equivalent(AlgebraObject x);
}
