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

Representation Invariant:
- No two tables in the HashMap can be the same
- HashMap cannot be null
- HashMap indentifiers are unique objects only
- Database is mutable
- Does not contain more elements than there are unique indentifiers
- Each Table in HashMap have the same amount of key elements  

Abstraction Function:
- Takes in a list of tables returns the JSON of each table entry  

- Constructors:
	- Empty
	- Pass in a database
	- Pass in a table
	- Three filenames represeting JSON files

Methods:
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

Representation Invariant:
- Hashmap is not null
- Is mutable
- Key values are unique
- All key values are of the same class type  

Abstraction Function:
- Takes in a hashmap and returns each key-value pair to construct a table  

Constructors:
- empty constuctor

Methods:

- Add an entry
- Update an entry
- Remove an entry
- Clear an entry
- Check if an entry exists
- Get the value of an entry
- Return all records under a certain type of entry (e.g. all URLs, all locations)
- Cloneable
- Return the number of entries (Size of table)
