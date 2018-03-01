package com.gerasin.oleg.semanticsearch.beans;

import com.gerasin.oleg.semanticsearch.helpers.DbHelper;
import com.gerasin.oleg.semanticsearch.model.User;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author geras
 */
@Named
@SessionScoped
public class ProfileBean
        implements Serializable
{
    @Inject
    private LoginBean loginBean;

    @Inject
    private DbHelper dbHelper;

    private static Logger log = LogManager.getLogger(ProfileBean.class.getName());

    public String save()
    {
        User user = loginBean.getUser();

        dbHelper.updateInterests(user);

        FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Saved",
                            "Saved"));

        return null;
    }
}
