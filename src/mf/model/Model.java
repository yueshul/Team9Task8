package mf.model;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;


public class Model {
    private CustomerDAO customerDAO;
    private EmployeeDAO employeeDAO;
    private FundDAO fundDAO;
    private TransactionDAO transactionDAO;
    private FundPriceHistoryDAO fundPriceHistoryDAO;
    private PositionDAO positionDAO;
	public Model(ServletConfig config) throws ServletException {
		try {
			String jdbcDriver = config.getInitParameter("jdbcDriverName");
			String jdbcURL = config.getInitParameter("jdbcURL");
			ConnectionPool pool = new ConnectionPool(jdbcDriver, jdbcURL);
			
			System.out.println(pool.getDriverName());
			customerDAO = new CustomerDAO(pool);
            employeeDAO = new EmployeeDAO(pool);
            fundDAO = new FundDAO(pool);
            transactionDAO = new TransactionDAO(pool);
            fundPriceHistoryDAO = new FundPriceHistoryDAO(pool);
            positionDAO = new PositionDAO(pool);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}

    public static String formatSharesNumber(double number) {
        DecimalFormat df = new DecimalFormat(",##0.000");
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(number);
    }
    
    public static String formatCashNumber(double number) {
        DecimalFormat df = new DecimalFormat(",##0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(number);
    }
    public static String formatPriceNumber(double number) {
        return formatCashNumber(number);
    }
    
    public static String formatDate(Date date){
    		if(date == null) {
    			return "";
    		}
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public EmployeeDAO getEmployeeDAO() {
        return employeeDAO;
    }

    public FundDAO getFundDAO() {
        return fundDAO;
    }

    public TransactionDAO getTransactionDAO() {
        return transactionDAO;
    }

    public FundPriceHistoryDAO getFundPriceHistoryDAO() {
        return fundPriceHistoryDAO;
    }

    public PositionDAO getPositionDAO() {
        return positionDAO;
    }
    
}
