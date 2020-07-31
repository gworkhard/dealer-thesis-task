package com.company.dealer.core.app;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.thesis.core.app.defaultprocactors.CardAuthorProcessActorStrategy;
import com.haulmont.thesis.core.entity.defaultactor.TsDefaultProcActor;
import com.haulmont.workflow.core.entity.ProcRole;
import org.apache.commons.lang.exception.ExceptionUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.codec.digest.DigestUtils;

import com.haulmont.thesis.core.app.AbstractDeployer;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Config;

import com.haulmont.cuba.security.app.EntityLogAPI;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.security.app.Authenticated;

import java.io.File;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;

import org.dom4j.Document;
import org.dom4j.Element;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.workflow.core.entity.Proc;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.reports.entity.Report;
import com.haulmont.reports.entity.ReportScreen;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.*;


import javax.annotation.ManagedBean;
import javax.inject.Inject;

import java.io.File;


@ManagedBean("dealer_ExtDeployer")
public class ExtDeployer extends AbstractDeployer implements ExtDeployerMBean {

    protected Log log = LogFactory.getLog(ExtDeployer.class);
    @Inject
    protected EntityLogAPI entityLogAPI;
    @Inject
    private Messages messages;

    public ExtDeployer() {
        createAppContextListener();
    }

    protected void createAppContextListener() {
        AppContext.addListener(new AppContext.Listener() {
            public void applicationStarted() {
                checkForFirstInit();
            }

            public void applicationStopped() {
            }
        });
    }

    protected void checkForFirstInit() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            List<Config> configs = em.createQuery("select c from sys$Config c where c.name='dealer.initDefaultInFirstStart'",
                    Config.class).getResultList();
            String initDefaultInFirstStart = null;
            if (!configs.isEmpty()) {
                initDefaultInFirstStart = configs.get(0).getValue();
            }

            if (Boolean.TRUE.toString().equals(initDefaultInFirstStart) || initDefaultInFirstStart == null) {
                String edition = AppContext.getProperty("thesis.initDefaultEdition");
                if (StringUtils.isNotBlank(edition) && Integer.parseInt(edition) > 1) {
                    initDefault("init");
                    if (initDefaultInFirstStart == null) {
                        try {
                            login();
                            Config config = new Config();
                            config.setName("dealer.initDefaultInFirstStart");
                            config.setValue("false");
                            em.persist(config);
                            tx.commit();
                        } finally {
                            logout();
                        }
                    } else {
                        try {
                            login();
                            em.createQuery("update sys$Config c set c.value='false' where c.name='dealer.initDefaultInFirstStart'",
                                    Config.class).executeUpdate();
                            tx.commit();
                        } finally {
                            logout();
                        }
                    }
                }
            }
        } finally {
            tx.end();
        }
    }

    @Authenticated
    public String initDefault(String password) {
        if (password != null && initDefaultPassword.equals(DigestUtils.md5Hex(password))) {
            try {
                executeInitScripts();

                initExtensionDocumentsFunctionality();
                initStandardFilters();
                entityLogAPI.invalidateCache();
                deployApprovalProcess();
                return INIT_SUCESS;
            } catch (Exception e) {
                return ExceptionUtils.getStackTrace(e);
            }
        }
        return "Error password";
    }

    protected void executeInitScripts() {
        String dbDirPath = ConfigProvider.getConfig(ServerConfig.class).getDbDir();
        String dbmsType = AppContext.getProperty("cuba.dbmsType");
        File folderDB = new File(dbDirPath + "/50-dealer/init/" + dbmsType);
        File[] scripts = folderDB.listFiles();
        for (File script : scripts) {
            if (script.getName().contains("init")) {
                executeSqlScript(script);
            }
        }
    }


    public String initExtensionDocumentsFunctionality() {
        ArrayList<String> procCodes = Lists.newArrayList("Endorsement", "Resolution", "Acquaintance", "Registration");
        ArrayList<String> reportCodes = Lists.newArrayList("EndorsementList");

        String extensionDocuments = AppContext.getProperty("ext.extensionDocuments");
        if (extensionDocuments == null || extensionDocuments.isEmpty())
            return "No extension documents found";

        String[] extDocMetaClasses = extensionDocuments.split(",");
        Transaction tx = persistence.getTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            TypedQuery<Proc> query = em.createQuery("select p from wf$Proc p where p.code in :procCodes", Proc.class);
            query.setParameter("procCodes", procCodes);
            List<Proc> procs = query.getResultList();

            for (Proc proc : procs) {
                String cardTypes = proc.getCardTypes();
                for (String metaClass : extDocMetaClasses)
                    if (!cardTypes.contains(metaClass))
                        cardTypes += metaClass + ",";

                proc.setCardTypes(cardTypes);
            }

            TypedQuery<Report> reportQuery = em.createQuery("select r from report$Report r where r.code in :reportCodes", Report.class);
            reportQuery.setParameter("reportCodes", reportCodes);
            List<Report> reports = reportQuery.getResultList();

            for (Report report : reports) {
                Report reportFromXml = reportingApi.convertToReport(report.getXml());
                List<ReportScreen> reportScreens = reportFromXml.getReportScreens();

                for (String metaClass : extDocMetaClasses) {
                    final String screenId = metaClass + ".edit";
                    Optional<ReportScreen> oReportScreen = Iterables.tryFind(reportScreens, new Predicate<ReportScreen>() {
                        @Override
                        public boolean apply(ReportScreen input) {
                            return screenId.equals(input.getScreenId());
                        }
                    });

                    if (!oReportScreen.isPresent()) {
                        ReportScreen reportScreen = new ReportScreen();
                        reportScreen.setReport(reportFromXml);
                        reportScreen.setScreenId(screenId);
                        reportScreens.add(reportScreen);
                    }
                }

                reportFromXml.setReportScreens(reportScreens);
                report.setXml(reportingApi.convertToXml(reportFromXml));
            }
            tx.commit();
        } catch (Exception e) {
            String stackTrace = ExceptionUtils.getStackTrace(e);
            log.error(stackTrace);
            return stackTrace;
        } finally {
            tx.end();
        }

        return "Standard functionality added to extension documents";
    }

    public String initStandardFilters() {
        StringBuilder resultMessage = new StringBuilder();
        String filtersDirPath = AppContext.getProperty("ext.filtersDir");
        File filtersDir = new File(filtersDirPath);
        if (filtersDir.exists()) {
            File[] filters = filtersDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".xml");
                }
            });
            Arrays.sort(filters);
            createFilterEntities(filters, resultMessage);
        } else {
            resultMessage.append("Folders dir " + filtersDirPath + " not found");
        }
        return resultMessage.toString();
    }

    protected void createFilterEntities(File[] filters, StringBuilder resultMessage) {
        Transaction tx = persistence.createTransaction();
        try {
            login();
            for (File filterXml : filters) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(filterXml);
                    String xmlContent = IOUtils.toString(fis);
                    Document document = Dom4j.readDocument(xmlContent);
                    Element filterEl = document.getRootElement();
                    createOrUpdateFilter(filterEl.attributeValue("code"), filterEl.attributeValue("name"),
                            filterEl.attributeValue("filterComponent"), Dom4j.writeDocument(document, true));
                    resultMessage.append("File ")
                            .append(filterXml.getName())
                            .append(" processed\n");
                } catch (FileNotFoundException e) {
                    resultMessage.append(ExceptionUtils.getStackTrace(e));
                } finally {
                    IOUtils.closeQuietly(fis);
                }
            }
            tx.commit();
        } catch (Exception ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
        } finally {
            tx.end();
            logout();
        }
    }

    @Override
    @Authenticated
    public String deployApprovalProcess() {
        String result = deployProcesses(Collections.singletonList("ApproveCarBuyRequest"));
        persistence.createTransaction().execute(new Transaction.Callable<Object>() {
            @Override
            public Object call(EntityManager em) {
                Proc proc = (Proc) em.createQuery("select p from wf$Proc p where p.name = :name")
                        .setParameter("name", "ApproveCarBuyRequest").setView(Proc.class,
                                "edit").getFirstResult();
                if (proc != null) {
                    Role roleInitiator = (Role) em.createQuery("select r from sec$Role r where " +
                            " r.name='doc_initiator'").getFirstResult();
                    Role roleManager = (Role) em.createQuery("select r from sec$Role r where " +
                            " r.name='Manager'").getFirstResult();
                    Role roleBankOperator = (Role) em.createQuery("select r from sec$Role r where " +
                            " r.name='BankOperator'").getFirstResult();
                    Role roleMaster = (Role) em.createQuery("select r from sec$Role r where " +
                            " r.name='Master'").getFirstResult();
                    for (ProcRole procRole : proc.getRoles()) {
                        switch (procRole.getCode()) {
                            case "CARD_CREATOR":
                                procRole.setRole(roleInitiator);
                                procRole.setSortOrder(0);
                                TsDefaultProcActor defaultProcActor = (TsDefaultProcActor) em
                                        .createQuery("select dpa from ts$DefaultProcActor dpa where" +
                                                " dpa.strategyId =:strategy and dpa.procRole.id = :procRole " )
                                    .setParameter("strategy", CardAuthorProcessActorStrategy.NAME)
                                    .setParameter("procRole", procRole.getId()).getFirstResult();
                                if (defaultProcActor == null) {
                                    defaultProcActor = metadata.create(TsDefaultProcActor.class);
                                    defaultProcActor.setProcRole(procRole);
                                    defaultProcActor.setStrategyId(CardAuthorProcessActorStrategy.NAME);
                                    em.persist(defaultProcActor);
                                }
                                break;
                            case "Manager":
                                procRole.setRole(roleManager);
                                procRole.setSortOrder(1);
                                break;
                            case "BankOperator":
                                procRole.setRole(roleBankOperator);
                                procRole.setSortOrder(2);
                                break;
                            case "Master":
                                procRole.setRole(roleMaster);
                                procRole.setSortOrder(3);
                                break;
                        }
                        procRole.setName(messages.getMessage(getClass(), procRole.getCode()));
                    }
                    em.merge(proc);
                }
                return null;
            }
        });
        return result;
    }
}

