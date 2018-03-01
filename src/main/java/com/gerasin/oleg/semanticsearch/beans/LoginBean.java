package com.gerasin.oleg.semanticsearch.beans;

import com.gerasin.oleg.semanticsearch.helpers.DbHelper;
import com.gerasin.oleg.semanticsearch.util.SessionUtils;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author geras
 */
@Named
@SessionScoped
public class LoginBean
        implements Serializable
{

    private String username;
    private String password;

    @Inject
    private DbHelper dbHelper;

    private static Logger log = LogManager.getLogger(LoginBean.class.getName());

    public String validateUsernamePassword()
    {
        boolean valid = dbHelper.validateUser(username, password);

        if (valid)
        {
            log.info("user {} is valid", username);
            HttpSession session = SessionUtils.getSession();
            session.setAttribute("username", username);
            return "index";
        }
        else
        {
            log.info("user {} is not valid", username);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Incorrect Username or Password",
                            "Please enter correct username and Password"));
            return "login";
        }
    }

    public String createUser()
    {
        boolean valid = dbHelper.createUser(username, password);

        if (valid)
        {
            log.info("user {} is created", username);
            HttpSession session = SessionUtils.getSession();
            session.setAttribute("username", username);
            return "index";
        }
        else
        {
            log.info("user {} is already exists", username);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "User with Username " + username + "is already exists",
                            "Please enter correct username and Password"));
            return "login";
        }
    }

    //logout event, invalidate session
    public String logout()
    {
        HttpSession session = SessionUtils.getSession();
        session.invalidate();
        return "login";
    }

    //<editor-fold defaultstate="collapsed" desc="getters/setters">
    public String getUsername()
    {
        log.info("getUsername with value: {}", username);
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
        log.info("setUsername with value: {}", username);
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
    //</editor-fold>
}
