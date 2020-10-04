CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT NOT NULL IDENTITY,
  `username` VARCHAR(256) NULL,
  `password` VARCHAR(256) NULL,
  `employee_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE IF NOT EXISTS `user_roles` (
  `id` BIGINT NOT NULL IDENTITY,
  `user_id` BIGINT NOT NULL,
  `role` VARCHAR(256) NULL,
    PRIMARY KEY (`id`),
    foreign key (user_id) references users(id)
);
