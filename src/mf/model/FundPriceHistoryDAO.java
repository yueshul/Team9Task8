package mf.model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import mf.databean.FundPriceHistoryBean;

public class FundPriceHistoryDAO extends GenericDAO<FundPriceHistoryBean>{

    public FundPriceHistoryDAO(ConnectionPool connectionPool) throws DAOException {
        super(FundPriceHistoryBean.class, "Fund_Price_History", connectionPool);
    }
    
    public void create(FundPriceHistoryBean f) throws RollbackException {
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
		super.create(f);
    }
    
}
