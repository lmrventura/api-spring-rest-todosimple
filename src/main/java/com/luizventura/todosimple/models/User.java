package com.luizventura.todosimple.models;

// import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
// import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
//@Table(name="user")
@Table(name = User.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class User {
    public interface CreateUser { //validação

    }
    public interface UpdateUser { //validação

    }

    public static final String TABLE_NAME = "user";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //autoincrement
    @Column(name = "id", unique = true) //para não duplicar valor
    private Long id;

    @Column(name = "username", length = 100, nullable = false, unique = true)
    @NotNull(groups = CreateUser.class) //não pode atualizar
    @NotEmpty(groups = CreateUser.class) // "" e null
    @Size(groups = CreateUser.class, min = 2, max = 100)
    private String username;

    @JsonProperty(access = Access.WRITE_ONLY) //WRITE_ONLY porque não retornamos a senha para o frontend/usuário
    @Column(name = "password", length = 60, nullable = false)
    @NotNull(groups = {CreateUser.class, UpdateUser.class}) //pode atualizar
    @NotEmpty(groups = {CreateUser.class, UpdateUser.class})
    @Size(groups = {CreateUser.class, UpdateUser.class}, min = 8, max = 60)
    private String password;

    @OneToMany(mappedBy = "user") //quem está mapeando/quem é o dono das tasks na classe Task
    @JsonProperty(access = Access.WRITE_ONLY) // Quando buscar um usuário eu não quero fazer a leitura, trazendo as tasks, mas quando eu buscar uma task eu trago o usuário.
    private List<Task> tasks = new ArrayList<Task>();

    // public User() {
    // }

    // public User(Long id, String username, String password) {
    //     this.id = id;
    //     this.username = username;
    //     this.password = password;
    // }

    // public Long getId() {
    //     return this.id;
    // }

    // public void setId(Long id) {
    //     this.id = id;
    // }

    // public String getUsername() {
    //     return this.username;
    // }

    // public void setUsername(String username) {
    //     this.username = username;
    // }

    // public String getPassword() {
    //     return this.password;
    // }

    // public void setPassword(String password) {
    //     this.password = password;
    // }
    
    // //Getters and Setters Tasks list
    // @JsonIgnore //para não retornar todas as tasks quando retornar o usuário. Ex: se o usuário tiver 500 tasks vai demorar de returnar o usuário por conta do volume de dados.
    // public List<Task> getTasks() {
    //     return this.tasks;
    // }

    // public void setTasks(List<Task> tasks) {
    //     this.tasks = tasks;
    // }


    // //hashcode and equals
    // @Override
    // public int hashCode() {
    //     final int prime = 31;
    //     int result = 1;
    //     result = prime * result + ((id == null) ? 0 : id.hashCode());
    //     result = prime * result + ((username == null) ? 0 : username.hashCode());
    //     result = prime * result + ((password == null) ? 0 : password.hashCode());
    //     return result;
    // }

    // @Override
    // public boolean equals(Object obj) {
    //     if (obj == this)
    //         return true;
    //     if (obj == null)
    //         return false;
    //     if (!(obj instanceof User))
    //         return false;
    //     User other = (User) obj;
    //     if (this.id == null)
    //         if (other.id != null)
    //             return false;
    //         else if (!this.id.equals(other.id))
    //             return false;
    //     return Objects.equals(this.id, other.id) && Objects.equals(this.username, other.username)
    //             && Objects.equals(this.password, other.password);
    // }

}
