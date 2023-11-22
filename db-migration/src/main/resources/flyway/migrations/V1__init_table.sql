CREATE TABLE IF NOT EXISTS `user`
(
  id	BIGINT AUTO_INCREMENT NOT NULL COMMENT 'ID',
  uuid CHAR(36) NOT NULL COMMENT 'UUID',
  user_name VARCHAR(1000) NOT NULL COMMENT '이름',
  email VARCHAR(1000) NULL COMMENT '이메일',
  is_enabled TINYINT(1) NOT NULL DEFAULT TRUE COMMENT '활성 여부',
  is_deleted TINYINT(1) NOT NULL DEFAULT FALSE COMMENT '삭제여부',
  created_at   datetime  null,
  updated_at   datetime(6)  null,
  PRIMARY KEY (id)
) engine = InnoDB;

