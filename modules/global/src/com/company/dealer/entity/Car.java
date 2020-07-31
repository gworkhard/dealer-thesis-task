/*
 * Copyright (c) 2020 com.company.dealer.entity
 */
package com.company.dealer.entity;


/**
 * @author ilya
 */
import com.haulmont.cuba.core.entity.annotation.EnableRestore;
import com.haulmont.cuba.core.entity.annotation.TrackEditScreenHistory;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.DiscriminatorValue;
import javax.persistence.InheritanceType;
import javax.persistence.Inheritance;
import javax.persistence.PrimaryKeyJoinColumn;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.haulmont.thesis.core.entity.TsCard;
import com.haulmont.cuba.core.entity.annotation.Listeners;

@Listeners("dealer_CarListener")
@PrimaryKeyJoinColumn(name = "CARD_ID", referencedColumnName = "ID")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("1")
@Table(name = "DEALER_CAR")
@Entity(name = "dealer$Car")
@EnableRestore
@TrackEditScreenHistory
public class Car extends TsCard {
    private static final long serialVersionUID = 328920653684322202L;

    @Column(name = "NUMBER_", length = 50)
    protected String number;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CAR_MODEL_ID")
    protected CarModel carModel;

    @Column(name = "NAME", nullable = false, length = 50)
    protected String name;

    @Column(name = "MANUFACTURE_YEAR", length = 50)
    protected Integer manufactureYear;

    @Column(name = "PRICE", length = 50)
    protected BigDecimal price;

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
    }

    public CarModel getCarModel() {
        return carModel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setManufactureYear(Integer manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public Integer getManufactureYear() {
        return manufactureYear;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }


}