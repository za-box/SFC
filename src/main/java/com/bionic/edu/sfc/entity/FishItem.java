package com.bionic.edu.sfc.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Ivan
 * 2014.10
 */
@Entity
public class FishItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String uuid;

    @ManyToOne
    private FishParcel fishParcel;

    private double price;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Bill bill;

    private boolean removedFromColdStore;

    private boolean forWriteOff;

    private boolean readyForShipment;

    private Date creationDate;

    private Date removedFromColdStoreDate;

    @Column(nullable = false)
    private Double weight;

    public FishItem(){ }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FishItem fishItem = (FishItem) o;

        return uuid.equals(fishItem.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return "FishItem{" +
                "id=" + id +
                ", fishParcel=" + fishParcel +
                ", price=" + price +
                ", customer=" + customer +
                ", offerDate=" + creationDate +
                ", bill=" + bill +
                ", isShipped=" + removedFromColdStore +
                ", shipmentDate=" + removedFromColdStoreDate +
                ", weight=" + weight +
                '}';
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FishParcel getFishParcel() {
        return fishParcel;
    }

    public void setFishParcel(FishParcel fishParcel) {
        this.fishParcel = fishParcel;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public boolean isRemovedFromColdStore() {
        return removedFromColdStore;
    }

    public void setRemovedFromColdStore(boolean removedFromColdStore) {
        this.removedFromColdStore = removedFromColdStore;
    }

    public boolean isForWriteOff() {
        return forWriteOff;
    }

    public void setForWriteOff(boolean forWriteOff) {
        this.forWriteOff = forWriteOff;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getRemovedFromColdStoreDate() {
        return removedFromColdStoreDate;
    }

    public void setRemovedFromColdStoreDate(Date shipmentDate) {
        this.removedFromColdStoreDate = shipmentDate;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public boolean isReadyForShipment() {
        return readyForShipment;
    }

    public void setReadyForShipment(boolean readyForShipment) {
        this.readyForShipment = readyForShipment;
    }
}