package mf.model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import mf.databean.EmployeeBean;
import mf.databean.FundBean;


public class FundDAO extends GenericDAO<FundBean> {

    public FundDAO(ConnectionPool connectionPool) throws DAOException {
        super(FundBean.class, "Fund", connectionPool);
    }
    
    public FundBean[] getFunds(MatchArg s) throws RollbackException {
    		FundBean[] funds;
		if (s == null) {
			funds = match();
		} else {
			funds = match(s);
		}

		return funds;
	}
    
    public FundBean[] getFundByTicker(String ticker) {
    		FundBean[] funds = null;
    		try {
				funds = match(MatchArg.equals("symbol", ticker));
						
    			} catch (RollbackException e) {
				e.printStackTrace();
				
			}
    		return funds;
    }
    
    public void create(FundBean f) throws RollbackException {
		if (!Transaction.isActive()) {
			try {
				Transaction.begin();
				create(f);
				Transaction.commit();
				return;
			} finally {
				if (Transaction.isActive()) {
					Transaction.rollback();
				}
			} 
		}
		FundBean existingFund = read(f.getFundId());
		if (existingFund != null) {
			throw new RollbackException("The fund already exist.");
		}
		super.create(f);
    }
    
    public FundBean[] getAllFunds() throws RollbackException {
    		FundBean[] funds;
    		funds = match();
    		return funds;
    }
}
