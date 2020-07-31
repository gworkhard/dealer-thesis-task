create table DEALER_CAR (
    CARD_ID uuid,
    --
    NUMBER_ varchar(50),
    CAR_MODEL_ID uuid not null,
    NAME varchar(50) not null,
    MANUFACTURE_YEAR integer,
    PRICE decimal(19, 2),
    --
    primary key (CARD_ID)
);
