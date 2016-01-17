package core;
/**
 * 
 * @author chgogos
 * 
 * ASCENDING
 */
public class IntDoublePair implements Comparable<IntDoublePair> {
	public final int intValue;
	public final double doubleValue;

	public IntDoublePair(int intValue, double doubleValue) {
		this.intValue = intValue;
		this.doubleValue = doubleValue;
	}

	public String toString() {
		return "(" + this.intValue + ", " + this.doubleValue + ")";
	}

	@Override
	public int compareTo(IntDoublePair x) {
		return Double.compare(x.doubleValue, this.doubleValue);
	}
}
