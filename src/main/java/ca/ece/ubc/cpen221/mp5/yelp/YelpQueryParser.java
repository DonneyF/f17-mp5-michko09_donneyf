package ca.ece.ubc.cpen221.mp5.yelp;

import ca.ece.ubc.cpen221.mp5.query.QueryCreator;
import ca.ece.ubc.cpen221.mp5.query.QueryLexer;
import ca.ece.ubc.cpen221.mp5.query.QueryParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class YelpQueryParser {

    private YelpDb database;
    private List<YelpRestaurantQuery> queries;

    public YelpQueryParser(YelpDb database) {
        this.database = database;
    }

    public Set<YelpRestaurant> getMatches(String queryString) {
        Set<YelpRestaurant> allMatches = new HashSet<>();

        // Get the parameters to match
        this.getStringMatches(queryString);

        for (YelpRestaurantQuery currentQuery : queries) {
            // Get a list of all restaurants
            List<YelpRestaurant> restaurantMatches = new ArrayList<>(database.getRestaurants());

            // Check name
            if (currentQuery.hasNameFilter()) restaurantMatches = restaurantMatches.parallelStream().filter(yelpRestaurant -> yelpRestaurant.getName()
                    .equals(currentQuery.getName())).collect(Collectors.toList());

            // Check neighborhoods
            if (currentQuery.hasNeighborhoodFilter()) {
                restaurantMatches = restaurantMatches.parallelStream().filter(yelpRestaurant -> yelpRestaurant.getNeighborhoods()
                        .contains(currentQuery.getNeighborhood())).collect(Collectors.toList());
            }

            // Check categories
            if (currentQuery.hasCategoryFilter()) {
                restaurantMatches = restaurantMatches.parallelStream().filter(yelpRestaurant -> yelpRestaurant.getCategories()
                        .contains(currentQuery.getCategory())).collect(Collectors.toList());
            }

            // Check prices
            if (currentQuery.hasPriceFilter()) {
                switch (currentQuery.getPriceEquality()) {
                    case ">=":
                        restaurantMatches = restaurantMatches.parallelStream()
                                .filter(yelpRestaurant -> yelpRestaurant.getPrice() >= currentQuery.getPrice()).collect(Collectors.toList());
                        break;
                    case "<=":
                        restaurantMatches = restaurantMatches.parallelStream()
                                .filter(yelpRestaurant -> yelpRestaurant.getPrice() <= currentQuery.getPrice()).collect(Collectors.toList());
                        break;
                    case "=":
                        restaurantMatches = restaurantMatches.parallelStream()
                                .filter(yelpRestaurant -> yelpRestaurant.getPrice() == currentQuery.getPrice()).collect(Collectors.toList());
                        break;
                    case ">":
                        restaurantMatches = restaurantMatches.parallelStream()
                                .filter(yelpRestaurant -> yelpRestaurant.getPrice() > currentQuery.getPrice()).collect(Collectors.toList());
                        break;
                    case "<":
                        restaurantMatches = restaurantMatches.parallelStream()
                                .filter(yelpRestaurant -> yelpRestaurant.getPrice() < currentQuery.getPrice()).collect(Collectors.toList());
                        break;
                }
            }

            // Check ratings
            if (currentQuery.hasRatingFilter()) {
                switch (currentQuery.getRatingEquality()) {
                    case ">=":
                        restaurantMatches = restaurantMatches.parallelStream()
                                .filter(yelpRestaurant -> yelpRestaurant.getStars() >= currentQuery.getRating()).collect(Collectors.toList());
                        break;
                    case "<=":
                        restaurantMatches = restaurantMatches.parallelStream()
                                .filter(yelpRestaurant -> yelpRestaurant.getStars() <= currentQuery.getRating()).collect(Collectors.toList());
                        break;
                    // Factor in double arithmetic precision
                    case "=":
                        restaurantMatches = restaurantMatches.parallelStream()
                                .filter(yelpRestaurant -> yelpRestaurant.getStars() == currentQuery.getRating()).collect(Collectors.toList());
                        break;
                    case ">":
                        restaurantMatches = restaurantMatches.parallelStream()
                                .filter(yelpRestaurant -> yelpRestaurant.getStars() > currentQuery.getRating()).collect(Collectors.toList());
                        break;
                    case "<":
                        restaurantMatches = restaurantMatches.parallelStream()
                                .filter(yelpRestaurant -> yelpRestaurant.getStars() < currentQuery.getRating()).collect(Collectors.toList());
                        break;
                }
            }
            allMatches.addAll(restaurantMatches);
        }
        return allMatches;
    }

    private void getStringMatches(String queryString) {
        CharStream stream = new ANTLRInputStream(queryString);
        QueryLexer lexer = new QueryLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        QueryParser parser = new QueryParser(tokens);

        ParseTree tree = parser.orExpr();

        ParseTreeWalker walker = new ParseTreeWalker();
        QueryCreator listener = new QueryCreator();
        walker.walk(listener, tree);

        this.queries = listener.getQueries();
    }

}
