package com.luizventura.todosimple.repositories;

import java.util.List;
//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luizventura.todosimple.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

    //buscar lista de tasks de um usuário - FORMA MAIS USADA ATUALMENTE:
    List<Task> findByUser_Id(Long id); //dessa forma findByUser_Id é a forma padrão da função no spring, não pode ser diferente

    //com SQL puro:
    // @Query(value = "SELECT * FROM task t WHERE t.user_id = :id", nativeQuery = true)
    // List<Task> findByUserIdList(@Param("id") Long id); 

    
    //  Outras formas: 
    // @Query(value = "SELECT t FROM Task t WHERE t.user.id = :id") 
    // List<Task> findByUserId(@Param("id") Long id); //- o nome da função pode ser qualquer um. O ruim dessa forma é que utiliza muitas notações no value

    //Optional <Task> findById(Long id); - Optional é uma forma de tratar alguma coisa que é nula. Para não dar erro de poinerException - se não tiver, coloca como vazio. Mas não precisamos declarar pois já existe no JPA.
    
}
