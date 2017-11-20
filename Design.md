Each dataset represents a database  

## Classes to implement:  

Generic database:
  - Contains tables uniquely identified by some identifier
  - Internally represented as an HashMap of Tables
  - Subtypes (Strings):
	  - Restuarant
	  - Users
	  - Reviews
	  - Subclass will implement the MP5Db interface

Table:  
- Consists of a key and a set of values
- Internally represented as a HashMap of Lists


## Operations to implement:

Database:

- Constructors:
	- Empty
	- Pass in a database
	- Pass in a table
	- Three filenames represeting JSON files
- Parse the JSON files into databases
	- Check proper JSON formatting
	- The unique identifier must exists for a table entry
	- If the entry does not exist, then we set to empty
- Add an entry (Internally a Table)
- Add an entry to a table
- Check if an entry exists
- Update an entry in a table
- Remove an entry in a table
- Check if two databases are equal
- Get a file representation of the database
- Pass in a database and return similar entries
- Pass in a database and return different entries
- Pass in a database and append differnt entries
- Pass in a database and remove these entries
- Clear the database
- Cloneable
- Return the number of entries (Size of database)

Table:

- Add an entry
- Update an entry
- Remove an entry
- Clear an entry
- Check if an entry exists
- Get the value of an entry
- Return all records under a certain type of entry (e.g. all URLs, all locations)
- Cloneable
- Return the number of entries (Size of table)
