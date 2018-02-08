package mf.model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import mf.databean.CustomerBean;
import mf.databean.EmployeeBean;


public class EmployeeDAO extends GenericDAO<EmployeeBean>{

	private EmployeeBean[] employeeBean;
    public EmployeeDAO(ConnectionPool connectionPool) throws DAOException {
        super(EmployeeBean.class, "Employee", connectionPool);
    }
    public void create(EmployeeBean e) throws RollbackException {
		if (!Transaction.isActive()) {
			try {
				Transaction.begin();
				create(e);
				Transaction.commit();
				return;
			} finally {
				if (Transaction.isActive()) {
					Transaction.rollback();
				}
			} 
		}
		EmployeeBean existingEmployee = read(e.getUserName());
		if (existingEmployee != null) {
			throw new RollbackException("The employee already exist.");
		}
		super.create(e);
    }
    
//    public EmployeeBean getEmployeeUsername(String userName) {
//		try {
//			employeeBean = match(MatchArg.equals("userName", userName));
//		} catch (RollbackException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return employeeBean[0];
//	
//}
}
