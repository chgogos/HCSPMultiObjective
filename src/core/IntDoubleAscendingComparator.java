package core;
import java.util.Comparator;

public class IntDoubleAscendingComparator implements Comparator<IntDoublePair>

{
	@Override
	public int compare(IntDoublePair a, IntDoublePair b) {
		return ((Double) a.doubleValue).compareTo((Double) b.doubleValue);
	}

}