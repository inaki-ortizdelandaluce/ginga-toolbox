package org.ginga.tools.utils;

import org.ginga.tools.lacdump.LacDumpEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();

            // SET CONFIGURATION PROPERTIES

            // connection properties
            DatabaseProperties jdbcProperties = DatabaseProperties.getInstance();
            configuration.setProperty("hibernate.connection.driver_class",
                    jdbcProperties.getDatabaseDriverClassName());

            configuration.setProperty("hibernate.connection.url", jdbcProperties.getDatabaseUrl());
            configuration.setProperty("hibernate.connection.username",
                    jdbcProperties.getDatabaseUser());
            configuration.setProperty("hibernate.connection.password",
                    jdbcProperties.getDatabasePassword());
            configuration.setProperty("hibernate.default_schema", "ginga");

            // dialect properties:
            // MySQLInnoDBDialect/MySQLMyISAMDialect/MySQL5Dialect/MySQL5InnoDBDialect
            configuration.setProperty("hibernate.dialect",
                    "org.hibernate.dialect.MySQL5InnoDBDialect");

            // connection pooling
            // https://community.jboss.org/wiki/HowToConfigureTheC3P0ConnectionPool
            configuration.setProperty("hibernate.connection.provider_class",
                    "org.hibernate.service.jdbc.connections.internal.C3P0ConnectionProvider");
            configuration.setProperty("hibernate.c3p0.acquire_increment", "1");
            configuration.setProperty("hibernate.c3p0.idle_test_period", "50"); // seconds
            configuration.setProperty("hibernate.c3p0.max_size", "50");
            configuration.setProperty("hibernate.c3p0.max_statements", "50");
            configuration.setProperty("hibernate.c3p0.min_size", "8");
            configuration.setProperty("hibernate.c3p0.timeout", "100"); // seconds

            // sql properties - helps reducing SQL printed to stdout
            configuration.setProperty("hibernate.show_sql", "false");
            configuration.setProperty("hibernate.format_sql", "false");
            configuration.setProperty("hibernate.use_sql_comments", "false");

            // other properties
            configuration.setProperty("hibernate.current_session_context_class", "thread");
            configuration.setProperty("hibernate.bytecode.use_reflection_optimizer", "false");
            configuration.setProperty("javax.persistence.validation.mode", "none");

            // TODO caching properties
            // configuration.setProperty("hibernate.cache.region.factory_class","org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
            // configuration.setProperty("net.sf.ehcache.configurationResourceName","ehcache.xml");
            // configuration.setProperty("hibernate.cache.use_second_level_cache","true");
            // configuration.setProperty("hibernate.cache.use_query_cache","true");

            // ADD ANNOTATED CLASSES
            configuration.addAnnotatedClass(LacDumpEntity.class);

            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
            .applySettings(configuration.getProperties());
            SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
            return sessionFactory;

        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session beginTransaction() {
        Session hibernateSession = HibernateUtil.getSession();
        hibernateSession.beginTransaction();
        return hibernateSession;
    }

    public static void commitTransaction() {
        HibernateUtil.getSession().getTransaction().commit();
    }

    public static void rollbackTransaction() {
        HibernateUtil.getSession().getTransaction().rollback();
    }

    public static void closeSession() {
        HibernateUtil.getSession().close();
    }

    public static Session getSession() {
        Session hibernateSession = sessionFactory.getCurrentSession();
        return hibernateSession;
    }

    public static void shutdown() {
        // Close caches and connection pools
        sessionFactory.close();
    }

}