package org.project.gas_station.app.gas_station_coursework.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.project.gas_station.app.gas_station_coursework.model.PriceDynamics;
import org.project.gas_station.app.gas_station_coursework.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class PriceDynamicsDAO {

    public List<PriceDynamics> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM PriceDynamics", PriceDynamics.class).getResultList();
        }
    }

    public Optional<PriceDynamics> get(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(PriceDynamics.class, id));
        }
    }

    public void save(PriceDynamics pd) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(pd);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            throw e;
        }
    }

    public void update(PriceDynamics pd) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(pd);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            throw e;
        }
    }

    public boolean delete(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            PriceDynamics pd = session.get(PriceDynamics.class, id);
            if (pd != null) {
                session.remove(pd);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            throw e;
        }
    }
}
