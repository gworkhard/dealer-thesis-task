package com.company.dealer.web.car;


import com.haulmont.thesis.core.entity.TsCard;
import com.haulmont.thesis.web.ui.basic.editor.AbstractCardEditor;
import com.haulmont.workflow.core.app.WfUtils;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Component;
import org.apache.commons.lang.StringUtils;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.thesis.core.entity.Numerator;
import com.haulmont.thesis.core.app.NumerationService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.PersistenceHelper;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;

public class CarEdit<T extends TsCard> extends AbstractCardEditor<T> {

    @Inject
    protected NumerationService numerationService;
    

    @Override
    protected String getHiddenTabsConfig() {
        return "securityTab,cardLogTab,processTab";
    }

    @Override
    public void setItem(Entity item) {
        super.setItem(item);
        if (isNumberAssignNeeded())
            setNumber(item);
        
    }

    protected boolean isNumberAssignNeeded() {
        return true;
    }

    protected void setNumber(Entity item) {
        LoadContext ctx = new LoadContext(Numerator.class);
        if (PersistenceHelper.isNew(item) && item.getMetaClass().getProperty("number") != null) {
            ctx.setQueryString("select n from df$Numerator n where n.code=:name").setParameter("name",
                    getItem().getClass().getSimpleName() + "Numerator");
            List<Numerator> numeratorList = getDsContext().getDataSupplier().loadList(ctx);
            if (!numeratorList.isEmpty()) {
                Numerator numerator = numeratorList.get(0);
                HashMap<String, Object> params = new HashMap<>();
                params.put("entity", getItem());
                item.setValue("number", numerationService.getNextNumber(numerator, params));
            }
        }
    }

    

    @Override
    protected Container getStateHolder() {
        return getComponent("stateHolder");
    }

    @Override
    protected Component createState() {
        if (WfUtils.isCardInState(getItem(), "New") || StringUtils.isEmpty(getItem().getState())) {
            Label label = componentsFactory.createComponent(Label.NAME);
            label.setValue(StringUtils.isEmpty(getItem().getState()) ? "" : getItem().getLocState());
            return label;
        } else {
            return super.createState();
        }
     }
}