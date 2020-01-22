import com.silverbrain.hs.core.DataSet;
import com.silverbrain.hs.core.IDataSet;
import com.silverbrain.hs.core.Observation;
import com.silverbrain.hs.core.DataType;
import com.silverbrain.hs.core.JoinType;
import com.silverbrain.hs.core.VariableFactory;
import com.silverbrain.hs.core.HSUtils;
 
import java.util.Objects;
 
public class VerifyItem {
 
    public static void main(String args[]) {
 
        VariableFactory columnFactory = new VariableFactory();
 
        DataSet supplierDT = new DataSet(
            columnFactory.createVariable("item_id", HSUtils.createList("a1", "a2", "a3", "a4")),
            columnFactory.createVariable("item_name", HSUtils.createStringList("item-a1", "item-a2", "item-a3", "item-a4")),
            columnFactory.createVariable("quantity_required", HSUtils.createIntegerList(5, 4, 10, 6))
        );
                             
        DataSet manufacturerDT = new DataSet(
            columnFactory.createVariable("item_id", HSUtils.createStringList("a1", "a2", "a4", "a5")),
            columnFactory.createVariable("item_name", HSUtils.createList("item-a1", "item-a2", "item-a4", "item-a5")),
            columnFactory.createVariable("quantity_sent", HSUtils.createIntegerList(5, 2, 8, 3))
        );
         
        supplierDT.renameVariable("item_name = supplier_item_name");          
        supplierDT.addCalculatedVariable("supplierRecord", DataType.CHARACTER, row -> 'x');
         
        manufacturerDT.renameVariable("item_name = manufacturer_item_name");      
        manufacturerDT.addCalculatedVariable("manufacturerRecord", DataType.CHARACTER, row -> 'x');
         
        supplierDT.print();
        manufacturerDT.print();
         
        IDataSet suppManJoined = supplierDT.join(manufacturerDT, "item_id", JoinType.FULL, true);     
         
        suppManJoined.addCalculatedVariable("item_name", DataType.STRING, row -> 
             HSUtils.getFirstNonNull(row.getValue("supplier_item_name"), row.getValue("manufacturer_item_name"))
        );
		
		suppManJoined.print();
		
        suppManJoined.addCalculatedVariable("comments", DataType.STRING, row -> {
			System.out.println(row.getIntegerValue("quantity_required"));
            String comments = "";
            if(Objects.isNull(row.getValue("supplierRecord"))) 
                comments = "This is an extra item which is not requested.";
            else if(Objects.isNull(row.getValue("manufacturerRecord")))
                comments = "This requested item is missing.";
            else if(row.getIntegerValue("quantity_required") - row.getIntegerValue("quantity_sent") > 0)
                comments = "Less Quantity of this item has been sent.";
            else if(row.getIntegerValue("quantity_required") - row.getIntegerValue("quantity_sent") < 0)
                comments = "Extra Quantity of this item has been sent.";
            else
                comments = "All okay with this item";
             
            return comments;
        });
         
        IDataSet finalDT = suppManJoined.dropVariable("supplier_item_name manufacturer_item_name supplierRecord manufacturerRecord");
        finalDT.sort("item_id");
        finalDT.print();
    }
     
}