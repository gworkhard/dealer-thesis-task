/*
 * Copyright (c) 2020 com.company.dealer.service
 */
package com.company.dealer.service;

import com.haulmont.thesis.core.entity.Contractor;

/**
 * @author ilya
 */
public interface CarBuyRequestService {
    String NAME = "dealer_CarBuyRequestService";

    Integer getCarBuyRequestCountForContractor(Contractor contractor);
}