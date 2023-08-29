package com.arsiu.eduhub.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

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

    override fun toString(): String {
        return "Course \"$name\" from $owner "
    }
}
