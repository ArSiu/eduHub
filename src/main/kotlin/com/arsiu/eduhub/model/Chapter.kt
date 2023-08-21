package com.arsiu.eduhub.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
@Table(name = "chapter")
data class Chapter(

    @Id
    @JsonProperty("id")
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0L,

    @JsonProperty("name")
    @Column(name = "name", length = 50)
    var name: String = "",

    @ManyToOne
    @JoinColumn(name = "course_id")
    var course: Course = Course()

) {

    @OneToMany(mappedBy = "chapter", cascade = [CascadeType.ALL], orphanRemoval = true)
    var lessons: MutableList<Lesson> = mutableListOf()

}
