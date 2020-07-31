/*
 * Copyright (c) 2020 com.company.dealer.service
 */
package com.company.dealer.service;

import com.company.dealer.entity.CarBuyRequest;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.thesis.core.entity.Contractor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * @author ilya
 */
@Service(CarBuyRequestService.NAME)
public class CarBuyRequestServiceBean implements CarBuyRequestService {

    @Inject
    private Persistence persistence;

    @Override
    public Integer getCarBuyRequestCountForContractor(Contractor contractor) {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            TypedQuery<CarBuyRequest> query = em.createQuery(
                    "select cbr from dealer$CarBuyRequest cbr where cbr.customer.id = :customerId", CarBuyRequest.class);
            query.setParameter("customerId", contractor.getId());
            List<CarBuyRequest> requests = query.getResultList();
            tx.commit();
            return requests.size();
        } finally {
            tx.end();
        }
    }
}