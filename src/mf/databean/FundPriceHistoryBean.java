package mf.databean;

import java.util.Date;

import org.genericdao.PrimaryKey;

@PrimaryKey("fundId,priceDate")
public class FundPriceHistoryBean {
    private int fundId;
    private Date priceDate;
    private double price;
    public FundPriceHistoryBean(int fundId, Date priceDate, long price) {
        this.fundId = fundId;
        this.priceDate = priceDate;
        this.price = price;
    }
    public FundPriceHistoryBean() {}
    public int getFundId() {
        return fundId;
    }
    public void setFundId(int fundId) {
        this.fundId = fundId;
    }
    public Date getPriceDate() {
        return priceDate;
    }
    public void setPriceDate(Date priceDate) {
        this.priceDate = priceDate;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    
}
