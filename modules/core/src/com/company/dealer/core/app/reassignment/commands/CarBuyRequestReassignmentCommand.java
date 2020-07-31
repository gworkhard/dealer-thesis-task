/*
 * Copyright (c) 2020 com.company.dealer.core.app.reassignment.commands
 */
package com.company.dealer.core.app.reassignment.commands;


import com.haulmont.thesis.core.app.reassignment.commands.AbstractDocReassignmentCommand;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;

import com.company.dealer.entity.CarBuyRequest;

/**
 * @author ilya
 */
@ManagedBean(CarBuyRequestReassignmentCommand.NAME)
public class CarBuyRequestReassignmentCommand extends AbstractDocReassignmentCommand<CarBuyRequest> {
    protected static final String NAME = "carbuyrequest_reassignment_command";

    @PostConstruct
    protected void postInit() {
        type = "CarBuyRequest";
        docQuery = String.format(DOC_QUERY_TEMPLATE, "dealer$CarBuyRequest");
    }

    @Override
    public String getName() {
        return NAME;
    }
}