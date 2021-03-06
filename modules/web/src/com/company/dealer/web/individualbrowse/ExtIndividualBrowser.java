/*
 * Copyright (c) 2020 com.company.dealer.web.individualbrowse
 */
package com.company.dealer.web.individualbrowse;

import com.company.dealer.service.CarBuyRequestService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.thesis.core.entity.Contractor;
import com.haulmont.thesis.web.ui.individual.IndividualBrowser;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author ilya
 */
public class ExtIndividualBrowser extends IndividualBrowser {
    @Inject
    private CarBuyRequestService carBuyRequestService;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        individualsTable.addGeneratedColumn(getMessage("requestCount"),
                new Table.PrintableColumnGenerator<Contractor, String>() {
                    @Override
                    public String getValue(Contractor item) {
                        return String.valueOf(carBuyRequestService.getCarBuyRequestCountForContractor(item));
                    }

                    @Override
                    public Component generateCell(Contractor entity) {
                        Integer count = carBuyRequestService.getCarBuyRequestCountForContractor(entity);
                        return new Table.PlainTextCell(count.toString());
                    }
                });
    }
}
