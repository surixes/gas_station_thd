package org.project.gas_station.app.gas_station_coursework.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.project.gas_station.app.gas_station_coursework.model.Firm;
import org.project.gas_station.app.gas_station_coursework.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class FirmDAO {

    public List<Firm> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Firm", Firm.class).getResultList();
        }
    }

    public Optional<Firm> get(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Firm.class, id));
        }
    }

    public void save(Firm firm) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(firm);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            throw e;
        }
    }

    public void update(Firm firm) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(firm);
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
            Firm firm = session.get(Firm.class, id);
            if (firm != null) {
                session.remove(firm);
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
