package org.project.gas_station.app.gas_station_coursework.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "supplier_fuel", schema = "public")
public class SupplierFuel {

    @EmbeddedId
    private PK id;

    @ManyToOne
    @MapsId("fuelId")
    @JoinColumn(name = "fuel_id")
    private Fuel fuel;

    @ManyToOne
    @MapsId("firmId")
    @JoinColumn(name = "firm_id")
    private Firm firm;

    public SupplierFuel() {
    }

    public SupplierFuel(Firm firm, Fuel fuel) {
        this.firm = firm;
        this.fuel = fuel;
        this.id = new PK(firm.getId(), fuel.getId());
    }

    public Firm getFirm() {
        return firm;
    }

    public Fuel getFuel() {
        return fuel;
    }

    @Embeddable
    public static class PK implements Serializable {
        @Column(name = "firm_id")
        private Integer firmId;

        @Column(name = "fuel_id")
        private Integer fuelId;

        public PK() {
        }

        public PK(Integer firmId, Integer fuelId) {
            this.firmId = firmId;
            this.fuelId = fuelId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof PK))
                return false;
            PK pk = (PK) o;
            return Objects.equals(firmId, pk.firmId) && Objects.equals(fuelId, pk.fuelId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(firmId, fuelId);
        }
    }
}
