--database: ../DataBase/matches.sqlite
DROP TABLE IF EXISTS matches;

CREATE TABLE matches (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    player1 TEXT NOT NULL,
    player2 TEXT NOT NULL,
    player1_score INTEGER NOT NULL,
    player2_score INTEGER NOT NULL,
    duration_seconds INTEGER NOT NULL,
    date DATETIME DEFAULT (datetime('now', '-5 hours'))
);
