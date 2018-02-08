package mf.model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import mf.databean.TransactionBean;


public class TransactionDAO extends GenericDAO<TransactionBean> {

    public TransactionDAO(ConnectionPool connectionPool) throws DAOException {
        super(TransactionBean.class, "Transaction", connectionPool);
    }
    
    public void create(TransactionBean t) throws RollbackException {
    		if (!Transaction.isActive()) {
    			try {
    				Transaction.begin();
    				create(t);
    				Transaction.commit();
    				return;
    			} finally {
    				if (Transaction.isActive()) {
    					Transaction.rollback();
    				}
    			} 
    		}
    		TransactionBean existingTrans = read(t.getTransactionId());
    		if (existingTrans != null) {
    			throw new RollbackException("The transaction already exist.");
    		}
    		super.create(t);
    }
    
    public TransactionBean[] getTransactionByCustomer(String username) throws RollbackException {
    	TransactionBean[] transaction = match(MatchArg.equals("userName", username));
    	return transaction;
    }
}
