package com.yudiz.beacondemo;

public class VotePosition {

    private String positionName;
    private double voteNumber;

    public VotePosition(String name,double vote){
        positionName=name;
        voteNumber=vote;
    }

    public double getVoteNumber() {
        return voteNumber;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public void setVoteNumber(double voteNumber) {
        this.voteNumber = voteNumber;
    }
}
