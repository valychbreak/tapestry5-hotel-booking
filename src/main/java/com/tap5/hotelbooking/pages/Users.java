package com.tap5.hotelbooking.pages;

import com.tap5.hotelbooking.data.SearchCriteria;
import com.tap5.hotelbooking.entities.User;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.hibernate.HibernateGridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Look up for users.
 *
 * @author Valeriy
 */
public class Users {
    @Inject
    private Session session;

    @InjectComponent
    private Zone userResult;

    @SessionState
    @Property
    private SearchCriteria criteria;

    @Property
    private GridDataSource dataSource = new UserGridDataSource(this.session, User.class);

    @Property
    private GridDataSource allUsers = new HibernateGridDataSource(this.session, User.class);

    @Property
    private User currentUser;

    @OnEvent(value = EventConstants.SUCCESS)
    Object searchUsers() {
        return userResult.getBody();
    }

    Object onAction() {
        return userResult.getBody();
    }

    private final class UserGridDataSource extends HibernateGridDataSource {
        Session session;

        public UserGridDataSource(Session session, Class entityType) {
            super(session, entityType);
            this.session = session;
        }

        @Override
        public int getAvailableRows() {
            return criteria.getSearchPattern() == null ? 0 : super.getAvailableRows();
        }

        @Override
        protected void applyAdditionalConstraints(Criteria crit) {
            crit.add(Restrictions.ilike("username", criteria.getSearchPattern()));

        }
    }
}
