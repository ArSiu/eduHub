package com.arsiu.eduhub.v2.assignmentsvc

object NatsSubject {
    const val ASSIGNMENT_BY_ID = "v2.assignment_svc.input.reqreply.assignment.find_by_id"
    const val ASSIGNMENT_FIND_ALL = "v2.assignment_svc.input.reqreply.assignment.find_all"
    const val ASSIGNMENT_UPDATE_BY_ID = "v2.assignment_svc.input.reqreply.assignment.update"
    const val ASSIGNMENT_DELETE_BY_ID = "v2.assignment_svc.input.reqreply.assignment.delete_by_id"
    const val ASSIGNMENT_UPDATE_BUS = "v2.assignment_svc.input.pubsub.assignment.update"
}
