INSERT INTO `category` (`name`,`slug`,`color`) values ('フロントエンド','front','#ff7f50');
INSERT INTO `category` (`name`,`slug`,`color`) values ('バックエンド','back','#87ceeb');
INSERT INTO `category` (`name`,`slug`,`color`) values ('インフラ','infra','#c0c0c0');

INSERT INTO `to_do` (`category_id`,`title`,`body`,`state`) values (1,'デザインをいい感じにする','ヘッダーのデザインをもっといい感じに',1);
INSERT INTO `to_do` (`category_id`,`title`,`body`,`state`) values (2,'Controllerの修正','Controller名をもっといい感じに',2);
INSERT INTO `to_do` (`category_id`,`title`,`body`,`state`) values (3,'新しいDB環境の作成','タイトル通り',3);
