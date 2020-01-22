//var Util = Java.type("java.lang.Thread");
dt = new DataTable(Column.createString("user", Util.createStringArrayList("20.30", "40.50")), Column.create("company", Util.createList("Mac", "Mac")), Column.create("company2", Util.createList("")), Column.create("company3", Util.createList("20", "10")), Column.createDouble("company4", Util.createDoubleArrayList(2000.00, 3000.00)));
dt.where(function (rowData) {
    row = rowData.getRow();
    keep = false;
    if (row.getStringValue("user") == 20.30)
        keep = true;
    return new WhereData(keep, null);
});
print(dt);
print(dt.getColumnMetaData());

studentTable = Haapus.importCSV(new ParameterBuilder.ImportCSVParameter("../data/student.csv"));

studentTable.printTop();