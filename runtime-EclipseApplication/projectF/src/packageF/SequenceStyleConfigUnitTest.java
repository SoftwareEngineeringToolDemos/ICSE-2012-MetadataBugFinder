package packageF;

import java.util.Properties;
import org.junit.Test;
import junit.framework.TestSuite;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SequenceStyleConfigUnitTest {
	private void assertClassAssignability(Class expected, Class actual) {
		if (!expected.isAssignableFrom(actual)) {
			fail("Actual type [" + actual.getName()
					+ "] is not assignable to expected type ["
					+ expected.getName() + "]");
		}
	}

	/**
	 * Test all params defaulted with a dialect supporting sequences
	 */
	//@Test 
	public void testDefaultedSequenceBackedConfiguration() {
	
	
	
	
	}

	/**
	 * Test all params defaulted with a dialect which does not support sequences
	 */
	@Test
	public void testDefaultedTableBackedConfiguration() {
	}

	/**
	 * Test default optimizer selection for sequence backed generators based on
	 * the configured increment size; both in the case of the dialect supporting
	 * pooled sequences (pooled) and not (hilo)
	 */
	@Test
	public void testDefaultOptimizerBasedOnIncrementBackedBySequence() {
	}

	/**
	 * Test default optimizer selection for table backed generators based on the
	 * configured increment size. Here we always prefer pooled.
	 */
	@Test
	public void testDefaultOptimizerBasedOnIncrementBackedByTable() {
	}

	/**
	 * Test forcing of table as backing strucuture with dialect supporting
	 * sequences
	 */
	@Test
	public void testForceTableUse() {
	}

	/**
	 * Test explicitly specifying both optimizer and increment
	 */
	@Test
	public void testExplicitOptimizerWithExplicitIncrementSize() {
	}

	@Test
	public void testPreferPooledLoSettingHonored() {
	}

}
// update  