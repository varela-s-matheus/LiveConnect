# LiveConnect - WebChat

## Technologies Used
This project uses the Java programming language with the SpringBoot framework and SpringData JPA for communication with the PostgreSQL database. 
It also uses Spring Security, JWT for authentication, as well as Spring WebSocket. 
The project proposes communication with the front-end through a REST API and WebSocket for message exchange.


## MVC Pattern
The project uses the MVC (Model-View-Controller) pattern, focusing on organization and separation of responsibilities.


## PostgreSQL Database Configuration

* Install PostgreSQL on your machine if it is not already installed.
* Create a database for the application using the following command:
```
CREATE DATABASE live_connect;
```
Create the database entities and their relationships:
```
--Users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

-- Chat groups table
CREATE TABLE chat_groups (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    creation_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Messages table
CREATE TABLE messages (
    id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    sender_user_id INT NOT NULL,
    chat_group_id INT NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_user_id) REFERENCES users (id),
    FOREIGN KEY (chat_group_id) REFERENCES chat_groups (id)
);

-- Chat group participants table
CREATE TABLE chat_group_participants (
    id SERIAL PRIMARY KEY,
    chat_group_id INT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (chat_group_id) REFERENCES chat_groups (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Indexes for messages
CREATE INDEX idx_message_chat_group_id ON messages (chat_group_id);
CREATE INDEX idx_message_sender_user_id ON messages (sender_user_id);
CREATE INDEX idx_message_timestamp ON messages (timestamp);

-- Indexes for group participants
CREATE INDEX idx_chat_group_participants_group_id ON chat_group_participants (chat_group_id);
CREATE INDEX idx_chat_group_participants_user_id ON chat_group_participants (user_id);

```

It is very important to check the environment variables to establish the connection with the database in <strong>application.properties</strong>.


## Contacts

<a href="https://linkedin.com/in/varela-s-matheus" target="_blank">
  <img align="center" src="https://img.shields.io/badge/-MatheusVarela-05122A?style=flat&logo=linkedin" alt="linkedin"/>
</a>
<a href="https://www.instagram.com/varela_matheuus/" target="_blank">
 <img align="center" src="https://img.shields.io/badge/-MatheusVarela-05122A?style=flat&logo=instagram" alt="instagram"/>
</a>
