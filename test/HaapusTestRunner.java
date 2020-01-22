import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.haapus.test.*;

public class HaapusTestRunner {
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(TestDataTable.class);
		
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		
		if(result.wasSuccessful())
		System.out.println("All Test Passed.");
	}
} 