package org.project.gas_station.app.gas_station_coursework.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.project.gas_station.app.gas_station_coursework.model.Firm;
import org.project.gas_station.app.gas_station_coursework.model.GasStation;
import org.project.gas_station.app.gas_station_coursework.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class GasStationDAO {
    public List<GasStation> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from GasStation", GasStation.class).getResultList();
        }
    }

    public List<Firm> getAllFirms() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Firm", Firm.class).getResultList();
        }
    }

    public Optional<GasStation> get(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(GasStation.class, id));
        }
    }

    public void save(GasStation station) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(station);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            throw ex;
        }
    }

    public void update(GasStation station) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(station);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            throw ex;
        }
    }

    public boolean delete(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            GasStation station = session.get(GasStation.class, id);
            if (station != null) {
                session.remove(station);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            throw ex;
        }
    }
}
