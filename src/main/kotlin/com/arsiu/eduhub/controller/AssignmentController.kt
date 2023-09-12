package com.arsiu.eduhub.controller

import com.arsiu.eduhub.dto.request.AssignmentDtoRequest
import com.arsiu.eduhub.dto.response.AssignmentDtoResponse
import com.arsiu.eduhub.mapper.AssignmentMapper
import com.arsiu.eduhub.service.AssignmentService
import jakarta.validation.Valid
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
    fun getAllAssignments(): List<AssignmentDtoResponse> =
        assignmentMapper.toResponseDtoList(assignmentService.findAll())

    @GetMapping("/{id}")
    fun getAssignmentById(@PathVariable id: String): AssignmentDtoResponse =
        assignmentMapper.toResponseDto(assignmentService.findById(id))

    @PutMapping
    fun updateAssignmentById(@Valid @RequestBody assignment: AssignmentDtoRequest): AssignmentDtoResponse {
        val updated = assignmentService.update(assignmentMapper.toEntityUpdate(assignment))
        return assignmentMapper.toResponseDto(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteAssignmentById(@PathVariable id: String) =
        assignmentService.delete(id)

}
