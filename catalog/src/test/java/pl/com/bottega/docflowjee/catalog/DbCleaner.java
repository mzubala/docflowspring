package pl.com.bottega.docflowjee.catalog;

import org.hibernate.Session;
import org.hibernate.metamodel.internal.EntityTypeImpl;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import org.hibernate.Metamodel;


@Component
public class DbCleaner {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private EntityManager em;

    public void cleanDb() {
        transactionTemplate.execute((c) -> {
            Session session = em.unwrap(Session.class);
            Metamodel hibernateMetadata = session.getSessionFactory().getMetamodel();
            session.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
            hibernateMetadata.getEntities().stream()
                .map(e ->
                    ((AbstractEntityPersister)session.getSessionFactory().getClassMetadata(e.getJavaType())).getTableName()
                )
                .forEach(tableName -> session.createNativeQuery("TRUNCATE TABLE " + tableName)
                    .executeUpdate()
                );
            session.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
            return null;
        });
    }

}
