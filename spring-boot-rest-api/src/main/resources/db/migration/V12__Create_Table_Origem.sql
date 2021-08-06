CREATE TABLE `origem` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO origem(descricao) VALUES('Aquisição'),('Doação'),('Doação Apostila');