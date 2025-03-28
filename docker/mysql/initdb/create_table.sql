-- DBの作成
CREATE DATABASE IF NOT EXISTS mt_library CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS mt_library.accounts
(
    id bigint NOT NULL AUTO_INCREMENT comment 'ID',
    employee_id varchar(50) NOT NULL comment '社員番号',
    name varchar(255) NOT NULL comment '氏名',
    email varchar(255) NOT NULL comment 'メールアドレス',
    password varchar(255) NOT NULL comment 'パスワード',
    authorization_type tinyint NOT NULL DEFAULT 0 comment '権限区分',
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_employee_id (employee_id),
    PRIMARY KEY (id, employee_id),
    UNIQUE KEY uq_email (email)
) 
COMMENT = 'アカウント'
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS mt_library.book_mst
(
    id bigint NOT NULL AUTO_INCREMENT comment '書籍ID',
    isbn varchar(13) NOT NULL comment 'ISBN',
    title varchar(255) NOT NULL comment '書籍名',
    deleted_at datetime,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_isbn (isbn)
)
COMMENT = '書籍マスタ'
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
