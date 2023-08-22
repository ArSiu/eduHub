package com.arsiu.eduhub.controller

import com.arsiu.eduhub.dto.request.AssignmentDtoRequest
import com.arsiu.eduhub.dto.response.AssignmentDtoResponse
import com.arsiu.eduhub.mapper.AssignmentMapper
import com.arsiu.eduhub.service.AssignmentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/assignment")
class AssignmentController(
    private val assignmentService: AssignmentService,
    private val assignmentMapper: AssignmentMapper
) {

    @GetMapping
    fun getAllAssignments(): ResponseEntity<List<AssignmentDtoResponse>> =
        ResponseEntity(
            assignmentMapper.toDtoResponseList(assignmentService.findAll()),
            HttpStatus.OK
        )

    @GetMapping("/{id}")
    fun getAssignmentById(@PathVariable(value = "id") id: Long): ResponseEntity<AssignmentDtoResponse> =
        ResponseEntity(
            assignmentMapper.toDtoResponse(assignmentService.findById(id)),
            HttpStatus.OK
        )

    @PutMapping("/{id}")
    fun updateAssignmentById(
        @PathVariable(value = "id") id: Long,
        @Valid @RequestBody assignment: AssignmentDtoRequest
    ): ResponseEntity<Void> {
        assignmentService.update(
            id,
            assignmentMapper.toEntity(assignment)
        )
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteAssignmentById(@PathVariable(value = "id") id: Long): ResponseEntity<Void> {
        assignmentService.delete(id)
        return ResponseEntity(HttpStatus.OK)
    }
}
