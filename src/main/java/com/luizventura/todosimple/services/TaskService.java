package com.luizventura.todosimple.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.luizventura.todosimple.models.Task;
import com.luizventura.todosimple.models.User;
import com.luizventura.todosimple.repositories.TaskRepository;

public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired //precisa instanciar a classe e seus métodos
    private UserService userService;

    public Task findById(Long id) {
        Optional<Task> task = new this.taskRepository.findById(id);
        return task.orElseThrow(() -> new RuntimeException(
            "Tarefa não encontrada! Id "+ id +", Tipo: "+ Task.class.getName()
        ));
    }

    @Transactional
    public Task create(Task obj) {
        User user = this.userService.findById(obj.getUser().getId()); //existe um User instanciado dentro do model Task
        obj.setId(id: null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj) {
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return this.taskRepository.save(newObj);
    }

    public void delete(Long id) {
        //Task task = findById(id);
        findById(id);
        try {
            // this.taskRepository.delete(id);
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Não é possível excluir pois há entidades relacionadas!");//caso se relacione com outra entidade futuramente
        }
    }
}
