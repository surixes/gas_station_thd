package org.project.gas_station.app.gas_station_coursework.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "price_dynamics", schema = "public")
@SequenceGenerator(name = "price_dynamics_seq", sequenceName = "public.price_dynamics_id_seq", allocationSize = 1)
public class PriceDynamics {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "price_dynamics_seq")
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fuel_id", nullable = false)
    private Fuel fuel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "firm_id", nullable = false)
    private Firm firm;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "price", nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    public PriceDynamics() {
    }

    public PriceDynamics(Fuel fuel, LocalDate startDate, LocalDate endDate, BigDecimal price) {
        this.fuel = fuel;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public Fuel getFuel() {
        return fuel;
    }

    public void setFuel(Fuel fuel) {
        this.fuel = fuel;
    }

    public Firm getFirm() {
        return firm;
    }

    public void setFirm(Firm firm) {
        this.firm = firm;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return fuel.getType() + ": " + price + " от " + startDate;
    }
}
