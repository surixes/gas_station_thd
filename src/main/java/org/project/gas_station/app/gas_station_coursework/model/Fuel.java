package org.project.gas_station.app.gas_station_coursework.model;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "fuels", schema = "public")
@SequenceGenerator(name = "fuels_seq", sequenceName = "public.fuels_id_seq", allocationSize = 1)
public class Fuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "unit", nullable = false, length = 20)
    private String unit;

    public Fuel() {
    }

    public Fuel(String type, String unit) {
        this.type = type;
        this.unit = unit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return type + " (" + unit + ")";
    }

    @OneToMany(mappedBy = "fuel", fetch = FetchType.LAZY)
    private Set<SupplierFuel> suppliers;

}
