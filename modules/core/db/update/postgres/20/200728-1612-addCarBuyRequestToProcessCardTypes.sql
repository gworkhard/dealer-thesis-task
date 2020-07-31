--Update process card_types for entity dealer$CarBuyRequest
update wf_proc set card_types = regexp_replace(card_types, E',dealer\\$CarBuyRequest', '') where code in ('Endorsement','Resolution','Acquaintance','Registration')^
update wf_proc set updated_by='admin', card_types = card_types || 'dealer$CarBuyRequest,' where code in ('Endorsement','Resolution','Acquaintance','Registration')^
