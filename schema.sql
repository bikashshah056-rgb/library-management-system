CREATE TABLE books (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       title TEXT NOT NULL,
                       author TEXT NOT NULL,
                       isbn TEXT UNIQUE,
                       category TEXT,
                       total_copies INTEGER,
                       available_copies INTEGER
);

CREATE TABLE members (
                         id INTEGER PRIMARY KEY AUTOINCREMENT,
                         name TEXT NOT NULL,
                         email TEXT,
                         phone TEXT
);

CREATE TABLE borrow_records (
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                book_id INTEGER,
                                member_id INTEGER,
                                borrow_date TEXT,
                                due_date TEXT,
                                return_date TEXT,
                                FOREIGN KEY (book_id) REFERENCES books(id),
                                FOREIGN KEY (member_id) REFERENCES members(id)
);