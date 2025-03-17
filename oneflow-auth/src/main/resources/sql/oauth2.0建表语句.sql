/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 8.0.16 : Database - security_authority
*********************************************************************

OAuth2.0中表结构以及字段说明：https://blog.csdn.net/xiangjunyes/article/details/121582654
*/

/*Table structure for table `oauth_access_token` */

DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token`
(
    `token_id`          varchar(255) DEFAULT NULL,
    `token`             longblob,
    `authentication_id` varchar(255) DEFAULT NULL,
    `user_name`         varchar(255) DEFAULT NULL,
    `client_id`         varchar(255) DEFAULT NULL,
    `authentication`    longblob,
    `refresh_token`     varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `oauth_access_token` */

/*Table structure for table `oauth_approvals` */

DROP TABLE IF EXISTS `oauth_approvals`;
CREATE TABLE `oauth_approvals`
(
    `userId`         varchar(255) DEFAULT NULL,
    `clientId`       varchar(255) DEFAULT NULL,
    `scope`          varchar(255) DEFAULT NULL,
    `status`         varchar(10)  DEFAULT NULL,
    `expiresAt`      datetime     DEFAULT NULL,
    `lastModifiedAt` datetime     DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `oauth_approvals` */

/*Table structure for table `oauth_client_details` */

DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`
(
    `client_id`               varchar(255) NOT NULL,
    `resource_ids`            varchar(255) DEFAULT NULL,
    `client_secret`           varchar(255) DEFAULT NULL,
    `scope`                   varchar(255) DEFAULT NULL,
    `authorized_grant_types`  varchar(255) DEFAULT NULL,
    `web_server_redirect_uri` varchar(255) DEFAULT NULL,
    `authorities`             varchar(255) DEFAULT NULL,
    `access_token_validity`   int(11) DEFAULT NULL,
    `refresh_token_validity`  int(11) DEFAULT NULL,
    `additional_information`  varchar(255) DEFAULT NULL,
    `autoapprove`             varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `oauth_client_details` */

/*Table structure for table `oauth_client_token` */

DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE `oauth_client_token`
(
    `token_id`          varchar(255) DEFAULT NULL,
    `token`             longblob,
    `authentication_id` varchar(255) DEFAULT NULL,
    `user_name`         varchar(255) DEFAULT NULL,
    `client_id`         varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `oauth_client_token` */

/*Table structure for table `oauth_code` */

DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code`
(
    `code`           varchar(255) DEFAULT NULL,
    `authentication` varbinary(2550) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `oauth_code` */

/*Table structure for table `oauth_refresh_token` */

DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token`
(
    `token_id`       varchar(255) DEFAULT NULL,
    `token`          longblob,
    `authentication` longblob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `oauth_refresh_token` */



