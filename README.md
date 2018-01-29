CPEN 221 / Machine Problem 5 

# Restaurants, Queries and Statistical Learning

This machine problem incorporates the [Yelp Academic Dataset](https://www.yelp.com/academic_dataset). Specifically, data (in [JSON](https://en.wikipedia.org/wiki/JSON) format) contains information on restaurants, reviews of the restaurants, and user information (for those contributing reviews). 

### Part I: A Database as a Datatype 

`MP5Db` is the abstract interface that is the backbone of this database. `MP5Database` is an abstract class implementing this interfance. `YelpDb` extends this dabase with support for these functions: 

+ Retrieving the current list of users, restaurants, and reviews.
+ Adding a new user, restaurant, and review.
+ Parsing a preset JSON list of user, restaurant, or reviews.
+ Perform a structured query and return the set of YelpRestaurants that matches the query
+ Cluster objects using k-means clustering
+ Retrieving a function that predicts the user's ratings for objects

There are also abstract `Business`, `User`, and `Review` interfaces which have `YelpRestaurant`, `YelpUser`, and `YelpReview` as their implementations, respectively. Each of these classes also have JSON serializer and deserializers for [Jackson JSON parsing](https://github.com/FasterXML/jackson).

### Part II: Statistical Learning

#### k-means Clustering

The k-means algorithm finds k centroids within a dataset that each correspond to a cluster of inputs. To do so, k-means clustering begins by choosing k centroids at random, then alternates between the following two steps:

1. Group the restaurants into clusters, where each cluster contains all restaurants that are closest to the same centroid.
2. Compute a new centroid (average position) for each non-empty cluster. 

This [visualization](http://tech.nitoyon.com/en/blog/2013/11/07/k-means/) is a good way to understand how the algorithm works. The internal class method `kMeansClusters_json()` returns a String, in JSON format, that represents the clusters formatted to be useable by the voronoi visualizer. 

#### Least Squares Regression 

By analyzing a user's past ratings, we can try to predict what rating the user might give to a new restaurant. To predict ratings, we implemented a simple least-squares linear regression, a widely used statistical method that approximates a relationship between some input feature (such as price) and an output value (the rating) with a line. The algorithm takes a sequence of input-output pairs and computes the slope and intercept of the line that minimizes the mean of the squared difference between the line and the outputs.

### Part III: A YelpDB Server

An internal `YelpDBServer` class wraps a `YelpDB` instance. To start the server, one needs to compile a JAR or run the server and client from the IDE. The server is able to handle more than one connection concurrently. 

### Part IV: Handling Simple Requests 

The server can handle some simple requests from a client that connects to it. 

+ `GETRESTAURANT <business id>`: The server responds with the restaurant details in JSON format for the restaurant that has the provided business identifier. If there is no such restaurant then the user is presented with an error message. (Note that the business is is not wrapped in `< >`. The use of `< >` is to indicate that the command should be followed by a required argument. So the request will look like this: `GETRESTAURANT h_we4E3zofRTf4G0JTEF0A` and this example refers to the restaurant Fondue Fred in the provided dataset.)
+ `ADDUSER <user information>`: This request is a string that begins with the keyword (in our protocol), `ADDUSER`, followed by user details in JSON, formatted as suited for the Yelp dataset. Please see the provided JSON to see legal keys. The server will create a new user, generate a random user ID, a new URL (although it is a dummy URL) and then acknowledge that the user was created by responding with a more complete JSON string.
+ `ADDRESTAURANT <restaurant information>`: This command has structure similar to the `ADDUSER` command in that the JSON string representing a restaurant should have all the necessary details to create a new restaurant except for details such as `business_id` and `stars`.
+ `ADDREVIEW <review information>`: The last simple command has the same principle as the other commands. This database also is free of data races.

### Part V: Structured Queries

We would like to process queries such as "list all restaurants in a neighbourhood that serve Chinese food and have moderate ($$) price." In this request-response model, the request would begin with the keyword `QUERY` followed by a string that represents the query.

A query string may be: `in(Telegraph Ave) && (category(Chinese) || category(Italian)) && price <= 2`. This query string represents a query to obtain a list of Chinese and Italian restaurants in the Telegraph Avenue neighbourhood that have a price range of 1-2. For this query string, the server would respond with a list of restaurants in JSON notation.

Please reference the source code for the grammar. This README.md was adpated from the original CPEN 221 MP5 README.md.