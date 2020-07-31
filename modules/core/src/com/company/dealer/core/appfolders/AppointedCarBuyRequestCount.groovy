/*
 * Copyright (c) ${YEAR} ${PACKAGE_NAME}
 */

package com.company.dealer.core.appfolders

import com.haulmont.cuba.core.EntityManager
import com.haulmont.cuba.core.Persistence
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.UserSessionSource

Persistence persistence = AppBeans.get(Persistence.class)
EntityManager em = persistence.getEntityManager()
UserSessionSource uss = AppBeans.get(UserSessionSource.class)
return em.createQuery("SELECT COUNT(c.id) FROM dealer\$CarBuyRequest c WHERE :userId in c.currentActorsIds")
        .setParameter("userId", uss.getUserSession().getUser().getId()).getFirstResult()