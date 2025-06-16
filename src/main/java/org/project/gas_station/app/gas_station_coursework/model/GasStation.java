package org.project.gas_station.app.gas_station_coursework.model;

import javax.persistence.*;

@Entity
@Table(name = "gas_stations", schema = "public")
public class GasStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "firm_id", nullable = false)
    private Firm firm;

    public GasStation() {
    }

    public GasStation(String address, Firm firm) {
        this.address = address;
        this.firm = firm;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Firm getFirm() {
        return firm;
    }

    public void setFirm(Firm firm) {
        this.firm = firm;
    }

    @Override
    public String toString() {
        return address;
    }
}
