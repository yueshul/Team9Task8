package mf.databean;

import org.genericdao.PrimaryKey;

@PrimaryKey("fundId")
public class FundBean {
    private int fundId;
    private String name;
    private String symbol;
    private double latestPrice;
    public FundBean(int fundId, String name, String symbol,double latestPrice) {
        this.fundId = fundId;
        this.name = name;
        this.symbol = symbol;
        this.latestPrice = latestPrice;
    }
    public FundBean() {}
    public int getFundId() {
        return fundId;
    }
    public void setFundId(int fundId) {
        this.fundId = fundId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public double getLatestPrice() {
        return latestPrice;
    }
    public void setLatestPrice(double latestPrice) {
        this.latestPrice = latestPrice;
    }
    
}
