-- Study Cafe Reservation schema (MySQL)

CREATE TABLE IF NOT EXISTS User (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    tel      VARCHAR(20)  NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Room (
    id        INT PRIMARY KEY,
    room_size INT NOT NULL,
    price     INT NOT NULL
);

CREATE TABLE IF NOT EXISTS Room_history (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    room_id    INT      NOT NULL,
    user_id    INT      NOT NULL,
    start_time DATETIME NOT NULL,
    end_time   DATETIME NOT NULL,
    user_count INT      NOT NULL,
    CONSTRAINT fk_rh_room FOREIGN KEY (room_id) REFERENCES Room(id),
    CONSTRAINT fk_rh_user FOREIGN KEY (user_id) REFERENCES User(id)
);

CREATE TABLE IF NOT EXISTS payment_history (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT      NOT NULL,
    room_id         INT      NOT NULL,
    price           INT      NOT NULL,
    payment_date    DATETIME NOT NULL,
    room_history_id INT      NOT NULL,
    CONSTRAINT fk_ph_user FOREIGN KEY (user_id) REFERENCES User(id),
    CONSTRAINT fk_ph_room FOREIGN KEY (room_id) REFERENCES Room(id),
    CONSTRAINT fk_ph_rh   FOREIGN KEY (room_history_id) REFERENCES Room_history(id)
);
