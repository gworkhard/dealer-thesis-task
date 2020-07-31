-- begin DEALER_CAR
alter table DEALER_CAR add constraint FK_DEALER_CAR_CAR_MODEL_ID foreign key (CAR_MODEL_ID) references DEALER_CAR_MODEL(ID)^
alter table DEALER_CAR add constraint FK_DEALER_CAR_CARD_ID foreign key (CARD_ID) references WF_CARD(ID)^
create index IDX_DEALER_CAR_CAR_MODEL on DEALER_CAR (CAR_MODEL_ID)^
-- end DEALER_CAR
-- begin DEALER_CAR_BUY_REQUEST
alter table DEALER_CAR_BUY_REQUEST add constraint FK_DEALER_CAR_BUY_REQUEST_CAR_ID foreign key (CAR_ID) references DEALER_CAR(CARD_ID)^
alter table DEALER_CAR_BUY_REQUEST add constraint FK_DEALER_CAR_BUY_REQUEST_CUSTOMER_ID foreign key (CUSTOMER_ID) references DF_CONTRACTOR(CORRESPONDENT_ID)^
alter table DEALER_CAR_BUY_REQUEST add constraint FK_DEALER_CAR_BUY_REQUEST_BANK_ID foreign key (BANK_ID) references DF_BANK(ID)^
alter table DEALER_CAR_BUY_REQUEST add constraint FK_DEALER_CAR_BUY_REQUEST_CARD_ID foreign key (CARD_ID) references DF_DOC(CARD_ID)^
create index IDX_DEALER_CAR_BUY_REQUEST_CUSTOMER on DEALER_CAR_BUY_REQUEST (CUSTOMER_ID)^
create index IDX_DEALER_CAR_BUY_REQUEST_CAR on DEALER_CAR_BUY_REQUEST (CAR_ID)^
create index IDX_DEALER_CAR_BUY_REQUEST_BANK on DEALER_CAR_BUY_REQUEST (BANK_ID)^
-- end DEALER_CAR_BUY_REQUEST
