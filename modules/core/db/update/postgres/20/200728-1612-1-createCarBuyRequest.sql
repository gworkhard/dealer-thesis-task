create table DEALER_CAR_BUY_REQUEST (
    CARD_ID uuid,
    --
    CAR_ID uuid,
    CUSTOMER_ID uuid not null,
    BANK_ID uuid not null,
    IS_PAID boolean,
    --
    primary key (CARD_ID)
);
