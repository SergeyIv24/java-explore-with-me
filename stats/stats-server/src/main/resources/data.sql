INSERT INTO apps (app_name)
SELECT 'ewm-main-service'
WHERE (SELECT COUNT(app_name) FROM apps) < 1;


--INSERT INTO apps
--VALUES (1, 'ewm-main-service')
--ON CONFLICT DO NOTHING;