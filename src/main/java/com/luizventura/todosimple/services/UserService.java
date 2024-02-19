package com.luizventura.todosimple.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luizventura.todosimple.models.User;
import com.luizventura.todosimple.repositories.TaskRepository;
import com.luizventura.todosimple.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired //construtor
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;
    //Não usamos get e set aqui, apenas Construtores para podermos instanciar os atributos.
    

    // public UserService() {
    //     this.UserRepository = new UserRepository(); // o new NÃO FUNCIONA porque o Repository é uma INTERFACE, o certo é com @Autowired
    //     this.TaskRepository = new TaskRepository();
    // }
    
    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id); //JpaRepository-PagingAndSortingRepository-CrudRepository-FindById
        user.orElseThrow(() -> new RuntimeException(){
            "Usuário não encontrado! Id: "+ id +", Tipo: "+ User.class.getName()
        }); //() -> new Exception(); - essa arrowfunction não pode ser usada porque a Exception padrão para a aplicação.
    }

    @Transactional //do spring - utilizar sempre que for persistir algo (create or update)) no banco
    public User create(User obj) {
        obj.setId(id: null); //para garantir que está criando um novo objeto/user, caso um usuário malicioso tentar alterar o banco.
        obj = this.userRepository.save(obj);
        this.taskRepository.saveAll(obj.getTasks()); //caso já tenha criado usuário com tasks, apesar de não ser comum.
        return obj;
    }

    @Transactional
    public User update(User obj) {
        User validatedUser = findById(obj.getId()); //para garantir que o usuário existe
        validatedUser.setPassword(obj.getPassword());
        return this.userRepository.save(validatedUser);
    }

    public void delete(Long id) {
        findById(id);
        try{
            this.userRepository.deleteById(id);
        }catch(Exception e){
            throw new RuntimeException("Não é possível excluir pois há entidades relacionadas."); //como o usuário tem tasks não faz sentido deletar as tasks para deletar os usuários. O mais recomendado seria um delete lógico, apenas desativando do banco o User ao invés de apagar.
        }
    }
}
