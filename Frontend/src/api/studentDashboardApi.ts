import axios from "axios";
import { Course } from "../models/Course";
import { Login } from "../models/Login";
import { NewSignUp } from "../models/NewSignUp";
import { Student } from "../models/Student";

const api = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true,
});

export async function login(login: Login): Promise<Student> {
  return api.post(`/login`, login).then((res) => res.data);
}

export async function getStudent(id: number): Promise<Student> {
  return api.get(`/students/${id}`).then((res) => res.data);
}

export async function getRegisteredCourses(id: number): Promise<Course[]> {
  return api.get(`/students/${id}/courses`).then((res) => res.data);
}

export async function getCourses(): Promise<Course[]> {
  return api.get(`/courses/`).then((res) => res.data);
}

export async function signUpNewStudent(signupData: NewSignUp): Promise<string> {
  return api.post(`/sign-up`, signupData).then((res) => res.data);
}

export async function addCourse(
  studentId: string,
  courseId: string
): Promise<string> {
  const reqData = {
    action: "ADD",
    courseId: courseId,
  };
  return api
    .post(`/students/${studentId}/courses`, reqData)
    .then((res) => res.data);
}

export async function removeCourse(
  studentId: string,
  courseId: string
): Promise<string> {
  const reqData = {
    action: "REMOVE",
    courseId: courseId,
  };
  return api
    .post(`/students/${studentId}/courses`, reqData)
    .then((res) => res.data);
}
