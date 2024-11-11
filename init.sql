CREATE TABLE IF NOT EXISTS tasks (
                                     id SERIAL PRIMARY KEY,
                                     completed BOOLEAN NOT NULL,
                                     completed_date DATE,
                                     creation_date DATE NOT NULL,
                                     description TEXT,
                                     limit_date DATE NOT NULL,
                                     priority VARCHAR(10) NOT NULL,
    reward_level INT NOT NULL,
    title VARCHAR(255) NOT NULL
    );

INSERT INTO tasks (completed,completed_date,creation_date,description,limit_date,priority,reward_level,title) VALUES
                                                                                                                  (0,'','2024-10-20','Paginas 12 ','2024-10-22','HIGH',1,'Leer Libro'),
                                                                                                                  (1,'2024-10-20','2024-10-20','Paginas 11222 ','2024-10-22','HIGH',2,'Hacer Tarea'),
                                                                                                                  (0,'','','','2024-10-21','MEDIUM',2,'Something'),
                                                                                                                  (1,'2024-10-23','2024-10-23','ssss','2024-10-24','MEDIUM',1,'Tas'),
                                                                                                                  (1,'','2024-10-23','Si','2024-10-30','MEDIUM',4,'Estudiar '),
                                                                                                                  (0,'','2024-10-23','Hola','2024-10-30','MEDIUM',1,'Hola'),
                                                                                                                  (0,'','2024-10-23','','2024-10-24','HIGH',1,'Si');