package com.luizventura.todosimple.models;

// import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = Task.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode //@°opção: substituir tudo dapois de @Table por @Data
public class Task {
    public static final String TABLE_NAME = "task";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false) //updatable é para não deixar uma tarefa atualizar de um uzuário para outro. nullable não era necessário porque o bd não vai deixar fazer uma associação com um id que não existe, mas foi colocado para garantir/forçar que um uxuário não coloque vazio.
    private User user; //task pertence a 1 usuário

    //@Size é para não deixar o erro cair no banco de dados e tratar antes no spring
    @Column(name = "description", length = 255, nullable = false)
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 255)
    private String description;


//     public Task() {
//     }

//     public Task(Long id, User user, String description) {
//         this.id = id;
//         this.user = user;
//         this.description = description;
//     }

//     public Long getId() {
//         return this.id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public User getUser() {
//         return this.user;
//     }

//     public void setUser(User user) {
//         this.user = user;
//     }

//     public String getDescription() {
//         return this.description;
//     }

//     public void setDescription(String description) {
//         this.description = description;
//     }

//     public Task id(Long id) {
//         setId(id);
//         return this;
//     }

//     public Task user(User user) {
//         setUser(user);
//         return this;
//     }

//     public Task description(String description) {
//         setDescription(description);
//         return this;
//     }

//     @Override
//     public int hashCode() {
//         final int prime = 31;
//         int result = 1;
//         result = prime * result + ((this.id == null) ? 0 : id.hashCode());
//         return result;
//     }

//     @Override
//     public boolean equals(Object obj) {
//         if (obj == this)
//             return true;
//         if (obj == null)
//             return false;
//         if (!(obj instanceof Task))
//             return false;
//         Task other = (Task) obj;
//         if (this.id == null)
//             if (other.id != null)
//                 return false;
//             else if (!this.id.equals(other.id))
//                 return false;
//         return Objects.equals(this.id, other.id) && Objects.equals(this.user, other.user)
//                 && Objects.equals(this.description, other.description);
//     }
}
