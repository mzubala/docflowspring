CREATE TABLE IF NOT EXISTS `users` (
  `id` INT NOT NULL IDENTITY,
  `username` VARCHAR(256) NULL,
  `password` VARCHAR(256) NULL,
  `employeeId` INT NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE IF NOT EXISTS `user_roles` (
  `id` INT NOT NULL IDENTITY,
  `user_id` INT NOT NULL,
  `role` VARCHAR(256) NULL,
    PRIMARY KEY (`id`),
    foreign key (user_id) references users(id)
);
