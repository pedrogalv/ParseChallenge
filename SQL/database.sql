CREATE SCHEMA wallethub;

CREATE TABLE `access_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `browser` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `http_status` int(11) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `request_method` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `query_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `block_description` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;