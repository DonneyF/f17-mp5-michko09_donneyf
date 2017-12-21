package ca.ece.ubc.cpen221.mp5.query;

import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurantQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * QueryCreator identifies the atomic elements of a query request and restructures the request into a form which
 * will be easier to filter to database with. In the case of an OR statement, the class creates the same number
 * of possible branches the OR statement creates.
 *
 * Representation Invariant:
 * 		- There size of the list is equal to the number of distinct OR statements in the query request.
 * 	    - There are only unique and distinct maps in the masterList.
 * 	    - Each map within the list can only have a maximum of one unique category and its corresponding value of
 * 	      the same type.
 * 	    - Unless the user inputs an improper query request, masterList is never empty.
 *
 * Abstraction Function:
 * 		- Takes in a string representing a query request and breaks it down into its unique and separate elements.
 * 	    - Transforms the request into a list of maps, each map containing a possible solution of the query request.
 */
public class QueryCreator extends QueryBaseListener {

    private List<HashMap<String, String>> masterList;
    private List<YelpRestaurantQuery> queries;
    private int numOrPaths;
    private int repeatingFactor;
    private int indexingFactor;
    private int counter;

    /**
     * Creates a QueryCreator Constructor, which will be used to construct a List of Maps which are
     * viable solutions for the input query.
     */
    public QueryCreator() {
        this.masterList = new ArrayList<>();
        // Initialize paths and aspects as zero
        this.numOrPaths = 0;
        this.repeatingFactor = 0;
        this.indexingFactor = 0;
        this.counter = 0;
    }

    @Override
    public void enterOrExpr(QueryParser.OrExprContext ctx) {
        String orStatement = ctx.getText();

        // If this has more than one possible OR pathway, we need to account for all of them
        numOrPaths = ctx.getChildCount() / 2 + 1;
        counter = ctx.getChildCount() / 2 + 1;
        repeatingFactor = counter;
        indexingFactor = numOrPaths;

        // If empty, make numPaths number of new HashMaps
        if (masterList.isEmpty()) {
            int count = numOrPaths;
            while (count > 0) {
                masterList.add(new HashMap<>());
                count--;
            }
            if (numOrPaths == 1) {
                numOrPaths = 0;
            }
        } else {
            repeatingFactor = masterList.size() * numOrPaths;
            indexingFactor = repeatingFactor;
            // Copy each map and account for new number of paths created by the OR Statement
            List<HashMap<String, String>> copy = new ArrayList<>();
            int count = 0;
            while (count < numOrPaths - 1) {
                for (HashMap<String, String> path : masterList) {
                    HashMap<String, String> mapCopy = new HashMap<>();
                    for (String id : path.keySet()) {
                        mapCopy.put(id, path.get(id));
                    }
                    copy.add(mapCopy);
                }
                count++;
            }
            masterList.addAll(copy);
        }

    }

    @Override
    public void enterIn(QueryParser.InContext ctx) {
        String neighborhood = ctx.STRING().getText();

        // If this is not a result of an OR Statement, it is a result of an AND Statement and
        // thus must be added to all paths as a requirement
        if (numOrPaths == 0) {
            for (HashMap paths : masterList) {
                paths.put("neighborhoods", neighborhood);
            }
        } else {
            // If this is a result of an OR Statement, only need to add this to one branch
            int count = 0;
            while(count < repeatingFactor / counter) {
                masterList.get(indexingFactor - 1).put("neighborhoods", neighborhood);
                indexingFactor--;
                count++;
            }
            numOrPaths--;
        }
    }

    @Override
    public void enterCategory(QueryParser.CategoryContext ctx) {
        String category = ctx.STRING().getText();

        // If this is not a result of an OR Statement, it is a result of an AND Statement and
        // thus must be added to all paths as a requirement
        if (numOrPaths == 0) {
            for (HashMap paths : masterList) {
                paths.put("categories", category);
            }
        } else {
            // If this is a result of an OR Statement, only need to add this to one branch
            int count = 0;
            while(count < repeatingFactor / counter) {
                masterList.get(indexingFactor - 1).put("categories", category);
                indexingFactor--;
                count++;
            }
            numOrPaths--;
        }
    }

    @Override
    public void enterName(QueryParser.NameContext ctx) {
        String name = ctx.STRING().getText();

        // If this is not a result of an OR Statement, it is a result of an AND Statement and
        // thus must be added to all paths as a requirement
        if (numOrPaths == 0) {
            for (HashMap paths : masterList) {
                paths.put("name", name);
            }
        } else {
            // If this is a result of an OR Statement, only need to add this to one branch
            int count = 0;
            while(count < repeatingFactor / counter) {
                masterList.get(indexingFactor - 1).put("name", name);
                indexingFactor--;
                count++;
            }
            numOrPaths--;
        }
    }

    @Override
    public void enterRating(QueryParser.RatingContext ctx) {
        String ineq = ctx.ineq().getText();
        String rating = ctx.NUM().getText();

        // If this is not a result of an OR Statement, it is a result of an AND Statement and
        // thus must be added to all paths as a requirement
        if (numOrPaths == 0) {
            for (HashMap paths : masterList) {
                paths.put("rating", ineq + rating);
                //paths.put("stars", equalityCheck(ineq, rating));
            }
        } else {
            // If this is a result of an OR Statement, only need to add this to one branch
            int count = 0;
            while(count < repeatingFactor / counter) {
                masterList.get(indexingFactor - 1).put("rating", ineq + rating);
                //masterList.get(indexingFactor - 1).put("stars", equalityCheck(ineq, rating));
                indexingFactor--;
                count++;
            }
            numOrPaths--;
        }
    }

    @Override
    public void enterPrice(QueryParser.PriceContext ctx) {
        String ineq = ctx.ineq().getText();
        String price = ctx.NUM().getText();

        // If this is not a result of an OR Statement, it is a result of an AND Statement and
        // thus must be added to all paths as a requirement
        if (numOrPaths == 0) {
            for (HashMap paths : masterList) {
                paths.put("price", ineq + price);
                //paths.put("price", equalityCheck(ineq, price));
            }
        } else {
            // If this is a result of an OR Statement, only need to add this to one branch
            int count = 0;
            while(count < repeatingFactor / counter) {
                masterList.get(indexingFactor - 1).put("price", ineq + price);
                //masterList.get(indexingFactor - 1).put("price", equalityCheck(ineq, price));
                indexingFactor--;
                count++;
            }
            numOrPaths--;
        }
    }

    /**
     * After creating all possible solutions paths that correspond to the input Query request, formatQuery() formats
     * the solutions into a form that will be much simpler to convert to JSON format later in the process.
     *
     * Iterates through each major section and creates a more organized output using YelpRestaurantQuery, a data type
     * that stores restaurant query information.
     */
    private void formatQuery(){
        queries = new ArrayList<>();
        for(HashMap<String, String> currentNode : masterList) {
            YelpRestaurantQuery currentQuery = new YelpRestaurantQuery();
            // Format the prices
            if (currentNode.containsKey("price")){
                String priceWithEquality = currentNode.get("price");
                // Get index of the first integer
                String firstNumber = priceWithEquality.replaceAll("^\\D*(\\d+).*", "$1");
                int integerIndex = priceWithEquality.indexOf(firstNumber);

                String equality = priceWithEquality.substring(0, integerIndex);
                int value = Integer.parseInt(priceWithEquality.substring(integerIndex, priceWithEquality.length()));
                currentQuery.setPrice(value);
                currentQuery.setPriceEquality(equality);
            }
            // Format the ratings
            if (currentNode.containsKey("rating")){
                String priceWithEquality = currentNode.get("rating");
                // Get index of the first integer
                String firstNumber = priceWithEquality.replaceAll("^\\D*(\\d+).*", "$1");
                int integerIndex = priceWithEquality.indexOf(firstNumber);

                String equality = priceWithEquality.substring(0, integerIndex);
                double value = Double.parseDouble(priceWithEquality.substring(integerIndex, priceWithEquality.length()));

                currentQuery.setRating(value);
                currentQuery.setRatingEquality(equality);
            }

            // Get categories
            if (currentNode.containsKey("categories")) currentQuery.setCategory(currentNode.get("categories"));

            // Get neighborhoods
            if (currentNode.containsKey("neighborhoods")) currentQuery.setNeighborhood(currentNode.get("neighborhoods"));

            // Get Name
            if (currentNode.containsKey("name")) currentQuery.setName(currentNode.get("name"));

            queries.add(currentQuery);
        }
    }

    /**
     * Returns the YelpRestaurantQuery containing all viable solutions to the input Query.
     *
     * @return a list, which:
     *      - contains all YelpRestaurantQuery representing possible database solutions to the query.
     */
    public List<YelpRestaurantQuery> getQueries() {
        this.formatQuery();
        return new ArrayList<>(queries);
    }
}
