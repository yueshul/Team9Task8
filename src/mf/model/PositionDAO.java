package mf.model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import mf.databean.FundBean;
import mf.databean.PositionBean;

public class PositionDAO extends GenericDAO<PositionBean> {

    public PositionDAO(ConnectionPool connectionPool) throws DAOException {
        super(PositionBean.class, "Position", connectionPool);
    }
    public PositionBean[] getPositions(MatchArg s) throws RollbackException {
    	PositionBean[] positions;
	if (s == null) {
		positions = match();
	} else {
		positions = match(s);
	}

	return positions;
}
    public PositionBean[] getPositionsByCustomer(String username) throws RollbackException {
    	PositionBean[] positions = match(MatchArg.equals("userName", username));
    	return positions;
}
    public void create(PositionBean p) throws RollbackException {
		if (!Transaction.isActive()) {
			try {
				Transaction.begin();
				create(p);
				Transaction.commit();
				return;
			} finally {
				if (Transaction.isActive()) {
					Transaction.rollback();
				}
			} 
		} 
		super.create(p);
    }
}
