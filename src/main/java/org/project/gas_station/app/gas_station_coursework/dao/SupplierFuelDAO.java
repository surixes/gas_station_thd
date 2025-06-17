package org.project.gas_station.app.gas_station_coursework.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.project.gas_station.app.gas_station_coursework.model.SupplierFuel;
import org.project.gas_station.app.gas_station_coursework.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class SupplierFuelDAO {

    public List<SupplierFuel> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM SupplierFuel", SupplierFuel.class).getResultList();
        }
    }

    public Optional<SupplierFuel> get(int fuelId, int firmId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM SupplierFuel sf WHERE sf.fuel.id = :fuelId AND sf.firm.id = :firmId";
            SupplierFuel sf = session.createQuery(hql, SupplierFuel.class)
                    .setParameter("fuelId", fuelId)
                    .setParameter("firmId", firmId)
                    .uniqueResult();
            return Optional.ofNullable(sf);
        }
    }

    public void save(SupplierFuel sf) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(sf);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            throw e;
        }
    }

    public void delete(int fuelId, int firmId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            get(fuelId, firmId).ifPresent(session::remove);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            throw e;
        }
    }
}
