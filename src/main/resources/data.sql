MERGE INTO rating(id, name) VALUES (1, 'G');
MERGE INTO rating(id, name) VALUES (2, 'PG');
MERGE INTO rating(id, name) VALUES (3, 'PG-13');
MERGE INTO rating(id, name) VALUES (4, 'R');
MERGE INTO rating(id, name) VALUES (5, 'NC-17');

MERGE INTO genre(id, name) VALUES (1, 'Comedy');
MERGE INTO genre(id, name) VALUES (2, 'Drama');
MERGE INTO genre(id, name) VALUES (3, 'Cartoon');
MERGE INTO genre(id, name) VALUES (4, 'Thriller');
MERGE INTO genre(id, name) VALUES (5, 'Documentary');
MERGE INTO genre(id, name) VALUES (6, 'Action');