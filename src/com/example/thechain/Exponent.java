package com.example.thechain;

public class Exponent implements AlgebraObject {

	AlgebraObject mantissa;
	int exp;  //And ain't getting harder than that for a while.
	
	public Exponent(AlgebraObject m, int e) {
		mantissa = m;
		exp = e;
	}
	
//TODO: solve the problem of "does this object have the same factors as that one?"
	
	@Override
	public int countFactor(Symbol symbolFactor) {
		return mantissa.countFactor(symbolFactor) * exp;
	}

	@Override
	public boolean hasSymbolicFactors(Product factors) {
		return mantissa.hasSymbolicFactors(factors);
	}
	
	@Override
	public String toString() {
		//if(1 == exp) return mantissa.toString();
		//else return "Exp(" + mantissa.toString() + ", " + exp + ")";
		return "Exp(" + mantissa.toString() + ", " + exp + ")";
		}
	
	public Product toProduct() {
		// TODO: extend to negative indices
		return Product.repeat(mantissa, exp);
	}
	
	public AlgebraObject flatten() {
		if( 1 == exp ) return mantissa;
		else return this;
	}

}
