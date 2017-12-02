/*
package ca.ece.ubc.cpen221.mp5.query;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

public class QueryTestCases {

    @Test
    public void test1() {
        CharStream stream = new ANTLRInputStream("in(Telegraph Ave) && (category(Chinese) || category(Italian)) && price <= 2");
        QueryLexer lexer = new QueryLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        QueryParser parser = new QueryParser(tokens);

        ParseTree tree = parser.orExpr();

        ParseTreeWalker walker = new ParseTreeWalker();
        QueryCreator listener = new QueryCreator();
        walker.walk(listener, tree);

        System.out.println(listener.getMasterList());
    }

    @Test
    public void test2() {
        CharStream stream = new ANTLRInputStream("in(Telegraph Ave) && (category(Chinese) || category(Italian)) " +
                "&& (price = 2 || price = 4)");
        QueryLexer lexer = new QueryLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        QueryParser parser = new QueryParser(tokens);

        ParseTree tree = parser.orExpr();

        ParseTreeWalker walker = new ParseTreeWalker();
        QueryCreator listener = new QueryCreator();
        walker.walk(listener, tree);

        System.out.println(listener.getMasterList());
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

        System.out.println(listener.getMasterList());
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

        System.out.println(listener.getMasterList());
    }

    @Test
    public void test5() {
        CharStream stream = new ANTLRInputStream("category(Chinese) && (rating = 4.5 || rating  = 2)");
        QueryLexer lexer = new QueryLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        QueryParser parser = new QueryParser(tokens);

        ParseTree tree = parser.orExpr();

        ParseTreeWalker walker = new ParseTreeWalker();
        QueryCreator listener = new QueryCreator();
        walker.walk(listener, tree);

        System.out.println(listener.getMasterList());
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

        System.out.println(listener.getMasterList());
    }


}
*/
