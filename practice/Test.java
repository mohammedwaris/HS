import com.silverbrain.hs.core.HS;
import com.silverbrain.hs.core.DataTable;
import com.silverbrain.hs.core.Row;
import com.silverbrain.hs.core.ColumnData;
import com.silverbrain.hs.core.DataType;
import com.silverbrain.hs.core.WhereData;
import com.silverbrain.hs.core.Informat;
import com.silverbrain.hs.core.ParameterBuilder;
import com.silverbrain.hs.core.Store;
import com.silverbrain.hs.core.HSUtils;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class Test {

	public static void main(String args[]) {
		
		
		
		DataTable dt1 = HSUtils.getSampleDataTable("college.student_course_supervisor");
		
		System.out.println(dt1);
		
		DataTable dt2 = dt1.clone().uniqueRows("StudentID SupervisorID");
		
		System.out.println(dt2);
		System.out.println(dt1);
		


		
		
	}
	
}