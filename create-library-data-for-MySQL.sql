/*
 *    File name: create-library-data-for-MySQL.sql
 *       Author: Max Chen
 *    Last Edit: 2018-07-05
 *  Description: This file is based on the SQL Library Project (2018)
 */
 
-- If the database "LIBRARY" already exists, then delete it.
DROP DATABASE IF EXISTS LIBRARY;
-- Create the Database "LIBRARY"
CREATE DATABASE LIBRARY;


-- Set the currently active database to be "LIBRARY"
USE LIBRARY;

-- Start creating all the tables--

DROP TABLE IF EXISTS BOOK;
CREATE TABLE BOOK (
  Isbn		BIGINT NOT NULL,
  Title		VARCHAR(255) NOT NULL,
  CONSTRAINT pk_book PRIMARY KEY (Isbn)
);

DROP TABLE IF EXISTS AUTHORS;
CREATE TABLE AUTHORS (
  Author_id		INT NOT NULL AUTO_INCREMENT,
  Name			VARCHAR(255) NOT NULL,
  CONSTRAINT pk_authors PRIMARY KEY (Author_id),
  CONSTRAINT uk_authors_name UNIQUE (Name)
);

DROP TABLE IF EXISTS BOOK_AUTHORS;
CREATE TABLE BOOK_AUTHORS (
  Author_id		INT NOT NULL,
  Isbn			BIGINT NOT NULL,
  CONSTRAINT pk_bookauthors PRIMARY KEY (Author_id, Isbn),
  CONSTRAINT fk_bookauthors_authors FOREIGN KEY (Author_id) references AUTHORS(Author_id),
  CONSTRAINT fk_bookauthors_book FOREIGN KEY (Isbn) references BOOK(Isbn)
);

-- Used the Heidi tool to import my tab delminated raw books data into LIBRARY database --
-- Using: "Books_Sheet_Clean.txt" from the "Processing Data" folder
-- Using a new created table:

DROP TABLE IF EXISTS BOOK_RAW;
CREATE TABLE BOOK_RAW (
	ISBN13 		BIGINT NOT NULL,
	Title 		VARCHAR(255) NOT NULL,
	Author1		VARCHAR(255) NOT NULL,
	Author2 	VARCHAR(255) NULL,
	Author3 	VARCHAR(255) NULL,
	Author4 	VARCHAR(255) NULL,
	Author5 	VARCHAR(255) NULL
);

-- Used the Heidi tool to import my comma delminated raw borrowers data into LIBRARY database --
-- Using: "Borrowers_Sheet_Clean.txt" from the "Processing Data" folder
-- Using a new created table:

DROP TABLE IF EXISTS BORROWER;
CREATE TABLE BORROWER (
	Card_id		INT NOT NULL AUTO_INCREMENT,
	Ssn			VARCHAR(255) NOT NULL,
	Bname		VARCHAR(255) NOT NULL,
	Address		VARCHAR(255) NOT NULL,
	Phone		VARCHAR(255),
	CONSTRAINT pk_borrower PRIMARY KEY (Card_id),
	CONSTRAINT uk_borrower_ssn UNIQUE (Ssn)
);

DROP TABLE IF EXISTS BOOK_LOANS;
CREATE TABLE BOOK_LOANS (
	Loan_id		INT NOT NULL AUTO_INCREMENT,
	Isbn		BIGINT NOT NULL,
	Card_id		INT NOT NULL,
	Date_out	DATE,
	Due_date	DATE,
	Date_in		DATE,
	CONSTRAINT pk_bookloans PRIMARY KEY (Loan_id),
	CONSTRAINT fk_bookloans_book FOREIGN KEY (Isbn) references BOOK(Isbn),
	CONSTRAINT fk_bookloans_borrower FOREIGN KEY (Card_id) references BORROWER(Card_id)
);

DROP TABLE IF EXISTS FINES;
CREATE TABLE FINES (
	Loan_id		INT NOT NULL,
	Fine_amt	DECIMAL(10,2),
	Paid		BOOLEAN,
	CONSTRAINT pk_fines PRIMARY KEY (Loan_id),
	CONSTRAINT fk_fines_bookloans FOREIGN KEY (Loan_id) references BOOK_LOANS(Loan_id)
);
	

-- Start populating all the tables--
	
-- Populate BOOK_RAW using Heidi tool--
-- 		*E:\UTD\UTD Courses\2018 Summer\SQL Library Project\Processing Data\Books_Sheet_Clean.txt

-- Populate BOOK using BOOK_RAW
INSERT INTO BOOK (Isbn, Title)
SELECT ISBN13, Title
FROM BOOK_RAW;

-- Populate AUTHORS using BOOK_RAW
INSERT INTO AUTHORS (Name)
SELECT Author1 
FROM (
	SELECT Author1 FROM BOOK_RAW WHERE Author1 IS NOT NULL
	UNION
	SELECT Author2 FROM BOOK_RAW WHERE Author2 IS NOT NULL
	UNION
	SELECT Author3 FROM BOOK_RAW WHERE Author3 IS NOT NULL
	UNION
	SELECT Author4 FROM BOOK_RAW WHERE Author4 IS NOT NULL
	UNION
	SELECT Author5 FROM BOOK_RAW WHERE Author5 IS NOT NULL
	) AS T1;

-- Populate BOOK_AUTHORS
INSERT INTO BOOK_AUTHORS (Author_id, Isbn)
Select Author_id, ISBN13 
FROM (
	SELECT ISBN13, Author1 FROM BOOK_RAW WHERE Author1 IS NOT NULL
	UNION
	SELECT ISBN13, Author2 FROM BOOK_RAW WHERE Author2 IS NOT NULL
	UNION
	SELECT ISBN13, Author3 FROM BOOK_RAW WHERE Author3 IS NOT NULL
	UNION
	SELECT ISBN13, Author4 FROM BOOK_RAW WHERE Author4 IS NOT NULL
	UNION
	SELECT ISBN13, Author5 FROM BOOK_RAW WHERE Author5 IS NOT NULL
	) AS T1, AUTHORS
WHERE	T1.Author1 = AUTHORS.Name;

-- Populate BORROWER using Heidi tool-- (DON'T FORGET TO UNCHECK THE CARD_ID column when importing)
-- 		*E:\UTD\UTD Courses\2018 Summer\SQL Library Project\Processing Data\Borrowers_Sheet_Clean.txt 
	
		
	





