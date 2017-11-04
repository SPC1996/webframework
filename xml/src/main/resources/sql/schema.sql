CREATE TABLE f_staff
(
  id       INT AUTO_INCREMENT
  COMMENT '水厂业务人员ID，自增'
    PRIMARY KEY,
  username VARCHAR(45)  NULL
  COMMENT '登录账号',
  password VARCHAR(255) NULL
  COMMENT '登录密码',
  name     VARCHAR(45)  NULL
  COMMENT '姓名',
  phone    VARCHAR(11)  NULL
  COMMENT '手机号',
  level    INT          NULL
  COMMENT '基本工资级别'
);

CREATE TABLE f_salary
(
  level INT NOT NULL
  COMMENT '工资级别',
  num   INT NOT NULL
  COMMENT '金额',
  PRIMARY KEY (level, num)
);

CREATE TABLE f_role
(
  id          INT AUTO_INCREMENT
  COMMENT '角色ID，自增'
    PRIMARY KEY,
  name        VARCHAR(45)  NULL
  COMMENT '角色名称',
  description VARCHAR(255) NULL
  COMMENT '角色描述'
);

CREATE TABLE f_staff_role
(
  staff_id INT NOT NULL
  COMMENT '业务人员ID',
  role_id  INT NOT NULL
  COMMENT '角色ID',
  PRIMARY KEY (staff_id, role_id)
);

CREATE TABLE f_role_permission
(
  role_id       INT NOT NULL
  COMMENT '角色ID',
  permission_id INT NOT NULL
  COMMENT '权限ID',
  PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE f_permission
(
  id          INT AUTO_INCREMENT
  COMMENT '权限ID'
    PRIMARY KEY,
  name        VARCHAR(45)  NULL
  COMMENT '权限名称',
  description VARCHAR(255) NULL
  COMMENT '权限描述'
);

