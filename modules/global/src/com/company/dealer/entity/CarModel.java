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
import com.haulmont.chile.core.annotations.NamePattern;
import javax.persistence.Column;
import com.haulmont.cuba.core.entity.StandardEntity;

@NamePattern("%s|name")
@Table(name = "DEALER_CAR_MODEL")
@Entity(name = "dealer$CarModel")
@EnableRestore
@TrackEditScreenHistory
public class CarModel extends StandardEntity {
    private static final long serialVersionUID = 7768199156739827029L;

    @Column(name = "NAME", nullable = false, length = 50)
    protected String name;

    @Column(name = "CODE", nullable = false, length = 3)
    protected String code;

    @Column(name = "COMMENT_", length = 400)
    protected String comment;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }



}