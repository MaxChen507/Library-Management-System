-- CREATE VIEWS:

-- V1) 
	DROP VIEW View_isbn_title_listAuthors;
	CREATE VIEW View_isbn_title_listAuthors AS
-- (This query displays Isbn, Title, and a list of all Authors that contributed)
	(SELECT T_i_t_al.Isbn, T_i_t_al.Title, GROUP_CONCAT(T_i_t_al.Name SEPARATOR ', ') AS Author_List
	FROM  
		(
			-- (This subquery displays Isbn, Title, and authors that contributed)
			SELECT BOOK.Isbn, BOOK.Title, AUTHORS.Name
			FROM BOOK, BOOK_AUTHORS, AUTHORS
			WHERE 
				BOOK.Isbn = BOOK_AUTHORS.Isbn 
				AND 
				AUTHORS.Author_ID = BOOK_AUTHORS.Author_ID
		) AS T_i_t_al
	GROUP BY T_i_t_al.Isbn
	);
	
-- V2)
	DROP VIEW View_books_unavailable;
	CREATE VIEW View_books_unavailable AS
-- (This query displays all of the books that are unavailable)
	SELECT DISTINCT View_isbn_title_listAuthors.Isbn AS Unavailable_Isbn
	FROM View_isbn_title_listAuthors
	LEFT JOIN BOOK_LOANS ON BOOK_LOANS.Isbn = View_isbn_title_listAuthors.Isbn
	WHERE 
		(BOOK_LOANS.Date_out IS NOT NULL AND BOOK_LOANS.Date_in IS NULL);
		
-- V3)
	DROP VIEW View_books_available;
	CREATE VIEW View_books_available AS
-- (This query displays all of the isbn's of the books that are available)
	SELECT View_books_may_available.Isbn AS Available_Isbn
	FROM
		(
			-- (This subquery displays all of the books that may be available)
			SELECT DISTINCT View_isbn_title_listAuthors.Isbn
			FROM View_isbn_title_listAuthors
			LEFT JOIN BOOK_LOANS ON BOOK_LOANS.Isbn = View_isbn_title_listAuthors.Isbn
			WHERE 
				(BOOK_LOANS.Date_out IS NOT NULL AND BOOK_LOANS.Date_in IS NOT NULL)
				OR
				(BOOK_LOANS.Date_out IS NULL AND BOOK_LOANS.Date_in IS NULL)
		) AS View_books_may_available
	LEFT JOIN 
		(
			View_books_unavailable
		)
	ON View_books_unavailable.Unavailable_Isbn = View_books_may_available.Isbn
	WHERE View_books_unavailable.Unavailable_Isbn IS NULL;
	
	
	
	