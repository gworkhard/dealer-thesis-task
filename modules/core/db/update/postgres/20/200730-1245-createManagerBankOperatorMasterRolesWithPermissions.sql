INSERT INTO sec_role ( ID , CREATE_TS, CREATED_BY, VERSION , NAME , LOC_NAME,DESCRIPTION) VALUES (newid(),
CURRENT_TIMESTAMP , USER , 1 , 'Manager' , 'Менеджер' , 'Роль позволяет создавать и редактировать заявки на покупку авто' )^
select create_or_update_sec_permissi( 'Manager' , 'dealer$CarBuyRequest:create' , 20 , 1 )^
select create_or_update_sec_permissi( 'Manager' , 'dealer$CarBuyRequest:update' , 20 , 1 )^
select create_or_update_sec_permissi( 'Manager' , 'dealer$CarBuyRequest:delete' , 20 , 1 )^
select create_or_update_sec_permissi( 'Manager' , 'dealer$Car:create' , 20 , 1 )^
select create_or_update_sec_permissi( 'Manager' , 'dealer$Car:update' , 20 , 1 )^
select create_or_update_sec_permissi( 'Manager' , 'dealer$Car:delete' , 20 , 1 )^

INSERT INTO sec_role ( ID , CREATE_TS, CREATED_BY, VERSION , NAME , LOC_NAME,DESCRIPTION) VALUES (newid(),
CURRENT_TIMESTAMP , USER , 1 , 'BankOperator' , 'Оператор банка' , 'Роль позволяет редактировать и удалять заявки на покупку авто' )^
select create_or_update_sec_permissi( 'BankOperator' , 'dealer$CarBuyRequest:update' , 20 , 1 )^
select create_or_update_sec_permissi( 'BankOperator' , 'dealer$CarBuyRequest:delete' , 20 , 1 )^
select create_or_update_sec_permissi( 'BankOperator' , 'dealer$Car:create' , 20 , 1 )^
select create_or_update_sec_permissi( 'BankOperator' , 'dealer$Car:update' , 20 , 1 )^
select create_or_update_sec_permissi( 'BankOperator' , 'dealer$Car:delete' , 20 , 1 )^

INSERT INTO sec_role ( ID , CREATE_TS, CREATED_BY, VERSION , NAME , LOC_NAME,DESCRIPTION) VALUES (newid(),
CURRENT_TIMESTAMP , USER , 1 , 'Master' , 'Мастер' , 'Роль позволяет редактировать и удалять заявки на покупку авто' )^
select create_or_update_sec_permissi( 'Master' , 'dealer$CarBuyRequest:update' , 20 , 1 )^
select create_or_update_sec_permissi( 'Master' , 'dealer$CarBuyRequest:delete' , 20 , 1 )^
select create_or_update_sec_permissi( 'Master' , 'dealer$Car:create' , 20 , 1 )^
select create_or_update_sec_permissi( 'Master' , 'dealer$Car:update' , 20 , 1 )^
select create_or_update_sec_permissi( 'Master' , 'dealer$Car:delete' , 20 , 1 )^