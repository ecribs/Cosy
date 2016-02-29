Create Table Classes 
(
	ClassID VARCHAR(10) Primary Key NOT NULL,
	Num_Students INT NOT NULL
);


Create Table Users 
(
	Username VARCHAR(20) Primary key NOT NULL,
	Password VARCHAR(10) NOT NULL,
	ClassID VARCHAR(10) FOREIGN KEY REFERENCES Classes(ClassID) NOT NULL,
	Role VARCHAR(10) NOT NULL
);

Create TABLE Subjects
(
	SubjectID INT PRIMARY KEY NOT NULL IDENTITY(1,1),
	SubjectName VARCHAR(20) NOT NULL,
	ClassID VARCHAR(10) FOREIGN KEY REFERENCES Classes(ClassID) NOT NULL
);

Create TABLE Books
( 
	Bookname VARCHAR(100) PRIMARY KEY,
	subjectID INT FOREIGN KEY REFERENCES Subjects(SubjectID)
);

Create TABLE Topic
( 
	TopicID INT PRIMARY KEY NOT NULL IDENTITY(1,1),
	TopicName VARCHAR(100),
	SubjectID INT FOREIGN KEY REFERENCES Subjects(SubjectID)
); 

Create Table Worksheet
(
WorksheetName varchar(100) PRIMARY KEY,
TopicID INT Foreign KEY References Topic(TopicID),
Num_Q INT NOT NULL,
W_Type varchar(30)
);

Create Table Worksheet
(
WorksheetID int PRIMARY KEY IDENTITY(1,1),
WorksheetName varchar(100),
TopicID INT Foreign KEY References Topic(TopicID),
Num_Q INT NOT NULL,
W_Type varchar(30),
W_Date Date
);

Create TABLE Worksheet_Questions
(
	WorksheetID INT  NOT NULL Foreign KEY References Worksheet(WorksheetID),
	Question_Num INT NOT NULL,
	Question VARCHAR(200) NOT NULL,
	Answer VARCHAR(200),
	PRIMARY KEY (WorksheetID, Question_Num)
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


insert into Subjects values('Irish', '1a');
insert into Subjects values('Maths', '1a');
insert into Subjects values('English', '1a');
insert into Subjects values('Science', '1a');
insert into Subjects values('Religion', '1a');
insert into Subjects values('Geography', '1a');
insert into Subjects values('Irish', '2a');
insert into Subjects values('Maths', '2a');
insert into Subjects values('English', '2a');
insert into Subjects values('Science', '2a');
insert into Subjects values('Religion', '2a');
insert into Subjects values('Geography', '2a');


insert into Books values( 'Bun Go Barr 1', 1);
insert into Books values( 'Planet Maths', 2);
insert into Books values( 'All Write Now', 3);
insert into Books values( 'Small World', 4 );
insert into Books values( 'Alive-O', 5);
insert into Books values( 'Bun Go Barr 2', 7);
insert into Books values( 'What A Wonderful World 1',6);


insert into Topic values( 'Algebra', 2);
insert into Topic values( 'shapes', 2);
insert into Topic values( 'Addition', 2);
insert into Topic values( 'Subtraction', 2);
insert into Topic values( 'Countires', 6);
insert into Topic values( 'Cities', 6);
insert into Topic values( 'Languages', 6);
insert into Topic values( 'Rocks', 6);
insert into Topic values( 'Eadai', 1);
insert into Topic values( 'Mo Ceantar', 1);
insert into Topic values( 'Caitheamh Aimsire', 1);
insert into Topic values( 'Pronunciation', 3);
insert into Topic values( 'cursive writing', 3);
insert into Topic values( 'Spelling', 3);
insert into Topic values( 'Reading', 3);
insert into Topic values( 'Experiments', 4);
insert into Topic values( 'Chemicals', 4);
insert into Topic values( 'Electricity', 4);
insert into Topic values( 'Biology', 4);
insert into Topic values( 'Temperature', 4);
insert into Topic values( 'Water', 4);
insert into Topic values( 'Catholism', 5);
insert into Topic values( 'Islam', 5);
insert into Topic values( 'Morals', 5);
insert into Topic values( 'Meditation', 5);
insert into Topic values( 'Ethics', 5);








