# SupportBank-Java-Template

## Running
`mvn package`
`java -jar target/supportbank-1.0-SNAPSHOT.jar [filename]`
e.g.  `java -jar target/supportbank-1.0-SNAPSHOT.jar .\Transactions2012.xml`

## Options
### List All
Lists all users and their net account balance
### List \[Account\]
Lists all transactions for account, e.g. `List Jon A` will show all transactions for Jon A
### Export \[Filename\]
Converts the input file into a file with specified ending, e.g. if we run
`java -jar target/supportbank-1.0-SNAPSHOT.jar .\Transactions2012.xml`
and use `Export file.csv`, the program will convert the XML file into a CSV file.

Only supports CSV, XML and JSON (same for parsing)