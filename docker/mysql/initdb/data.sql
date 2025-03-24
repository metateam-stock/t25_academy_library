-- 外部キー制約を一時的に無効化
SET foreign_key_checks = 0;

-- 初期データを作成
-- メタ太郎（pw: admin）
INSERT INTO mt_library.accounts
    (id, employee_id, name, email, password, authorization_type)
VALUES
    (1, '000001', '管理太郎', 'admin@example.com', '$2a$10$sI.U8nPC19YYlHo60B./ku/k411YndrvU2Rl65Pvs7FiZDNr0t04i', 1);

INSERT INTO mt_library.book_mst
    (id, isbn, title, deleted_at, created_at, updated_at)
VALUES
    (1, '9784295017936', 'スッキリわかるJava入門 第4版', NULL, '2024-03-01 10:00:00', '2024-03-01 11:00:00'),
    (2, '9784295015574', 'かんたん合格 ITパスポート教科書&必須問題 令和5年度', NULL, '2024-03-01 10:10:00', '2024-03-01 11:10:00'),
    (3, '9784284204705', '13歳から分かる！7つの習慣', NULL, '2024-03-02 10:20:00', '2024-03-05 10:00:00'),
    (4, '9784594030193', 'チーズはどこへ消えた？', NULL, '2024-03-03 10:00:00', '2024-03-03 12:00:00'),
    (5, '9784845263424', '高業績チームはここが違う', NULL, '2024-03-03 11:00:00', '2024-03-03 12:15:00');


-- 外部キー制約を有効化
SET foreign_key_checks = 1;