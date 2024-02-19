package com.luizventura.todosimple.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luizventura.todosimple.models.User;

//Long é o tipo do atributo id de User
//extends CrudRepository - está dentro de JPA. Control + Click em JPARepository para ver+
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    //utilizamos as funções do repository no service.
    //User findByUsername(String username); - Está comentado porque não precisamos implementar nada, já tem tudo dentro do JPA
}
