package com.arsiu.eduhub.assignment.infrastructure.rest

import com.arsiu.eduhub.assignment.application.port.AssignmentService
import com.arsiu.eduhub.assignment.infrastructure.dto.request.AssignmentDtoRequest
import com.arsiu.eduhub.assignment.infrastructure.dto.response.AssignmentDtoResponse
import com.arsiu.eduhub.assignment.infrastructure.mapper.AssignmentRestMapper
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/assignment")
class AssignmentController(
    private val assignmentService: AssignmentService,
    private val assignmentRestMapper: AssignmentRestMapper
) {

    @GetMapping
    fun getAllAssignments(): Mono<List<AssignmentDtoResponse>> =
        assignmentService.findAll()
            .collectList()
            .map { assignments -> assignmentRestMapper.toResponseDtoList(assignments) }

    @GetMapping("/{id}")
    fun getAssignmentById(@PathVariable id: String): Mono<AssignmentDtoResponse> =
        assignmentService.findById(id)
            .map { assignment -> assignmentRestMapper.toResponseDto(assignment) }

    @PutMapping
    fun updateAssignmentById(@Valid @RequestBody assignment: AssignmentDtoRequest):
            Mono<AssignmentDtoResponse> =
        assignmentService.update(assignmentRestMapper.toEntityUpdate(assignment))
            .map { updated -> assignmentRestMapper.toResponseDto(updated) }

    @DeleteMapping("/{id}")
    fun deleteAssignmentById(@PathVariable id: String): Mono<Void> =
        assignmentService.delete(id)

}
