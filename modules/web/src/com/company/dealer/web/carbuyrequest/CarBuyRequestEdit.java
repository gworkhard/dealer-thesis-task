package com.company.dealer.web.carbuyrequest;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.thesis.web.actions.PrintReportAction;
import com.haulmont.thesis.web.ui.basicdoc.editor.AbstractDocEditor;
import com.haulmont.workflow.core.app.WfUtils;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import org.apache.commons.lang.StringUtils;
import com.haulmont.cuba.core.entity.Entity;

import java.util.Map;

import com.company.dealer.entity.CarBuyRequest;

public class CarBuyRequestEdit extends AbstractDocEditor<CarBuyRequest> {
    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
    }

    @Override
    protected String getHiddenTabsConfig() {
        return "correspondenceHistoryTab,docLogTab,cardLinksTab,processTab,securityTab,docTransferLogTab,cardProjectsTab,versionsTab,openHistoryTab";
    }

    @Override
    public void setItem(Entity item) {
        super.setItem(item);
        printButton.addAction(new PrintReportAction("printExecutionList", this, "printDocExecutionListReportName"));
        for (Action action : getActions()) {
            if (!getItem().getIsPaid() && action.getId().equals("proc_20200730_160604433.Проверено")) {
                action.setEnabled(false);
            }
        }
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

    @Override
    protected void fillHiddenTabs() {
        hiddenTabs.put("office", getMessage("office"));
        hiddenTabs.put("attachmentsTab", getMessage("attachmentsTab"));
        hiddenTabs.put("docTreeTab", getMessage("docTreeTab"));
        hiddenTabs.put("cardCommentTab", getMessage("cardCommentTab"));
        super.fillHiddenTabs();
    }
}