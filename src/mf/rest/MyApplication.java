package mf.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import mf.model.Model;

@ApplicationPath("/")
public class MyApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        // register root resource
        classes.add(LoginAction.class);
        classes.add(SellFundAction.class);
        classes.add(BuyFundAction.class);
        classes.add(DepositCheckAction.class);
        classes.add(RequestCheckAction.class);
        classes.add(LogoutAction.class);
        classes.add(CreateCustomerAccountAction.class);
        classes.add(CreateFundAction.class);
        classes.add(TransitionDayAction.class);
        classes.add(ViewPortfolioAction.class);
        new Model();
        return classes;
    }
}
