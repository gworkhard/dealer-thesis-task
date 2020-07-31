alter table DEALER_CAR add constraint FK_DEALER_CAR_CAR_MODEL_ID foreign key (CAR_MODEL_ID) references DEALER_CAR_MODEL(ID);
alter table DEALER_CAR add constraint FK_DEALER_CAR_CARD_ID foreign key (CARD_ID) references WF_CARD(ID);
create index IDX_DEALER_CAR_CAR_MODEL on DEALER_CAR (CAR_MODEL_ID);
