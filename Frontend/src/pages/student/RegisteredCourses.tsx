import { AxiosError } from "axios";
import React, { useContext, useEffect, useState } from "react";
import { toast } from "react-hot-toast";
import { QueryClient } from "react-query";
import {
  ActionFunctionArgs,
  LoaderFunctionArgs,
  useLoaderData,
  useNavigate,
  useNavigation,
} from "react-router";
import { useSubmit } from "react-router-dom";
import {
  getRegisteredCourses,
  removeCourse,
} from "../../api/studentDashboardApi";
import Button from "../../components/Button";
import CourseWrapper from "../../components/CourseWrapper";
import Modal from "../../components/Modal";
import Spinner from "../../components/Spinner";
import { Course } from "../../models/Course";
import { StudentCourse } from "../../models/StudentCourse";
import { AuthContext } from "../../store/AuthContext";

type Props = {};

let backgroundColor = "bg-gradient-to-br from-slate-900 to-slate-700";

function RegisteredCourses({}: Props) {
  const loaderData = useLoaderData() as Course[];
  const navigate = useNavigate();
  const authCtx = useContext(AuthContext);
  const submit = useSubmit();

  const navigation = useNavigation();
  const submitting = navigation.state === "submitting";

  const [modalState, setModalState] = useState({
    isOpen: false,
    selectedCourse: {} as Course,
    isSubmitted: false,
  });

  useEffect(() => {
    if (!submitting && modalState.isSubmitted) {
      if (modalState.selectedCourse.name !== "") {
        toast(`${modalState.selectedCourse.name} removed from your Courses.`);
      }
      closeModal();
      navigate(
        `/home/${authCtx.loggedInStudent?.id}/registeredCourses/${authCtx.loggedInStudent?.id}`
      );
    }
  }, [submitting, modalState.isSubmitted]);

  const openModal = (course: Course) => {
    setModalState({
      isOpen: true,
      selectedCourse: course,
      isSubmitted: false,
    });
  };

  const closeModal = () => {
    document.body.style.overflow = "unset";

    setModalState({
      isOpen: false,
      selectedCourse: {} as Course,
      isSubmitted: false,
    });
  };

  const removeCourse = (courseId: number) => {
    setModalState((prevState) => {
      return { ...prevState, isSubmitted: true };
    });

    submit(
      {
        studentId: `${authCtx.loggedInStudent?.id}`,
        courseId: String(courseId),
      } as StudentCourse,
      {
        method: "post",
        action: `/home/${authCtx.loggedInStudent?.id}/registeredCourses/${authCtx.loggedInStudent?.id}`,
      }
    );
  };

  return (
    <>
      <div className={`min-h-[90vh] ${backgroundColor} text-white`}>
        <h1 className="text-3xl text-center pt-2">Enlisted Courses</h1>
        <ul
          className={`px-3 ${
            loaderData.length == 0 ? "" : "md:grid md:grid-cols-3 md:gap-6"
          }`}
        >
          {loaderData.length == 0 ? (
            <div className="flex flex-col items-center">
              <h1 className="text-5xl pt-10">No Current Courses Enlisted In</h1>
              <a
                className="cursor-pointer underline text-3xl pt-5"
                onClick={() =>
                  navigate(
                    `/home/${authCtx.loggedInStudent?.id}/registerNewCourse/${authCtx.loggedInStudent?.id}`
                  )
                }
              >
                Enlist in new Courses
              </a>
            </div>
          ) : (
            loaderData.map((course) => (
              <CourseWrapper
                key={course.id}
                className="mt-2 flex gap-2 pt-2 pb-24 px-2 relative"
              >
                <div>
                  <h1 className="capitalize font-bold">{course.name}</h1>
                </div>
                <div>
                  <span className="font-bold">Course Description : </span>
                  <p>{course.description}</p>
                </div>
                <Button
                  bgColor="black"
                  textColor="white"
                  type="button"
                  rounded
                  className="self-end mr-5 absolute top-[65%] right-[0%]"
                  onClick={() => openModal(course)}
                >
                  Remove
                </Button>
              </CourseWrapper>
            ))
          )}
        </ul>
      </div>
      {modalState.isOpen && (
        <Modal closeModal={closeModal}>
          <div className="flex flex-col">
            <h2 className="text-3xl">
              Do you want to un-enroll from the{" "}
              <span className="capitalize">
                {modalState.selectedCourse.name + " ?"}
              </span>
            </h2>
            <div className="flex gap-2 justify-end">
              <Button
                rounded
                textColor="white"
                bgColor="black"
                type="button"
                onClick={() => removeCourse(modalState.selectedCourse.id)}
              >
                {submitting ? <Spinner /> : "Yes"}
              </Button>
              <Button
                className={submitting ? "hidden" : ""}
                rounded
                textColor="white"
                bgColor="black"
                type="button"
                onClick={closeModal}
              >
                No
              </Button>
            </div>
          </div>
        </Modal>
      )}
    </>
  );
}

export default RegisteredCourses;

export const loader =
  (queryClient: QueryClient) =>
  ({ params }: LoaderFunctionArgs) => {
    const data = queryClient.fetchQuery(
      ["studentRegisteredCourses", params.studentId],
      () => getRegisteredCourses(Number(params.studentId))
    );

    return data;
  };

export const action =
  (queryClient: QueryClient) =>
  async ({ params, request }: ActionFunctionArgs) => {
    const formData = Object.fromEntries(
      await request.formData()
    ) as StudentCourse;

    let data = { error: "", response: "" };

    try {
      data.response = await queryClient.fetchQuery("loggedinUser", () =>
        removeCourse(formData.studentId, formData.courseId)
      );
    } catch (err) {
      if (err instanceof AxiosError) {
        if (err.response?.status == 400) {
          data.error = "Course does not exist";
        } else {
          data.error =
            "There is some error with the server, Please check with the server Admin.";
        }
      }
    }

    if (data.error !== "" && data.error !== undefined) {
      toast(data.error);
    }

    return data;
  };
