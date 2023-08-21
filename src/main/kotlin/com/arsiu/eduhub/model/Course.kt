package com.arsiu.eduhub.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
@Table(name = "course")
data class Course(

    @Id
    @JsonProperty("id")
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0L,

    @JsonProperty("name")
    @Column(name = "name", length = 50)
    var name: String = "",

    @ManyToOne
    @JoinColumn(name = "owner_id")
    var owner: User = User()

) {

    @ManyToMany(mappedBy = "boughtCourses")
    var students: Set<User> = mutableSetOf()

    @OneToMany(mappedBy = "course", cascade = [CascadeType.ALL], orphanRemoval = true)
    var chapters: MutableList<Chapter> = mutableListOf()

}
