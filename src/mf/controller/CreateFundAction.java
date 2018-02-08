package mf.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.formbeanfactory.FormBeanFactory;
import org.formbeanfactory.FormBeanFactoryException;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import mf.databean.FundBean;
import mf.databean.FundPriceHistoryBean;
import mf.formbean.CreateFundForm;
import mf.model.FundDAO;
import mf.model.FundPriceHistoryDAO;
import mf.model.Model;

public class CreateFundAction extends Action{
    private FormBeanFactory<CreateFundForm> formBeanFactory = new FormBeanFactory
            <>(CreateFundForm.class);

    private FundDAO fundDAO;
    private FundPriceHistoryDAO fundPriceHistoryDAO;

    public CreateFundAction(Model model) {
        fundDAO = model.getFundDAO();
        fundPriceHistoryDAO = model.getFundPriceHistoryDAO();
    }

    public String getName() {
        return "CreateFund.do";
    }
    public String performGet(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("employee") == null) {
            return "login.do";
        }

        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors", errors);
        return "employee_createFund.jsp";
    }
    public String performPost(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("employee") == null) {
            return "login.do";
        }

        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors", errors);

        try {
            CreateFundForm form = formBeanFactory.create(request);
            request.setAttribute("form", form);
            errors.addAll(form.getValidationErrors());
            if (errors.size() != 0) {
                return "employee_createFund.jsp";
            }
            Transaction.begin();
            if (fundDAO.match(MatchArg.or(
                    MatchArg.equalsIgnoreCase("symbol", form.getSymbol()),
                    MatchArg.equalsIgnoreCase("name", form.getName()))).length != 0) {
                errors.add("Fund Name or Symbol already exists");
                return "employee_createFund.jsp";
            }
            FundBean fund = new FundBean();
            fund.setName(form.getName());
            fund.setSymbol(form.getSymbol());
            fund.setLatestPrice(0);
            fundDAO.create(fund);
            FundPriceHistoryBean history = new FundPriceHistoryBean();
            history.setPrice(0);
            history.setPriceDate(new Date());
            fundPriceHistoryDAO.create(history);
            Transaction.commit();
            request.setAttribute("message", "Successfully created fund "+form.getName());
            return "employee_createFund.jsp";
        } catch (RollbackException e) {
            if (Transaction.isActive()) Transaction.rollback();
            errors.add(e.getMessage());
            return "error.jsp";
        } catch (FormBeanFactoryException e) {
            errors.add(e.getMessage());
            return "error.jsp";

        }
    }
}
