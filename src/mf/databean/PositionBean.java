package mf.databean;
import org.genericdao.PrimaryKey;

@PrimaryKey("userName,fundId")
public class PositionBean {
    private String userName;
    private int fundId;
    private double shares;
    public PositionBean(String userName, int fundId, long shares) {
        super();
        this.userName = userName;
        this.fundId = fundId;
        this.shares = shares;
    }
    public PositionBean() {}
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public int getFundId() {
        return fundId;
    }
    public void setFundId(int fundId) {
        this.fundId = fundId;
    }
    public double getShares() {
        return shares;
    }

    public void setShares(double shares) {
        this.shares = shares;

    }
    
}
