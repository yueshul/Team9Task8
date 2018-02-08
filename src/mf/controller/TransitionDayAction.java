package mf.controller;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import mf.databean.FundBean;
import mf.databean.FundPriceHistoryBean;
import mf.databean.TransactionBean;
import mf.model.FundDAO;
import mf.model.FundPriceHistoryDAO;
import mf.model.Model;
import mf.model.TransactionDAO;

public class TransitionDayAction extends Action{
    
    private Model model;
    private FundDAO fundDAO;
    private FundPriceHistoryDAO fundPriceHistoryDAO;
    private TransactionDAO transactionDAO;
    
    public TransitionDayAction(Model model) {
        this.model = model;
        this.fundDAO = model.getFundDAO();
        this.transactionDAO = model.getTransactionDAO();
        this.fundPriceHistoryDAO = model.getFundPriceHistoryDAO();
    }
    
    @Override
    public String getName() {
        return "TransitionDay.do";
    }
    public String performGet(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("employee") == null) {
            return "login.do";
        }
        FundBean[] funds = null;
        Date date = null;
        try {
            funds = fundDAO.match();
            FundPriceHistoryBean[] history = fundPriceHistoryDAO.match(MatchArg.max("priceDate"));
            if(history.length>0) {
                date = history[0].getPriceDate();
            }
            
        } catch (RollbackException e) {
            e.printStackTrace();
        }
        request.setAttribute("funds", funds);
        request.setAttribute("lastTradingDay", date);
        return "employee_transitionDay.jsp";
    }
    public String performPost(HttpServletRequest request) {
        try {
            Transaction.begin();
            Date date = getExecuteDate(request);
            if(date == null) {
                String message = "New transition day should be greater than last trading day!";
                request.setAttribute("message", message);
                return performGet(request);
            }
            List<String> errors = new ArrayList<>();
            errors = changeFundPrice(request, date);
            if(errors.size()>0) {
                request.setAttribute("errors", errors);
                return performGet(request);
            }
            errors = updateExecuteDate(date);
            if(errors.size()>0) {
                request.setAttribute("errors", errors);
                return performGet(request);
            }
            TransactionBean[] pendingTransaction = transactionDAO.match(MatchArg.equals("status", "Pending"));
            for(TransactionBean t:pendingTransaction) {                
                t.executeTransaction(model, t.getTransactionType());
            }
            Transaction.commit();
        } catch (RollbackException e) {
            String message = "Transition Day failed";
            request.setAttribute("message", message);
            return performGet(request);
        }finally {
            if(Transaction.isActive())Transaction.rollback();
        }
        String message = "Transition Day success!";
        request.setAttribute("message", message);
        return performGet(request);
    }
    public List<String> changeFundPrice(HttpServletRequest request,Date executeDate) {
        Map<String, String[]> map = request.getParameterMap();
        List<String> errors = new ArrayList<>();
        for (String s : map.keySet()) {
            if (s.startsWith("price-")) {
                String fundSymbol = s.substring(6);
                String initial_price = map.get(s)[0];
                System.out.println("initial_price: "+initial_price);
                String[] nums = initial_price.split("\\.");
                if (nums.length != 1 && nums.length != 2 && nums.length != 0) {
                    errors.add("Invalid price");
                } else if (nums.length > 0){
                    if (nums[0].length() > 11) {
                        errors.add("Invalid price");
                    }
                    if (nums.length == 2 && nums[1].length() > 5) {
                        errors.add("Invalid price");
                    }
                } else if (nums.length == 0 && initial_price.length() > 5) {
                    errors.add("Invalid price");
                }
                double price;
                try {
                    price = Double.parseDouble(initial_price);
                } catch (NumberFormatException e) {
                    errors.add("Price must be numbers");
                    return errors;
                }
                if(price<=0) {
                    	errors.add("Price must larger than 0");
                    	return errors;
                }
                if(errors.size()>0)return errors;
                FundPriceHistoryBean bean = new FundPriceHistoryBean();
                int fundId;
                try {
                    FundBean fund = fundDAO.match(MatchArg.equals("symbol", fundSymbol))[0];
                    fundId = fund.getFundId();
                    bean.setFundId(fundId);
                    bean.setPrice(price);
                    bean.setPriceDate(executeDate);
                    fundPriceHistoryDAO.create(bean);
                    fund.setLatestPrice(price);
                    fundDAO.update(fund);
                } catch (RollbackException e) {
                   errors.add("change fund price failed");
                }
            }else {
                if(!s.equals("lastTradingDate")&&!s.equals("newDate")) {
                    errors.add("wrong input field!");
                }
            }
        }
        return errors;
    }
    public Date getExecuteDate(HttpServletRequest request) {
        Date lastTradingDay = null;
        FundPriceHistoryBean[] tmp = null;
        try {
            tmp = fundPriceHistoryDAO.match();
        } catch (RollbackException e1) {
            tmp = null;
        }
        if (tmp != null && tmp.length != 0) {
           lastTradingDay = tmp[tmp.length - 1].getPriceDate();
        }
        String newExecuteDate = request.getParameter("newDate");
        
        SimpleDateFormat dateFormat = null;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        Date last = null;
        try {
            date = dateFormat.parse(newExecuteDate);  
            last = lastTradingDay;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date.compareTo(last)<=0)return null;
        return date;
    }
    public List<String> updateExecuteDate(Date newDate) {
        List<String> errors = new ArrayList<>();
        try {
            TransactionBean[] pendingTransaction = transactionDAO.match(MatchArg.equals("status", "Pending"));
            for(TransactionBean tr:pendingTransaction) {
                tr.setExecuteDate(newDate);
                transactionDAO.update(tr);
            }
        } catch (RollbackException e) {
            errors.add("Update execute date failed");
        }
        return errors;
    }
    
}
