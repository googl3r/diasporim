package infrastructure.entities

import core.Student

case class StudentEntity(studentId: String,
                         name: String,
                         email: String) {
}
object StudentEntity {
  def fromStudent(student: Student): StudentEntity =
    StudentEntity(student.id.value, student.name.value, student.email.value)

  def toStudent(studentEntity: StudentEntity):  Student =
    Student.createStudent(studentEntity.studentId, studentEntity.name, studentEntity.email).toOption.get
}
