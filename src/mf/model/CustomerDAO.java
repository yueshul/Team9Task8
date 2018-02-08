package mf.model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import mf.databean.CustomerBean;
import mf.databean.TransactionBean;

public class CustomerDAO extends GenericDAO<CustomerBean>{
  // private CustomerBean[] customerBean; 
	
	public CustomerDAO(ConnectionPool connectionPool) throws DAOException {
        super(CustomerBean.class, "Customer", connectionPool);
    }
    
    public void create(CustomerBean c) throws RollbackException {
		if (!Transaction.isActive()) {
			try {
				Transaction.begin();
				create(c);
				Transaction.commit();
				return;
			} finally {
				if (Transaction.isActive()) {
					Transaction.rollback();
				}
			} 
		}
		CustomerBean existingCustomer = read(c.getUserName());
		if (existingCustomer != null) {
			throw new RollbackException("The user already exist.");
		}
		super.create(c);
    }
	
//    public CustomerBean getCustomerBy(String customerId) {
//    		try {
//    			customerBean = match(MatchArg.equals("customerId", customerId));
//			} catch (RollbackException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    		
//    		return customerBean[0];
//    	
//    }
}
