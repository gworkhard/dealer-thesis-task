/*
 * Copyright (c) ${YEAR} ${PACKAGE_NAME}
 */

package com.company.dealer.core.appfolders

import com.haulmont.cuba.core.EntityManager
import com.haulmont.cuba.core.Persistence
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.UserSessionProvider

Persistence persistence = AppBeans.get(Persistence.class)
EntityManager em = persistence.getEntityManager()
return em.createQuery("SELECT COUNT(c.id) FROM dealer\$CarBuyRequest c WHERE c.proc IS NULL AND c.creator.id= :userId")
        .setParameter("userId", UserSessionProvider.currentOrSubstitutedUserId()).getFirstResult()

