import com.silverbrain.hs.core.HS;
import com.silverbrain.hs.core.DataTable;
import com.silverbrain.hs.core.Row;
import com.silverbrain.hs.core.ColumnData;
import com.silverbrain.hs.core.DataType;
import com.silverbrain.hs.core.JoinType;
import com.silverbrain.hs.core.WhereData;
import com.silverbrain.hs.core.Column;
import com.silverbrain.hs.core.ColumnFactory;
import com.silverbrain.hs.core.HSUtils;
import com.silverbrain.hs.core.Informat;
import com.silverbrain.hs.core.Format;
import com.silverbrain.hs.core.ParameterBuilder;
import com.silverbrain.hs.core.Aggregate;
import com.silverbrain.hs.core.Retain;

import org.apache.commons.lang3.ObjectUtils;




import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class Code {

	public static void main(String args[]) {
		/*
		Map parser = new HashMap<String, Informat>();
		parser.put("TIME", Informat.INTEGER);
		parser.put("Value", Informat.LONG);
		
		//reading from CSV
		DataTable dataTable1 = HS.importCSV(new ParameterBuilder.ImportCSVParameter("urb_cpop1_1_Data.csv").
									parser(parser)
								);
								
		//creating a copy of the table
		DataTable dataTable2 = dataTable1.clone();
		
		//filtering the first table
		dataTable1.where(row -> {
			
			if(row.getStringValue("indic_ur").equals("Population on the 1st of January, total") && 
					row.getIntegerValue("time") == 2017 &&
					Objects.nonNull(row.getLongValue("value")))
				return true;
			else
				return false;
			

		});
		
		//sorting
		dataTable1.avg(new ParameterBuilder.AvgParameter("value").
							groupBy("cities indic_ur"));
							
		dataTable1.sort("value:desc");
		
		dataTable1.printTop(10);
		
		//keeping "cities" and "value" column, dropping the rest
		dataTable1.keepColumn("cities value");
		
		//renaming the columns
		dataTable1.renameColumn("cities=City value=Population_in_2017"); 

		
		
		
		//filtering the second table
		dataTable2.where(row -> {
			
			boolean keep = false;
			if(row.getStringValue("indic_ur").equals("Population on the 1st of January, total") && 
					(row.getIntegerValue("time") == 2010 || row.getIntegerValue("time") == 2016))
				keep = true;
	
			return keep;
		});
		
		//sorting
		dataTable2.avg(new ParameterBuilder.AvgParameter("value").
							groupBy("cities indic_ur"));
							
		dataTable1.sort("value:desc");
		
		//adding a new column to the table
		dataTable2.addCalculatedColumn("growth", DataType.DOUBLE, (row, store) -> {
			
			
			
			Long pop2016 = null;
			Double growth = null;
			if(row.getFirstValue("cities")) {
				if(Objects.nonNull(row.getLongValue("value")))
					store.save("pop2016", row.getLongValue("value"));
				else
					store.save("pop2016", 0L);
			}else if(row.getLastValue("cities")) {
				if(Objects.nonNull(row.getLongValue("value")))
					pop2016 = row.getLongValue("pop2016");
				else
					growth = 0.0;
			}
			return new ColumnData(growth, store);
		});
		
		//filtering the second table again
		dataTable2.where(row -> {
				pop2016 = (Long)store.retrieve("pop2016");
				if(Objects.nonNull(row.getLongValue("value")))
					growth = (double)(pop2016*100)/row.getLongValue("value");
			return row.getLastValue("cities");
		});
		
		//sorting
		dataTable2.sort("growth:desc");
		
		//keeping "cities" and "growth" column, dropping the rest
		dataTable2.keepColumn("cities growth");
		
		//renaming the columns
		dataTable2.renameColumn("cities=City growth=Growth_2010_to_2016");
		
		
		
		//printing the results
		System.out.println("\nTop 10 cities/regions highest population in 2017");
		dataTable1.printTop(10);
		
		System.out.println("\nTop 10 cities/regions highest relative growth from 2010 to 2016");
		dataTable2.printTop(10);
		
		*/
		


		
		
	}
	
}