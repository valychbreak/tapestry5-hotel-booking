package com.tap5.hotelbooking.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.tap5.hotelbooking.dal.CrudServiceDAO;
import com.tap5.hotelbooking.entities.User;
import com.tap5.hotelbooking.services.Authenticator;

/**
 * Allows the user to modify password
 * 
 * @author karesti
 */
public class Settings
{
    @Inject
    private CrudServiceDAO crudServiceDAO;

    @Inject
    private Messages messages;

    @Inject
    private Authenticator authenticator;

    @InjectPage
    private Signin signin;

    @Property
    @PageActivationContext
    private User user;

    @Property
    private String fullname;

    @Property
    private String password;

    @Property
    private String verifyPassword;

    @Component
    private Form settingsForm;

    public void onPrepare() {
        this.fullname = user.getFullname();
    }

    public Object onSuccess()
    {
        if(this.fullname.length() != 0) {
            user.setFullname(this.fullname);
        }

        if (!verifyPassword.equals(password))
        {
            settingsForm.recordError(messages.get("error.verifypassword"));

            return null;
        }

        user.setPassword(password);

        crudServiceDAO.update(user);

        if(this.user == this.authenticator.getLoggedUser()) {
            authenticator.logout();
            signin.setFlashMessage(messages.get("settings.password-changed"));
            return signin;
        }
        return Index.class;
    }
}
