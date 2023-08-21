package com.arsiu.eduhub.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
@Table(name = "assignment")
data class Assignment(

    @Id
    @JsonProperty("id")
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0L,

    @JsonProperty("name")
    @Column(name = "name", length = 50)
    var name: String = "",

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "lesson_id")
    var lesson: Lesson = Lesson()

)
