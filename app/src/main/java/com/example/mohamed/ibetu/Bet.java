package com.example.mohamed.ibetu;

import java.util.Date;

/**
 * Created by Mohamed on 2/21/2017.
 */

public class Bet {

    private Date betDate;
    private String betText ;
    private String betSender;

    public Date getBetDate(){
        return betDate;
    }
    public void setBetDate(Date betDate){
        this.betDate = betDate;
    }
    public String getBetText(){
        return betText;
    }

    public void setmText(String betText){
        this.betText = betText;
    }
    public String getBetSender(){
        return betSender;
    }
    public void setBetSender(String betSender){
        this.betSender = betSender;
    }

}
