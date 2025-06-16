package org.project.gas_station.app.gas_station_coursework.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();

            MetadataSources sources = new MetadataSources(standardRegistry)
                    .addAnnotatedClass(org.project.gas_station.app.gas_station_coursework.model.Fuel.class)
                    .addAnnotatedClass(org.project.gas_station.app.gas_station_coursework.model.Sale.class)
                    .addAnnotatedClass(org.project.gas_station.app.gas_station_coursework.model.GasStation.class)
                    .addAnnotatedClass(org.project.gas_station.app.gas_station_coursework.model.Client.class)
                    .addAnnotatedClass(org.project.gas_station.app.gas_station_coursework.model.Firm.class)
                    .addAnnotatedClass(org.project.gas_station.app.gas_station_coursework.model.PriceDynamics.class)
                    .addAnnotatedClass(org.project.gas_station.app.gas_station_coursework.model.SupplierFuel.class);

            Metadata metadata = sources.getMetadataBuilder().build();

            return metadata.getSessionFactoryBuilder().build();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}