import com.silverbrain.hs.core.*;
import javafx.application.Application;
import java.util.*;

public class EmpDemo {

	public static void main(String args[]) {
		
		IVariableFactory variableFactory = new VariableFactory();
 
        IDataSet empDT = new DataSet(
            variableFactory.createVariable("emp_id", HSUtils.createList("e1", "e2", "e3", "e4")),
            variableFactory.createVariable("emp_first_name", HSUtils.createStringList("John", "Alex", "Chris", "John")),
			variableFactory.createVariable("emp_last_name", HSUtils.createStringList("Sharma", "Dubey", "Yadav", "Khan")),
			variableFactory.createVariable("dept_id", HSUtils.createList("d1", "d2", "d3", "d2"))
        );
		
		IDataSet deptDT = new DataSet(
            variableFactory.createVariable("dept_id", HSUtils.createList("d1", "d2", "d3")),
            variableFactory.createVariable("dept_name", HSUtils.createStringList("Sales", "IT", "Marketing"))
        );
		
		ArrayList<IDataSet> dts = new ArrayList<IDataSet>();
		
		
		
		
		
		empDT.print();
		deptDT.print();
		
		IDataSet newDT = empDT.join(deptDT, "dept_id", JoinType.LEFT, true);
		
		
		newDT.print();
		
		dts.add(empDT);
		dts.add(deptDT);
		dts.add(newDT);
		DataSetView.setDataSets(dts);
		Application.launch(DataSetView.class);
		
		
		
	}

}