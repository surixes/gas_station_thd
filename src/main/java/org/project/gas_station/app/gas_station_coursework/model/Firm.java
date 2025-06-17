package org.project.gas_station.app.gas_station_coursework.model;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "firms", schema = "public")
@SequenceGenerator(name = "firms_seq", sequenceName = "public.firms_id_seq", allocationSize = 1)
public class Firm {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "firms_seq")
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "legal_address", length = 200)
    private String legalAddress;

    @Column(name = "legal_number", length = 200)
    private String legalNumber;

    @Column(name = "is_supplier", nullable = false)
    private boolean isSupplier = false;

    @Column(name = "is_owner", nullable = false)
    private boolean isOwner = false;

    public Firm() {
    }

    public Firm(String name, String legalAddress, String legalNumber, boolean isSupplier, boolean isOwner) {
        this.name = name;
        this.legalAddress = legalAddress;
        this.legalNumber = legalNumber;
        this.isSupplier = isSupplier;
        this.isOwner = isOwner;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLegalAddress() {
        return legalAddress;
    }

    public void setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
    }

    public String getLegalNumber() {
        return legalNumber;
    }

    public void setLegalNumber(String legalNumber) {
        this.legalNumber = legalNumber;
    }

    public boolean isSupplier() {
        return isSupplier;
    }

    public void setSupplier(boolean supplier) {
        isSupplier = supplier;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    @Override
    public String toString() {
        return name;
    }

    @OneToMany(mappedBy = "firm", fetch = FetchType.LAZY)
    private Set<SupplierFuel> suppliedFuels;

}
