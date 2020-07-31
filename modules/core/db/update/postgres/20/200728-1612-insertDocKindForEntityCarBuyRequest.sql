--Add default doc kind for dealer$CarBuyRequest
CREATE OR REPLACE FUNCTION baseInsert()
RETURNS integer
AS $$
DECLARE
cnt integer = 0;
BEGIN
cnt = (select count(CATEGORY_ID) from DF_DOC_KIND where category_id = '77267a94-17ec-460a-b9ea-f16d5a858af6');
if(cnt = 0) then
    insert into SYS_CATEGORY (ID, NAME, ENTITY_TYPE, IS_DEFAULT, CREATE_TS, CREATED_BY, VERSION, DISCRIMINATOR)
    values ( '77267a94-17ec-460a-b9ea-f16d5a858af6', 'Заявка на покупку автомобиля', 'dealer$CarBuyRequest', false, now(), USER, 1, 1);

    insert into DF_DOC_KIND (category_id, create_ts, created_by, version, doc_type_id, numerator_id, 
    numerator_type, category_attrs_place, tab_name, portal_publish_allowed, disable_add_process_actors, create_only_by_template)
    values ('77267a94-17ec-460a-b9ea-f16d5a858af6', 'now()', 'admin', 1, '473e7bf9-f5c9-4045-9f1f-240c22a8f7d8', '06accaa9-373a-4220-b9f7-53e40ab88c7c', 
    1, 1, 'Ð”Ð¾Ð¿. Ð¿Ð¾Ð»Ñ�', false, false, false);
end if;return 0;

END;
$$
LANGUAGE plpgsql;
^
select baseInsert();
^
drop function if exists baseInsert()^
