/*******************************************************************************
 * PROGRAMMER : ERIC OLAVESON
 * DATE       : 2/15/15
 ******************************************************************************/

/*******************************************************************************
 * CLASS BigMod
 * -----------------------------------------------------------------------------
 * This class encapsulates methods which define a modulus class with big
 *    integers. Any operations on the mantissa will stay in the range of the
 *    modulus.
 ******************************************************************************/
public class BigMod {
	
    private BigInt mantissa;
    private BigInt modulus;

 
    /***************************************************************************
     * CONSTRUCTOR
     * -------------------------------------------------------------------------
     * @param mantissa
     * @param modulus
     **************************************************************************/
    public BigMod(BigInt mantissa, BigInt modulus) {
    	this.mantissa = mantissa.mod(modulus);
        this.modulus  = modulus;
    }

    /***************************************************************************
     * CONSTRUCTOR
     * -------------------------------------------------------------------------
     * @param modulus
     **************************************************************************/
    public BigMod(BigInt modulus) {
        this.mantissa = new BigInt("1");
        this.modulus  = modulus;
    }

    /***************************************************************************
     * METHOD increment
     * -------------------------------------------------------------------------
     * Increments the mantissa by 1.
     **************************************************************************/
    public void increment() {
    	mantissa = mantissa.add(1);
    	mantissa = mantissa.mod(modulus);
    }
    
    /***************************************************************************
     * METHOD add
     * -------------------------------------------------------------------------
     * Adds an amount to the mantissa.
     **************************************************************************/
    public void add(BigInt addend) {
    	mantissa = mantissa.add(addend);
    	mantissa = mantissa.mod(modulus);
    }
    
    /***************************************************************************
     * METHOD add
     * -------------------------------------------------------------------------
     * Adds an amount to the mantissa.
     **************************************************************************/
    public void add(int addend) {
    	mantissa = mantissa.add(addend);
    	mantissa = mantissa.mod(modulus);
    }
    
    /***************************************************************************
     * METHOD multiply
     * -------------------------------------------------------------------------
     * Multiplies two BigMod objects together by only multiplying their
     *    mantissa.
     **************************************************************************/
    public void multiply(BigMod number) throws ArithmeticException {
        if(number.getModulus().compareTo(this.getModulus()) != 0)
            throw new ArithmeticException("Modules do not match!");

        mantissa = mantissa.multiply(number.getMantissa());
        mantissa = mantissa.mod(modulus);
    }
    
    /***************************************************************************
     * METHOD inverse
     * -------------------------------------------------------------------------
     * Finds the inverse of the mantissa by using Euclid's extended algorithm.
     **************************************************************************/
    public BigInt inverse() {
    	
    	int sign = 1;
    	BigInt[] x  = new BigInt[2];
    	BigInt[] y  = new BigInt[2];
    	BigInt a = mantissa;
    	BigInt b = modulus;
    	BigInt q;
    	BigInt r;
    	BigInt xx;
    	BigInt yy;
    	
    	// INITIALIZE - coefficients
    	x[0] = new BigInt(1);
    	x[1] = new BigInt(0);
    	y[0] = new BigInt(0);
    	y[1] = new BigInt(1);
    	
    	while(!b.equals(0)) {
    		
    		r = a.mod(b);
    		q = a.divide(b);
    		a = new BigInt(b);
    		b = new BigInt(r);
    		
    		xx = new BigInt(x[1]);
    		yy = new BigInt(y[1]);
    		
    		x[1] = (q.multiply(x[1])).add(x[0]);
    		y[1] = (q.multiply(y[1])).add(y[0]);
    		
    		x[0] = new BigInt(xx);
    		y[0] = new BigInt(yy);
    		
    		sign = -sign;
    	}
    	
    	x[0] = x[0].multiply(sign);
    	y[0] = y[0].multiply(sign);
    	
    	
    	if (x[0].less(0))
    		x[0] = x[0].add(modulus);
    	
    	return x[0];
    }
    
    /***************************************************************************
     * METHOD isCongruent
     * -------------------------------------------------------------------------
     * Checks to see if a number is congruent to the mantissa once it is loaded
     *    into a BigMod object.
     **************************************************************************/
    public boolean isCongruent(BigInt a) {
    	BigMod class2 = new BigMod(a, this.getModulus());
    	
    	return (this.getMantissa().equals(class2.getMantissa()));
    }
    
    /***************************************************************************
     * METHOD fastPow
     * -------------------------------------------------------------------------
     * Takes the mantissa to the power passed in, keeping within the modulus.
     **************************************************************************/
    public BigMod fastPow(BigInt pow) {
        BigMod result = new BigMod(new BigInt(1),getModulus());

        while(pow.isPositive()) {
            if(pow.getLowestSetBit() == 0) {
                result.multiply(this);
            }

            multiply(this);
            pow = pow.divide(2);
        }

        mantissa = result.getMantissa();

        return result;
    }

    /***************************************************************************
     * METHOD getMantissa
     * -------------------------------------------------------------------------
     * Returns the mantissa of the object.
     **************************************************************************/
    public BigInt getMantissa() {
        return this.mantissa;
    }

    /***************************************************************************
     * METHOD getModulus
     * -------------------------------------------------------------------------
     * Returns the modulus of the object.
     **************************************************************************/
    public BigInt getModulus() {
        return this.modulus;
    }

    /***************************************************************************
     * METHOD toString
     * -------------------------------------------------------------------------
     * Returns string representation of mantissa and modulus.
     **************************************************************************/
    public String toString() {
        return ("\nMantissa : " + getMantissa()
              + "\nModulus  : " + getModulus() + "\n");
    }

    public static void main(String[] args) {

    	
    }
}