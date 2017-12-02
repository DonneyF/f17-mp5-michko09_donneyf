package ca.ece.ubc.cpen221.mp5.interfaces;

import java.util.List;

public interface Restaurant extends Business {

    double getStars();

    List getCategories();

    int getReviewCount();

    int getPrice();
}
