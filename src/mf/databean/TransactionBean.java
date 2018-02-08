package mf.databean;

import java.util.Date;

import org.genericdao.MatchArg;
import org.genericdao.PrimaryKey;
import org.genericdao.RollbackException;

import mf.model.CustomerDAO;
import mf.model.Model;
import mf.model.TransactionDAO;

@PrimaryKey("transactionId")
public class TransactionBean {
	private int transactionId;
	// private int customerId;
	private String userName;
    private double price;
	private Date executeDate;
    private double shares;
    private String transactionType;
    private double amount;
    private String symbol;
    private String status;
   
    public void setPrice(double price) {
    	 this.price=price;
    }
    
    public double getPrice() {
    	 return price;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
    
    public int getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
   
    public Date getExecuteDate() {
        return executeDate;
    }
    public void setExecuteDate(Date executeDate) {
        this.executeDate = executeDate;
    }
    public double getShares() {
        return shares;
    }
    public void setShares(double shares) {
        this.shares = shares;
    }
    public String getTransactionType() {
        return transactionType;
    }
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	

	public void executeTransaction(Model model, String type) {
		switch (type) {
		case "Buy Fund":
			executeBuyFund(model);
			break;
		case "Sell Fund":
			executeSellFund(model);
			break;
		case "Deposit Check":
			executeDepositCheck(model);
			break;
		case "Request Check":
			executeRequestCheck(model);
			break;
		default:
			break;
		}
	}
	 public void executeBuyFund(Model model) {
	        try {
	        	if (model.getFundDAO().match(MatchArg.equals("symbol", this.symbol)).length != 0) {	
	        		int fundId = model.getFundDAO().match(MatchArg.equals("symbol", this.symbol))[0].getFundId();
	        		this.price = model.getFundPriceHistoryDAO().read(fundId, this.executeDate).getPrice();
				if (model.getCustomerDAO().read(this.userName).getCash() < this.amount) {
					this.status = "Declined";
					model.getTransactionDAO().update(this);
					return;
				}
				if (model.getPositionDAO().read(this.userName,fundId) == null) {
					PositionBean newPos = new PositionBean();
					newPos.setFundId(fundId);
					newPos.setUserName(this.userName);
					newPos.setShares((long) (this.amount / model.getFundPriceHistoryDAO().read(fundId, this.executeDate).getPrice()));
					model.getPositionDAO().create(newPos);
					
					double curCash = model.getCustomerDAO().read(this.userName).getCash();
					System.out.println("curCash" + curCash);
					model.getCustomerDAO().read(this.userName).setCash(curCash - this.amount);
					model.getCustomerDAO().update(model.getCustomerDAO().read(this.userName));
					double afterBan = model.getCustomerDAO().read(this.userName).getCash();
					System.out.println("after Balance" + afterBan);
					this.status = "Success";
					this.shares = this.amount / this.price;
					model.getTransactionDAO().update(this);
					return;
				} else {
					System.out.println("old fund++++++++++++++++++++" );
					double s = model.getPositionDAO().read(this.userName, fundId).getShares();
					//double s = model.getPositionDAO().read(this.symbol, this.userName).getShares();
					model.getPositionDAO().read(this.userName,fundId).setShares((long) (s + (this.amount / model.getFundPriceHistoryDAO().read(fundId, this.executeDate).getPrice())));
					
					double curCash = model.getCustomerDAO().read(this.userName).getCash();
					System.out.println("curCash" + curCash);
					double change = curCash - this.amount;
					System.out.println("change is " + change);
					CustomerBean customer = model.getCustomerDAO().read(this.userName);
					customer.setCash(change);
					model.getCustomerDAO().update(customer);
					double afterBan = customer.getCash();
					System.out.println("after Balance" + afterBan + "the amount is" + this.amount);
					this.status = "Success";
					model.getTransactionDAO().update(this);
					return;
				}
	        	}
	        		this.status = "Declined";
				model.getTransactionDAO().update(this);
	        	
	        	
			} catch (RollbackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    public void executeSellFund(Model model) {
	        System.out.println("enter executeSellFund method");
	        try {
	            int fundId = model.getFundDAO().match(MatchArg.equals("symbol", this.symbol))[0].getFundId();
	            double limit = 10000000000000000000000000000000000.00;
	            this.price = model.getFundPriceHistoryDAO().read(fundId, this.executeDate).getPrice();
				if (model.getPositionDAO().read(this.userName,fundId).getShares() < this.shares) {
					this.status = "Declined";
					model.getTransactionDAO().update(this);
					return;
				}
				if (model.getPositionDAO().read(this.userName,fundId).getShares() == this.shares) {
					
					if (model.getCustomerDAO().read(this.userName).getCash() + this.price > limit ) {
						this.setStatus("Declined");
						model.getTransactionDAO().update(this);
						return;
					}
					model.getPositionDAO().delete(fundId, this.userName);
					double curCash = model.getCustomerDAO().read(this.userName).getCash();
					model.getCustomerDAO().read(this.userName).setCash(curCash + this.shares * this.price);
					this.status = "Success";
					model.getTransactionDAO().update(this);
					return;
				} else {
					if (model.getCustomerDAO().read(this.userName).getCash() + this.price > limit ) {
						this.setStatus("Declined");
						model.getTransactionDAO().update(this);
						return;
					}
					double s = model.getPositionDAO().read(this.userName,fundId).getShares();
					model.getPositionDAO().read(this.userName,fundId).setShares((long) (s - this.shares));
					double curCash = model.getCustomerDAO().read(this.userName).getCash();
					model.getCustomerDAO().read(this.userName).setCash(curCash + this.shares * this.price);
					this.status = "Success";	
					model.getTransactionDAO().update(this);
					return;
				}
				
			} catch (RollbackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

	public void executeDepositCheck(Model model) {
	    System.out.println("enter executeDepositCheck method");
		CustomerDAO customerDAO;
		// TransactionDAO transactionDAO;

		customerDAO = model.getCustomerDAO();
		// transactionDAO = model.getTransactionDAO();
		try {
			CustomerBean customer = customerDAO.read(this.getUserName());
			if (customer == null) {
				this.setStatus("Declined");
				model.getTransactionDAO().update(this);
				return;
			}
			double limit = 10000000000000000000000000000000000.00;
			if (customer.getCash() + this.getAmount() > limit ) {
				this.setStatus("Declined");
				model.getTransactionDAO().update(this);
				return;
			}
			customer.setCash(this.getAmount() + customer.getCash());
			customerDAO.update(customer);
			this.setStatus("Success");
			model.getTransactionDAO().update(this);
			
		} catch (RollbackException e) {
			this.setStatus("Declined");
			e.printStackTrace();
		}
	}

	public void executeRequestCheck(Model model) {
	    System.out.println("enter executeRequestCheck method");
		CustomerDAO customerDAO;
		// TransactionDAO transactionDAO;

		customerDAO = model.getCustomerDAO();
		// transactionDAO = model.getTransactionDAO();
		try {
			CustomerBean customer = customerDAO.read(this.getUserName());
			if (customer == null) {
				this.setStatus("Declined");
				model.getTransactionDAO().update(this);
				return;
			}
			double amount = this.getAmount();
			if (amount >= customer.getCash()) {
				// errors.add(" The requested amount exceeds your balance");
				this.setStatus("Declined");
				model.getTransactionDAO().update(this);
				return;
			}

			customer.setCash(customer.getCash() - amount);
			customerDAO.update(customer);
			this.setStatus("Success");
			model.getTransactionDAO().update(this);

		} catch (RollbackException e) {
			this.setStatus("Declined");
			e.printStackTrace();
		}

	}
}
