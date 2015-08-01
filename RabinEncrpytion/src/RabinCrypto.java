import java.util.ArrayList;

public class RabinCrypto {

	private static final int PRIME_LENGTH = 20;
	
	public static RabinPrivateKeyPair generateKey() {
		
		BigInt p     = new BigInt(0);
		BigInt q     = new BigInt(0);
		BigInt three = new BigInt(3);
		BigInt four  = new BigInt(4);
		BigMod pMod;
		BigMod qMod;
		
		// GENERATE - random prime p congruent to 3 mod 4
		do {
			p = p.random(PRIME_LENGTH);
			pMod = new BigMod(p, four);
		} while (!p.isPrime() || !pMod.isCongruent(three));
		
		// GENERATE - random prime q congruent to 3 mod 4
		do {
			q = q.random(PRIME_LENGTH);
			qMod = new BigMod(q, four);
		} while (!q.isPrime() || !qMod.isCongruent(three));
		
		RabinPrivateKeyPair keyPair = new RabinPrivateKeyPair(p,q);
		
		return keyPair;
	}
	
	public static BigInt encrypt(BigInt message, BigInt publicKey) {
		
		BigInt cipherText;
		BigMod cipherMod = new BigMod(message, publicKey);
		
		// CALC - compute cipher text
		cipherMod.fastPow(new BigInt(2));
		cipherText = cipherMod.getMantissa();
		
		return cipherText;
	}
	
	public static ArrayList<BigInt> decrypt(BigInt cipherText, RabinPrivateKeyPair keyPair) {
		
		BigInt p;
		BigInt q;
		BigInt n;
		BigInt powP;
		BigInt powQ;
		BigInt eucP;
		BigInt eucQ;
		BigInt chineseSum;
		BigInt chineseDiff;
		BigInt chineseProductP;
		BigInt chineseProductQ;
		BigMod rootModP;
		BigMod rootModQ;
		BigMod posSumRoot;
		BigMod negSumRoot;
		BigMod posDiffRoot;
		BigMod negDiffRoot;
		BigIntPair eucPair;
		ArrayList<BigInt> rootsModN = new ArrayList<BigInt>();
		
		p = keyPair.getP();
		q = keyPair.getQ();
		n = p.multiply(q);
		
		// CALC - coefficients for CRT using extended Euclidean algorithm
		eucPair = RabinCrypto.euclidX(p, q);
		eucP = eucPair.x;
		eucQ = eucPair.y;
		
		// CALC - compute root mod P
		powP = new BigInt(p.add(1).divide(4));
		rootModP = new BigMod(cipherText, p);
		rootModP.fastPow(powP);
		
		// CALC - compute root mod Q
		powQ = new BigInt(q.add(1).divide(4));
		rootModQ = new BigMod(cipherText, q);
		rootModQ.fastPow(powQ);
		
		// CALC - chinese remainder theorem to find roots mod N
		chineseProductP = eucP.multiply(p).multiply(rootModQ.getMantissa());
		chineseProductQ = eucQ.multiply(q).multiply(rootModP.getMantissa());
		
		chineseSum = chineseProductP.add(chineseProductQ);
		chineseDiff = chineseProductP.subtract(chineseProductQ);
		
		posSumRoot = new BigMod(chineseSum, n);
		negSumRoot = new BigMod(posSumRoot.getMantissa().multiply(-1), n);
		posDiffRoot = new BigMod(chineseDiff, n);
		negDiffRoot = new BigMod(posDiffRoot.getMantissa().multiply(-1), n);
		
		// ADD - all roots mod N to be returned
		rootsModN.add(posSumRoot.getMantissa());
		rootsModN.add(negSumRoot.getMantissa());
		rootsModN.add(posDiffRoot.getMantissa());
		rootsModN.add(negDiffRoot.getMantissa());
		
		return rootsModN;
	}
	
	public static BigIntPair euclidX(BigInt first, BigInt second) {
		int sign = 1;
		BigInt[] x  = new BigInt[2];
		BigInt[] y  = new BigInt[2];
		BigInt a = first;
		BigInt b = second;
		BigInt q;
		BigInt r;
		BigInt xx;
		BigInt yy;
		BigIntPair pair;
		
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
		y[0] = y[0].multiply(-sign);
		
		pair = new BigIntPair(x[0],y[0]);
		
		return pair;
	}
	
	public static void main(String[] args) {
		
		BigInt message = new BigInt(1337);
		RabinPrivateKeyPair privateKey = RabinCrypto.generateKey();
		BigInt publicKey = privateKey.getP().multiply(privateKey.getQ());
		
		
		BigInt cipherText = RabinCrypto.encrypt(message, publicKey);
		
		System.out.println("Message    : " + message);
		System.out.println("PrivateKey : P = " + privateKey.getP() + " Q = " + privateKey.getQ());
		System.out.println("PublicKey  : " + publicKey);
		System.out.println("CipherText : " + cipherText + "\n\n");
		System.out.println("Decrypting...");
		
		ArrayList<BigInt> rootsModN = RabinCrypto.decrypt(cipherText, privateKey);
		
		System.out.println("\n\nDecrypted: " + rootsModN);
	}
}
