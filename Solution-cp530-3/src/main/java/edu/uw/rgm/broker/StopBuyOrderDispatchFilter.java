package edu.uw.rgm.broker;

import java.util.function.BiPredicate;
import edu.uw.ext.framework.order.StopBuyOrder;

/**
 * Use of the class is discouraged, the BiPredicate&lt;Integer, StopBuyOrder&gt; 
 * interface and a lambda expression, is sufficient. This class is provided
 * only to illustrate the sufficiency of BiPredicate, and soothe those 
 * uncomfortable with lambda expressions.
 */
public class StopBuyOrderDispatchFilter implements BiPredicate<Integer, StopBuyOrder> {

	@Override
	public boolean test(Integer t, StopBuyOrder o) {
        return o.getPrice() <= t;
	}
}
