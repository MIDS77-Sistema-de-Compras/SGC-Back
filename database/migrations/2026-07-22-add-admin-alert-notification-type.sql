DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conrelid = 'notification'::regclass
          AND conname = 'notification_notification_type_check'
    ) THEN
        ALTER TABLE notification
            DROP CONSTRAINT notification_notification_type_check;
    END IF;

    ALTER TABLE notification
        ADD CONSTRAINT notification_notification_type_check
        CHECK (notification_type IN (
            'STATUS_ALTERADO',
            'ITEM_PARA_RETIRADA',
            'ENTREGA_CRIADA',
            'SOLICITACAO_VINCULADA_CR',
            'NOTIFICACAO_TESTE',
            'ALERTA_ADMINISTRATIVO'
        ));
END $$;
