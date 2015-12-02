create table studyTest(
	id int(4) not null primary key auto_increment,
	name VARCHAR(20) null DEFAULT '',
	price decimal(20,2) null DEFAULT 0.00,
	num int(4) null DEFAULT 0,
	ts  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
)



