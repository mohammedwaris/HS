import xport
import csv
import sys

fields = [] 
rows = [] 

with open(sys.argv[1], 'r') as csvfile:  
    csvreader = csv.reader(csvfile) 
      
    fields = next(csvreader)
  
    for row in csvreader: 
        rows.append(row) 

records = []
for row in rows:
	i=0
	dict = {}
	for col in row:
		dict[fields[i]] = col
		i = i + 1
	records.append(dict)

with open(sys.argv[2], 'wb') as f:
    xport.from_rows(records, f)

