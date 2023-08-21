package com.arsiu.eduhub.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
@Table(name = "lesson")
data class Lesson(

    @Id
    @JsonProperty("id")
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0L,

    @JsonProperty("name")
    @Column(name = "name", length = 50)
    var name: String = "",

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    var chapter: Chapter = Chapter()

) {

    @OneToMany(mappedBy = "lesson", cascade = [CascadeType.ALL], orphanRemoval = true)
    var assignments: MutableList<Assignment> = mutableListOf()

}
