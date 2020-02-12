package org.fzillo.services.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.fzillo.services.api.Tag;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TagDAO extends AbstractDAO<Tag> {

    private static final Logger log = LoggerFactory.getLogger(TagDAO.class);

    public TagDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Tag findById(Integer  id) {
        return get(id);
    }

    public void deleteAll(){ //TODO this does delete all fks but not Tags
        currentSession().createQuery("delete from Tag").executeUpdate();
    }

}
