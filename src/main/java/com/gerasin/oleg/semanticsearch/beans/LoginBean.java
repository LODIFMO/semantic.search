package com.gerasin.oleg.semanticsearch.beans;

import com.gerasin.oleg.semanticsearch.helpers.DbHelper;
import com.gerasin.oleg.semanticsearch.model.User;
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
    private User user;

    @Inject
    private DbHelper dbHelper;

    private static Logger log = LogManager.getLogger(LoginBean.class.getName());

    public String validateUsernamePassword()
    {
        User usr = dbHelper.validateUser(username, password);

        if (usr != null)
        {
            log.info("user {} is valid", username);
            user = usr;
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
            return "";
        }
    }

    public String createUser()
    {
        User usr = dbHelper.createUser(username, password);

        if (usr != null)
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
                            "User with Username '" + username + "' is already exists",
                            "Please enter correct username and Password"));
            return "";
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

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
    //</editor-fold>
}
