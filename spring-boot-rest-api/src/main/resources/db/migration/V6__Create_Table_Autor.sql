CREATE TABLE `autor` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(255) NOT NULL,
  `sobrenome` VARCHAR(255) NOT NULL,
  `info` TEXT,
  PRIMARY KEY (`id`)
)Engine=InnoDB;