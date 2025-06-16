package org.project.gas_station.app.gas_station_coursework.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.project.gas_station.app.gas_station_coursework.model.Fuel;
import org.project.gas_station.app.gas_station_coursework.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class FuelDAO {

    public List<Fuel> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Fuel", Fuel.class).getResultList();
        }
    }

    public Optional<Fuel> get(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Fuel.class, id));
        }
    }

    public void save(Fuel fuel) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(fuel);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            throw e;
        }
    }

    public void update(Fuel fuel) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(fuel);
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
            Fuel fuel = session.get(Fuel.class, id);
            if (fuel != null) {
                session.remove(fuel);
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
