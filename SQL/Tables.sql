
Create Table Classes 
(
	ClassID VARCHAR(10) Primary Key,
	Num_Students INT

);

Create Table Users 
(
	Username VARCHAR(20) Primary key,
	Password VARCHAR(10),
	ClassID VARCHAR(10) FOREIGN KEY REFERENCES Classes(ClassID),
	Role VARCHAR(10) 
);


Create TABLE Subjects
(
	SubjectID INT PRIMARY KEY,
	SubjectName VARCHAR(20)
);

Create TABLE Books
( 
	Bookname VARCHAR(20) PRIMARY KEY,
	subjectID INT FOREIGN KEY REFERENCES Subjects(SubjectID),
	ClassID VARCHAR(10) FOREIGN KEY REFERENCES Classes(ClassID)

);


 

insert into Classes values('1a', 20);
insert into Classes values('1b', 30);
insert into Classes values('2a', 25);
insert into Classes values('3a', 22);
insert into Classes values('2b', 21);


insert into users values( 'bob', 'bob', '1a', 'Student');
insert into users values('joe', 'bloggs', '1a', 'Student');
insert into users values('eoin', 'eoin', '1a', 'Teacher');
insert into users values( 'jlynch', 'abcd', '1a', 'Student');
insert into users values( 'dcaroll', '123456', '1a', 'Student');

insert into Subjects values(1, 'Irish');
insert into Subjects values(2, 'Maths');
insert into Subjects values(3, 'English');
insert into Subjects values(4, 'Science');
insert into Subjects values(5, 'Religion');

insert into Books values( 'Bun Go Barr 1', 1, '1a');
insert into Books values( 'Planet Maths', 2, '1a');
insert into Books values( 'All Write Now ', 3, '1a');
insert into Books values( 'Small World', 4, '1a');
insert into Books values( 'Alive-O', 5, '1a');
insert into Books values( 'Bun Go Barr 2', 1, '2a');




select ClassID from users where username = 'eoin';
 
select Bookname, SubjectName from books 
join Subjects on Subjects.SubjectID = Books.subjectID where classID='1a';










