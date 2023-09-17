package com.arsiu.eduhub.controller.rest

import com.arsiu.eduhub.dto.request.AssignmentDtoRequest
import com.arsiu.eduhub.dto.response.AssignmentDtoResponse
import com.arsiu.eduhub.mapper.AssignmentRestMapper
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
    private val assignmentRestMapper: AssignmentRestMapper
) {

    @GetMapping
    fun getAllAssignments(): List<AssignmentDtoResponse> =
        assignmentRestMapper.toResponseDtoList(assignmentService.findAll())

    @GetMapping("/{id}")
    fun getAssignmentById(@PathVariable id: String): AssignmentDtoResponse =
        assignmentRestMapper.toResponseDto(assignmentService.findById(id))

    @PutMapping
    fun updateAssignmentById(@Valid @RequestBody assignment: AssignmentDtoRequest): AssignmentDtoResponse {
        val updated = assignmentService.update(assignmentRestMapper.toEntityUpdate(assignment))
        return assignmentRestMapper.toResponseDto(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteAssignmentById(@PathVariable id: String) =
        assignmentService.delete(id)

}
