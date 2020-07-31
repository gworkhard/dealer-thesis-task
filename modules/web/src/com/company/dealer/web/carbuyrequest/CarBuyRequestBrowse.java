package com.company.dealer.web.carbuyrequest;

import java.util.Map;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.thesis.web.ui.basicdoc.browse.AbstractDocBrowser;
import com.company.dealer.entity.CarBuyRequest;

public class CarBuyRequestBrowse extends AbstractDocBrowser<CarBuyRequest> {

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        entityName = "dealer$CarBuyRequest";
    }
}