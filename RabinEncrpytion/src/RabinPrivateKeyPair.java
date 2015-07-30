
public class RabinPrivateKeyPair {

	private BigInt p;
	private BigInt q;
	
	public RabinPrivateKeyPair(BigInt p, BigInt q) {
		this.p = p;
		this.q = q;
	}
	
	public BigInt getP() {
		return p;
	}
	
	public BigInt getQ() {
		return q;
	}
}
