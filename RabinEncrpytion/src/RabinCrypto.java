import java.util.ArrayList;


public class RabinCrypto {

	private static final int PRIME_LENGTH = 500;
	
	public static RabinPrivateKeyPair generateKey() {
		
		BigInt p     = new BigInt(0);
		BigInt q     = new BigInt(0);
		BigInt three = new BigInt(3);
		BigInt four  = new BigInt(4);
		BigMod pMod;
		BigMod qMod;
		
		do {
			p = p.random(PRIME_LENGTH);
			pMod = new BigMod(p, four);
		} while (!p.isPrime() && !pMod.isCongruent(three));
		
		do {
			q = q.random(PRIME_LENGTH);
			qMod = new BigMod(q, four);
		} while (!q.isPrime() && !qMod.isCongruent(three));
		
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
		
		
		BigInt eucP;
		BigInt eucQ;
		BigInt chineseSum;
		BigInt chineseDiff;
		BigMod rootModP;
		BigMod rootModQ;
		BigMod sumRoot;
		BigMod diffRoot;
		ArrayList<BigInt> rootsModN = new ArrayList<BigInt>();
		
		
		
		return rootsModN;
	}
}
