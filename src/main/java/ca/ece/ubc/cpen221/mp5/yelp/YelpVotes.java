package ca.ece.ubc.cpen221.mp5.yelp;

import ca.ece.ubc.cpen221.mp5.interfaces.Votes;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A Yelp votes object. Stores information of the votes given on Yelp
 *
 * Representation Invariant: funny, useful, and cool > 0
 *
 * Abstraction Function: AF(this) -> A set of votes such that each vote category corresponds to the integer stored in this
 */
public class YelpVotes implements Votes {

    private int funny;
    private int useful;
    private int cool;

    /**
     * Gets the total number of votes
     *
     * @return the total number of votes
     */
    public int getTotalVotes(){
        return funny + useful + cool;
    }

    /**
     * Gets the number of funny votes
     *
     * @return the number of funny votes
     */
    public int getFunny() {
        return funny;
    }

    /**
     * Sets the number of funny votes
     *
     * @param funny > 0
     */
    public void setFunny(int funny) {
        if (funny < 0) throw new IllegalArgumentException("Number of votes must be greater than 0");
        this.funny = funny;
    }

    /**
     * Gets the number of useful votes
     *
     * @return the number of useful votes
     */
    public int getUseful() {
        return useful;
    }

    /**
     * Sets the number of useful votes
     *
     * @param useful > 0
     */
    public void setUseful(int useful) {
        if (useful < 0) throw new IllegalArgumentException("Number of votes must be greater than 0");
        this.useful = useful;
    }

    /**
     * Gets the number of cool votes
     *
     * @return the number of cool votes
     */
    public int getCool() {
        return cool;
    }

    /**
     * Sets the number of cool votes
     *
     * @param cool > 0
     */
    public void setCool(int cool) {
        if (cool < 0) throw new IllegalArgumentException("Number of votes must be greater than 0");
        this.cool = cool;
    }

    /**
     * Gets the string representation of these votes
     *
     * @return a JSON string representing this
     */
    @Override
    public String toString(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e){
            return null;
        }
    }

    /**
     * Checks for equality of this votes
     *
     * @param object is not null
     * @return true if each vote category is the same for both objects
     */
    @Override
    public boolean equals(Object object) {
        return object instanceof YelpVotes && this.getCool() == ((YelpVotes) object).getCool() && this.getFunny() == ((YelpVotes) object).getFunny()
                && this.getUseful() == ((YelpVotes) object).getUseful();
    }

    /**
     * Gets the hashcode of this object
     *
     * @return the hashcode of this object
     */
    @Override
    public int hashCode(){
        return getTotalVotes();
    }

}
