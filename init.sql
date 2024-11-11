CREATE TABLE IF NOT EXISTS tasks (
                                     id SERIAL PRIMARY KEY,
                                     completed BOOLEAN ,
                                     completed_date DATE,
                                     creation_date DATE ,
                                     description TEXT,
                                     limit_date DATE ,
                                     priority VARCHAR(10) ,
    reward_level INT,
    title VARCHAR(255)
    );

INSERT INTO tasks (completed,completed_date,creation_date,description,limit_date,priority,reward_level,title) VALUES
                                                                                                                  (0,NULL,'2024-10-20','Paginas 12 ','2024-10-22','HIGH',1,'Leer Libro'),
                                                                                                                  (1,'2024-10-20','2024-10-20','Paginas 11222 ','2024-10-22','HIGH',2,'Hacer Tarea'),
                                                                                                                  (0,NULL,NULL,'','2024-10-21','MEDIUM',2,'Something'),
                                                                                                                  (1,'2024-10-23','2024-10-23','ssss','2024-10-24','MEDIUM',1,'Tas'),
                                                                                                                  (1,NULL,'2024-10-23','Si','2024-10-30','MEDIUM',4,'Estudiar '),
                                                                                                                  (0,NULL,'2024-10-23','Hola','2024-10-30','MEDIUM',1,'Hola'),
                                                                                                                  (0,NULL,'2024-10-23','','2024-10-24','HIGH',1,'Si');