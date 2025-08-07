CREATE TABLE `category` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '識別子',
  `name` varchar(32) NOT NULL COMMENT 'カテゴリ名',
  `slug` varchar(32) NOT NULL COMMENT 'カテゴリの俗名',
  `color` varchar(8) NOT NULL COMMENT 'カテゴリの色',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'データ更新日',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'データ作成日',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `to_do` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '識別子',
  `category_id` bigint unsigned DEFAULT NULL COMMENT 'カテゴリID (category.id)',
  `title` varchar(255) NOT NULL COMMENT 'ToDoのタイトル',
  `body` text COMMENT 'ToDoの内容',
  `state` smallint NOT NULL COMMENT 'ToDoの状態 (1: Todo, 2: 進行中, 3: 完了)',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'データ更新日',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'データ作成日',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;
