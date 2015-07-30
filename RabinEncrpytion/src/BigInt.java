/*******************************************************************************
 * PROGRAMMER : ERIC OLAVESON
 * DATE       : 7/21/14
 ******************************************************************************/

import java.math.BigInteger;
import java.util.Random;

/*******************************************************************************
 * CLASS BigInt
 * -----------------------------------------------------------------------------
 * This wrapper class encapsulates methods for a big integers and arithmetic
 *    for such elements.
 ******************************************************************************/
public class BigInt extends BigInteger {
		
	// CONSTRUCTOR
    public BigInt(int n) {
        super(n + "");
    }

    // CONSTRUCTOR
    public BigInt(String n) {
        super(n);
    }

    // CONSTRUCTOR
    public BigInt(BigInteger n) {
        super(n.toString());
    }
    
    /***************************************************************************
     * METHOD fastPow
     * -------------------------------------------------------------------------
     * Exponentiates an integer super fast.
     **************************************************************************/
    public BigInt fastPow(BigInt base, BigInt pow) {
        BigInt result = new BigInt(1);

        while(pow.isPositive()) {
            if(pow.getLowestSetBit() == 0)
                result = result.multiply(base);

            base = base.multiply(base);
            pow  = pow.divide(2);
        }

        return result;
    }
    
    /***************************************************************************
     * METHOD fastPow
     * -------------------------------------------------------------------------
     * Exponentiates an integer super fast.
     **************************************************************************/
    public BigInt fastPow(BigInt pow) {
    	BigInt result = new BigInt(1);
    	BigInt base = new BigInt(this);

        while(pow.isPositive()) {
            if(pow.getLowestSetBit() == 0)
                result = result.multiply(base);

            base = base.multiply(base);
            pow  = pow.divide(2);
        }

        return result;
    }
    
    /***************************************************************************
     * METHOD isPrime
     * -------------------------------------------------------------------------
     * Checks to see if a number is prime, using the millerRabinTest.
     **************************************************************************/
    public boolean isPrime() {
        if (!isPositive())
            return false;

        if (equals(2))
        	return true;
        
        if (!isOdd())
            return false;
        
        return millerRabinTest();
    }
    
    /***************************************************************************
     * METHOD millerRabinTest
     * -------------------------------------------------------------------------
     * Checks to see if a number is prime, using Miller-Rabin theory.
     **************************************************************************/
    public boolean millerRabinTest() {
        BigInt s = this.highestPowOfTwoWhichDivides();
        BigInt d = this.subtract(1).divide(fastPow(new BigInt(2), s));
        
        boolean isPrime = true;
        
        for (int i = 0; i < 10; ++i)
        	isPrime &= !isComposite(d, s);

        return isPrime;
    }
    
    /***************************************************************************
     * METHOD isComposite
     * -------------------------------------------------------------------------
     * Checks to see if a number is composite as a continuation of the Miller-
     *    Rabin test.
     **************************************************************************/
    private boolean isComposite(BigInt d, BigInt s) {
    	BigInt rand = this.random();
        BigInt two = new BigInt(2);
        
        if (!gcd(rand).equals(1))
        	return false;
        
        boolean isComposite;
        BigInt  exponent = new BigInt(0);        
        BigMod  witness  = new BigMod(rand, this);
        
        witness = witness.fastPow(d);
        isComposite =   !witness.isCongruent(new BigInt(1)) 
        		     && !witness.isCongruent(new BigInt(-1));
        
        while(exponent.less(s.subtract(1)) && isComposite) {
        	witness = witness.fastPow(two);
        	exponent = exponent.add(1);
        	isComposite = !witness.isCongruent(new BigInt(-1));
        }
        
        return isComposite;
    }

    /***************************************************************************
     * METHOD highestPowOfTwoWhichDivides
     * -------------------------------------------------------------------------
     * Finds the highest power of two which divides this integer.
     **************************************************************************/
    public BigInt highestPowOfTwoWhichDivides() {
        BigInt pow = new BigInt(0);

        while(fastPow(new BigInt(2), pow).divides(this.subtract(1)))
        	pow = pow.add(1);
        
        return pow.subtract(1);
    }
    
    /***************************************************************************
     * METHOD gcd
     * -------------------------------------------------------------------------
     * Finds the greatest common divisor of two integers.
     **************************************************************************/
    public BigInt gcd(BigInt b) {
    	BigInt a = (new BigInt(this)).abs();
    	BigInt r = new BigInt(1);
    	b = b.abs();
    		
    	while (!b.equals(0)) {
    		r = a.mod(b);
    		a = b;
    		b = r;
    	}
    	
    	return a;
    }
    
    /***************************************************************************
     * METHOD random
     * -------------------------------------------------------------------------
     * Finds a random integer that is less than this integer but greater than
     *    or equal to 2.
     **************************************************************************/
    public BigInt random() {
    	
    	BigInt result;
    	
    	do {
    	    result = new BigInt(new BigInteger(this.bitLength(), new Random()));
    	} while (!result.greaterEqual(2) || !result.less(this));
    	
    	return result;
    }
    
    /***************************************************************************
     * METHOD random
     * -------------------------------------------------------------------------
     * Finds a random integer that is of the specified bitlength.
     **************************************************************************/
    public BigInt random(int bitLength) {
    	
    	BigInteger result;
    	
    	result = new BigInteger(bitLength, new Random());
    	
    	return new BigInt(result);
    }
    
    /***************************************************************************
     * The methods below are simple arithmetic and comparison methods which
     *    are overloaded with BigInt and int parameters.
     **************************************************************************/
    
    public boolean less(BigInt x) {
    	return ((BigInteger)this).compareTo((BigInteger)x) < 0;
    }
    
    public boolean less(int x) {
    	return ((BigInteger)this).compareTo(new BigInteger(x + "")) < 0;
    }
    
    public boolean lessEqual(BigInt x) {
    	return ((BigInteger)this).compareTo((BigInteger)x) <= 0;
    }
    
    public boolean lessEqual(int x) {
    	return ((BigInteger)this).compareTo(new BigInteger(x + "")) <= 0;
    }
    
    public boolean greater(BigInt x) {
    	return ((BigInteger)this).compareTo((BigInteger)x) > 0;
    }
    
    public boolean greater(int x) {
    	return ((BigInteger)this).compareTo(new BigInteger(x + "")) > 0;
    }
    
    public boolean greaterEqual(BigInt x) {
    	return ((BigInteger)this).compareTo((BigInteger)x) >= 0;
    }
    
    public boolean greaterEqual(int x) {
    	return ((BigInteger)this).compareTo(new BigInteger(x + "")) >= 0;
    }
    
    @Override
    public BigInt abs() {
    	if (isPositive())
    		return this;
    	else
    		return this.multiply(-1);
    }
    
    public BigInt add(BigInt n) {
        return (new BigInt(super.add((BigInteger)n)));
    }

    public BigInt add(int n) {
        return (new BigInt(super.add(new BigInteger(n + ""))));
    }
    
    public BigInt subtract(BigInt n) {
        return (new BigInt(super.subtract((BigInteger)n)));
    }

    public BigInt subtract(int n) {
        return (new BigInt(super.subtract(new BigInteger(n + ""))));
    }

    public BigInt multiply(BigInt n) {
        return (new BigInt(super.multiply((BigInteger)n)));
    }

    public BigInt multiply(int n) {
        return (new BigInt(super.multiply(new BigInteger(n + ""))));
    }

    public BigInt divide(BigInt n) {
        return (new BigInt(super.divide((BigInteger)n)));
    }

    public BigInt divide(int n) {
        return (new BigInt(super.divide(new BigInteger(n + ""))));
    }
    
    public boolean divides(BigInt numerator) {
    	return numerator.remainder(this).equals(0);
    }

    public boolean divides(int numerator) {
    	return (new BigInt(numerator)).remainder(this).equals(0);
    }
    
    public BigInt remainder(BigInt n) {
    	return (new BigInt(super.remainder((BigInteger)n)));
    }

    public BigInt remainder(int n) {
        return (new BigInt(super.remainder(new BigInteger(n + ""))));
    }
    
    public BigInt mod(BigInt n) {
    	return (new BigInt(super.mod((BigInteger)n)));
    }

    public BigInt mod(int n) {
        return (new BigInt(super.mod(new BigInteger(n + ""))));
    }

    public boolean isPositive() {
    	return super.compareTo(new BigInteger(0 + "")) > 0;
    }
    
    public boolean equals(BigInt n) {
    	return super.compareTo((BigInteger)n) == 0;
    }
    
    public boolean equals(int n) {
    	return super.compareTo(new BigInteger(n + "")) == 0;
    }

    public boolean isOdd() { return this.getLowestSetBit() == 0; }

    public String toString() {
        return super.toString();
    }
}