package ca.ece.ubc.cpen221.mp5.query;

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
                paths.put("stars", equalityCheck(ineq, rating));
            }
        } else {
            // If this is a result of an OR Statement, only need to add this to one branch
            int count = 0;
            while(count < repeatingFactor / counter) {
                masterList.get(indexingFactor - 1).put("stars", equalityCheck(ineq, rating));
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
                paths.put("price", equalityCheck(ineq, price));
            }
        } else {
            // If this is a result of an OR Statement, only need to add this to one branch
            int count = 0;
            while(count < repeatingFactor / counter) {
                masterList.get(indexingFactor - 1).put("price", equalityCheck(ineq, price));
                indexingFactor--;
                count++;
            }
            numOrPaths--;
        }
    }

    /**
     * Determines what kind of equals operation the current node represents.
     *
     * @param equality, which:
     *      - is the string format of the equality sign.
     *      - is not null;
     *
     * @param value, which:
     *      - is the value which comes after the equality sign in string format.
     *      - is not null.
     *
     * @return a string, which:
     *      - is the evaluated version of the equality expression.
     *      - Ex. if input is: <= 5, the output is: 54321.
     */
    private String equalityCheck(String equality, String value) {
        String E = "=";
        String GT = ">";
        String GTE = ">=";
        String LT = "<";
        String LTE = "<=";
        String result = null;
        int count;
        StringBuilder stringBuild = new StringBuilder();

        if (equality.equals(GT)) {
            count = Integer.parseInt(value) + 1;
            while (count <= 5) {
                stringBuild.append(count);
                count++;
            }
        } else if (equality.equals(GTE)) {
            count = Integer.parseInt(value);
            while (count <= 5) {
                stringBuild.append(count);
                count++;
            }
        } else if (equality.equals(LT)) {
            count = Integer.parseInt(value) - 1;
            while (count >= 1) {
                stringBuild.append(count);
                count--;
            }
        } else if (equality.equals(LTE)) {
            count = Integer.parseInt(value);
            while (count >= 1) {
                stringBuild.append(count);
                count--;
            }
        } else {
            count = Integer.parseInt(value);
            stringBuild.append(count);
        }

        result = stringBuild.toString();
        return result;
    }

    /**
     * Returns the list containing all viable solutions to the input Query.
     *
     * @return a list, which:
     *      - contains all HashMaps representing possible database solutions to the query.
     */
    public List<HashMap<String, String>> getMasterList() {
        return masterList;
    }
}
