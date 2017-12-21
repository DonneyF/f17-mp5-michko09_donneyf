package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.query.QueryCreator;
import ca.ece.ubc.cpen221.mp5.query.QueryLexer;
import ca.ece.ubc.cpen221.mp5.query.QueryParser;
import ca.ece.ubc.cpen221.mp5.yelp.YelpRestaurantQuery;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class QueryTests {

    @Test
    public void test1() {
        CharStream stream = new ANTLRInputStream("in(Telegraph Ave) && (category(Persian/Iranian) || category(Italian)) && price <= 2");
        QueryLexer lexer = new QueryLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        QueryParser parser = new QueryParser(tokens);

        ParseTree tree = parser.orExpr();

        ParseTreeWalker walker = new ParseTreeWalker();
        QueryCreator listener = new QueryCreator();
        walker.walk(listener, tree);

        List<YelpRestaurantQuery> result = listener.getQueries();

        Integer expected = 2;

        assertEquals(result.get(0).getNeighborhood(), "Telegraph Ave");
        assertEquals(result.get(0).getPrice(), expected);
        assertEquals(result.get(0).getPriceEquality(), "<=");
        assertTrue("Persian/Iranian".equals(result.get(0).getCategory()) || "Italian".equals(result.get(0).getCategory()));
    }

    @Test
    public void test2() {
        CharStream stream = new ANTLRInputStream("in(Telegraph Ave) && (category(Persian/Iranian) || category(Italian)) " +
                "&& (price = 2 || price = 4)");
        QueryLexer lexer = new QueryLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        QueryParser parser = new QueryParser(tokens);

        ParseTree tree = parser.orExpr();

        ParseTreeWalker walker = new ParseTreeWalker();
        QueryCreator listener = new QueryCreator();
        walker.walk(listener, tree);

        List<YelpRestaurantQuery> result = listener.getQueries();

        Integer expected1 = 2;
        Integer expected2 = 4;

        assertEquals(result.get(0).getNeighborhood(), "Telegraph Ave");
        assertTrue(result.get(0).getPrice().equals(expected1) || result.get(0).getPrice().equals(expected2));
        assertEquals(result.get(0).getPriceEquality(), "=");
        assertTrue("Persian/Iranian".equals(result.get(0).getCategory()) || "Italian".equals(result.get(0).getCategory()));
    }

    @Test
    public void test3() {
        CharStream stream = new ANTLRInputStream("(in(Telegraph Ave) || in(UBC)) && (category(Chinese) || category(Italian) " +
                "|| category(Korean)) && (price = 2 || price = 4 || price = 5)");
        QueryLexer lexer = new QueryLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        QueryParser parser = new QueryParser(tokens);

        ParseTree tree = parser.orExpr();

        ParseTreeWalker walker = new ParseTreeWalker();
        QueryCreator listener = new QueryCreator();
        walker.walk(listener, tree);

        List<YelpRestaurantQuery> result = listener.getQueries();

        Integer expected1 = 2;
        Integer expected2 = 4;
        Integer expected3 = 5;

        assertTrue("Telegraph Ave".equals(result.get(0).getNeighborhood()) || "UBC".equals(result.get(0).getNeighborhood()));
        assertTrue(result.get(0).getPrice().equals(expected1) || result.get(0).getPrice().equals(expected2) || result.get(0).getPrice().equals(expected3));
        assertEquals(result.get(0).getPriceEquality(), "=");
        assertTrue("Chinese".equals(result.get(0).getCategory()) || "Italian".equals(result.get(0).getCategory()) || "Korean".equals(result.get(0).getCategory()));
    }

    @Test
    public void test4() {
        CharStream stream = new ANTLRInputStream("(name(Boston's Pizza) || in(UBC)) && (category(Chinese) || category(Italian) " +
                "|| category(Korean)) && (price = 2 || price = 4 || price = 5 || rating = 5)");
        QueryLexer lexer = new QueryLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        QueryParser parser = new QueryParser(tokens);

        ParseTree tree = parser.orExpr();

        ParseTreeWalker walker = new ParseTreeWalker();
        QueryCreator listener = new QueryCreator();
        walker.walk(listener, tree);

        List<YelpRestaurantQuery> result = listener.getQueries();

        System.out.println(result);

        assertTrue("Chinese".equals(result.get(0).getCategory()) || "Italian".equals(result.get(0).getCategory()) || "Korean".equals(result.get(0).getCategory()));
    }

    @Test
    public void test5() {
        CharStream stream = new ANTLRInputStream("category(Chinese) && (rating = 4.5 || rating = 2)");
        QueryLexer lexer = new QueryLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        QueryParser parser = new QueryParser(tokens);

        ParseTree tree = parser.orExpr();

        ParseTreeWalker walker = new ParseTreeWalker();
        QueryCreator listener = new QueryCreator();
        walker.walk(listener, tree);

        List<YelpRestaurantQuery> result = listener.getQueries();

        Double expected1 = 2.0;
        Double expected2 = 4.5;

        assertEquals(result.get(0).getCategory(), "Chinese");
        assertTrue(result.get(0).getRating().equals(expected1) || result.get(0).getRating().equals(expected2));
        assertEquals(result.get(0).getRatingEquality(), "=");
    }

    @Test
    public void test6() {
        CharStream stream = new ANTLRInputStream("category(Chinese & Italian) || price = 5 || rating = 5");
        QueryLexer lexer = new QueryLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        QueryParser parser = new QueryParser(tokens);

        ParseTree tree = parser.orExpr();

        ParseTreeWalker walker = new ParseTreeWalker();
        QueryCreator listener = new QueryCreator();
        walker.walk(listener, tree);

        List<YelpRestaurantQuery> result = listener.getQueries();

        Integer price = 5;
        Double rating = 5.0;

        if (result.get(0).getRating() != null) {
            assertEquals(result.get(0).getRating(), rating);
        } else if (result.get(0).getPrice() != null) {
            assertEquals(result.get(0).getPrice(), price);
        } else {
            assertEquals(result.get(0).getCategory(), "Chinese & Italian");
        }
    }

    @Test
    public void test7() {
        CharStream stream = new ANTLRInputStream("name(Boston's Pizza) && rating = 5");
        QueryLexer lexer = new QueryLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        QueryParser parser = new QueryParser(tokens);

        ParseTree tree = parser.orExpr();

        ParseTreeWalker walker = new ParseTreeWalker();
        QueryCreator listener = new QueryCreator();
        walker.walk(listener, tree);

        List<YelpRestaurantQuery> result = listener.getQueries();

        Double rating = 5.0;

        assertEquals(result.get(0).getName(), "Boston's Pizza");
        assertEquals(result.get(0).getRating(), rating);
    }

    @Test
    public void test8() {
        CharStream stream = new ANTLRInputStream("price = 5");
        QueryLexer lexer = new QueryLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        QueryParser parser = new QueryParser(tokens);

        ParseTree tree = parser.orExpr();

        ParseTreeWalker walker = new ParseTreeWalker();
        QueryCreator listener = new QueryCreator();
        walker.walk(listener, tree);

        List<YelpRestaurantQuery> result = listener.getQueries();

        Integer price = 5;

        assertEquals(result.get(0).getPrice(), price);
    }

}

