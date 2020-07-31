package com.company.dealer.web.car;

import com.company.dealer.entity.Car;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.data.impl.GroupDatasourceImpl;
import com.haulmont.thesis.core.entity.TsCard;
import com.haulmont.thesis.core.entity.CardUserInfo;
import com.haulmont.workflow.core.entity.Card;

import java.util.*;

public class CarBrowseDatasource extends GroupDatasourceImpl<Car, UUID> {
    @Override
    protected void loadData(Map<String, Object> params) {
        super.loadData(params);
        loadCardUserInfos();
    }

    private void loadCardUserInfos() {
        UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.class);
        DataService dataService = AppBeans.get(DataService.class);

        LoadContext ctx = new LoadContext(CardUserInfo.class);

        String queryString = "select cu from wf$CardUserInfo cu where cu.user.id = :userId ";
        
        LoadContext.Query q = ctx.setQueryString(queryString);
        q.setParameter("userId", userSessionSource.currentOrSubstitutedUserId());
        ctx.setView("card-browse");
        List<CardUserInfo> cardUserInfos = dataService.loadList(ctx);
        for (CardUserInfo cardUserInfo : cardUserInfos) {
            Card card = cardUserInfo.getCard();
            if (card != null) {
                TsCard abstractCard = (TsCard) data.get(card.getId());
                if (abstractCard != null)
                    abstractCard.setCardUserInfo(cardUserInfo);
            }
        }
    }
}
