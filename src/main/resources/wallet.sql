CREATE DATABASE IF NOT EXISTS BitcoinWalletTest;

USE BitcoinWalletTest;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `wallet_info`
--

DROP TABLE IF EXISTS `wallet_info`;

CREATE TABLE `wallet_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;



--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;

CREATE TABLE `status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(90) DEFAULT NULL,
  `balance` float DEFAULT NULL,
  `transaction` varchar(90) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `wallet_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `wallet_id_idx` (`wallet_id`),
  KEY `user_id_idx` (`user_id`),
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `wallet_id` FOREIGN KEY (`wallet_id`) REFERENCES `wallet_info` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




