package ca.ece.ubc.cpen221.mp5.yelp;

import ca.ece.ubc.cpen221.mp5.interfaces.Votes;
import com.fasterxml.jackson.databind.ObjectMapper;

public class YelpVotes implements Votes {

    private int funny;
    private int useful;
    private int cool;

    public int getTotalVotes(){
        return funny + useful + cool;
    }

    public int getFunny() {
        return funny;
    }

    public void setFunny(int funny) {
        this.funny = funny;
    }

    public int getUseful() {
        return useful;
    }

    public void setUseful(int useful) {
        this.useful = useful;
    }

    public int getCool() {
        return cool;
    }

    public void setCool(int cool) {
        this.cool = cool;
    }

    @Override
    public String toString(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof YelpVotes && this.getCool() == ((YelpVotes) object).getCool() && this.getFunny() == ((YelpVotes) object).getFunny()
                && this.getUseful() == ((YelpVotes) object).getUseful();
    }

}
