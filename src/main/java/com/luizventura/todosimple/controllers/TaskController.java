package com.luizventura.todosimple.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.luizventura.todosimple.models.Task;
import com.luizventura.todosimple.services.TaskService;
import com.luizventura.todosimple.services.UserService;

@RestController
@RequestMapping("/task")
@Validated
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {
        Task obj = this.taskService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/user/{userId}") //tem outra forma de fazer isso, com query parameters
    public ResponseEntity<List<Task>> findAllByUserId(@PathVariable Long userId) {
        this.userService.findById(userId); //returns Usuário não existe - final da aula 14
        List<Task> tasks = this.taskService.findAllById(userId);
        return ResponseEntity.ok().body(tasks);
    }

    @PostMapping
    @Validated
    public ResponseEntity<Void> create(@Valid @RequestBody Task obj) {
        this.taskService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build(); //created retorna 200 passando Uri de onde foi criado
    }
    
    //Apenas operações de inserção passam no Body (create and delete). Respondemos o FrontEnd no Post e Update passando o dado pelo Body (ResquestBody); - *Get e Delete não passa dado no body!, *passamos o dado na URL (Pathvariable). Fugir disso quebra a API Restful
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody Task obj, @PathVariable Long id){
        obj.setId(id);
        this.taskService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
