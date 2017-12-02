package ca.ece.ubc.cpen221.mp5.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QueryCreator extends QueryBaseListener {

    List<HashMap<String, String>> masterList;
    int numOrPaths;
    int repeatingFactor;
    int indexingFactor;
    int counter;

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
            List<HashMap<String, String>> copy = new ArrayList<HashMap<String, String>>();
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
    public void exitOrExpr(QueryParser.OrExprContext ctx) {
        //System.err.println("exiting orExpr");
    }
    public void enterAndExpr(QueryParser.AndExprContext ctx) {
        //System.err.println("entering andExpr");
        String contains = ctx.getText();
        //System.err.println("this andExpr contains: " + contains);
        //System.err.println(ctx.getChildCount());
    }
    public void exitAndExpr(QueryParser.AndExprContext ctx) {
        //System.err.println("exiting andExpr");
    }
    public void enterAtom(QueryParser.AtomContext ctx) {
        //System.err.println("entering atom");
        String contains = ctx.getText();
        //System.err.println("this atom contains: " + contains);
    }
    public void exitAtom(QueryParser.AtomContext ctx) {
        //System.err.println("exiting atom");
    }
    public void enterIneq(QueryParser.IneqContext ctx) {
        //System.err.println("entering ineq");
        String token = ctx.getText();
        //System.err.println("ineq is: " + token);
    }
    public void exitIneq(QueryParser.IneqContext ctx) {
        //System.err.println("exiting ineq");
    }
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
    public void exitIn(QueryParser.InContext ctx) {
        //System.err.println("exiting in");
    }
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
    public void exitCategory(QueryParser.CategoryContext ctx) {
        //System.err.println("exiting category");
    }
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
    public void exitName(QueryParser.NameContext ctx) {
        //System.err.println("exiting name");
    }
    public void enterRating(QueryParser.RatingContext ctx) {
        String ineq = ctx.ineq().getText();
        String rating = ctx.NUM().getText();

        // If this is not a result of an OR Statement, it is a result of an AND Statement and
        // thus must be added to all paths as a requirement
        if (numOrPaths == 0) {
            for (HashMap paths : masterList) {
                paths.put("stars", ineq + rating);
            }
        } else {
            // If this is a result of an OR Statement, only need to add this to one branch
            int count = 0;
            while(count < repeatingFactor / counter) {
                masterList.get(indexingFactor - 1).put("stars", ineq + rating);
                indexingFactor--;
                count++;
            }
            numOrPaths--;
        }
    }
    public void exitRating(QueryParser.RatingContext ctx) {
        //System.err.println("exiting rating");
    }
    public void enterPrice(QueryParser.PriceContext ctx) {
        //System.err.println("entering price");
        String ineq = ctx.ineq().getText();
        String price = ctx.NUM().getText();
        //System.err.println("price is: " + ineq + " " + price);

        // If this is not a result of an OR Statement, it is a result of an AND Statement and
        // thus must be added to all paths as a requirement
        if (numOrPaths == 0) {
            for (HashMap paths : masterList) {
                paths.put("price", ineq + price);
            }
        } else {
            // If this is a result of an OR Statement, only need to add this to one branch
            int count = 0;
            while(count < repeatingFactor / counter) {
                masterList.get(indexingFactor - 1).put("price", ineq + price);
                indexingFactor--;
                count++;
            }
            numOrPaths--;
        }
    }
    public void exitPrice(QueryParser.PriceContext ctx) {
        //System.err.println("exiting price");
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
